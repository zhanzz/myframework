package com.example.jetpack.nvigation.fragment

import androidx.navigation.Navigation
import com.example.jetpack.R
import com.framework.common.base_mvp.BaseFragment
import com.framework.common.base_mvp.BasePresenter
import kotlinx.android.synthetic.main.fragment_one.*

class OneFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_one
    }

    override fun bindData() {
    }

    override fun initEvent() {
        btn_one.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    override fun getPresenter(): BasePresenter<*> ?{
        return null
    }
}
