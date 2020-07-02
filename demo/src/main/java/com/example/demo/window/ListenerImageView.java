package com.example.demo.window;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ListenerImageView  extends ImageView {
    private StringBuilder builder = new StringBuilder();

    public ListenerImageView(Context context) {
        super(context);
        init();
    }

    public ListenerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListenerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ListenerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEnabled(true);

        setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction()==KeyEvent.ACTION_UP){
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
    }

    public interface ContentListener{
        void onContent(String content);
    }

    private ContentListener mListener;

    public void setContentListener(ContentListener listener){
        mListener = listener;
    }
}
