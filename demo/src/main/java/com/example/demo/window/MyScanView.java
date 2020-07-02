package com.example.demo.window;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.example.demo.R;
import com.example.demo.R2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyScanView extends ConstraintLayout {
    private StringBuilder builder = new StringBuilder();
    @BindView(R2.id.iv_scan_tip)
    ImageView ivScanTip;
    @BindView(R2.id.group_sha_lou)
    Group groupShaLou;

    public MyScanView(Context context) {
        super(context);
        init();
    }

    public MyScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_scan_code, this);
        ButterKnife.bind(this,this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEnabled(true);

        setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction()== KeyEvent.ACTION_UP){
                if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                    if(mListener!=null){
                        mListener.onContent(builder.toString());
                        builder.delete(0,builder.length());
                    }
                }else {
                    builder.append((char)event.getUnicodeChar());
                }
            }
            return true;
        });

        setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                groupShaLou.setVisibility(View.GONE);
                ivScanTip.setVisibility(View.VISIBLE);
            }else {
                groupShaLou.setVisibility(View.VISIBLE);
                ivScanTip.setVisibility(View.GONE);
                builder.delete(0,builder.length());
            }
        });
    }

    @OnClick(R2.id.tv_action)
    public void onClick() {
        requestFocus();
    }

    public interface ContentListener{
        void onContent(String content);
    }

    private ListenerImageView.ContentListener mListener;

    public void setContentListener(ListenerImageView.ContentListener listener){
        mListener = listener;
    }
}
