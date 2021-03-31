package com.example.demo.camera;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class CustomLifecycle implements LifecycleOwner {
    private LifecycleRegistry lifecycleRegistry;
    public CustomLifecycle() {
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
    }

    public void doOnResume() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
    }

    public void doOnDestroy(){
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    }

    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}