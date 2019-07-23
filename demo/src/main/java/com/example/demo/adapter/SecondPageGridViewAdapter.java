package com.example.demo.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.ActivityBean;
import java.util.List;
/**
 * Created by zhangzhiqiang on 2016/6/3.
 */
public class SecondPageGridViewAdapter extends BaseQuickAdapter<ActivityBean.ActBean, BaseViewHolder> {
    private int mImageWidth,mItemWidth;

    public SecondPageGridViewAdapter(List<ActivityBean.ActBean> datas) {
        super(R.layout.item_second_page_gridview_module,datas);
        int width = UIHelper.getDisplayWidth() - UIHelper.dip2px(20);
        mItemWidth = (int) (width / 5.0f + 0.5f);
        mImageWidth = mItemWidth -UIHelper.dip2px(22);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = super.onCreateDefViewHolder(parent, viewType);
        viewHolder.itemView.getLayoutParams().width = mItemWidth;
        SimpleDraweeView image = viewHolder.getView(R.id.iv_module_image);
        image.getLayoutParams().width = image.getLayoutParams().height = mImageWidth;
        return viewHolder;
    }

    @Override
    public void convert(BaseViewHolder holder, final ActivityBean.ActBean item) {
        SimpleDraweeView image = holder.getView(R.id.iv_module_image);
        FrescoUtils.showThumb(item.getActPicUrl(),image,mImageWidth,mImageWidth);
        holder.setText(R.id.tv_name,item.getActName());
        try{
            holder.setTextColor(R.id.tv_name, Color.parseColor(item.getFontColor()));
        }catch (Exception e){
            e.printStackTrace();
        }
//        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SecondPageListener(mContext, item.getActName(), item.getJumpType(),item.getParams()).onClick(null);
//            }
//        });
    }
}
