package com.example.demo.product_detail;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class GestureView extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {
    private static final String DEBUG_TAG = "GestureView";
    private GestureDetectorCompat mDetector;
    private VelocityTracker mVelocityTracker = null;
    private ScaleGestureDetector mScaleDetector;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX;
    private float mPosY;

    public GestureView(Context context) {
        super(context);
        init();
    }

    public GestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(getContext(),this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

        mScaleDetector = new ScaleGestureDetector(getContext(),this);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("zhang","valueKey="+event.getKeyCode());
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event){
////        if (this.mDetector.onTouchEvent(event)) {
////            return true;
////        }
////        return super.onTouchEvent(event);
//
//        int index = event.getActionIndex();
//        int action = event.getActionMasked();
//        int pointerId = event.getPointerId(index);
//
//        switch(action) {
//            case MotionEvent.ACTION_DOWN:
//                if(mVelocityTracker == null) {
//                    // Retrieve a new VelocityTracker object to watch the
//                    // velocity of a motion.
//                    mVelocityTracker = VelocityTracker.obtain();
//                }
//                else {
//                    // Reset the velocity tracker back to its initial state.
//                    mVelocityTracker.clear();
//                }
//                // Add a user's movement to the tracker.
//                mVelocityTracker.addMovement(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mVelocityTracker.addMovement(event);
//                // When you want to determine the velocity, call
//                // computeCurrentVelocity(). Then call getXVelocity()
//                // and getYVelocity() to retrieve the velocity for each pointer ID.
//                mVelocityTracker.computeCurrentVelocity(1000);
//                // Log velocity of pixels per second
//                // Best practice to use VelocityTrackerCompat where possible.
//                //您应该在 ACTION_MOVE 事件（而不是 ACTION_UP）发生后计算速度。在 ACTION_UP 发生后，X 速度和 Y 速度将为 0。
//                Log.d("zhang", "X velocity: " + mVelocityTracker.getXVelocity(pointerId));
//                Log.d("zhang", "Y velocity: " + mVelocityTracker.getYVelocity(pointerId));
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                // Return a VelocityTracker object back to be re-used by others.
//                if (mVelocityTracker != null) {
//                    mVelocityTracker.recycle();
//                    mVelocityTracker = null;
//                }
//                break;
//        }
//        return true;
//    }

    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = ev.getActionIndex();
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                Log.e("zhang","Downvalue=="+ev.getActionIndex());
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                invalidate();

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                int index = ev.getActionIndex();
                Log.e("zhang","value=="+index);
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                Log.e("zhang","Downvalue=="+ev.getActionIndex());
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                Log.e("zhang","Downvalue=="+ev.getActionIndex());
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }


    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

    private  float mScaleFactor;
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScaleFactor *= detector.getScaleFactor();
        Log.e("scale","value="+detector.getScaleFactor());
        // Don't let the object get too small or too large.
        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
