package com.example.jetpack.nvigation

import com.example.jetpack.DemoApi
import com.framework.common.base_mvp.BasePagePresenter
import com.framework.common.data.Result
import com.framework.common.manager.NetWorkManager
import com.framework.common.retrofit.RetorfitUtil
import com.framework.model.demo.ProductAndFilterListBean
import com.framework.model.demo.ProductBean
import io.reactivex.Observable

/**
 * @author zhangzhiqiang
 * @date 2020/1/8.
 * description：
 */
class MainFragmentPresenter : BasePagePresenter<ProductAndFilterListBean, IMainFragmentView>(10) {
    override fun onPageData(data: ProductAndFilterListBean?, mCurrentPage: Int, mPageSize: Int) {
        mvpView.onPageData(data?.commodityList,mCurrentPage,mPageSize)
    }

    override fun getObservable(params: MutableMap<String, Any>): Observable<Result<ProductAndFilterListBean>> {
        params["queryString"] = "纸"
        return RetorfitUtil.getMallRetorfitApi(DemoApi::class.java).getSearchProduct(params)
    }
}