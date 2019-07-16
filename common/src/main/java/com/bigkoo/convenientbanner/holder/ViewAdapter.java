package com.bigkoo.convenientbanner.holder;

/**
 * Created by Sai on 15/12/14.
 * @param <T> 任何你指定的对象
 */

import android.content.Context;
import android.view.View;

public interface ViewAdapter<T,V extends View>{
    V createView(Context context,int position);
    void UpdateUI(V view, int position, T data);
    int getItemType(int position);
}