package com.framework.common.image_select;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.common.R;
import com.framework.common.image_select.adapter.FolderAdapter;
import com.framework.common.image_select.adapter.ImageGridAdapter;
import com.framework.common.image_select.bean.Folder;
import com.framework.common.image_select.bean.Image;
import com.framework.common.image_select.utils.FileUtils;
import com.framework.common.image_select.utils.TimeUtils;
import com.framework.common.manager.PermissionManager;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * 图片选择Fragment
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorFragment extends Fragment implements MediaScannerConnection.MediaScannerConnectionClient {

    private static final String TAG = "MultiImageSelector";

    /** 最大图片选择次数，int类型 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，int类型 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，boolean类型 */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** 默认选择的数据集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
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
    private GridView mGridView;
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

    private int mGridWidth, mGridHeight;

    private File mTmpFile;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        }catch (ClassCastException e){
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 选择图片数量
        mDesireImageCount = getArguments().getInt(EXTRA_SELECT_COUNT);

        // 图片选择模式
        final int mode = getArguments().getInt(EXTRA_SELECT_MODE);

        // 默认选择
        if(mode == MODE_MULTI) {
            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if(tmp != null && tmp.size()>0) {
                resultList = tmp;
            }
        }

        // 是否显示照相机
        mIsShowCamera = getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
        mImageAdapter = new ImageGridAdapter(getActivity(), mIsShowCamera);
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);

        mPopupAnchorView = view.findViewById(R.id.footer);

        mTimeLineText = (TextView) view.findViewById(R.id.timeline_area);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        // 初始化，加载所有图片
        mCategoryText.setText(R.string.folder_all);
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mFolderPopupWindow == null){
                    createPopupFolderList(mGridWidth, mGridHeight);
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        mPreviewBtn = (Button) view.findViewById(R.id.preview);
        // 初始化，按钮状态初始化
        if(resultList == null || resultList.size()<=0){
            mPreviewBtn.setText(R.string.preview);
            mPreviewBtn.setEnabled(false);
        }
        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {

                if(state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL){
                    FrescoUtils.resume();
                }else{
                    FrescoUtils.pause();
                }

                if(state == SCROLL_STATE_IDLE){
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                }else if(state == SCROLL_STATE_FLING){
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mTimeLineText.getVisibility() == View.VISIBLE) {
                    int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                    Image image = (Image) view.getAdapter().getItem(index);
                    if (image != null) {
                        mTimeLineText.setText(TimeUtils.formatPhotoDate(image.path));
                    }
                }
            }
        });
        mGridView.setAdapter(mImageAdapter);
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int width = mGridView.getWidth();
                final int height = mGridView.getHeight();

                mGridWidth = width;
                mGridHeight = height;

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = width / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (width - columnSpace*(numCount-1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mImageAdapter.isShowCamera()){
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if(i == 0){
                        if(AppTools.isMarshmallowOrHigher()){
                            if(PermissionManager.getInstance().lacksPermissions(new String[]{
                                    Manifest.permission.CAMERA
                            })){
                                requestPermissions(new String[]{
                                        Manifest.permission.CAMERA
                                }, PermissionManager.CAMERA_CODE);
                            }else{
                                showCameraAction();
                            }
                        }else{
                            showCameraAction();
                        }
                    }else{
                        // 正常操作
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(image, mode);
                    }
                }else{
                    // 正常操作
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, mode);
                }
            }
        });

        mFolderAdapter = new FolderAdapter(getActivity());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.CAMERA_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (PackageManager.PERMISSION_GRANTED == grantResult) {
                        showCameraAction();
                    } else {
                        ToastUtil.show(getActivity(),PermissionManager.getInstance().toDescription(Manifest.permission.CAMERA));
                    }
                }
            }
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
        mFolderPopupWindow.setHeight(height * 5 / 8);
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
                            getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mCategoryText.setText(R.string.folder_all);
                            if (mIsShowCamera) {
                                mImageAdapter.setShowCamera(true);
                            } else {
                                mImageAdapter.setShowCamera(false);
                            }
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                mCategoryText.setText(folder.name);
                                // 设定默认选择
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                        }
                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 首次加载所有图片
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if(requestCode == REQUEST_CAMERA){
        	if(resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile);
                        //刷新该图片到图库
                        startScan();
                    }
                }
            }else{
                if(mTmpFile != null && mTmpFile.exists()){
                    mTmpFile.delete();
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(mFolderPopupWindow != null){
            if(mFolderPopupWindow.isShowing()){
                mFolderPopupWindow.dismiss();
            }
        }

        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int height = mGridView.getHeight();

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = mGridView.getWidth() / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (mGridView.getWidth() - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if (mFolderPopupWindow != null) {
                    mFolderPopupWindow.setHeight(height * 5 / 8);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        super.onConfigurationChanged(newConfig);

    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = FileUtils.createTmpFile(getActivity());
            Uri uri = FileProvider.getUriForFile(getActivity(), getContext().getPackageName()+".FileProvider", mTmpFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
            
        }else{
            Toast.makeText(getActivity(),R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择图片操作
     * @param image
     */
    private void selectImageFromGrid(Image image, int mode) {
        if(image != null) {
            // 多选模式
            if(mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    if(resultList.size() != 0) {
                        mPreviewBtn.setEnabled(true);
                        mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    }else{
                        mPreviewBtn.setEnabled(false);
                        mPreviewBtn.setText(R.string.preview);
                    }
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image.path);
                    }
                } else {
                    // 判断选择数量问题
                    if(mDesireImageCount == resultList.size()){
                        ToastUtil.show(getContext(),R.string.msg_amount_limit);
                        return;
                    }

                    resultList.add(image.path);
                    mPreviewBtn.setEnabled(true);
                    mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    if (mCallback != null) {
                        mCallback.onImageSelected(image.path);
                    }
                }
                mImageAdapter.select(image);
            }else if(mode == MODE_SINGLE){
                // 单选模式
                if(mCallback != null){
                    mCallback.onSingleImageSelected(image.path);
                }
            }
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if(id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                cursorLoader.forceLoad();
                return cursorLoader;
            }else if(id == LOADER_CATEGORY){
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0]+" like '%"+args.getString("path")+"%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<Image>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do{
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if( !hasFolderGened ) {
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

                    }while(data.moveToNext());

                    mImageAdapter.setData(images);

                    // 设定默认选择
                    if(resultList != null && resultList.size()>0){
                        mImageAdapter.setDefaultSelected(resultList);
                    }

                    mFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 回调接口
     */
    public interface Callback{
        void onSingleImageSelected(String path);
        void onImageSelected(String path);
        void onImageUnselected(String path);
        void onCameraShot(File imageFile);
    }
    
	public  boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    MediaScannerConnection conn;

    private void startScan() {
        if (conn != null)
            conn.disconnect();
        if (conn == null)
            conn = new MediaScannerConnection(getContext(), this);
        conn.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        try {
            //必须指定好文件的名字才可以刷新，不能直接指定目录名，拍完照片之后直接刷新下
            conn.scanFile(mTmpFile.getPath(), "image/*");
        } catch (Exception e) {
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
