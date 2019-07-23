package com.example.reactnative.module;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import javax.annotation.Nonnull;

/**
 * @author zhangzhiqiang
 * @date 2019/7/22.
 * description：
 */
public class ScanModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final int SCAN_REQUEST = 0x20;
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_PICKER_CANCELLED = "E_PICKER_CANCELLED";
    private static final String E_FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER";
    private static final String E_NO_IMAGE_DATA_FOUND = "E_NO_IMAGE_DATA_FOUND";

    private Promise mPickerPromise;

    public ScanModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Scan";
    }

    @ReactMethod
    public void scan(final Promise promise){
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        // Store the promise to resolve/reject when picker returns data
        mPickerPromise = promise;
        try {
            CaptureActivity.startMeForResult(currentActivity,SCAN_REQUEST);
        } catch (Exception e) {
            mPickerPromise.reject(E_FAILED_TO_SHOW_PICKER, e);
            mPickerPromise = null;
        }
    }


    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_REQUEST) {
            if (mPickerPromise != null) {
                if (resultCode == Activity.RESULT_CANCELED) {
                    mPickerPromise.reject(E_PICKER_CANCELLED, "Scan was cancelled");
                } else if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                    if (TextUtils.isEmpty(result)) {
                        mPickerPromise.reject(E_NO_IMAGE_DATA_FOUND, "No result data found");
                    } else {
                        mPickerPromise.resolve(result);
                    }
                }
                mPickerPromise = null;
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
