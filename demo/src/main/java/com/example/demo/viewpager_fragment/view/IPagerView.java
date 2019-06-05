package com.example.demo.viewpager_fragment.view;

import com.framework.common.base_mvp.IBaseView;
import com.framework.model.demo.PresellBean;
import java.util.List;
/**
 * @author zhangzhiqiang
 * @date 2019/5/24.
 * description：
 */
public interface IPagerView extends IBaseView {
    void onProductList(List<PresellBean.PresellProduct> list,int pageIndex);
    void onFailList(int pageIndex);
}
