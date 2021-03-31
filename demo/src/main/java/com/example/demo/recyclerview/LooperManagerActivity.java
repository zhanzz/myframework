package com.example.demo.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.demo.R;
import com.example.demo.recyclerview.adapter.ImageAdapter;
import com.example.demo.recyclerview.manager.LooperLayoutManager;

public class LooperManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_manager);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LooperLayoutManager manager = new LooperLayoutManager();
        manager.setLooperEnable(true);
        PagerSnapHelper helper = new PagerSnapHelper(){
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                int itemCount = layoutManager.getItemCount();
                int pos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                if (pos >= itemCount) {
                    return 0;
                }
                return pos;
            }
        };
        helper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new ImageAdapter());
//        Button btn = findViewById(R.id.btn);
//        //foucs=true;touch=false
//        Log.e("zhang",String.format("foucs=%s;touch=%s",btn.isFocusable(),btn.isFocusableInTouchMode()));
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LooperManagerActivity.class);
        context.startActivity(starter);
    }
}