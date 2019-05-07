package com.example.retrofitframemwork.login.view;

import android.graphics.Bitmap;

import com.framework.common.base_mvp.IBaseView;
import com.framework.model.UserBean;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author zhangzhiqiang
 * @date 2019/4/17.
 * description：
 */
public interface ILoginView extends IBaseView {
    void loginSucess(UserBean bean);
    void showBitmap(Bitmap bitmap);
}
