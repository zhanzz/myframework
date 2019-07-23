package com.example.retrofitframemwork.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zhangzhiqiang
 * @date 2019/5/28.
 * description：
 */
public class TestDrawView extends View {
    public TestDrawView(Context context) {
        super(context);
    }

    public TestDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestDrawView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestDrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(1.5f,1.5f);
        Paint paint = new Paint();
        paint.setTextSize(30);
        canvas.drawText("我",getWidth()/2,getHeight()/2,paint);
        //new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP); //先画的dest,后画的src
    }
}
