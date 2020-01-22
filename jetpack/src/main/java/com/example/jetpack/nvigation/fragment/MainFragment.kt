package com.example.jetpack.nvigation.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.example.jetpack.R
import com.example.jetpack.nvigation.IMainFragmentView
import com.example.jetpack.nvigation.MainFragmentPresenter
import com.example.jetpack.viewmodel.OneViewModel
import com.framework.common.adapter.BaseAdapter
import com.framework.common.base_mvp.BasePageFragment
import com.framework.common.data.LoadType
import com.framework.common.data.LoadViewType
import com.framework.common.item_decoration.GridSpaceItemDecoration
import com.framework.common.utils.ToastUtil
import com.framework.common.utils.UIHelper
import com.framework.model.demo.ProductBean
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BasePageFragment<ProductBean>(),IMainFragmentView{
    private lateinit var mOneViewModel:OneViewModel//使用 lateinit 推迟属性初始化
    private var mAdapter:TestSaveAdapter? = null
    private var mPresenter:MainFragmentPresenter? = null

    override fun getAdapter(): BaseAdapter<ProductBean, out BaseViewHolder> {
        return mAdapter!!
    }

    override fun getSmartRefreshLayout(): SmartRefreshLayout {
        return smart_RefreshLayout
    }

    override fun getRecyclerView(): RecyclerView {
        return recycler_View
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun bindData() {
        mAdapter = TestSaveAdapter(recyclerView)
        super.bindData()
        recycler_View.addItemDecoration(GridSpaceItemDecoration(UIHelper.dip2px(5f), UIHelper.dip2px(5f), 0))
        mOneViewModel = ViewModelProviders.of(this).get(OneViewModel::class.java!!)
        //mPresenter!!.getFirst(LoadType.LOAD)
        mOneViewModel.getFirst(LoadType.LOAD)
        //ToastUtil.show(context,mOneViewModel.test.toString())
    }

    override fun initEvent() {
        super.initEvent()
        mAdapter!!.setRefreshListener(object : BaseAdapter.RefreshListener {
            override fun onRefresh() {
                mOneViewModel.getFirst(LoadType.NONE)
            }

            override fun onLoadMore() {
                mOneViewModel.getMore()
            }
        })
        btn_one.setOnClickListener {
            //点击跳转page2
            Navigation.findNavController(it).navigate(R.id.action_page2)
            //mOneViewModel.test = 100
        }
        mOneViewModel.presellBean.observe(this,Observer{
            onPageData(it.commodityList,mOneViewModel.currentPage,mOneViewModel.pageSize)
        })

        mOneViewModel.loading.observe(this, Observer {
            when(it){
                LoadViewType.HIDE_LOAD->hideLoading()
                LoadViewType.HIDE_DIALOG-> hideLoadingDialog()
                LoadViewType.SHOW_DIALOG->showLoadingDialog()
                LoadViewType.SHOW_LOAD->showLoading()
            }
        })
        mOneViewModel.errorView.observe(this, Observer {
            if(it){
                showErrorView()
            }else{
                hideErrorView()
            }
        })
    }

    override fun getPresenter(): MainFragmentPresenter{
        if(mPresenter==null){
            mPresenter = MainFragmentPresenter()
        }
        return mPresenter!!
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
