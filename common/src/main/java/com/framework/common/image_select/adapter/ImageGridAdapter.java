package com.framework.common.image_select.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.R;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.image_select.bean.Image;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.UIHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 */
public class ImageGridAdapter extends BaseMultiItemQuickAdapter<Image, BaseViewHolder> {
    private boolean showSelectIndicator = true;
    private List<Image> mSelectedImages = new ArrayList<>();
    private int mItemWidth,mItemHeight;
    public ImageGridAdapter() {
        super(null);
        addItemType(Image.TYPE_CAMERA,R.layout.list_item_camera);
        addItemType(Image.TYPE_NORMAL,R.layout.list_item_image);
        mItemWidth = mItemHeight = (UIHelper.getDisplayWidth()-UIHelper.dip2px(12))/4;
    }

    /**
     * 显示选择指示器
     * @param b
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    /**
     * 选择某个图片，改变选择状态
     * @param image
     */
    public void select(Image image,int position) {
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
        }else{
            mSelectedImages.add(image);
        }
        notifyItemChanged(position);
    }

    /**
     * 通过图片路径设置默认选择
     * @param resultList
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        mSelectedImages.clear();
        for(String path : resultList){
            Image image = getImageByPath(path);
            if(image != null){
                mSelectedImages.add(image);
            }
        }
        if(mSelectedImages.size() > 0){
            notifyDataSetChanged();
        }
    }

    private Image getImageByPath(String path){
        if(mData != null && mData.size()>0){
            for(Image image : mData){
                if(image.path.equalsIgnoreCase(path)){
                    return image;
                }
            }
        }
        return null;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = super.onCreateDefViewHolder(parent, viewType);
        ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
        params.width = mItemWidth;
        params.height = mItemHeight;
        return viewHolder;
    }

    @Override
    protected void convert(BaseViewHolder helper, Image item) {
        if(item.itemType == Image.TYPE_CAMERA) return;
        // 处理单选和多选状态
        if(showSelectIndicator){
            helper.setVisible(R.id.checkmark,true);
            if(mSelectedImages.contains(item)){
                // 设置选中状态
                helper.setImageResource(R.id.checkmark,R.drawable.btn_selected);
                helper.setVisible(R.id.mask,true);
            }else{
                // 未选择
                helper.setImageResource(R.id.checkmark,R.drawable.btn_unselected);
                helper.setGone(R.id.mask,false);
            }
        }else{
            helper.setGone(R.id.checkmark,false);
        }
        SimpleDraweeView simpleDraweeView = helper.getView(R.id.image);
        simpleDraweeView.setImageURI(item.uri);
//        try {
//            simpleDraweeView.setActualImageResource(R.drawable.default_error);//复用
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                Bitmap bitmap = mContext.getContentResolver().loadThumbnail(item.uri,new Size(mItemWidth,mItemHeight),null);
//                simpleDraweeView.setImageBitmap(bitmap);
//            }else {
//                FrescoUtils.showThumb("file://"+item.path,simpleDraweeView,mItemWidth,mItemHeight);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
