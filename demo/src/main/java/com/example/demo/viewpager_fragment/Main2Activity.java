package com.example.demo.viewpager_fragment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.demo.R;
import com.framework.common.utils.LogUtil;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("zhang",getClass().getSimpleName()+";"+getTaskId()+";pid="+android.os.Process.myPid());
        setContentView(R.layout.activity_main2);
    }
}
