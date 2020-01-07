package com.example.jetpack.nvigation.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.jetpack.R
import com.example.jetpack.viewmodel.OneViewModel
import com.framework.common.base_mvp.BaseFragment
import com.framework.common.base_mvp.BasePresenter
import com.framework.model.demo.PresellBean
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment() {

    private lateinit var mOneViewModel:OneViewModel//使用 lateinit 推迟属性初始化

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun bindData() {
        mOneViewModel = ViewModelProviders.of(this).get(OneViewModel::class.java!!)
    }

    override fun initEvent() {
        btn_one.setOnClickListener {
            //点击跳转page2
            Navigation.findNavController(it).navigate(R.id.action_page2)
        }
        mOneViewModel.presellBean.observe(this,Observer<PresellBean>{

        })
    }

    override fun getPresenter(): BasePresenter<*> ?{
        return null
    }
}
