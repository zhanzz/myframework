package com.example.demo.aidl;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.example.demo.IMyAidlInterface;
import com.example.demo.IMyCallbackListener;

public class MyAidlBinder extends IMyAidlInterface.Stub {
    //private IMyCallbackListener mListener;
    //作为服务要能处理多个listener
    private RemoteCallbackList<IMyCallbackListener> mListener=new RemoteCallbackList<>();
    private MyService mService;

    public MyAidlBinder(MyService service) {
        this.mService = service;
    }

    @Override
    public void testMethod(String str) throws RemoteException {
        mService.serviceMothod(str);
    }

    @Override
    public void regisListener(IMyCallbackListener listener) throws RemoteException {
        //mListener = listener;
        mListener.register(listener);
    }

    @Override
    public void unregisListener(IMyCallbackListener listener) throws RemoteException {
        mListener.unregister(listener);
    }
}
