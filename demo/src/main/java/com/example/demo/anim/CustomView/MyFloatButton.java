package com.example.demo.anim.CustomView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFloatButton extends FloatingActionButton {
    public MyFloatButton(@NonNull Context context) {
        super(context);
    }

    public MyFloatButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFloatButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public float getTranslationY() {
        return super.getTranslationY();
    }
}
