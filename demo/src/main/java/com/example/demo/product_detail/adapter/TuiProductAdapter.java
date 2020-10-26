package com.example.demo.product_detail.adapter;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.ProductBean;

public class TuiProductAdapter extends BaseAdapter<ProductBean, BaseViewHolder> {
    private int mImageWidth;
    public TuiProductAdapter(RecyclerView recyclerView) {
        super(recyclerView,R.layout.item_grid_product);
        mImageWidth = (UIHelper.getDisplayWidth()-UIHelper.dip2px(5))/2;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ProductBean commodity) {
        SimpleDraweeView iv=holder.getView(R.id.img_good);
        Log.e("width","iv.getLayoutParams().weight="+iv.getLayoutParams().width);
        iv.getLayoutParams().height = mImageWidth;
        if(TextUtils.isEmpty(commodity.getThumPicture())){
            FrescoUtils.showThumb(commodity.getProductIcon(), iv,mImageWidth,mImageWidth);
        }else{
            FrescoUtils.showThumb(commodity.getThumPicture(),iv,mImageWidth,mImageWidth);
        }
        holder.setText(R.id.txt_good_name,commodity.getProductName());
    }
}
