package com.framework.common.image_select;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.common.util.UriUtil;
import com.framework.common.R;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.base_mvp.BaseFragment;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.image_select.adapter.FolderAdapter;
import com.framework.common.image_select.adapter.ImageGridAdapter;
import com.framework.common.image_select.bean.Folder;
import com.framework.common.image_select.bean.Image;
import com.framework.common.image_select.bean.TakeBean;
import com.framework.common.image_select.utils.TimeUtils;
import com.framework.common.manager.PermissionManager;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.item_decoration.GridSpaceItemDecoration;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.TakePhotoUtil;
import com.framework.common.utils.ToastUtil;
import com.framework.common.utils.UIHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择Fragment
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorFragment extends BaseFragment {

    private static final String TAG = "MultiImageSelector";

    /**
     * 最大图片选择次数，int类型
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，int类型
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，boolean类型
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 默认选择的数据集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;

    private String mCurrentCropImagePath;
    // 结果数据
    private ArrayList<String> resultList = new ArrayList<String>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<Folder>();

    // 图片Grid
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;
    private Callback mCallback;

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;

    // 时间线
    private TextView mTimeLineText;
    // 类别
    private TextView mCategoryText;
    // 预览按钮
    private Button mPreviewBtn;
    // 底部View
    private View mPopupAnchorView;

    private int mDesireImageCount;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;

    private TakeBean mTmpUri;
    private int mMode;//多选还是单选
    private WeakReference<Cursor> mCursor;

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID};

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTmpUri = savedInstanceState.getParcelable("tempUri");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("tempUri", mTmpUri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        if (PermissionManager.CAMERA_CODE == requestCode
                && permissions.contains(Manifest.permission.CAMERA)) {
            //if (AppTools.isQOrHigher()) {
                // 跳转到系统照相机
                mTmpUri = TakePhotoUtil.takePhotoV4(this, REQUEST_CAMERA);
            //} else if (permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
              //  mTmpUri = Uri.fromFile(TakePhotoUtil.takePhotoV2(this, REQUEST_CAMERA));
            //}
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(Math.min(mFolderAdapter.getCount() * UIHelper.dip2px(92), height * 5 / 8));
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mFolderAdapter.setSelectIndex(i);
                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();
                        if (index == 0) {
                            mImageAdapter.setNewData(mAllImages);
                            mRecyclerView.getLayoutManager().scrollToPosition(0);
                            mCategoryText.setText(R.string.folder_all);
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setNewData(folder.images);
                                mCategoryText.setText(folder.name);
                                // 设定默认选择
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                        }
                        // 滑动到最初始位置
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                }, 100);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_multi_image;
    }

    /**
     * android:horizontalSpacing="@dimen/space_size"
     * android:verticalSpacing="@dimen/space_size"
     * android:numColumns="auto_fit"
     * android:columnWidth="@dimen/image_size"
     *
     * @param bundle
     */
    @Override
    public void getParamData(Bundle bundle) {
        // 选择图片数量
        mDesireImageCount = bundle.getInt(EXTRA_SELECT_COUNT);

        // 图片选择模式
        mMode = bundle.getInt(EXTRA_SELECT_MODE);

        // 默认选择
        if (mMode == MODE_MULTI) {
            ArrayList<String> tmp = bundle.getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList = tmp;
            }
        }
        // 是否显示照相机
        mIsShowCamera = bundle.getBoolean(EXTRA_SHOW_CAMERA, true);
    }

    @Override
    public void bindData() {
        View view = getView();
        mPopupAnchorView = view.findViewById(R.id.footer);
        mTimeLineText = view.findViewById(R.id.timeline_area);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);
        mCategoryText = view.findViewById(R.id.category_btn);
        mPreviewBtn = view.findViewById(R.id.preview);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);

        // 初始化，按钮状态初始化
        if (resultList == null || resultList.size() <= 0) {
            mPreviewBtn.setText(R.string.preview);
            mPreviewBtn.setEnabled(false);
        }

        mImageAdapter = new ImageGridAdapter();
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mMode == MODE_MULTI);
        // 初始化，加载所有图片
        mCategoryText.setText(R.string.folder_all);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(manager);
        GridSpaceItemDecoration dirver = new GridSpaceItemDecoration(UIHelper.dip2px(4), UIHelper.dip2px(4), 0);
        mRecyclerView.addItemDecoration(dirver);
        mRecyclerView.setAdapter(mImageAdapter);
        mImageAdapter.setLoadEndText("无更多图片数据");

        showLoadingDialog();
        loadDataByPage();

        mFolderAdapter = new FolderAdapter(getActivity());
    }

    @Override
    public void initEvent() {
        mCategoryText.setOnClickListener(this);
        mPreviewBtn.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int state) {
                if (state == RecyclerView.SCROLL_STATE_IDLE || state == RecyclerView.SCROLL_STATE_DRAGGING) {
                    FrescoUtils.resume();
                } else {
                    FrescoUtils.pause();
                }
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                } else if (state == RecyclerView.SCROLL_STATE_SETTLING) {
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (mTimeLineText.getVisibility() == View.VISIBLE) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    if (manager instanceof LinearLayoutManager) {
                        int index = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                        Image image = mImageAdapter.getItem(index);
                        if (image != null) {
                            mTimeLineText.setText(TimeUtils.formatPhotoDate(image.path));
                        }
                    }
                }
            }
        });

        mImageAdapter.setSmartRefreshLayout(mSmartRefreshLayout);

        mImageAdapter.setRefreshListener(new BaseAdapter.RefreshListener() {
            @Override
            public void onRefresh() {
                currentPage=1;
                loadDataByPage();
            }

            @Override
            public void onLoadMore() {
                loadDataByPage();
            }
        });

        mImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Image image = mImageAdapter.getItem(position);
                // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                if (image.itemType == Image.TYPE_CAMERA) {
                    if (AppTools.isQOrHigher()) {
                        requestNeedPermissions(PermissionManager.CAMERA_CODE, Manifest.permission.CAMERA);
                    } else {
                        requestNeedPermissions(PermissionManager.CAMERA_CODE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    // 正常操作
                    selectImageFromGrid(image, mMode, position);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.category_btn) {
            if (mFolderPopupWindow == null) {
                createPopupFolderList(UIHelper.getDisplayWidth(), UIHelper.getDisplayHeight());
            }
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.show();
                int index = mFolderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                mFolderPopupWindow.getListView().setSelection(index);
            }
        } else if (v.getId() == R.id.preview) {

        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {//resultCode小米返回为0
                if (mTmpUri != null) {
                    if (mMode == MODE_SINGLE) {
                        if (mCallback != null) {
                            mCallback.onCameraShot(mTmpUri.getUri());
                        }
                    } else {
                        currentPage=1;
                        loadDataByPage();
                    }
                }
            } else {
                if (mTmpUri != null) {
                    getContext().getContentResolver().delete(mTmpUri.getUri(), null, null);
                    if(!TextUtils.isEmpty(mTmpUri.getPath())){
                        new File(mTmpUri.getPath()).delete();
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid(Image image, int mode, int position) {
        if (image != null) {
            // 多选模式
            if (mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    if (resultList.size() != 0) {
                        mPreviewBtn.setEnabled(true);
                        mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    } else {
                        mPreviewBtn.setEnabled(false);
                        mPreviewBtn.setText(R.string.preview);
                    }
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image.uri);
                    }
                } else {
                    // 判断选择数量问题
                    if (mDesireImageCount == resultList.size()) {
                        ToastUtil.show(getContext(), R.string.msg_amount_limit);
                        return;
                    }

                    resultList.add(image.path);
                    mPreviewBtn.setEnabled(true);
                    mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    if (mCallback != null) {
                        mCallback.onImageSelected(image.uri);
                    }
                }
                mImageAdapter.select(image, position);
            } else if (mode == MODE_SINGLE) {
                // 单选模式
                if (mCallback != null) {
                    mCallback.onSingleImageSelected(image.uri);
                }
            }
        }
    }

    private CursorLoader mCursorLoader;
    private List<Image> mAllImages = new ArrayList<>();
    private int currentPage = 1,pageSize=36;//从1开始

    private void loadDataByPage(){
        if(mCursorLoader!=null){
            mCursorLoader.stopLoading();
        }
        mCursorLoader = new CursorLoader(getActivity(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                null, null, IMAGE_PROJECTION[2] +
                String.format(" DESC limit %s offset %s",pageSize*currentPage,pageSize*(currentPage-1)));
        mCursorLoader.registerListener(0, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(@NonNull Loader<Cursor> loader, @Nullable Cursor data) {
                hideLoadingDialog();
                mCursorLoader.unregisterListener(this);
                if (data != null) {
                    List<Image> images = new ArrayList<Image>();
                    if(currentPage==1){
                        mResultFolder.clear();
                        mAllImages.clear();
                        mFolderAdapter.setSelectIndex(0);
                        if (mIsShowCamera) {
                            Image cimage = new Image();
                            cimage.itemType = Image.TYPE_CAMERA;
                            images.add(cimage);
                        }
                    }
                    int count = data.getCount();
                    if (count > 0) {
                        data.moveToFirst();
                        int pathColumn = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                        int nameColumn = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                        int timeColumn = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                        int idColumn = data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]);
                        do {
                            String path = data.getString(pathColumn);
                            String name = data.getString(nameColumn);
                            long dateTime = data.getLong(timeColumn);
                            long id = data.getLong(idColumn);
                            Image image = new Image(path, name, dateTime);
                            if(AppTools.isQOrHigher()){
                                image.uri = ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                            }else {
                                image.uri = Uri.fromFile(new File(path));
                            }
                            File file = new File(path);
                            if (!file.exists()) {
                                continue;
                            }
                            images.add(image);
                            if (!hasFolderGened) {
                                // 获取文件夹名称
                                File imageFile = new File(path);
                                File folderFile = imageFile.getParentFile();
                                Folder folder = new Folder();
                                folder.name = folderFile.getName();
                                folder.path = folderFile.getAbsolutePath();
                                folder.cover = image;
                                if (!mResultFolder.contains(folder)) {
                                    List<Image> imageList = new ArrayList<Image>();
                                    imageList.add(image);
                                    folder.images = imageList;
                                    mResultFolder.add(folder);
                                } else {
                                    // 更新
                                    Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                    f.images.add(image);
                                }
                            }

                        } while (data.moveToNext());

                        // 设定默认选择
                        if (resultList != null && resultList.size() > 0) {
                            mImageAdapter.setDefaultSelected(resultList);
                        }
                        mFolderAdapter.setData(mResultFolder);
                        //hasFolderGened = true;
                    }
                    mAllImages.addAll(images);
                    int index = mFolderAdapter.getSelectIndex();
                    if (index == 0) {
                        mImageAdapter.setOrAddData(images,currentPage,pageSize);
                    } else {
                        Folder folder = mFolderAdapter.getItem(index);
                        if (null != folder) {
                            mImageAdapter.setNewData(folder.images);
                            mCategoryText.setText(folder.name);
                            // 设定默认选择
                            if (resultList != null && resultList.size() > 0) {
                                mImageAdapter.setDefaultSelected(resultList);
                            }
                        }
                        int size = images.size();
                        if(size<pageSize){
                            mImageAdapter.loadMoreEnd();
                        }else {
                            mImageAdapter.loadMoreComplete();
                        }
                        if(mSmartRefreshLayout!=null){
                            mSmartRefreshLayout.finishRefresh(true);
                        }
                    }

                    currentPage++;
                }
            }
        });
        mCursorLoader.startLoading();
    }

    @Override
    public void onDestroyView() {
        if(mCursorLoader!=null){
            mCursorLoader.stopLoading();
        }
        super.onDestroyView();
    }

    /**
     * 回调接口
     */
    public interface Callback {
        void onSingleImageSelected(Uri path);

        void onImageSelected(Uri path);

        void onImageUnselected(Uri path);

        void onCameraShot(Uri imageFile);
    }
}
