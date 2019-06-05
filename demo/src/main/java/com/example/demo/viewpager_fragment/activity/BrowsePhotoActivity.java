package com.example.demo.viewpager_fragment.activity;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.demo.R;
import com.example.demo.R2;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ListUtils;
import com.framework.common.widget.MyPhotoDraweeView;
import com.framework.common.widget.drawable.MyAutoRotateDrawable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * 浏览大图
 */
public class BrowsePhotoActivity extends BaseActivity {

    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.tv_indicator)
    TextView tvIndicator;

    private int mPosition;
    private ArrayList<String> mPaths;
    private PagerAdapter mAdapter;
    private Drawable mRoteDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_browse_photo;
    }

    @Override
    public void getParamData(Intent intent) {
        mPosition = intent.getIntExtra("position",0);
        mPaths = (ArrayList<String>) intent.getSerializableExtra("paths");
    }

    @Override
    public void bindData() {
        mRoteDrawable = getResources().getDrawable(R.drawable.load_more_quan);
        if(!ListUtils.isEmpty(mPaths)){
            mAdapter = new PagerAdapter() {
                private Map<Integer,PhotoDraweeView>  mCache = new HashMap<>();

                @Override
                public int getCount() {
                    return mPaths.size();
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                    return view==o;
                }

                @NonNull
                @Override
                public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    PhotoDraweeView view = mCache.get(position);
                    if(view==null){
                        view  = new MyPhotoDraweeView(container.getContext());
                        mCache.put(position,view);
                    }
                    GenericDraweeHierarchy hierarchy = view.getHierarchy();

                    MyAutoRotateDrawable drawable = new MyAutoRotateDrawable(mRoteDrawable,1000);
                    hierarchy.setProgressBarImage(drawable,ScalingUtils.ScaleType.CENTER_INSIDE);
                    hierarchy.setRetryImage(R.drawable.ic_network_top_tip,ScalingUtils.ScaleType.CENTER_INSIDE);
                    view.setPhotoUri(Uri.parse(mPaths.get(position)));
                    if(view.getParent()==null){
                        container.addView(view);
                    }
                    return view;
                }

                @Override
                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                    container.removeView(mCache.get(position));
                }
            };
            viewPager.setAdapter(mAdapter);
            changeIndicator();
            viewPager.setCurrentItem(mPosition);
        }
        Activity a;
    }

    @Override
    public void initEvent() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                changeIndicator();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    View view = (View) mAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                    names.clear();
                    sharedElements.clear();
                    sharedElements.put("image", view);
                }
            });
        }
    }

    private void changeIndicator() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(viewPager.getCurrentItem()+1)).append("/").append(String.valueOf(mPaths.size()));
        tvIndicator.setText(builder.toString());
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    /**
     *
     * @param context
     * @param position 初始时浏览第几张图片
     * @param paths 图片列表
     */
    public static void start(Activity context,int position,ArrayList<String> paths,View view) {
        Intent starter = new Intent(context, BrowsePhotoActivity.class);
        starter.putExtra("position",0);
        starter.putExtra("paths",paths);
        if(view!=null){
            context.startActivity(starter,ActivityOptionsCompat.makeSceneTransitionAnimation(context,view,"image").toBundle());
        }else {
            context.startActivity(starter);
        }
    }
}
