package com.example.demo.recyclerview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.recyclerview.adapter.Adapter;
import com.example.demo.recyclerview.adapter.Item;
import com.example.demo.recyclerview.adapter.SpaceItemDecoration;
import com.example.demo.recyclerview.manager.CustomLayoutManager;
import com.example.demo.recyclerview.manager.ScatteredLayoutManager;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.demo.recyclerview.presenter.CustomManagerPresenter;
import com.example.demo.recyclerview.view.ICustomManagerView;
import com.example.demo.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CustomManagerActivity extends BaseActivity implements ICustomManagerView {
    private CustomManagerPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        // App Logo
        //toolbar.setLogo(R.drawable.ic_launcher);
        // Title
        toolbar.setTitle("");
        // Sub Title
        //toolbar.setSubtitle("Sub title");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_black_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
     *利用反射把 PopupMenu 中的图标显示出来
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getContext(), "这里", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_edit) {
                msg += "Click edit";
            } else if (itemId == R.id.action_share) {
                msg += "Click share";
            } else if (itemId == R.id.action_settings) {
                msg += "Click setting";
            }
            if(!msg.equals("")) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_manager;
    }

    @Override
    public void bindData() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
//        recyclerView.setLayoutManager(new ScatteredLayoutManager());
        recyclerView.setLayoutManager(new CustomLayoutManager());
        List<Item> list = getItems();
        recyclerView.setAdapter(new Adapter(this,list));
    }

    @Override
    public void initEvent() {

    }

    private List<Item> getItems() {
        List<Item> showItems = new ArrayList<>();
        showItems.add(new Item("北京"));
        showItems.add(new Item("天津"));
        showItems.add(new Item("上海"));
        showItems.add(new Item("重庆"));
        showItems.add(new Item("广州"));
        showItems.add(new Item("深圳"));
        showItems.add(new Item("成都"));
        showItems.add(new Item("武汉"));
        showItems.add(new Item("南京"));
        showItems.add(new Item("大连"));
        showItems.add(new Item("青岛"));
        showItems.add(new Item("厦门"));
        showItems.add(new Item("南宁"));
        showItems.add(new Item("柳州"));
        showItems.add(new Item("桂林"));
        showItems.add(new Item("包头"));
        showItems.add(new Item("呼和浩特"));
        showItems.add(new Item("绵阳"));
        showItems.add(new Item("遂宁"));
        showItems.add(new Item("宜宾"));
        showItems.add(new Item("牡丹江"));
        showItems.add(new Item("五大连池"));
        showItems.add(new Item("张家界"));
        showItems.add(new Item("福州"));
        showItems.add(new Item("三亚"));
        showItems.add(new Item("海口"));
        showItems.add(new Item("鸡西"));
        showItems.add(new Item("沈阳"));
        showItems.add(new Item("长春"));
        showItems.add(new Item("唐山"));
        showItems.add(new Item("五台山"));
        showItems.add(new Item("哈尔滨"));
        showItems.add(new Item("大庆"));
        showItems.add(new Item("齐齐哈尔"));
        showItems.add(new Item("鞍山"));
        showItems.add(new Item("抚顺"));
        showItems.add(new Item("淄博"));
        showItems.add(new Item("德州"));
        showItems.add(new Item("烟台"));
        showItems.add(new Item("潍坊"));
        return showItems;
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new CustomManagerPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CustomManagerActivity.class);
        context.startActivity(starter);
    }
}