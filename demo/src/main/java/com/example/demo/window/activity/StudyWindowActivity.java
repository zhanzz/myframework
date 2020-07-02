package com.example.demo.window.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.print.PrintHelper;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.window.ListenerImageView;
import com.example.demo.window.MyScanView;
import com.example.demo.window.WindowService.DialogService;
import com.example.demo.window.WindowService.WindowService;
import com.example.demo.window.presenter.StudyWindowPresenter;
import com.example.demo.window.view.IStudyWindowView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.ToastUtil;
import com.framework.common.utils.UIHelper;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * //Activity中 mWindow.getLayoutInflater().setPrivateFactory(this);
 * 视图的显示过程
 * <p>
 * ActivityThread-->performLaunchActivity(创建activity实例)
 * -->Activity.attach(创建了PhoneWindow，生成了属于activity的WindowManagerImp,并设置给了PhoneWindow)
 * -->Activity.setContentView(实际是调用了PhoneWindow的setContentView)
 * -->PhoneWindow-->installDecor(创建了DecorView并添加了根据各主题样式生成的mContentRoot（含有ID_ANDROID_CONTENT的视图）)
 * -->mLayoutInflater.inflate(layoutResID, mContentParent)(将我们setContentView传入的layoutResID 添加到mContentParent中)
 * -->ActivityThread-->handleResumeActivity(Window的type被赋值为TYPE_BASE_APPLICATION)-->(为WindowManagerImp)wm.addView(decor, l);
 * -->(为全局单例WindowManagerGlobal)mGlobal.addView(view, params, mContext.getDisplay(), (为activity的PhoneWindow)mParentWindow);
 * -->WindowManagerGlobal.addView(中创建了ViewRootImpl,DecorView.assignParent(ViewRootImpl)并requestLayout)
 * -->Activity.makeVisible(ActivityThread-->handleResumeActivity中调用)
 */

/**
 * 第二屏 Activity中代理 mBase.createDisplayContext();
 */
