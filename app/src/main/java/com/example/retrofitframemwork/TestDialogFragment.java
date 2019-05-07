package com.example.retrofitframemwork;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framework.common.base_mvp.BaseDialog;
import com.framework.common.base_mvp.BasePresenter;

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
    Unbinder unbinder;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

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
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public interface ActionListener {
        void onPrice(String price);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onPrice("7");
        }
    }
}
