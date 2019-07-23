package com.example.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.framework.common.utils.UIHelper;

/**
 * @author zhangzhiqiang
 * @date 2019/7/8.
 * description：
 */
public class IndicatorView extends View {
    private Paint mBgPaint,mBarPaint;
    private int mBgColor,mBarColor;
    private float mIndicatorHeight;
    private float mRate;//比例
    private float offset;
    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mBgColor = 0xffe1e1e1;
        mBarColor = 0xffde3428;
        mIndicatorHeight = UIHelper.dip2px(3);

        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStrokeWidth(mIndicatorHeight);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);

        mBarPaint = new Paint();
        mBarPaint.setColor(mBarColor);
        mBarPaint.setStrokeWidth(mIndicatorHeight);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);
        mBarPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 计算出bar的宽度比例
     * @param extent
     * @param range
     */
    public void setExtentAndRange(int extent,int range){
        mRate = extent/(float)range;
    }

    public void setOffset(int offset,int range){
        this.offset = offset/(float)range;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if((mRate-1)>0.01){ //可滚动
            RectF rect = new RectF(0,0,getWidth(),mIndicatorHeight);
            canvas.drawRoundRect(rect,100,100,mBgPaint);
            RectF rectBar = new RectF(offset*getWidth(),0,getWidth()*(mRate+offset),mIndicatorHeight);
            canvas.drawRoundRect(rectBar,100,100,mBarPaint);
        }
    }
}
