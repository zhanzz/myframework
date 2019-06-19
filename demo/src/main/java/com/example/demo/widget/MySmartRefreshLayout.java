package com.example.demo.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author zhangzhiqiang
 * @date 2019/6/19.
 * description：
 */
public class MySmartRefreshLayout extends SmartRefreshLayout {
    public MySmartRefreshLayout(Context context) {
        super(context);
    }

    public MySmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setStateRefreshing(boolean notify) {
        int oldHeight = mHeaderHeight;
        mHeaderHeight = (int) (mHeaderHeight*mHeaderTriggerRate);
        super.setStateRefreshing(notify);
        mHeaderHeight = oldHeight;
    }
}
