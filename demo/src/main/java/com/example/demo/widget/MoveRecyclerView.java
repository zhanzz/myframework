package com.example.demo.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
/**
 * Created by zhangzhiqiang on 2017/2/9.
 */
public class MoveRecyclerView extends RecyclerView {
    private float mLastX;
    private int mMoveLastDis;//能够拖动的最大距离
    private VelocityTracker vTracker = null;
    private float mMoveSpeed;//滑动需达到的速度才能让为是触发动作
    private boolean mIsNeedMore = true;
    public MoveRecyclerView(Context context) {
        super(context);
        init();
    }

    public MoveRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        DisplayMetrics mc=getResources().getDisplayMetrics();
        mMoveLastDis = (int) (mc.density*10);
        mMoveSpeed = -600;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(mIsNeedMore&&isLastItemVisible()){
            switch (e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mLastX = e.getX();
                    if(vTracker == null){
                        vTracker = VelocityTracker.obtain();
                    }else{
                        vTracker.clear();
                    }
                    vTracker.addMovement(e);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx=e.getX()-mLastX;
                    mLastX = e.getX();
                    if(dx<0){
                        setTranslationX(getTranslationX()+dx);
                        if(getTranslationX()<-mMoveLastDis){
                            setTranslationX(-mMoveLastDis);
                        }
                    }else{
                        setTranslationX(getTranslationX()+dx);
                        if(getTranslationX()>0){
                            setTranslationX(0);
                        }
                    }
                    if(vTracker == null){
                        vTracker = VelocityTracker.obtain();
                    }
                    if(vTracker!=null){
                        vTracker.addMovement(e);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    mLastX = 0;
                    setTranslationX(0);
                    if(vTracker!=null){
                        vTracker.computeCurrentVelocity(1000);
                        //Log.e("zhang","v="+vTracker.getXVelocity());
                        if(vTracker.getXVelocity()<mMoveSpeed){ //速度为负数
                            //Toast.makeText(getContext(),"触发了动作v="+vTracker.getXVelocity(),Toast.LENGTH_SHORT).show();
                            if(mListener!=null){
                                mListener.onNext();
                            }
                        }
                        vTracker.recycle();
                        vTracker = null;
                    }
                    break;
            }
            return super.onTouchEvent(e);
        }else{
            setTranslationX(0);
            return super.onTouchEvent(e);
        }
    }

    private OnActionNext mListener;
    public void setOnActionNextListener(OnActionNext listener){
        this.mListener = listener;
    }
    public interface OnActionNext{
        void onNext();
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        RecyclerView.Adapter adapter = getAdapter();
        int position=((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
        if(null == adapter || adapter.getItemCount() == 1){
            return false;
        }
        View lastView = ((LinearLayoutManager)getLayoutManager()).findViewByPosition(position);
        return position>=adapter.getItemCount()-1&& lastView.getRight()<=getRight();
    }

    public boolean ismIsNeedMore() {
        return mIsNeedMore;
    }

    public void setmIsNeedMore(boolean mIsNeedMore) {
        this.mIsNeedMore = mIsNeedMore;
    }
}
