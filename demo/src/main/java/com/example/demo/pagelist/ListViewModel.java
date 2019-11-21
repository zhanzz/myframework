package com.example.demo.pagelist;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.framework.common.base_mvp.IBaseView;
import com.framework.model.demo.ProductBean;

/**
 * @author zhangzhiqiang
 * @date 2019/11/11.
 * description：
 */
public class ListViewModel extends ViewModel {
    private int pageSize = 10;
    private PagedList.Config config = new PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize * 2) //初始加载数量
            .setPageSize(pageSize) //每页面加载数量
            .build();

    private MutableLiveData<IDataSource> dataSource = new MutableLiveData<>();

    public LiveData<ResultBean> result = Transformations.switchMap(dataSource, new Function<IDataSource, LiveData<ResultBean>>() {
        @Override
        public MutableLiveData<ResultBean> apply(IDataSource input) {
            return input.getResultBean();
        }
    });

    private DataSource.Factory dataSourceFactory = new DataSource.Factory<Integer, ProductBean>(){

        @NonNull
        @Override
        public DataSource<Integer, ProductBean> create() {
            DataSourceByItem dataSourceByItem = new DataSourceByItem();
            dataSourceByItem.setMvp(baseView);
            dataSource.postValue(dataSourceByItem);
            return dataSourceByItem;
        }
    };

    public LiveData<PagedList<ProductBean>> livePagedList = new LivePagedListBuilder<Integer, ProductBean>(dataSourceFactory, config).build();

    public void refresh(){
        if(dataSource.getValue()!=null){
            dataSource.getValue().refresh();
        }
    }

    public void retry(){
        if(dataSource.getValue()!=null){
            dataSource.getValue().retry();
        }
    }

    private IBaseView baseView;

    public void setMvp(IBaseView baseView){
        this.baseView = baseView;
    }
}
