package com.example.demo.product_detail.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo.R;
import com.framework.common.utils.DrawableUtils;
import com.framework.common.utils.UIHelper;
import java.util.List;

public class MenuAdapter  extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean needScroll = false;
    private String selectContent;
    private int selectPosition = 0;
    public MenuAdapter(@Nullable List<String> data,RecyclerView recyclerView) {
        super(data);
        selectContent = data.get(0);
        bindToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                 if(needScroll && RecyclerView.SCROLL_STATE_IDLE==newState){
                     needScroll = false;
                     scrollTo(selectContent);
                 }
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        TextView tv = new TextView(parent.getContext());
        tv.setMinWidth(UIHelper.dipRes2px(R.dimen.dp_80));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,UIHelper.spRes2px(R.dimen.sp_16));
        tv.setPadding(UIHelper.dipRes2px(R.dimen.dp_20),UIHelper.dipRes2px(R.dimen.dp_10),
                UIHelper.dipRes2px(R.dimen.dp_20),UIHelper.dipRes2px(R.dimen.dp_10));
        RecyclerView.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                UIHelper.dipRes2px(R.dimen.dp_50));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        return new BaseViewHolder(tv);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        TextView tv = (TextView) helper.itemView;
        if(helper.getLayoutPosition()==-1){
            StaggeredGridLayoutManager manager=((StaggeredGridLayoutManager)getRecyclerView().getLayoutManager());
            if (manager != null && manager.getSpanCount()>1) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)helper.itemView.getLayoutParams();
                params.setFullSpan(true);
                tv.setMinWidth(UIHelper.dipRes2px(R.dimen.dp_60));
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                tv.setText(addBr(item));
                tv.setTag(item);

                tv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("zhang","oneheight="+tv.getMeasuredHeight()+";120=="+UIHelper.dipRes2px(R.dimen.dp_120));
                    }
                },2000);
            }
        }else {
            tv.setMaxWidth(Integer.MAX_VALUE);
            tv.setMinWidth(UIHelper.dipRes2px(R.dimen.dp_80));
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)helper.itemView.getLayoutParams();
            params.setFullSpan(false);
            params.height = UIHelper.dipRes2px(R.dimen.dp_50);
            tv.setText(item);
            tv.setTag(item);
            tv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("zhang","twoheight="+tv.getMeasuredHeight()+";120=="+UIHelper.dipRes2px(R.dimen.dp_120));
                }
            },2000);
        }
        if(item.equals(selectContent)){
            tv.setBackground(DrawableUtils.makeGradientShape(new int[]{0xff111424,0xff111C2D},UIHelper.dipRes2px(R.dimen.dp_5)));
            tv.setTextColor(0xff00F1F1);
            tv.getPaint().setFakeBoldText(true);
            tv.setAlpha(1.0f);
            tv.setScaleX(1.01f);
            tv.setScaleY(1.01f);
        }else {
            tv.setBackground(DrawableUtils.makeGradientShape(new int[]{0xff111424,0xff111C2D},UIHelper.dipRes2px(R.dimen.dp_5)));
            tv.setTextColor(0xffFFffff);
            tv.getPaint().setFakeBoldText(false);
            tv.setAlpha(0.9f);
            tv.setScaleX(1.0f);
            tv.setScaleY(1.0f);
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick(item,selectPosition==helper.getLayoutPosition());
                }
                int oldPosition = selectPosition;
                selectPosition = helper.getLayoutPosition();
                if(oldPosition!=selectPosition){
                    if(selectPosition>=0){
                        notifyItemChanged(selectPosition);
                    }
                    if(oldPosition>=0){
                        notifyItemChanged(oldPosition);
                    }
                }
                selectContent = item;
                int move = getRecyclerView().getMeasuredWidth()/2 -  (tv.getLeft()+tv.getMeasuredWidth()/2);
                getRecyclerView().smoothScrollBy(-move,0);
            }
        });
    }

    private String addBr(String item) {
        if(item!=null){
            int length = item.length();
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<length-2;i=i+2){
                builder.append(item.substring(i,i+2));
                builder.append("\n");
            }
            if(length%2==0){
                builder.append(item.substring(length-2,length));
            }else {
                builder.append(item.substring(length-length%2,length));
            }
            return builder.toString();
        }
        return item;
    }

    public void scrollTo(String content) {
        if(TextUtils.isEmpty(content)){
            return;
        }
        selectContent = content;
        int position = -1;
        for(int i=0,count=mData.size();i<count;i++){
            if(content.equals(mData.get(i))){
                position = i;
                break;
            }
        }
        int oldPosition = selectPosition;
        StaggeredGridLayoutManager manager=((StaggeredGridLayoutManager)getRecyclerView().getLayoutManager());
        if (manager != null) {
            View itemView = manager.findViewByPosition(position);
            if(itemView==null){
                manager.smoothScrollToPosition(getRecyclerView(),null,position);
                needScroll = true;
            }else {
                TextView tv = (TextView) itemView;
                int move = getRecyclerView().getMeasuredWidth()/2 -  (itemView.getLeft()+itemView.getMeasuredWidth()/2);
                getRecyclerView().smoothScrollBy(-move,0);
                if(!content.equals(tv.getTag().toString())){
                    if(move>UIHelper.dipRes2px(R.dimen.dp_8)){
                        needScroll = true;
                    }else {
                        View needView = null;
                        for(int i=position-3;i<6;i++){
                            View tempView = manager.findViewByPosition(i);
                            if(tempView instanceof TextView){
                                if(content.equals(((TextView) tempView).getTag().toString())){
                                    needView = tempView;
                                    selectPosition = i;
                                    break;
                                }
                            }
                        }
                        if(needView!=null){
                            int needMove = getRecyclerView().getMeasuredWidth()/2 -  (needView.getLeft()+needView.getMeasuredWidth()/2);
                            getRecyclerView().smoothScrollBy(-needMove,0);
                        }
                    }
                }else {
                    selectPosition = position;
                }
            }
            if(selectPosition!=oldPosition){
                if(selectPosition>=0){
                    notifyItemChanged(selectPosition);
                }
                if(oldPosition>=0){
                    notifyItemChanged(oldPosition);
                }
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(String data,boolean again);
    }

    private ItemClickListener mListener;

    public void setItemClickListener(ItemClickListener listener){
        mListener = listener;
    }
}