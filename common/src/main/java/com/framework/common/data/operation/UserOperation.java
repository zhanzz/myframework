package com.framework.common.data.operation;

import android.text.TextUtils;

import com.framework.common.data.sharedPreference.BasePrefDao;
import com.framework.model.UserBean;
import com.framework.model.UserEntity;

/**
 * @author zhangzhiqiang
 * @date 2019/4/25.
 * description：
 */
public class UserOperation extends BasePrefDao<UserEntity> {
    private final static String CACHE_NAME = "cache_user_info";
    public UserOperation() {
        super(CACHE_NAME,1);
    }

    private volatile static UserOperation instance;
    private UserEntity mUserEntity;

    public static UserOperation getInstance() {
        if (instance == null) {
            synchronized (UserOperation.class) {
                if (instance == null) {
                    instance = new UserOperation();
                }
            }
        }
        return instance;
    }

    @Override
    public UserEntity getData() {
        if(mUserEntity==null){
            mUserEntity = super.getData();
        }
        if(mUserEntity==null){
            mUserEntity = new UserEntity();
        }
        return mUserEntity;
    }

    @Override
    public void setData(UserEntity data) {
        mUserEntity = data;
        super.setData(data);
    }

    public String getToken() {
        UserEntity entity = getData();
        if(entity!=null){
            return entity.getToken();
        }
        return "";
    }

    public boolean isLogin(){
        return !TextUtils.isEmpty(getData().getToken());
    }

    public void setToken(String token) {
        UserEntity entity = getData();
        entity.setToken(token);
        setData(entity);
    }
}
