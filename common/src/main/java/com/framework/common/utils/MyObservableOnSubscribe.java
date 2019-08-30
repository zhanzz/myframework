package com.framework.common.utils;

import io.reactivex.ObservableOnSubscribe;

/**
 * @author zhangzhiqiang
 * @date 2019/8/13.
 * description：
 */
public interface MyObservableOnSubscribe<T> extends ObservableOnSubscribe<T> {
    void dispose();
}
