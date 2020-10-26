package com.framework.common.base_mvp;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.framework.common.utils.AppTools;

public class CloseSoftKeyDialog extends Dialog {
    public CloseSoftKeyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void dismiss() {
        AppTools.hideKeyBoard(getContext(),getWindow());
        super.dismiss();
    }
}
