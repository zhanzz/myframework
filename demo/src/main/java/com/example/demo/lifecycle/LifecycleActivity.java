package com.example.demo.lifecycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.demo.R;
import com.example.demo.databinding.ActivityLifecycleBinding;
import com.framework.model.demo.User;

/**
 * Android Architecture Components
 * android架构组件
 */
public class LifecycleActivity extends AppCompatActivity {
    ActivityLifecycleBinding binding;
    UserViewModel userViewModel;
    public class MyObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void connectListener() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void disconnectListener() {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lifecycle);
        /*binding = ActivityLifecycleBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());*/
        getLifecycle().addObserver(new MyObserver());

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init("44");
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user);
            }
        });
        //userViewModel.getUser().setValue();
    }
}
