package com.framework.common.base_mvp;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.framework.common.R;

/**
 * @author zhangzhiqiang
 * @date 2019/4/18.
 * description：
 */
public class LoadingAndErrorView extends FrameLayout implements View.OnClickListener{

    private View mLinearRetry;
    private ProgressBar mProgressBar;
    private int mCount,mCountDialog;
    public LoadingAndErrorView(Context context) {
        super(context);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.layout_loading_error,this);
        mLinearRetry = findViewById(R.id.linear_retry);
        mProgressBar = findViewById(R.id.progress_bar);
        mLinearRetry.setOnClickListener(this);
    }

    public void showLoading(){
        mCount++;
        mLinearRetry.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showLoadingDialog(){
        mCountDialog++;
        mLinearRetry.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        setClickable(true);
    }

    public void hideLoading(){
        mCount--;
        if(mCount<=0){
            mCount = 0;
        }
        if(mCount<=0&&mCountDialog<=0){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void hideLoadingDialog(){
        mCountDialog--;
        if(mCountDialog<=0){
            mCountDialog = 0;
            setClickable(false);
        }
        if(mCount<=0&&mCountDialog<=0){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void showError(){
        mCount = 0;
        mCountDialog = 0;
        mProgressBar.setVisibility(View.INVISIBLE);
        mLinearRetry.setVisibility(View.VISIBLE);
    }

    public void hideError(){
        mLinearRetry.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        mLinearRetry.setVisibility(View.INVISIBLE);
        if(mListener!=null){
            mListener.clickRetry();
        }
    }

    public interface ActionListener{
        void clickRetry();
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener){
        mListener = listener;
    }
}
