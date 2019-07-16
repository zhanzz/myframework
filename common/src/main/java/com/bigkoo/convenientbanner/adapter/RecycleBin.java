package com.bigkoo.convenientbanner.adapter;

import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;

import com.framework.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The RecycleBin facilitates reuse of views across layouts. The RecycleBin has two levels of
 * storage: ActiveViews and ScrapViews. ActiveViews are those views which were onscreen at the
 * start of a layout. By construction, they are displaying current information. At the end of
 * layout, all views in ActiveViews are demoted to ScrapViews. ScrapViews are old views that
 * could potentially be used by the adapter to avoid allocating views unnecessarily.
 * <p>
 * This class was taken from Android's implementation of {@link android.widget.AbsListView} which
 * is copyrighted 2006 The Android Open Source Project.
 */
public class RecycleBin {
    /**
     * Unsorted views that can be used by the adapter as a convert view.
     */
    private SparseArray<List<View>> scrapViews = new SparseArray<>();
    /**
     * @return A view from the ScrapViews collection. These are unordered.
     */
    View getScrapView(int viewType) {
        List<View> views = scrapViews.get(viewType);
        View view = ListUtils.isEmpty(views)? null:views.get(0);
        if(view!=null){
            views.remove(view);
        }
        return view;
    }

    /**
     * Put a view into the ScrapViews list. These views are unordered.
     *
     * @param scrap The view to add
     */
    void addScrapView(View scrap,int viewType) {
        if(scrapViews.get(viewType)==null){
            List<View> list = new ArrayList<>();
            list.add(scrap);
            scrapViews.put(viewType,list);
        }else {
            if(!scrapViews.get(viewType).contains(scrap)){
                scrapViews.get(viewType).add(scrap);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            scrap.setAccessibilityDelegate(null);
        }
    }

    public void clear() {
        scrapViews.clear();
    }
}
