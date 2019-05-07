package com.framework.common.callBack;

import com.framework.common.net.RequestBaseObserver;

import java.text.DecimalFormat;

public abstract class LoadCallBack<T> extends RequestBaseObserver<T> {
    DecimalFormat df = new DecimalFormat("##0.#");
    public LoadCallBack() {
    }

    @Override
    public void onNext(Object t) {
        if (t instanceof Double) {
            String percent = df.format((double)t);
            onProgress(percent);
        } else {
            super.onNext(t);
        }
    }
}