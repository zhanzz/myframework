package com.example.retrofitframemwork.data;

import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.data.sharedPreference.BasePrefDao;
import com.framework.common.manager.NetWorkManager;
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

    private static UserOperation instance;

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

    public String getToken() {
        UserEntity entity = getData();
        if(entity!=null){
            return entity.getToken();
        }
        return "";
    }
}
