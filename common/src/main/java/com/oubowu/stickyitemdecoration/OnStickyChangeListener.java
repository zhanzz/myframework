package com.oubowu.stickyitemdecoration;

public interface OnStickyChangeListener {
    int getChildHeight();

    void onDataChange(int position);

    void onScrollable(int offset);

    void onInVisible();

    void setVisibility(int visibility);

    void reset();
}