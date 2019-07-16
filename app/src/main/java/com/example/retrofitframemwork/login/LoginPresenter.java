package com.example.retrofitframemwork.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.retrofitframemwork.AppApi;
import com.example.retrofitframemwork.data.UserOperation;
import com.example.retrofitframemwork.login.view.ILoginView;
import com.example.retrofitframemwork.utils.Events;
import com.framework.common.data.LoadType;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.common.BaseApplication;
import com.framework.common.BuildConfig;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.callBack.FileCallBack;
import com.framework.common.callBack.FileUploadCallBack;
import com.framework.common.image_util.Compressor;
import com.framework.common.manager.CacheDirManager;
import com.framework.common.retrofit.SchedulerProvider;
import com.framework.common.net.RxNet;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.utils.AppTools;
import com.framework.model.UploadImgV2Bean;
import com.framework.model.UserEntity;
import com.framework.model.VersionInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author zhangzhiqiang
 * @date 2019/4/17.
 * description：
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

    public void login(String userName, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("UserName", userName);
        params.put("Password", password);
        params.put("Code", "");

        RxNet.request(RetorfitUtil.getRetorfitApi(AppApi.class)
                .loginByResponse(params), getMvpView(), LoadType.LOAD, new RxNetCallBack<UserEntity>() {
            @Override
            public void onSuccess(UserEntity data, int code, String msg) {
                UserOperation.getInstance().setData(data);
                getMvpView().showToast("登录成功");
                //getMvpView().showToast(msg);
            }

            @Override
            public void onFailure(int code, String msg) {
                getMvpView().showErrorView();
                getMvpView().showToast("登录失败=" + msg);
                Log.e("zhang", "fail=" + msg);
            }
        });
    }

    public void downLoadFile() {
        File dir = BaseApplication.getApp().getExternalCacheDir();
        Disposable d = RxNet.downLoadFile("https://img2.zhidianlife.com/appStore/app-release_legu_56_aligned_signed_yingyongbao.apk", new FileCallBack(dir.getAbsolutePath(), "test.apk") {
            @Override
            public void onFile(File file) {
                ///data/user/0/com.example.retrofitframemwork/cache/test.apk   cacheDir 不可以安装
                ///storage/emulated/0/Android/data/com.example.retrofitframemwork/cache/test.apk  ExtraCacheDir  可以安装
                Log.e("zhang", "ok=" + file.getAbsolutePath());
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(BaseApplication.getApp(), BaseApplication.getApp().getPackageName() + ".FileProvider", file);
                } else {
                    uri = Uri.fromFile(file);
                }
                //BaseApplication.getApp().grantUriPermission("目标包名", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                getMvpView().getContext().startActivity(intent);
            }

            @Override
            public void onFail(Throwable e) {
                Log.e("zhang", "fail=" + e.getMessage());
            }

            @Override
            public void inProgress(float progress, long total) {
                Log.e("zhang", "progress=" + progress);
            }
        });
        CompositeDisposable compositeDisposable = getMvpView().getCompositeDisposable();
        if (compositeDisposable != null) {
            compositeDisposable.add(d);
        } else {
            d.dispose();
        }
    }

    public void checkUpdate() {
        RxNet.request(RetorfitUtil.getRetorfitApi(AppApi.class)
                .checkUpdate(), getMvpView(), LoadType.NONE, new RxNetCallBack<VersionInfo>() {
            @Override
            public void onSuccess(VersionInfo versionBean, int code, String msg) {
                if (null != versionBean) {
                    int lastVersionCode = versionBean.getVersioncode();
                    if ((lastVersionCode > BuildConfig.VERSION_CODE && versionBean.getPopup() == 1)){
                        //缓存的版本比当前的版本要高
                        getMvpView().onShowUpdateDialog(versionBean);
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    public void uploadFile(Map<String, Object> params) {
        Disposable d = RxNet.uploadFile(BuildConfig.GLOBAL_HOST + "v1/uploadsdb", params, new FileUploadCallBack<UploadImgV2Bean>() {
            @Override
            public void onSuccess(UploadImgV2Bean result) {
                //Log.e("zhang",result.getData().getPath());
                getMvpView().showToast(result.getMessage());
            }

            @Override
            public void onFail(String msg) {
                getMvpView().showToast(msg);
            }

            @Override
            public void onProgress(String progress) {
                Log.e("zhang", "progress=" + progress);
            }
        });
        CompositeDisposable compositeDisposable = getMvpView().getCompositeDisposable();
        if (compositeDisposable != null) {
            compositeDisposable.add(d);
        } else {
            d.dispose();
        }
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void zipFile(final String filePath) {
        new Compressor(BaseApplication.getApp())
                .setMaxHeight(1920)
                .setMaxWidth(1080)
                .setQuality(80)
                .setConfig(Bitmap.Config.RGB_565)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(CacheDirManager.getTempFileDir().getAbsolutePath())
                .compressToFileAsObservable(new File(filePath))
                .compose(SchedulerProvider.getInstance().<File>applySchedulers())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        Log.e("zhang", "Width=" + bitmap.getWidth() + ";height=" + bitmap.getHeight()
                                + "bitmapSize=" + bitmap.getByteCount() + ";fileSize=" + file.length());
                        File[] files = CacheDirManager.getTempFileDir().listFiles();
                        if (files != null) {
                            for (File sfile : files) {
                                sfile.delete();
                            }
                        }
                        getMvpView().showBitmap(bitmap);
                    }
                });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.LoginOut event) {
        getMvpView().showToast("我收到消息啦");
    }
}
