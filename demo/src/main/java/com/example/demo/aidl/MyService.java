package com.example.demo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private static final String TAG="wcy";
    //实例化一个AidlBinder对象
    //private MyBinder myBinder=new MyBinder(this);
    MyAidlBinder mAidlBinder=new MyAidlBinder(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAidlBinder;
    }

    public void serviceMothod(String str){
        Log.i(TAG,"receive msg from activity:"+str);
    }


}
