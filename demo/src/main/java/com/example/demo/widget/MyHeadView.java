package com.example.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.demo.R;

public class MyHeadView extends View {
    private Bitmap mBitmap;
    private Paint mPaint,mBitmapPaint,mWaterPaint;
    private int mWidth, mHeight;

    private int mRadius;
    private int mCircleX, mCircleY;

    private int mBorderColor;
    private Paint mBorderPaint;
    private int mBorderWidth;
    private PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
    private Paint mTextPaint;

    public MyHeadView(Context context) {
        super(context);
        init(context,null);
    }

    public MyHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        if(attrs==null){
            return;
        }
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.MyHeadView);
        mBorderColor = type.getColor(R.styleable.MyHeadView_mborder_color,0);
        Drawable drawable = type.getDrawable(R.styleable.MyHeadView_msrc);
        //将获得的 Drawable 转换成 Bitmap
        if(drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            mBitmap = bitmapDrawable.getBitmap();
        }
        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(0xffffff00);
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBorderWidth = type.getDimensionPixelSize(R.styleable.MyHeadView_mborder_width, 2);

        mWaterPaint = new Paint();
        mWaterPaint.setColor(0xff0000ff);
        mWaterPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);
        int temp = Math.min(mWidth, mHeight);
        mWidth = mHeight = temp;
        initView();
        setMeasuredDimension(mWidth, mHeight);
    }

    private int measureHeight(int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int sizeMode = MeasureSpec.getMode(heightMeasureSpec);
        int result = 0;
        if (sizeMode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 200;
            if (sizeMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int sizeMode = MeasureSpec.getMode(widthMeasureSpec);
        int result = 0;
        if (sizeMode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 200;
            if (sizeMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private void initView() {
        mTextPaint = new Paint();
        mTextPaint.setColor(0xffde3428);
        mTextPaint.setTextSize(24);
        if(mBitmap==null){
            return;
        }
        Bitmap mSrcBitmap = Bitmap.createScaledBitmap(mBitmap, mWidth, mHeight, false);
        BitmapShader mShader = new BitmapShader(mSrcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setShader(mShader);
        mRadius = (mWidth - mBorderWidth * 2) / 2;
        mCircleX = (mWidth) / 2;
        mCircleY = (mHeight) / 2;

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeJoin(Paint.Join.ROUND);
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    //先画为dst后画为src
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap==null){
            return;
        }
        /**
         * canvas就像一张无限大的画布，在view中画布的原点（0，0）一般与控制的左上角在同一个位置，
         * translate就是移动原点坐标的位置，画布不会动
         * scale就是缩小坐标系的值，画布不会动，比如原来距画布上原点是20的位置， 缩小0.5后就是10的位置了
         * scale的中心会影响到原点在画布上的位置
         */
        canvas.drawText("haha",0,10,mTextPaint);
        canvas.translate(100,100);
        canvas.drawText("hehe",20,0,mTextPaint);
        Path path = new Path();
        path.addCircle(mWidth/2.0f,mHeight/2.0f, Math.min(mWidth/2.0f,mHeight/2.0f), Path.Direction.CCW);
        //canvas.clipPath(path);
        canvas.scale(0.5f,0.5f,(mWidth)/2.0f,(mHeight)/2.0f);
        canvas.drawText("hello",20,20,mTextPaint);
        canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
        /**
         * 设置View的离屏缓冲。在绘图的时候新建一个“层”，所有的操作都在该层而不会影响该层以外的图像
         * 必须设置，否则设置的PorterDuffXfermode会无效，具体原因不明
         */
//        int sc=canvas.saveLayer(0,0,mWidth,mHeight,null,Canvas.ALL_SAVE_FLAG);
//        canvas.drawRect(0,0,mWidth/2f,mHeight/2.0f,mPaint);
//        canvas.drawCircle(mCircleX, mCircleY, mRadius-20, mPaint);
////        canvas.drawCircle(mCircleX, mCircleY, mRadius, mBitmapPaint);
//        mBitmapPaint.setXfermode(mode);
//        canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
//        //canvas.drawBitmap(makeCircle(),0,0,mBitmapPaint);
////        mBitmapPaint.setXfermode(null);
//        /**
//         * 还原画布，与canvas.saveLayer配套使用
//         */
//        canvas.restoreToCount(sc);
//        canvas.drawCircle(mCircleX, mCircleY, mRadius-20, mBorderPaint);

        //类似于移动的加载动画效果 //SRC_OVER
//        int sc=canvas.saveLayer(0,0,mWidth,mHeight,null,Canvas.ALL_SAVE_FLAG);
//        canvas.drawCircle(mWidth/2,mHeight/2, Math.min(mWidth/2,mHeight/2),mWaterPaint);
//        //mBitmapPaint.setXfermode(mode);
//        canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
//        mBitmapPaint.setXfermode(null);
//        canvas.restoreToCount(sc);
    }

    /**
     * 生成圆形Bitmap
     * @return
     */
    private Bitmap makeCircle(){
        Bitmap bitmap=Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        int radius=mWidth/2;
        canvas.drawCircle(mWidth/2f,mHeight/2f,radius,paint);
        return bitmap;
    }
}
