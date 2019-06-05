package com.example.retrofitframemwork.login.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.viewpager_fragment.activity.ExpandRecyclerViewActivity;
import com.example.demo.viewpager_fragment.activity.PageFragmentActivity;
import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.TestDialogFragment;
import com.example.retrofitframemwork.login.LoginPresenter;
import com.example.retrofitframemwork.login.presenter.TestPresenter;
import com.example.retrofitframemwork.login.view.ILoginView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.data.EventMessage;
import com.framework.common.image_select.MultiImageSelectorActivity;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;
import com.framework.common.widget.drawable.ImageLoadingDrawable;
import com.framework.model.UserBean;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.tv_show)
    TextView tvShow;

    LoginPresenter mPresenter;
    @BindView(R.id.image)
    ImageView image;

    Random mRandom = new Random();
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void getParamData(Intent intent) {
        super.getParamData(intent);
    }

    @Override
    public void bindData() {
        ImageLoadingDrawable drawable = new ImageLoadingDrawable();
        image.setImageDrawable(drawable);
        drawable.setLevel(2000);
    }

    @Override
    public void initEvent() {
        try {
            Method method = LoginPresenter.class.getSuperclass().getDeclaredMethod("onReceiveEvent",EventMessage.class);
            Method method2 = TestPresenter.class.getSuperclass().getDeclaredMethod("onReceiveEvent",EventMessage.class);
            if(method==method2){
                LogUtil.i("相同");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        LogUtil.e("has="+getWindow().hasFeature(Window.FEATURE_ACTIVITY_TRANSITIONS));
        LogUtil.e("has="+getWindow().hasFeature(Window.FEATURE_CONTENT_TRANSITIONS));
    }

    @Override
    protected BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new LoginPresenter();
        }
        return mPresenter;
    }

    private void typeThree(Map<String, Object> parmas) {

    }

    private void typeTwo(Map<String, Object> parmas) {
//        NetWorkManager.getInstance().getRetorfit(AppInterfaceValue.HOST)
//                .create(AppApi.class)
//                .loginByRxJava(parmas)
//                .compose(SchedulerProvider.getInstance().<String>applySchedulers())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.e("zhang", "ok=" + s);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.e("zhang", "fail=" + throwable.getMessage());
//                    }
//                });
    }

    private void typeOne(Map<String, Object> parmas) {
//        NetWorkManager.getInstance().getRetorfit(AppInterfaceValue.HOST)
//                .create(AppApi.class)
//                .login(parmas).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("zhang", "ok=" + response.body());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("zhang", "fail:" + t.getMessage());
//            }
//        });
    }

    @OnClick({R.id.tv_show,R.id.image})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_show:
                //mPresenter.downLoadFile();
                //getProcessName(this);
                //mPresenter.login("13695157045","1");
                showFragment(0);
                break;
            case R.id.image:
                ExpandRecyclerViewActivity.start(this);
                //PageFragmentActivity.start(this);
                //Main2Activity.start(this);
                //MultiImageSelectorActivity.startMe(this,2,200);
                //showFragment(mRandom.nextInt());
                break;
        }
        //Main2Activity.start(this);
//        mPresenter.downLoadFile();
//        mPresenter.uploadFile();
//         重新构造Uri：content://
//        requestNeedPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //passPermission("");
        //showFragment(mRandom.nextInt());
//        TestFragment fragment = TestFragment.newInstance(2);
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment).commit();
//        showLoadingDialog();
//        tvShow.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showLoading();
//            }
//        },1000);
//        tvShow.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideLoadingDialog();
//            }
//        },3000);
//        tvShow.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideLoading();
//            }
//        },6000);
    }

    private void showFragment(final int position) {
        Log.e("zhang", "ppposition=" + position);
        final int pposition = position;
        if (frament == null) {
            frament = new TestDialogFragment();
        }
        frament.setActionListener(new TestDialogFragment.ActionListener() {
            @Override
            public void onPrice(String price) {
                //Log.e("zhang", "position=" + pposition);
                Toast.makeText(MainActivity.this,"成功",Toast.LENGTH_SHORT).show();
            }
        });
        frament.showNow(getSupportFragmentManager(), "changePrice");
    }

    TestDialogFragment frament;

    @Override
    public void passPermission(String permission) {
//        File imagePath = new File(getCacheDir(), "images");
//        if (!imagePath.exists()) {
//            imagePath.mkdirs();
//        }
//        File newFile = new File(imagePath, "default_image.jpg");
//        Uri contentUri = FileProvider.getUriForFile(getContext(),
//                BaseApplication.getApp().getPackageName() + ".FileProvider", newFile);
//        //Uri contentUri = Uri.fromFile(newFile);//7.0以上应用间只能用content:// 不管私有还是外部存储
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
//        // 授予目录临时共享权限
////        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
////                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        startActivityForResult(intent, 100);

//        UserOperation opration = new UserOperation();
//        long start = System.currentTimeMillis();
//        for(int i=0;i<100;i++){
//            opration.setCacheWithoutKeyAsy(content+i);
//        }
//        Log.e("zhang","userTime="+(System.currentTimeMillis()-start));
    }

    @Override
    public void loginSucess(UserBean bean) {

    }

    @Override
    public void showBitmap(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case 100:
                    File imagePath = new File(getCacheDir(), "images");
                    if (!imagePath.exists()) {
                        imagePath.mkdirs();
                    }
                    File newFile = new File(imagePath, "default_image.jpg");
                    Bitmap bitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                    break;
                case 200:
                    List<String> photoPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ListUtils.isEmpty(photoPath)) {
                        Map<String,Object> params = new HashMap<>();
                        params.put("dir","oaimage");
                        params.put("img",new File(photoPath.get(0)));
                        mPresenter.uploadFile(params);
                        //mPresenter.zipFile(photoPath.get(0));
                    }
                    break;
            }
        }
    }

    String content = "";
    public static String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
}
