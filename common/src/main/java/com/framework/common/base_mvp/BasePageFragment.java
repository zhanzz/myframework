package com.framework.common.base_mvp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.data.LoadType;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import java.util.List;
/**
 * @author zhangzhiqiang
 * @date 2019/4/23.
 * description：
 */
public abstract class BasePageFragment<T> extends BaseFragment implements IPageBaseView<T>{
    public abstract @NonNull
    BaseAdapter<T,? extends BaseViewHolder> getAdapter();

    @Override
    public void bindData() {
        mCalled = true;
        BaseAdapter mAdapter = getAdapter();
        mAdapter.setRealControlMoreEnable(true);
        mAdapter.setIsRecyclerHeadAndFooter(false);
        mAdapter.setLoadEndText("暂无更多数据");
        mAdapter.setEmptyView(ViewUtil.createEmptyView(getContext()));
    }

    @Override
    public void initEvent() {
        mCalled = true;
        BaseAdapter mAdapter = getAdapter();
        mAdapter.setSmartRefreshLayout(getSmartRefreshLayout());
        mAdapter.setRefreshListener(new BaseAdapter.RefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().getFirst(LoadType.NONE);
            }

            @Override
            public void onLoadMore() {
                getPresenter().getMore();
            }
        });
    }

    protected abstract @NonNull SmartRefreshLayout getSmartRefreshLayout();

    @Override
    public void onPageData(List<T> data, int currentPage, int pageSize) {
        getAdapter().setOrAddData(data,currentPage,pageSize);
        if(getRecyclerView().getAdapter()==null){
            getRecyclerView().setAdapter(getAdapter());
        }
    }

    protected abstract @NonNull RecyclerView getRecyclerView();

    @Override
    public void onPageFail(int code, String msg, int currentPage) {
        getAdapter().loadMoreFail(currentPage);
        if(ListUtils.isEmpty(getAdapter().getData())){
            showErrorView();
        }
    }

    @Override
    public void reloadData() {
        getPresenter().getFirst(LoadType.LOAD);
    }

    @Override
    protected abstract BasePagePresenter getPresenter();

    @Override
    public boolean getValid() {
        return true;
    }
}
