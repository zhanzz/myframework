package com.example.demo.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CanvasView extends View {
    private Paint mPaint;
    private Path mPath;
    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(0xffde3428);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPath.lineTo(30,30);
            //mPath.moveTo(50,50);
            //mPath.quadTo(80,20,120,20);
            mPath.arcTo(0f, 0f, 100f, 100f, 270f, -180f, true);
            mPath.lineTo(100,100);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }
}
