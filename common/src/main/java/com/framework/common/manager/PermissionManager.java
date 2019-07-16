package com.framework.common.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.framework.common.BaseApplication;
import com.framework.common.utils.ToastUtil;

/**
 * Created by zhenggl on 2016/5/26.
 *
 * @desc 权限管理类
 */
public class PermissionManager {

    private final String TAG = PermissionManager.class.getSimpleName();

    private static PermissionManager instance;

    public static final int READ_PHONE_STATE_CODE = 1;//读取手机权限
    public static final int CAMERA_CODE = 2;            //获取相机权限
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 3;//获取存储权限
    public static final int CALL_PHONE_CODE = 4;             //拨打电话
    public static final int ACCESS_COARSE_LOCATION_CODE = 5;//定位相关


    private PermissionManager() {
    }

    public static PermissionManager getInstance(){
        synchronized (PermissionManager.class){
            if(null == instance){
                instance = new PermissionManager();
            }
            return instance;
        }
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 权限是否被禁用 true是，false 不是
     * @param permission
     * @return
     */
    public boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(BaseApplication.getApp(), permission)
                == PackageManager.PERMISSION_DENIED;
    }

    /**
     * 是否有该权限 true 有，false无
     * @param permission
     * @return
     */
    public boolean hasPremission(String permission){
        return ContextCompat.checkSelfPermission(BaseApplication.getApp(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 显示一个toast提示
     * @param permission 要显示的提示的权限
     */
    public void toastTip(Context context, String permission){
        ToastUtil.show(context,toDescription(permission));
    }

    public String toDescription(String permission) {
        switch (permission) {
            case Manifest.permission.READ_PHONE_STATE:
                return "需要在系统“权限”中打开“电话”开关，才能更好的为你服务";
            case Manifest.permission.CAMERA:
                return "需要在系统“权限”中打开“相机”开关，才能相机拍照";
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return "需要在系统“权限”中打开“存储”开关，才能离线缓存";
            case Manifest.permission.CALL_PHONE:
                return "需要在系统“权限”中打开“拨打电话”开关，才能拨打电话";
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return "需要在系统“权限”中打开“定位”开关，才能发起定位动作";
            default:
                return "需要在系统“权限”中打开相关权限，才能更好的为你服务";
        }
    }

}
