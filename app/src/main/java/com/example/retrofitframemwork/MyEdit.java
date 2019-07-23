package com.example.retrofitframemwork;

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author zhangzhiqiang
 * @date 2019/4/22.
 * description：
 */
public class MyEdit extends EditText {
    public MyEdit(Context context) {
        super(context);
    }

    public MyEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyEdit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
