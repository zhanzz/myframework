package com.bigkoo.convenientbanner.listener;

import android.view.View;

/**
 * Created by Sai on 15/11/13.
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view, int position,T data);
}
