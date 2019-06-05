package com.example.demo.viewpager_fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.view.View;
import android.view.Window;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.viewpager_fragment.fragment.PagerFragment;
import com.example.demo.viewpager_fragment.presenter.PageFragmentPresenter;
import com.example.demo.viewpager_fragment.view.IPageFragmentView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ListUtils;
import com.framework.model.demo.PresellBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PageFragmentActivity extends BaseActivity implements IPageFragmentView {
    @BindView(R2.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R2.id.view_pager)
    ViewPager viewPager;
    private PageFragmentPresenter mPresenter;
    private List<PresellBean.CategoryItem> mTitleDataList = new ArrayList<>();
    private ArrayList<PresellBean.PresellProduct> mFirstProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementExitTransition(new ChangeBounds());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_page_fragment;
    }

    @Override
    public void bindData() {
        mPresenter.getCategory();
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleDataList == null ? 0 : mTitleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setText(mTitleDataList.get(index).getName());
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index,false);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
    }

    @Override
    public void initEvent() {
        getLoaderManager();
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new PageFragmentPresenter();
        }
        return mPresenter;
    }

    @Override
    public void reloadData() {
        mPresenter.getCategory();
    }

    private FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return PagerFragment.newInstance(mTitleDataList.get(position).getId(),mFirstProductList);
            }else {
                return PagerFragment.newInstance(mTitleDataList.get(position).getId(),null);
            }
        }

        @Override
        public int getCount() {
            return mTitleDataList==null ? 0:mTitleDataList.size();
        }
    };

    @Override
    public void onCategoryData(PresellBean bean) {
        mTitleDataList.clear();
        if(!ListUtils.isEmpty(bean.getCategories())){
            mTitleDataList.addAll(bean.getCategories());
        }
        mFirstProductList = bean.getProductList();
        magicIndicator.getNavigator().notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PageFragmentActivity.class);
        context.startActivity(starter);
    }
}