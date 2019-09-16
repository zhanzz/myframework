package com.example.demo.some_test.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.demo.widget.LogTv;
import com.framework.common.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import leakcanary.internal.activity.ui.SimpleListAdapter;
/**
 * @author zhangzhiqiang
 * @date 2019/9/7.
 * description：
 */
public class ListAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<>();
    public ListAdapter(){
        for(int i=0;i<30;i++){
            list.add("String"+i);
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = new LogTv(parent.getContext());
        }
        LogTv tv = (LogTv) convertView;
        tv.setPosition(position);
        tv.setText(getItem(position));
        return convertView;
    }
}
