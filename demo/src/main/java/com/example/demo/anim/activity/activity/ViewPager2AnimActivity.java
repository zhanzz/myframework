package com.example.demo.anim.activity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.contrarywind.interfaces.IPickerViewData;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.anim.activity.OptionProvider;
import com.example.demo.anim.activity.presenter.ViewPager2AnimPresenter;
import com.example.demo.anim.activity.view.IViewPager2AnimView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPager2AnimActivity extends BaseActivity implements IViewPager2AnimView {
    @BindView(R2.id.viewPager)
    ViewPager2 viewPager;
    private ViewPager2AnimPresenter mPresenter;
    ScreenSlidePagerAdapter pagerAdapter;
    OptionProvider mOptionProvider;
    @Override
    public int getLayoutId() {
        return R.layout.activity_view_pager2_anim;
    }

    @Override
    public void bindData() {
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        List<TestBean> list = new ArrayList<>();
        list.add(new TestBean("aaa"));
        list.add(new TestBean("bbb"));
        mOptionProvider = new OptionProvider(this, "选择行业", list, new OptionProvider.OptionListener() {
            @Override
            public void onSelect(String context, int index) {
                showToast(context+index);
            }
        });
    }

    @Override
    public void initEvent() {

    }

    public void changeData(View view){
        //pagerAdapter.notifyDataSetChanged();
        mOptionProvider.showOptionDialog();
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ViewPager2AnimPresenter();
        }
        return mPresenter;
    }



    public static class ScreenSlidePageFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_screen_slide_page, container, false);
            rootView.setBackgroundColor(new Random().nextInt());
            TextView tv = rootView.findViewById(R.id.text);
            tv.setMovementMethod(ScrollingMovementMethod.getInstance());
            Log.e("zhang","onCreateView");
            return rootView;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.e("zhang","onCreate");
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            Log.e("zhang","onDestroyView");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.e("zhang","onDestroy");
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(@NonNull FragmentActivity fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    //Log.e("zhang","left="+(horzMargin - vertMargin / 2));
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    //Log.e("zhang","right="+(-horzMargin + vertMargin / 2));
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

    public static class TestBean implements IPickerViewData {
        public TestBean(String name){
            this.name = name;
        }
        String name;
        @Override
        public String getPickerViewText() {
            return name;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ViewPager2AnimActivity.class);
        context.startActivity(starter);
    }
}