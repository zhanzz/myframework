package com.example.demo.contact.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.contact.adapter.ContactAdapter;
import com.example.demo.contact.presenter.PhoneListPresenter;
import com.example.demo.contact.view.IPhoneListView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ListUtils;
import com.framework.model.demo.ContactBean;
import com.oubowu.stickyitemdecoration.StickyHeadContainer;
import com.oubowu.stickyitemdecoration.StickyItemDecoration;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneListActivity extends BaseActivity implements IPhoneListView {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.stickyHeadContainer)
    StickyHeadContainer stickyHeadContainer;
    //head内的id
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.btn_select_phone)
    Button btnSelect;
    private PhoneListPresenter mPresenter;
    private ContactAdapter mAdapter;
    private WeakReference<Cursor> mCursor;

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_list;
    }

    @Override
    public void bindData() {
        mAdapter = new ContactAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PhoneListActivity.this));
        //StickyItemDecoration stickyItemDecoration = new StickyItemDecoration(stickyHeadContainer, ContactBean.TYPE_HEAD);
        //recyclerView.addItemDecoration(stickyItemDecoration);
        recyclerView.setAdapter(mAdapter);
        requestNeedPermissions(Manifest.permission.READ_CONTACTS);
    }

    @Override
    public void initEvent() {
        stickyHeadContainer.setDataCallback(new StickyHeadContainer.DataCallback() {
            @Override
            public void onDataChange(int pos) {
                ContactBean item = mAdapter.getItem(pos);
                tvName.setText(String.valueOf(item.category));
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAdapter.getData().add(0,new ContactBean("xx","xxx",'x'));
//                mAdapter.notifyItemInserted(0);
            }
        });
    }

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        if (permissions.contains(Manifest.permission.READ_CONTACTS)) {
            startLoad();
        }
    }

    private void startLoad() {
        showLoading();
        LoaderManager manager = LoaderManager.getInstance(this);
        // 此处的第一二个参数和Callback中onCreateLoader的值对应，可以传一些查询条件等
        // 第二个为刚刚的回调类实例
        manager.initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                /**
                 * 这里返回执行查询的Loader对象，传入的参数和ContentResolver的执行的参数是一样的
                 */
                return new CursorLoader(PhoneListActivity.this, ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                        null, null, null);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                hideLoading();
                List<ContactBean> list = new ArrayList<>();
                if (null != data) {
                    if (mCursor != null && mCursor.get() == data) {
                        return;//数据集没有变化
                    }
                    mCursor = new WeakReference<>(data);
                    int count = data.getCount();
                    if (count > 0) {
                        data.moveToFirst();
                        do {
                            // 获取备注名
                            String name = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            // 获取电话号码
                            String phone = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // 其他的常量自行查询文档
                            list.add(new ContactBean(phone, name, getFirstChar(name)));
                        } while (data.moveToNext());
                    }
                }
                Collections.sort(list);
                insertHeadData(list);
                mAdapter.setNewData(list);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }

    private void insertHeadData(List<ContactBean> list) {
        if (!ListUtils.isEmpty(list)) {
            //result>=65&&result<=90 A<result<Z
            for (char i = 65; i <= 90; i++) {
                ContactBean bean = new ContactBean();
                bean.category = i;
                bean.itemType = ContactBean.TYPE_HEAD;

                int index = list.indexOf(bean);
                if (index >= 0) {
                    list.add(index, bean);
                }
            }
            ContactBean bean = new ContactBean();
            bean.category = '#';
            bean.itemType = ContactBean.TYPE_HEAD;
            int index = list.indexOf(bean);
            if (index >= 0) {
                list.add(index, bean);
            }
        }
    }

    private char getFirstChar(String name) {
        char result;
        if (TextUtils.isEmpty(name)) {
            result = '#';
        } else {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
            if (pinyinArray != null) {
                result = pinyinArray[0].charAt(0);
            } else {
                result = name.charAt(0);
            }
        }
        if (result >= 97 && result <= 122) { // a<=result<=z //小写转大写
            result -= 32;
        } else if (result < 65 || result > 90) {//不是大写字母
            result = '#';
        }
        return result;
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new PhoneListPresenter();
        }
        return mPresenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    startNumberLoad(contactData);
//                    String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
//                    ContentResolver cr = getContentResolver();
//                    Cursor cursor = cr.query(contactData, projection, null, null, null);
//                    if (cursor != null && cursor.moveToFirst()) {
//                        int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//                        String name = cursor.getString(nameFieldColumnIndex);
//                        int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                        String number = cursor.getString(numberFieldColumnIndex);
//                        btnSelect.setText("name为"+name+";所选手机号为：" + number);
//                        cursor.close();
//                    }
                }
                break;

            default:
                break;
        }
    }

    private void startNumberLoad(final Uri uri) {
        showLoading();
        LoaderManager manager = LoaderManager.getInstance(this);
        Loader<Cursor> cursorLoader = manager.getLoader(1);
        if(cursorLoader==null){
            // 此处的第一二个参数和Callback中onCreateLoader的值对应，可以传一些查询条件等
            // 第二个为刚刚的回调类实例
            manager.initLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                @NonNull
                @Override
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    /**
                     * 这里返回执行查询的Loader对象，传入的参数和ContentResolver的执行的参数是一样的
                     */
                    return new CursorLoader(PhoneListActivity.this,uri,null,
                            null, null, null);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {//onstop回到onresume也会再次加载
                    hideLoading();
                    if(cursor==null){
                        return;
                    }
                    cursor.moveToFirst();
                    //String num = getContactPhone(cursor);
                    if (cursor.moveToFirst()) {
                        int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        String name = cursor.getString(nameFieldColumnIndex);
                        int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String number = cursor.getString(numberFieldColumnIndex);
                        btnSelect.setText("name为"+name+";所选手机号为：" + number);
                        //cursor.close();//不用关闭，生命周期会自已关闭
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {

                }
            });
        }else {
            ((CursorLoader)cursorLoader).setUri(uri);
            cursorLoader.forceLoad();
        }
    }

    private String getContactPhone(Cursor cursor) {
        // TODO Auto-generated method stub
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phone.getInt(typeindex);
                    String phoneNumber = phone.getString(index);
                    result = phoneNumber;
//					switch (phone_type) {//此处请看下方注释
//					case 2:
//						result = phoneNumber;
//						break;
//
//					default:
//						break;
//					}
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
        return result;
    }

    public static void start(Context context, Bundle bundle) {
        Intent starter = new Intent(context, PhoneListActivity.class);
        if (bundle != null) {
            starter.putExtras(bundle);
        }
        context.startActivity(starter);
    }

    @OnClick(R2.id.btn_select_phone)
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, 1);
    }
}