package com.example.demo.anim.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.interfaces.IPickerViewData;
import com.example.demo.R;
import com.framework.common.utils.UIHelper;
import java.util.List;

public class OptionProvider<T extends IPickerViewData>{
    private OptionsPickerView<T> pvOptions;
    private OptionListener mListener;
    private Context context;
    private String title;
    private List<T> items;

    public OptionProvider(Context context, String title, List<T> items, OptionListener listener){
        this.context = context;
        this.title = title;
        this.items = items;
        mListener = listener;
    }

    public void showOptionDialog() {

        if (pvOptions == null) {
            pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String opt1tx = items.size() > options1 ?
                            items.get(options1).getPickerViewText() : "";
                    if(mListener!=null){
                        mListener.onSelect(opt1tx,options1);
                    }

                }
            }) .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                @Override
                public void onOptionsSelectChanged(int options1, int options2, int options3) {
//                    String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                }
            })
                    .setSubmitText("确定")//确定按钮文字
                    .setCancelText("取消")//取消按钮文字
                    .setTitleText(title)//标题
                    .setSubCalSize(16)//确定和取消文字大小
                    .setTitleSize(18)//标题文字大小
                    .setTitleColor(0xff333333)//标题文字颜色
                    .setSubmitColor(0xff3B9FFF)//确定按钮文字颜色
                    .setCancelColor(0xff3B9FFF)//取消按钮文字颜色
                    .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
                    .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                    .setContentTextSize(16)//滚轮文字大小
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .setOutSideCancelable(false)//点击外部dismiss default true
                    .isDialog(true)//是否显示为对话框样式
                    .isRestoreItem(false)//切换时是否还原，设置默认选中第一项。
                    .build();

            pvOptions.setPicker(items);//添加数据源

            Dialog mDialog = pvOptions.getDialog();
            if (mDialog != null) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER);
                params.leftMargin = 0;
                params.rightMargin = 0;
                pvOptions.getDialogContainerLayout().setLayoutParams(params);
                Window dialogWindow = mDialog.getWindow();
                if (dialogWindow != null) {
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.gravity = Gravity.CENTER;
                    lp.dimAmount = 0.4f;//越大背景越黑
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = dip2px(context,350);
                    //lp.windowAnimations = R.style.DialogAnimation;
                    dialogWindow.setAttributes(lp);
                }
            }
        }
        pvOptions.show();
    }

    public interface OptionListener {
        void onSelect(String context,int index);
    }

    public OptionsPickerView getPvTime() {
        return pvOptions;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
