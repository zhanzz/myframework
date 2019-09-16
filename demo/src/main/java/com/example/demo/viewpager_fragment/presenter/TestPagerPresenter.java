package com.example.demo.viewpager_fragment.presenter;

import com.example.demo.DemoApi;
import com.framework.common.base_mvp.BasePagePresenter;
import com.framework.common.base_mvp.IPageBaseView;
import com.framework.common.data.Result;
import com.framework.common.retrofit.RetorfitUtil;
import com.framework.model.demo.PresellBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.Observable;
/**
 * @author zhangzhiqiang
 * @date 2019/9/12.
 * description：
 */
public class TestPagerPresenter extends BasePagePresenter<PresellBean, IPageBaseView<PresellBean>> {
    private ArrayList<PresellBean.PresellProduct> mData;
    private String id;
    public TestPagerPresenter(int pageSize) {
        super(pageSize);
    }

    @Override
    protected Observable<Result<PresellBean>> getObservable() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize",getPageSize());
        params.put("startPage",getCurrentPage());
        params.put("activityId",id);
        return RetorfitUtil.getMallRetorfitApi(DemoApi.class).loadCategoryData(params);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ArrayList<PresellBean.PresellProduct> getData() {
        return mData;
    }

    public void setData(ArrayList<PresellBean.PresellProduct> mData) {
        this.mData = mData;
    }
}
