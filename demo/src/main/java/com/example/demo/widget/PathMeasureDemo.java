package com.example.demo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.example.demo.R;
/**
 * Created by ThinkPad on 2019/11/21.
 * 先在中心画个圆
 * 然后将图片放置到圆上 ===切线的点  不断改变角度
 * 然后让这个图片沿着这个圆开始运动   tanslate方法
 *
 */

public class PathMeasureDemo extends View{
 
 
    private Path mPath;
 
   private Paint mPaint;
 
   private Paint mLinePaint;
 
   private Bitmap mBitmap;
 
    private Matrix matrix;
    PathMeasure pathMeasure;
    private float mTotalLength;

    public PathMeasureDemo(Context context) {
        super(context);
        init();
    }
 
    public PathMeasureDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
 
    public PathMeasureDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
 
 
 
    private void init(){
        mPath =new Path();
 
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
 
        mLinePaint=new Paint();
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(4);
 
 
        BitmapFactory.Options options =new BitmapFactory.Options();
        options.inSampleSize = 4;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cart,options);
 
        matrix =new Matrix();
 
 
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //再画一个圆

        mPath.addCircle(0,0,400,Path.Direction.CW);
        Path path = new Path();
        path.moveTo(400,0);
        path.lineTo(500,500);
        //以下两种方式都可
        //mPath.addPath(path);
        mPath.lineTo(500,500);
        //然后让图片显示到圆上
        //1 最开始显示到某个点上 这个点通过PathMeasure的方法来确定  比如在0.1这个点开始
        pathMeasure =new PathMeasure(mPath,false);
        mTotalLength = pathMeasure.getLength();
    }

    private float[] pos =new float[2];
     private  float[] tan =new float[2];
     private float mFloat =0;
 
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
 
        canvas.drawLine(0,getHeight()/2,getWidth(),getHeight()/2,mPaint);//先画一条横线
 
        canvas.drawLine(getWidth()/2,0,getWidth()/2,getHeight(),mPaint);//再画一条竖线
 
        //再画一个圆
 
        //将圆心设置到中心
        canvas.translate(getWidth()/2,getHeight()/2);
 
          canvas.drawPath(mPath,mPaint);//不通过canvans直接画圆
          mFloat += 2;
        if (mFloat >= mTotalLength){
            mFloat = 0;
            if(!pathMeasure.nextContour()){
                pathMeasure.setPath(mPath,false);
            }
            mTotalLength = pathMeasure.getLength();
        }
         //从哪个点开始=pos[] 表示的是表示当前点在画布上的位置  tan[]表示的是(当前点pos与x轴切点的点的坐标)
         pathMeasure.getPosTan(mFloat,pos,tan);
 
         double degree = Math.atan2(tan[1],tan[0]) * 180.0 /Math.PI;
 
         matrix.reset();
         //将图片根据每次不同的角度 而进行旋转
         matrix.postRotate((float)degree,mBitmap.getWidth() /2,mBitmap.getHeight()/2);
         //
 
        //将图片的绘制点中心与当前点重合，通过上面我们已经获取到了当前点 所以我们将图片移动到这个位置就好了啊
        matrix.postTranslate(pos[0]-mBitmap.getWidth()/2,pos[1]-mBitmap.getHeight()/2);
 
        canvas.drawBitmap(mBitmap,matrix,mPaint);
 
       invalidate();
 
 
    }
 
 
 
}