package com.example.retrofitframemwork.login.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter.base.BaseViewHolder
import com.example.retrofitframemwork.R
import com.framework.common.adapter.BaseAdapter

/**
 * @author zhangzhiqiang
 * @date 2019/7/18.
 * description：
 */
class HomeAdapter(recyclerView: RecyclerView) : BaseAdapter<String, BaseViewHolder>(recyclerView, R.layout.item_home) {

    override fun convert(helper: BaseViewHolder, item: String) {
        if(helper.itemView is TextView){
            (helper.itemView as TextView).setText(item)
        }
    }
}
