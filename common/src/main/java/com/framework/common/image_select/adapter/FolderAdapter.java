package com.framework.common.image_select.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.R;
import com.framework.common.image_select.bean.Folder;
import com.framework.common.utils.FrescoUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹Adapter
 * Created by Nereo on 2015/4/7.
 */
public class FolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Folder> mFolders = new ArrayList<Folder>();

    int mImageSize;

    int lastSelected = 0;

    public FolderAdapter(Context context){
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageSize = mContext.getResources().getDimensionPixelOffset(R.dimen.folder_cover_size);
    }

    /**
     * 设置数据集
     * @param folders
     */
    public void setData(List<Folder> folders) {
        if(folders != null && folders.size()>0){
            mFolders = folders;
        }else{
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size()+1;
    }

    @Override
    public Folder getItem(int i) {
        if(i == 0) return null;
        return mFolders.get(i-1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = mInflater.inflate(R.layout.list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if(i == 0){
                holder.name.setText("所有图片");
                holder.size.setText(getTotalImageSize()+"张");
                if(mFolders.size()>0){
                    Folder f = mFolders.get(0);
                    SimpleDraweeView simpleDraweeView = holder.cover;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            Bitmap bitmap = mContext.getContentResolver().loadThumbnail(f.cover.uri,new Size(mImageSize,mImageSize),null);
                            simpleDraweeView.setImageBitmap(bitmap);
                        }else {
                            FrescoUtils.showThumb("file://"+f.cover.path,simpleDraweeView,mImageSize,mImageSize);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                holder.bindData(getItem(i));
            }
            if(lastSelected == i){
                holder.indicator.setVisibility(View.VISIBLE);
            }else{
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize(){
        int result = 0;
        if(mFolders != null && mFolders.size()>0){
            for (Folder f: mFolders){
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if(lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex(){
        return lastSelected;
    }

    class ViewHolder{
        SimpleDraweeView cover;
        TextView name;
        TextView size;
        ImageView indicator;
        ViewHolder(View view){
            cover = (SimpleDraweeView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(Folder data) {
            name.setText(data.name);
            size.setText(data.images.size()+"张");
            // 显示图片
            FrescoUtils.showThumb("file://"+data.cover.path,cover,mImageSize,mImageSize);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Bitmap bitmap = mContext.getContentResolver().loadThumbnail(data.cover.uri,new Size(mImageSize,mImageSize),null);
                    cover.setImageBitmap(bitmap);
                }else {
                    FrescoUtils.showThumb("file://"+data.cover.path,cover,mImageSize,mImageSize);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
