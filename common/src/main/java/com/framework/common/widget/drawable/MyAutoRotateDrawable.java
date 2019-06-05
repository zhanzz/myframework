package com.framework.common.widget.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.framework.common.utils.UIHelper;

/**
 * @author zhangzhiqiang
 * @date 2019/5/28.
 * description：
 */
public class MyAutoRotateDrawable extends AutoRotateDrawable {
    // 文字的画笔
    private Paint mTextPaint;
    private int mTextColor;

    public MyAutoRotateDrawable(Drawable drawable, int interval) {
        super(drawable, interval);
        init();
    }

    public MyAutoRotateDrawable(Drawable drawable, int interval, boolean clockwise) {
        super(drawable, interval, clockwise);
        init();
    }

    private void init() {
        mTextColor = 0xff666666;
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(UIHelper.sp2px(13));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        Rect bound= getBounds();
        int baseLineY = (int) (bound.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式

        canvas.drawText(String.format("%d%%",(int)(((float) getLevel() / 10000)*100)),bound.centerX(),baseLineY,mTextPaint);
    }
}