public class StudyWindowActivity extends BaseActivity implements IStudyWindowView {
    @BindView(R2.id.btn_show_pop)
    Button btnShowPop;
    @BindView(R2.id.btn_show_service_pop)
    Button btnShowServicePop;
    @BindView(R2.id.btn_show_pop2)
    Button btnShowPop2;
    @BindView(R2.id.btn_show_test_click_down)
    Button btnShowTestClickDown;
    @BindView(R2.id.tb_switch)
    ToggleButton tbSwitch;
    @BindView(R2.id.scan_view)
    MyScanView listenerImageView;
    private StudyWindowPresenter mPresenter;
    /**
     * popupWindow视图结构
     * <p>
     * PopupDecorView（FrameLayout）(WindowManager.LayoutParams)
     * 根据是否设置背景，没有设置背景 mbackView ==contentView,有设置
     * 创建一个PopupBackgroundView（FrameLayout）添加contentView(宽为MATCH_PARENT,高为contentView的layoutHeight,如无则是MATCH_PARENT)
     * PopupDecorView添加mBackgroundView(宽为MATCH_PARENT,高为contentView的layoutHeight,如无则是MATCH_PARENT)
     */
    PopupWindow popupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_study_window;
    }

    @Override
    public void bindData() {
        TextView tv = new TextView(this);
        tv.setText("我是popwindow");
        //tv.setBackgroundColor(0xffff0000);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.show(getContext(), "点击了我");
//            }
//        });
        popupWindow = new PopupWindow(tv, UIHelper.dip2px(100), UIHelper.dip2px(100));
        popupWindow.setFocusable(false);
        popupWindow.setTouchable(true);//内容可点击
        try {
            Field method = PopupWindow.class.getField("mNotTouchModal");
            method.setAccessible(true);
            method.set(popupWindow, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //popupWindow.setTouchModal(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xff00ff00));
        //popupWindow.showAtLocation();

        tbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //listenerImageView.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
                listenerImageView.postDelayed(() -> {
                    if(isChecked){
                        boolean request = listenerImageView.requestFocus();
                        Log.e("zhang","hasFoucs="+request);
                    }else {
                        listenerImageView.clearFocus();
                    }
                },16);

            }
        });

        listenerImageView.setContentListener(content -> Log.e("zhang", "获取了输入："+content));
    }

    @Override
    public void initEvent() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(event.isShiftPressed()&&event.getAction()==KeyEvent.ACTION_DOWN&&event.getKeyCode()==KeyEvent.KEYCODE_Q){
//            showToast("shift+q");
//        }
//        return super.dispatchKeyEvent(event);
//        doPhotoPrint();
        //Log.e("zhang",((char)event.getUnicodeChar())+"");
//        if (event.getAction() == KeyEvent.ACTION_UP) {
//            builder.append((char) event.getUnicodeChar());
//            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                Log.e("zhang", builder.toString());
//            }
//        }
        return super.dispatchKeyEvent(event);
    }

    private StringBuilder builder = new StringBuilder();


    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_cloud_store_mall_home);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }


    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new StudyWindowPresenter();
        }
        return mPresenter;
    }

    boolean change = false;

    @OnClick({R2.id.btn_show_pop, R2.id.btn_show_service_pop, R2.id.btn_show_test_click
            , R2.id.btn_show_test_click_down, R2.id.btn_show_dialog_service})
    public void onClick(View view) {

        if (view.getId() == R.id.btn_show_service_pop) {
            Log.e("zhang","isFocusableInTouchMode="+btnShowServicePop.isFocusableInTouchMode());
            startFloatingButtonService();
        } else if (view.getId() == R.id.btn_show_dialog_service) {
            DialogService.start(this);
        } else if (view.getId() == R.id.btn_show_test_click) {
            View anrview = null;
            //if (change) {
            change = false;
            anrview = btnShowPop;
//            } else {
//                change = true;
//                anrview = btnShowTestClickDown;
//            }
            int[] location = new int[2];
            anrview.getLocationInWindow(location);
            popupWindow.getContentView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 0);
            if (location[1] + anrview.getHeight() > view.getRootView().getHeight() - popupWindow.getContentView().getMeasuredHeight()) {
                popupWindow.showAtLocation(anrview, Gravity.LEFT | Gravity.TOP, location[0], location[1] - popupWindow.getContentView().getHeight());
            } else {
                popupWindow.showAtLocation(anrview, Gravity.LEFT | Gravity.TOP, location[0], location[1] + anrview.getHeight());
            }
            //
            //ToastUtil.show(this, "点击到我了");
        } else {
            TextView tv = new TextView(this);
            tv.setText("我是自定义window");
            tv.setBackgroundColor(0xffff0000);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(getContext(), "点击到自定义了");
                }
            });

            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.FIRST_SUB_WINDOW + 3;
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//会同时设置FLAG_NOT_TOUCH_MODAL
            //FLAG_NOT_TOUCH_MODAL 如果不设置这个值,则window消费掉所有点击事件,不管这些点击事件是不是在window的范围之内
            getWindowManager().addView(tv, params);
        }
    }


    public void startFloatingButtonService() {
        LogUtil.e("测试流程", "测试流程");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//判断系统版本
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
                LogUtil.e("测试流程3", "测试流程3");
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())), 100);
            } else {
                LogUtil.e("测试流程4", "测试流程4");
                WindowService.start(this);
            }
        } else {
            WindowService.start(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        LogUtil.e("测试流程5", "测试流程5");
                        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                    } else {
                        LogUtil.e("测试流程6", "测试流程6");
                        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                        WindowService.start(this);
                    }
                }
                break;
        }
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, StudyWindowActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.btn_show_pop2)
    public void onClick() {
        showToast("点击了下面内容");
    }
}