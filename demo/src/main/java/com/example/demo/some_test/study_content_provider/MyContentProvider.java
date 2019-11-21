package com.example.demo.some_test.study_content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;

/**
 * @author zhangzhiqiang
 * @date 2019/10/26.
 * description：
 */
public class MyContentProvider extends ContentProvider {

    //这里的AUTHORITY就是我们在AndroidManifest.xml中配置的authorities
    private static final String AUTHORITY = "com.zhang.studentProvider";

    //匹配成功后的匹配码
    private static final int MATCH_CODE = 100;

    private static UriMatcher uriMatcher;

    private UserDao studentDao;

    //数据改变后指定通知的Uri
    private static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHORITY + "/student");

    //常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码

//如果match()方法匹配content://cn.xxt.provider.personprovider/person路径，返回匹配码为1
//sMatcher.addURI(“cn.xxt.provider.personprovider”, “person”, 1);//添加需要匹配uri，如果匹配就会返回匹配码
//如果match()方法匹配content://cn.xxt.provider.personprovider/person/230路径，返回匹配码为2
//sMatcher.addURI(“cn.xxt.provider.personprovider”, “person/#”, 2);//#号为通配符

    static {
        //匹配不成功返回NO_MATCH(-1)
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //添加我们需要匹配的uri
        uriMatcher.addURI(AUTHORITY,"student", MATCH_CODE);
    }

    @Override
    public boolean onCreate() {
        LogUtil.e("provider","创建了"+this.toString());
        studentDao = UserDatabase.getInstance(getContext()).getUserDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //pid=13608;thread=Binder:13608_1 binder属于另一个线程
        LogUtil.e("zhang","pid="+android.os.Process.myPid()+";thread="+Thread.currentThread().getName());
        int match = uriMatcher.match(uri);
        if (match == MATCH_CODE){
            //Cursor cursor = studentDao.queryStudent(4);
            //return cursor;
            String sql = SQLiteQueryBuilder.buildQueryString(false,"User",projection,selection,null,null,sortOrder,null);
            return UserDatabase.getInstance(getContext()).query(sql,selectionArgs);
        }else {
            throw new IllegalArgumentException("无效的uri");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if(values==null || values.size()==0){
            return null;
        }
        int match = uriMatcher.match(uri);
        if (match == MATCH_CODE){
            User user = new User();
            if(values.containsKey("firstName")){
                user.setFirstName((String) values.get("firstName"));
            }
            if(values.containsKey("lastName")){
                user.setLastName((String) values.get("lastName"));
            }
            if(values.containsKey("age")){
                user.setAge((Integer) values.get("age"));
            }
            UserDatabase.getInstance(getContext()).getUserDao().insert(user);
            return Uri.withAppendedPath(uri,String.valueOf(user.getId()));
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private void notifyChange(){
        getContext().getContentResolver().notifyChange(NOTIFY_URI,null);
    }
}
