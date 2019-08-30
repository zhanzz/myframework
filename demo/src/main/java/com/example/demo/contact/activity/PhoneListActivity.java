package com.example.demo.contact.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.contact.adapter.ContactAdapter;
import com.example.demo.contact.presenter.PhoneListPresenter;
import com.example.demo.contact.view.IPhoneListView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ListUtils;
import com.framework.model.demo.ContactBean;
import com.oubowu.stickyitemdecoration.StickyHeadContainer;
import com.oubowu.stickyitemdecoration.StickyItemDecoration;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneListActivity extends BaseActivity implements IPhoneListView {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.stickyHeadContainer)
    StickyHeadContainer stickyHeadContainer;
    //head内的id
    @BindView(R2.id.tv_name)
    TextView tvName;
    private PhoneListPresenter mPresenter;
    private ContactAdapter mAdapter;
    private WeakReference<Cursor> mCursor;

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_list;
    }

    @Override
    public void bindData() {
        mAdapter = new ContactAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(PhoneListActivity.this));
        StickyItemDecoration stickyItemDecoration = new StickyItemDecoration(stickyHeadContainer, ContactBean.TYPE_HEAD);
        recyclerView.addItemDecoration(stickyItemDecoration);
        recyclerView.setAdapter(mAdapter);
        requestNeedPermissions(Manifest.permission.READ_CONTACTS);
    }

    @Override
    public void initEvent() {
        stickyHeadContainer.setDataCallback(new StickyHeadContainer.DataCallback() {
            @Override
            public void onDataChange(int pos) {
                ContactBean item = mAdapter.getItem(pos);
                tvName.setText(String.valueOf(item.category));
            }
        });
    }

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        if (permissions.contains(Manifest.permission.READ_CONTACTS)) {
            startLoad();
        }
    }

    private void startLoad() {
        showLoading();
        LoaderManager manager = LoaderManager.getInstance(this);
        // 此处的第一二个参数和Callback中onCreateLoader的值对应，可以传一些查询条件等
        // 第二个为刚刚的回调类实例
        manager.initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                /**
                 * 这里返回执行查询的Loader对象，传入的参数和ContentResolver的执行的参数是一样的
                 */
                return new CursorLoader(PhoneListActivity.this, ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                        null, null, null);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                hideLoading();
                List<ContactBean> list = new ArrayList<>();
                if (null != data) {
                    if (mCursor != null && mCursor.get() == data) {
                        return;//数据集没有变化
                    }
                    mCursor = new WeakReference<>(data);
                    int count = data.getCount();
                    if (count > 0) {
                        data.moveToFirst();
                        do {
                            // 获取备注名
                            String name = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            // 获取电话号码
                            String phone = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // 其他的常量自行查询文档
                            list.add(new ContactBean(phone, name, getFirstChar(name)));
                        } while (data.moveToNext());
                    }
                }
                Collections.sort(list);
                insertHeadData(list);
                mAdapter.setNewData(list);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }

    private void insertHeadData(List<ContactBean> list) {
        if (!ListUtils.isEmpty(list)) {
            //result>=65&&result<=90 A<result<Z
            for (char i = 65; i <= 90; i++) {
                ContactBean bean = new ContactBean();
                bean.category = i;
                bean.itemType = ContactBean.TYPE_HEAD;

                int index = list.indexOf(bean);
                if (index >= 0) {
                    list.add(index, bean);
                }
            }
            ContactBean bean = new ContactBean();
            bean.category = '#';
            bean.itemType = ContactBean.TYPE_HEAD;
            int index = list.indexOf(bean);
            if (index >= 0) {
                list.add(index, bean);
            }
        }
    }

    private char getFirstChar(String name) {
        char result;
        if (TextUtils.isEmpty(name)) {
            result = '#';
        } else {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
            if (pinyinArray != null) {
                result = pinyinArray[0].charAt(0);
            } else {
                result = name.charAt(0);
            }
        }
        if (result >= 97 && result <= 122) { // a<=result<=z //小写转大写
            result -= 32;
        } else if (result < 65 || result > 90) {//不是大写字母
            result = '#';
        }
        return result;
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new PhoneListPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context, Bundle bundle) {
        Intent starter = new Intent(context, PhoneListActivity.class);
        if (bundle != null) {
            starter.putExtras(bundle);
        }
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}