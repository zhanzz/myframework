package com.example.demo.pagelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.framework.common.utils.LogUtil;

import java.util.List;

public class MyViewModel extends ViewModel {
    public MediatorLiveData<String> mediator = new MediatorLiveData<>();
    public MutableLiveData<String> strLive = new MutableLiveData<>();

    public MyViewModel() {
        mediator.addSource(strLive, new Observer<String>() {
            @Override
            public void onChanged(String strings) {
                LogUtil.e("live","inner="+strings);
                mediator.setValue(strings);
            }
        });
    }

    private void setInput(String address) {
        strLive.setValue(address);
    }
}