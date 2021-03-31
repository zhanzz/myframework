package com.example.demo.recyclerview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demo.R;
import com.example.demo.recyclerview.Constants;

public class ImageAdapter extends RecyclerView.Adapter<MyHolder> {
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ((ImageView)holder.itemView).setImageResource(Constants.IMG_ARRAY[position]);
    }

    @Override
    public int getItemCount() {
        return Constants.IMG_ARRAY.length;
    }
}

class MyHolder extends RecyclerView.ViewHolder{

    public MyHolder(@NonNull View itemView) {
        super(itemView);
    }
}