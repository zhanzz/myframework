package com.example.demo.lifecycle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.framework.model.demo.User;

/**
 * @author zhangzhiqiang
 * @date 2019/11/2.
 * description：
 */
public class UserViewModel extends ViewModel {
    private String userId;
    private MutableLiveData<User> user;

    LiveData<String> userName = Transformations.map(user, user -> user.getFirstName() + " " + user.getLastName());
    //初始化传递uid进来
    public void init(String userId) {
        this.userId = userId;
        user = new MutableLiveData<>();
    }
    //提供完整的用户信息
    public MutableLiveData<User> getUser() {
        return user;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
