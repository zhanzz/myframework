package com.framework.common.data;

import com.framework.common.BuildConfig;
import com.framework.common.data.sharedPreference.BasePrefDao;

/**
 * @author zhangzhiqiang
 * @date 2019/7/13.
 * description：
 */
public class ConfigOperation extends BasePrefDao<ConfigBean> {
    private static final String CACHE_NAME = "config";
    public ConfigOperation() {
        super(CACHE_NAME, BuildConfig.VERSION_CODE);
    }
}
