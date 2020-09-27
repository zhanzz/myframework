package com.example.demo.product_detail.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.framework.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<String> list = makeData();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = new TextView(parent.getContext());
        return new BaseViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        TextView tv = (TextView) holder.itemView;
        tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static List<String> makeData(){
        List<String> list = new ArrayList<>();
        for(int i=0;i<50;i++){
            list.add("test"+i);
        }
        return list;
    }
}
