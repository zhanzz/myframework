package com.example.demo.aidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.demo.IMyAidlInterface;
import com.example.demo.IMyCallbackListener;
import com.example.demo.R;

public class AidlActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "FirstActivity";
    private Button mBtnMain;
    public IMyAidlInterface mAidlService;
    private MyListener myListener;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAidlService=IMyAidlInterface.Stub.asInterface(service);
            try {
                myListener = new MyListener(service);
                mAidlService.regisListener(myListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //同进程用
            //IMyAidlInterface.Stub mBinder = (IMyAidlInterface.Stub)service;
            //IMyAidlInterface.Stub mService = (IMyAidlInterface.Stub)IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        mBtnMain = findViewById(R.id.btn_main);
        //跳转到Service
        Intent intent=new Intent(this,MyService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
        mBtnMain.setOnClickListener(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当Activity销毁的时候解服务
        if(mConnection!=null){
            if(mAidlService!=null&&myListener!=null){
                try {
                    mAidlService.unregisListener(myListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            unbindService(mConnection);
            mConnection=null;
        }
    }

    @Override
    public void onClick(View v) {
        try {
            mAidlService.testMethod("hi,service.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class MyListener extends IMyCallbackListener.Stub{
        IBinder service;
        public MyListener(IBinder service){
            this.service = service;
        }

        @Override
        public void onResponse(String str) throws RemoteException {
            Log.i(TAG,"receive message from service:"+str);
        }

        @Override
        public IBinder asBinder() {
            return service;
        }
    }
}
