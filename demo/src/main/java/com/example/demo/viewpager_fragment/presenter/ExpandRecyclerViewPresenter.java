package com.example.demo.viewpager_fragment.presenter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.framework.common.base_mvp.BasePresenter;
import com.example.demo.viewpager_fragment.view.IExpandRecyclerViewView;
import com.framework.model.demo.BigCategory;
import com.framework.model.demo.SmallCategory;

import java.util.ArrayList;
import java.util.List;

public class ExpandRecyclerViewPresenter extends BasePresenter<IExpandRecyclerViewView> {
    public List<SectionEntity> getData(){
        List<BigCategory> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            BigCategory bigCategory = new BigCategory();
            bigCategory.categoryId = String.valueOf(i);
            bigCategory.categoryName = "bigCategory"+i;

            List<SmallCategory> sList = new ArrayList<>();
            for(int x=0;x<10;x++){
                SmallCategory smallCategory = new SmallCategory();
                smallCategory.categoryId = String.format("%d-%d",i,x);
                smallCategory.categoryName = String.format("smallCategory%d",x);
                sList.add(smallCategory);
            }
            bigCategory.list = sList;
            list.add(bigCategory);
        }
        List<SectionEntity> result = new ArrayList<>();
        for(BigCategory big:list){
            result.add(big);
            for(SmallCategory small:big.list){
                result.add(small);
            }
        }
        return result;
    }

    public List<MultiItemEntity> getMultiData(){
        List<BigCategory> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            BigCategory bigCategory = new BigCategory();
            bigCategory.categoryId = String.valueOf(i);
            bigCategory.categoryName = "bigCategory"+i;

            List<SmallCategory> sList = new ArrayList<>();
            for(int x=0;x<10;x++){
                SmallCategory smallCategory = new SmallCategory();
                smallCategory.categoryId = String.format("%d-%d",i,x);
                smallCategory.categoryName = String.format("smallCategory%d",x);
                sList.add(smallCategory);
            }
            bigCategory.list = sList;
            list.add(bigCategory);
        }
        List<MultiItemEntity> result = new ArrayList<>();
        for(BigCategory big:list){
            result.add(big);
//            for(SmallCategory small:big.list){
//                result.add(small);
//            }
        }
        return result;
    }
}