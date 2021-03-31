package com.example.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.demo.R;
import com.framework.common.utils.LogUtil;

/**
 * @author zhangzhiqiang
 * @date 2020/1/10.
 * description：
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    //先画为dst后画为src
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyTextView, R.attr.xin_tv_attr, R.style.xin_tv2);
        int color2 = typedArray.getColor(R.styleable.MyTextView_xin_color, 0xffeeeeee);
        typedArray.recycle();
        setTextColor(color2);
        String result = Integer.toHexString(color2);
        LogUtil.e("color",result);
        //set>defStyleAttr(主题可配置样式)>defStyleRes(默认样式)>NULL(主题中直接指定)
        int count = attrs.getAttributeCount();
        for(int i=0;i<count;i++){
            String name = attrs.getAttributeName(i);
            LogUtil.e("value",
                    String.format("name=%s;value=%s;getAttributeNameResource=%s",
                            name,attrs.getAttributeValue(i),attrs.getAttributeNameResource(i)));
            if(name.equals("id")){
               int id = Integer.parseInt(attrs.getAttributeValue(i).substring(1));
               LogUtil.e("value",String.format("id=%d",id));//@656513132
               String resourceName =getResources().getResourceName(id);
               String entryName = getResources().getResourceEntryName(id);
               String packageName = getResources().getResourcePackageName(id);
               String typeName = getResources().getResourceTypeName(id);
               int value =  attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "id", 0);
               String getAttributeValue =  attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
               LogUtil.e("value",
                       String.format("resourceName=%s;entryName=%s;packageName=%s;typeName=%s;getAttributeResourceValue=%s;getAttributeValue=%s",
                       resourceName,entryName,packageName,typeName,value,getAttributeValue));
            }else if("textColor".equalsIgnoreCase(name)){
                String id = attrs.getAttributeValue(i).substring(1);
                LogUtil.e("value",String.format("id=%s",id));//#ff333333
                //String resourceName =getResources().getResourceName(id);
                //String entryName = getResources().getResourceEntryName(id);
                //String packageName = getResources().getResourcePackageName(id);
                //String typeName = getResources().getResourceTypeName(id);
//                int value =  attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "id", 0);
//                String getAttributeValue =  attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
//                LogUtil.e("value",
//                        String.format("resourceName=%s;entryName=%s;packageName=%s;typeName=%s;getAttributeResourceValue=%s;getAttributeValue=%s",
//                                resourceName,entryName,packageName,typeName,value,getAttributeValue));
            }else if("layout_width".equalsIgnoreCase(name)){
                int value =  attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "layout_width", 0);
                String getAttributeValue =  attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
                int id = attrs.getAttributeNameResource(i);
                LogUtil.e("value",String.format("id=%d",id));
                String resourceName =getResources().getResourceName(id);
                String entryName = getResources().getResourceEntryName(id);
                String packageName = getResources().getResourcePackageName(id);
                String typeName = getResources().getResourceTypeName(id);

                LogUtil.e("value",
                        String.format("resourceName=%s;entryName=%s;packageName=%s;typeName=%s;getAttributeResourceValue=%s;getAttributeValue=%s",
                                resourceName,entryName,packageName,typeName,value,getAttributeValue));
            }
        }
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return super.startNestedScroll(axes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e("drawable","调用了onMeasure");
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        LogUtil.e("drawable","调用了layout");
    }

    @Override
    public void invalidate() {
        super.invalidate();

    }
}
