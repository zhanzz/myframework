package com.example.jetpack.nvigation.fragment

import com.example.jetpack.R
import com.framework.common.base_mvp.BaseFragment
import com.framework.common.base_mvp.BasePresenter

class TwoFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_two
    }

    override fun bindData() {
    }

    override fun initEvent() {
    }

    override fun getPresenter(): BasePresenter<*> ?{
        return null
    }
}
