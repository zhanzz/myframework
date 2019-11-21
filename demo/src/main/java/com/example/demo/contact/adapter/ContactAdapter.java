package com.example.demo.contact.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo.R;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.utils.LogUtil;
import com.framework.model.demo.ContactBean;

/**
 * @author zhangzhiqiang
 * @date 2019/8/3.
 * description：
 */
public class ContactAdapter extends BaseMultiItemQuickAdapter<ContactBean, BaseViewHolder> {

    public ContactAdapter() {
        super(null);
        addItemType(ContactBean.TYPE_HEAD, R.layout.item_contact_head);
        addItemType(ContactBean.TYPE_NORMAL, R.layout.item_contact);
    }

    @Override
    protected void convert(final BaseViewHolder helper, ContactBean item) {
        switch (item.itemType) {
            case ContactBean.TYPE_HEAD:
                helper.setText(R.id.tv_name,String.valueOf(item.category));
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.e("before position="+helper.getAdapterPosition());
                        getData().add(helper.getLayoutPosition(),new ContactBean("xx","xxx",'x'));
                        notifyItemInserted(helper.getLayoutPosition());
                        //helper.getAdapterPosition() ，正在删除或调用dataSetChanged后返回为-1
                        LogUtil.e("after position="+helper.getLayoutPosition());
                    }
                });
                break;
            case ContactBean.TYPE_NORMAL:
                helper.setText(R.id.tv_phone,item.phone);
                helper.setText(R.id.tv_name,item.name);
                helper.setText(R.id.tv_category,String.valueOf(item.category));
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.e("before position="+helper.getAdapterPosition());
                        getData().add(helper.getLayoutPosition(),new ContactBean("xx","xxx",'x'));
                        notifyItemInserted(helper.getLayoutPosition());
                        //helper.getAdapterPosition() ，正在删除或调用dataSetChanged后返回为-1
                        LogUtil.e("after position="+helper.getLayoutPosition());
                    }
                });
                break;
        }
    }
}
