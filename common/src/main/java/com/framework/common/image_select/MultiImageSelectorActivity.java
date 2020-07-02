package com.framework.common.image_select;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.framework.common.R;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.manager.PermissionManager;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorActivity extends BaseActivity implements MultiImageSelectorFragment.Callback{

    private static final String[] LOCAL_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    /** 最大图片选择次数，int类型，默认9 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，默认多选 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，默认显示 */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "select_result";
    /** 默认选择集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
    public static final int MODE_MULTI = 1;

    private ArrayList<Uri> resultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount;
    private int mode;
    private boolean isShow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_default;
    }

    @Override
    public void getParamData(Intent intent) {
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if(mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getParcelableArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }
    }

    @Override
    public void bindData() {
        requestNeedPermissions(LOCAL_PERMISSION);
    }

    @Override
    public void initEvent() {
        // 返回按钮
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        // 完成按钮
        mSubmitButton = findViewById(R.id.commit);
        if(resultList == null || resultList.size()<=0){
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }else{
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
            mSubmitButton.setEnabled(true);
        }
        if(MODE_SINGLE == mode){
            mSubmitButton.setVisibility(View.INVISIBLE);
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultList != null && resultList.size() >0){
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putParcelableArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        init();
    }

    private void init(){
        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putParcelableArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commitAllowingStateLoss();
    }

    @Override
    public void failPermission(@NonNull List<String> permissions, int requestCode) {
        finish();
    }

    @Override
    public void onSingleImageSelected(Uri path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putParcelableArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(Uri path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if(resultList.size() > 0){
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
            if(!mSubmitButton.isEnabled()){
                mSubmitButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(Uri path) {
        if(resultList.contains(path)){
            resultList.remove(path);
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
        }else{
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
        }
        // 当为选择图片时候的状态
        if(resultList.size() == 0){
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(Uri imageFile) {
        if(imageFile != null) {
            Intent data = new Intent();
            resultList.add(imageFile);
            data.putParcelableArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    public static void startMe(Activity activity, int num, int requestCode){
        Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, num);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startMe(Activity activity, int num,ArrayList<Uri> selectUri, int requestCode){
        Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, num);
        intent.putParcelableArrayListExtra(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST,selectUri);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startMe(Activity activity,int requestCode){
        Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        activity.startActivityForResult(intent, requestCode);
    }
}
