package com.example.demo.some_test.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import com.framework.common.adapter.ViewHolder;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.TestDiffBean;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/6/28.
 * description：
 */
public class TestDiffAdapter extends RecyclerView.Adapter {
    private List<TestDiffBean> mDatas;
    private AsyncListDiffer<TestDiffBean> mDiffer;
    private DiffUtil.ItemCallback<TestDiffBean> diffCallback = new DiffUtil.ItemCallback<TestDiffBean>() {
        @Override
        public boolean areItemsTheSame(TestDiffBean oldItem, TestDiffBean newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(TestDiffBean oldItem, TestDiffBean newItem) {
            return TextUtils.equals(oldItem.getContent(),newItem.getContent());
        }
    };
    public TestDiffAdapter(){
        mDiffer = new AsyncListDiffer<>(this, diffCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //LogUtil.e("onCreateViewHolder");
        TextView textView = new TextView(viewGroup.getContext());
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIHelper.dip2px(40));
        textView.setLayoutParams(params);
        textView.setBackgroundColor(0xff000000);
        return new ViewHolder(viewGroup.getContext(),textView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //LogUtil.e("onBindViewHolder");
        ((TextView)viewHolder.itemView).setText(getItem(i).getContent());

    }

    public void submitList(List<TestDiffBean> data) {
        mDiffer.submitList(data);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public TestDiffBean getItem(int position){
        return mDiffer.getCurrentList().get(position);
    }

    public List<TestDiffBean> getDatas() {
        return mDatas;
    }

    public void setDatas(List<TestDiffBean> mDatas) {
        this.mDatas = mDatas;
    }
}
