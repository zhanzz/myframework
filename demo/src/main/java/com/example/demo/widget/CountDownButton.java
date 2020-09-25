package com.example.demo.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by zhangzhiqiang on 2016/5/23.
 * 倒计时button
 */
public class CountDownButton extends Button {
    private int TOTAL_TIME = 120;//总倒计时时间秒

    public CountDownButton(Context context) {
        super(context);
        init();
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setEnabled(true);
        //setBackgroundResource(R.drawable.selector_countdown_bg);
        //setTextColor(createColorStateList(0xff666666,0xffffffff));
    }

    private CountDownTimer mTimer = new CountDownTimer(TOTAL_TIME*1000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            setText(String.format(Locale.CHINA,"剩余%ss",millisUntilFinished/1000));
            setEnabled(false);
        }

        @Override
        public void onFinish() {
            setText("重新发送");
            setEnabled(true);
        }
    };

    public void start(){
        mTimer.start();
    }

    public void cancel(){
        mTimer.cancel();
        setText("重新发送");
        setEnabled(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        cancel();
        super.onDetachedFromWindow();
    }

    /** 对TextView设置不同状态时其文字颜色。 */
    private ColorStateList createColorStateList(int normal, int enable) {
        int[] colors = new int[] {enable,normal};
        int[][] states = new int[2][];
        states[0] = new int[] { android.R.attr.state_enabled};
        states[1] = new int[] {}; //normal
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public int getTOTAL_TIME() {
        return TOTAL_TIME;
    }

    public void setTOTAL_TIME(int TOTAL_TIME) {
        this.TOTAL_TIME = TOTAL_TIME;
    }
}
