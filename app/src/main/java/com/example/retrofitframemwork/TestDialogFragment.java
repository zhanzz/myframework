package com.example.retrofitframemwork;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.StaticLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framework.common.base_mvp.BaseDialog;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhangzhiqiang
 * @date 2019/4/22.
 * description：
 */
public class TestDialogFragment extends BaseDialog {

    @BindView(R.id.edit)
    MyEdit edit;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_dialog;
    }

    @Override
    public void bindData() {
        edit.setText("4");
        String str = "我";
        float width= edit.getPaint().measureText(str);
        LogUtil.e("width="+width);
        float width2= edit.getPaint().measureText("1");
        LogUtil.e("width2="+width2);
        float width3= edit.getPaint().measureText("日");
        LogUtil.e("width3="+width3);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(mListener!=null){
            mListener.onPrice(edit.getText().toString());
        }
        super.onDismiss(dialog);
    }

    public interface ActionListener {
        void onPrice(String price);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }
}
