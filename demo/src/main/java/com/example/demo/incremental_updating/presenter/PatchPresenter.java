package com.example.demo.incremental_updating.presenter;

import com.example.demo.incremental_updating.ApkUtils;
import com.example.demo.incremental_updating.BsPatch;
import com.example.demo.incremental_updating.Constants;
import com.framework.common.BaseApplication;
import com.framework.common.base_mvp.BasePresenter;
import com.example.demo.incremental_updating.view.IPatchView;
import com.framework.common.callBack.FileCallBack;
import com.framework.common.net.RxNet;
import com.framework.common.utils.ToastUtil;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PatchPresenter extends BasePresenter<IPatchView> {
    public void downLoadPatch() {
        final File dir = BaseApplication.getApp().getCacheDir();
        RxNet.downLoadFile(Constants.URL_PATCH_DOWNLOAD, new FileCallBack(dir.getAbsolutePath(), "bspatch.apk") {
            @Override
            public void onFile(final File file) {
                Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                        String oldfile = ApkUtils.getSourceApkPath(BaseApplication.getApp(), BaseApplication.getApp().getPackageName());
                        String newfile = dir.getAbsolutePath() + File.separator + "dn_apk_new.apk";
                        String patchfile = file.getAbsolutePath();
                        BsPatch.patch(oldfile,patchfile,newfile);
                        emitter.onNext(true);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean){
                        if(aBoolean){
                            ApkUtils.installApk(BaseApplication.getApp(),dir.getAbsolutePath() + File.separator + "dn_apk_new.apk");
                        }
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                getMvpView().showToast(e.getMessage());
            }
        });
    }
}