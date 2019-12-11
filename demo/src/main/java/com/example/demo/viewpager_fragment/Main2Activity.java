package com.example.demo.viewpager_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.viewpager_fragment.activity.ExpandRecyclerViewActivity;
import com.example.demo.work.UploadWorker;
import com.framework.common.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    @BindView(R2.id.btn_one)
    Button btnOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //另一个应用启动过来，pid不同， taskId相同
        LogUtil.e("zhang", getClass().getSimpleName() + ";" + getTaskId() + ";pid=" + Process.myPid());
        setContentView(R.layout.activity_demo_main2);
        ButterKnife.bind(this);
    }

    public void testWorker() {
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .build();
        uploadWorkRequest.getId();
        WorkManager.getInstance(this).enqueue(uploadWorkRequest);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Main2Activity.class);
        context.startActivity(starter);
    }

    @OnClick(R2.id.btn_one)
    public void onClick() {
        ExpandRecyclerViewActivity.start(this);
    }
}
