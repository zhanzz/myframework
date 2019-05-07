package com.framework.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.common.R;

/**
 * @author zhangzhiqiang
 * @date 2019/5/6.
 * description：
 */
public class ViewUtil {
    public static View createEmptyView(Context context){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_empty_product);
        linearLayout.addView(imageView);

        TextView textView = new TextView(context);
        textView.setTextColor(0xff999999);
        textView.setTextSize(14);
        textView.setText("数据为空");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = UIHelper.dip2px(20);
        linearLayout.addView(textView,params);
        return linearLayout;
    }
}
