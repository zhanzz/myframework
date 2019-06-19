package com.example.demo.react_native;

import android.content.Context;
import android.content.Intent;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView;

public class RnMainActivity extends ReactActivity {
    @Override
    protected String getMainComponentName() {
        return "ExampleNavigation";
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this, getMainComponentName()) {
            @Override
            protected ReactRootView createRootView() {
                return new RNGestureHandlerEnabledRootView(RnMainActivity.this);
            }
        };
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, RnMainActivity.class);
        context.startActivity(starter);
    }
}
