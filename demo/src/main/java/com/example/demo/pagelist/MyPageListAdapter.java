package com.example.demo.pagelist;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;

import com.example.demo.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.adapter.LoadMoreView;
import com.framework.common.adapter.ViewHolder;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.ProductBean;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/11/11.
 * description：
 */
public class MyPageListAdapter extends PagedListAdapter<ProductBean, ViewHolder> {

    private int mImageWidth;

    private LoadingState listState = LoadingState.Normal;

    public MyPageListAdapter() {
        super(comparator);
        mImageWidth = (UIHelper.getDisplayWidth()-UIHelper.dip2px(5))/2;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.createViewHolder(parent.getContext(), parent, R.layout.item_grid_product);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductBean commodity = getItem(position);
        SimpleDraweeView iv=holder.getView(R.id.img_good);
        iv.getLayoutParams().height = mImageWidth;
        if(TextUtils.isEmpty(commodity.getThumPicture())){
            FrescoUtils.showThumb(commodity.getProductIcon(), iv,mImageWidth,mImageWidth);
        }else{
            FrescoUtils.showThumb(commodity.getThumPicture(),iv,mImageWidth,mImageWidth);
        }
        holder.setText(R.id.txt_good_name,commodity.getProductName());
    }

    private static DiffUtil.ItemCallback<ProductBean> comparator = new DiffUtil.ItemCallback<ProductBean>(){
        @Override
        public boolean areItemsTheSame(@NonNull ProductBean oldItem, @NonNull ProductBean newItem) {
            // 返回两个item是否相同
            // 例如：此处两个item的数据实体是User类，所以以id作为两个item是否相同的依据
            // 即此处返回两个user的id是否相同
            return TextUtils.equals(oldItem.getProductId(),newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductBean oldItem, @NonNull ProductBean newItem) {
            // 当areItemsTheSame返回true时，我们还需要判断两个item的内容是否相同
            // 此处以User的age作为两个item内容是否相同的依据
            // 即返回两个user的age是否相同
            return TextUtils.equals(oldItem.getSkuId(),newItem.getSkuId());
        }
    };
}
