package com.example.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.demo.R;

public class MyCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {
    private int mViewWidth;
    private int mViewHeight;
    private Paint mDeafultPaint = new Paint();

    public MyCheckBox(Context context) {
        super(context);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CompoundButton, 0, 0);
        //final Drawable d = a.getDrawable(R.styleable.CompoundButton_button);
        int value = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android","button",0);
        Log.e("zhang","value="+value);
        //        if (d != null) {
//            setButtonDrawable(d);
//        }
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //mCompoundButtonHelper.loadFromAttributes(attrs, defStyleAttr);4.4及以下
    //会使用R.styleable.CompoundButton_buttonCompat导致 button='@null'不能去掉默认样式
    @Override
    public void setButtonDrawable(@Nullable Drawable drawable) {
        super.setButtonDrawable(drawable);
    }

    @Override
    public void setButtonDrawable(int resId) {
        super.setButtonDrawable(resId);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 移动canvas,mViewWidth与mViewHeight在 onSizeChanged 方法中获得
        canvas.translate(mViewWidth/2,mViewHeight/2);

        RectF rect1 = new RectF();              // 存放测量结果的矩形

        Path path = new Path();                 // 创建Path并添加一些内容
        path.lineTo(100,-50);
        path.lineTo(100,50);
        path.close();
        path.addCircle(-100,0,100, Path.Direction.CW);

        path.computeBounds(rect1,true);         // 测量Path

        canvas.drawPath(path,mDeafultPaint);    // 绘制Path

        mDeafultPaint.setStyle(Paint.Style.STROKE);
        mDeafultPaint.setColor(Color.RED);
        canvas.drawRect(rect1,mDeafultPaint);   // 绘制边界

    }
}
