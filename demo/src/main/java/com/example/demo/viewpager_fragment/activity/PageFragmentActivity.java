package com.example.demo.viewpager_fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.viewpager_fragment.fragment.PagerFragment;
import com.example.demo.viewpager_fragment.presenter.PageFragmentPresenter;
import com.example.demo.viewpager_fragment.view.IPageFragmentView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
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
    private RecyclerView.RecycledViewPool mRecyclerViewPool;
    TextView view;
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
        mRecyclerViewPool = new RecyclerView.RecycledViewPool();
        mRecyclerViewPool.setMaxRecycledViews(0, 10);
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
                        viewPager.setCurrentItem(index, false);
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
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, final List<View> sharedElements, List<View> sharedElementSnapshots) {
                if(!ListUtils.isEmpty(sharedElements)){
                    for(View view:sharedElements){
                        view.setVisibility(View.VISIBLE);
                    }
                }
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
            }
        });
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new PageFragmentPresenter();
        }
        return mPresenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void loadPageData() {
        mPresenter.getCategory();
    }

    private FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager(),
            FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT) {
        @Override
        public Fragment getItem(int position) {
            PagerFragment fragment;
            if (position == 0) {
                fragment = PagerFragment.newInstance(mTitleDataList.get(position).getId(), mFirstProductList);
            } else {
                fragment = PagerFragment.newInstance(mTitleDataList.get(position).getId(), null);
            }
            fragment.setRecyclerViewPool(mRecyclerViewPool);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTitleDataList == null ? 0 : mTitleDataList.size();
        }
    };

    @Override
    public void onCategoryData(PresellBean bean) {
        mTitleDataList.clear();
        if (!ListUtils.isEmpty(bean.getCategories())) {
            mTitleDataList.addAll(bean.getCategories());
        }
        mFirstProductList = bean.getProductList();
        magicIndicator.getNavigator().notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        Fragment fragment = (Fragment) mAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (fragment instanceof PagerFragment) {
            ((PagerFragment) fragment).onReenter(resultCode, data);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PageFragmentActivity.class);
        context.startActivity(starter);
    }
}