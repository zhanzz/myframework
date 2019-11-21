package com.example.demo.pagelist

import androidx.lifecycle.MutableLiveData
import com.framework.common.base_mvp.IBaseView

interface IDataSource {

    var mvp: IBaseView

    fun  getResultBean(): MutableLiveData<ResultBean>

    fun refresh()

    fun retry()

}