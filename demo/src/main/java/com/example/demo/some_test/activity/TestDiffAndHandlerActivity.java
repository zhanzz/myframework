package com.example.demo.some_test.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.some_test.adapter.TestDiffAdapter;
import com.example.demo.some_test.presenter.TestDiffAndHandlerPresenter;
import com.example.demo.some_test.view.ITestDiffAndHandlerView;
import com.example.study_gradle.TestHas;
import com.example.study_gradle2.TestGlideLoad;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.BaseApplication;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.FileUtils;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.TestDiffBean;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TestDiffAndHandlerActivity extends BaseActivity implements ITestDiffAndHandlerView {
    @BindView(R2.id.btn_change)
    Button btnChange;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_context)
    TextView tvContext;
    @BindView(R2.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R2.id.tv_anim)
    TextView tvAnim;
    @BindView(R2.id.iv_anim)
    SimpleDraweeView ivAnim;
    @BindView(R2.id.image)
    SimpleDraweeView image;
    @BindView(R2.id.iv_inbitmap)
    ImageView ivInbitmap;
    @BindView(R2.id.iv_inbitmap2)
    ImageView ivInbitmap2;
    private TestDiffAndHandlerPresenter mPresenter;
    private TestDiffAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_diff_and_handler;
    }

    AdSwitchTask adSwitchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementExitTransition(new ChangeBounds());
        }
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate=" + getClass().getSimpleName() + ";pid=" + Process.myPid() + ";name=" + getAppName(this));
        printMemory();
        getLifecycle().addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            void start(LifecycleOwner activity) {
                // connect
                Log.i("huoying", "ON_START");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            void onResume(LifecycleOwner activity) {
                Log.i("huoying", "onResume");

            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            void onDestroy() {
                Log.i("huoying", "onDestroy");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            void stop(LifecycleOwner activity) {
                // disconnect if connected
                Log.i("huoying", "ON_STOP");

            }
        });
        testBitmap();
    }

    Bitmap bitmap;
    private void testBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bg_red_packet);
        ivInbitmap.setImageBitmap(bitmap);
    }


    /**
     * 获取当前进程的名字，一般就是当前app的包名
     *
     * @param context 当前上下文
     * @return 返回进程的名字
     */
    public static String getAppName(Context context) {
        int pid = Process.myPid(); // Returns the identifier of this process
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    return info.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    @Override
    public void bindData() {
        TestGlideLoad load = new TestGlideLoad();
        load.doTest();
        final TestHas testHas = new TestHas();
        showToast(testHas.hello());
        /**
         * 因此方法为底版本才存在，此模块引用了低版本的方法且编译时不会报错，
         * gradle打包时会使用最新版本的，
         * 所以运行时报java.lang.NoSuchMethodError: No virtual method world()
         */
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //showToast(testHas.world());
            }
        }, 3000);
        List<TestDiffBean> list = new ArrayList<>();
        for (int i = 0, count = 5; i < count; i++) {
            list.add(new TestDiffBean(i, "StringContent" + i));
        }
        mAdapter = new TestDiffAdapter();
        mAdapter.submitList(list);
        mAdapter.setDatas(list);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                LogUtil.e("回收了");
            }
        });

        btnChange.setText("0");
        adSwitchTask = new AdSwitchTask(btnChange);

        testGhostView();
        ivAnim.setImageURI("http://imgsrc.baidu.com/imgad/pic/item/d000baa1cd11728bdf999dd4c2fcc3cec2fd2c8b.jpg");
    }

    private void testGhostView() {
        ColorDrawable colorDrawable = new ColorDrawable(Color.RED);
        colorDrawable.setAlpha(100);
        colorDrawable.setBounds(0, 0, 200, 200);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            tvContext.getOverlay().add(colorDrawable);
        }
        frameLayout.setRight(frameLayout.getLeft() + UIHelper.dip2px(800));
        frameLayout.setBottom(frameLayout.getTop() + UIHelper.dip2px(200));
        addGhost(tvContext, frameLayout);
    }

    private void addGhost(View view, ViewGroup viewGroup) {
        try {
            Class ghostViewClass = Class.forName("android.view.GhostView");
            Method addGhostMethod = ghostViewClass.getMethod("addGhost", View.class,
                    ViewGroup.class, Matrix.class);
            View ghostView = (View) addGhostMethod.invoke(null, view, viewGroup, null);
            ghostView.setBackgroundColor(Color.YELLOW);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initEvent() {
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                if (!ListUtils.isEmpty(sharedElements)) {
                    for (View view : sharedElements) {
                        view.setVisibility(View.VISIBLE);//解决fresco共享元素动画bug,返回空白
                    }
                }
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
            }
        });
        tvContext.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.e("visible=" + (tvContext.getVisibility() == View.VISIBLE));
            }
        }, 1000);
        tvContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了");
            }
        });

//        SQLiteOpenHelper helper = new SQLiteOpenHelper() {
//            @Override
//            public void onCreate(SQLiteDatabase db) {
//
//            }
//
//            @Override
//            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//            }
//        };
//        helper.getWritableDatabase();
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TestDiffAndHandlerPresenter();
        }
        return mPresenter;
    }

    @OnClick(R2.id.btn_change)
    public void onClick() {
//        List<TestDiffBean> old = mAdapter.getDatas();
//        List<TestDiffBean> newList = new ArrayList<>(old);
//        for(int i=5,count=10;i<count;i++){
//            newList.add(new TestDiffBean(i,"StringContent"+i));
//        }
//        newList.set(0,new TestDiffBean(0,"我是改变的内容"));
//        //DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtil(old,newList),false);
//        //mAdapter.setDatas(newList);//以新数据为依据
//        //diffResult.dispatchUpdatesTo(mAdapter);
//        mAdapter.submitList(newList);
//        btnChange.removeCallbacks(adSwitchTask);
//        btnChange.postDelayed(adSwitchTask,5000);
//        mAdapter.notifyItemChanged(0);
        //tvContext.setText("Change To another");
        //BrowseAnimActivity.start(this, ivAnim);
//        BitmapFactory.Options options2 = new BitmapFactory.Options();
//        options2.inPreferredConfig = Bitmap.Config.RGB_565;
//        options2.inBitmap = bitmap;
//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_empty_product);
//        ivInbitmap2.setImageBitmap(bitmap2);
        //requestNeedPermissions(Manifest.permission.CAMERA);
        ivInbitmap.offsetLeftAndRight(60);
        ivInbitmap.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivInbitmap.requestLayout();
            }
        },3000);
    }

    private File newFile;

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        if (permissions.contains(Manifest.permission.CAMERA)) {
            File imagePath = new File(BaseApplication.getApp().getCacheDir(), "images");
            newFile = FileUtils.createFile(imagePath, "IMG_", ".jpg");
            Uri contentUri = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //android.os.FileUriExposedException
                contentUri = Uri.fromFile(newFile);//7.0以上应用间只能用content:// 不管私有内部还是私有外部还是公有外部存储
            } else {
                contentUri = FileProvider.getUriForFile(this,
                        BaseApplication.getApp().getPackageName() + ".FileProvider", newFile);
            }
            /**
             * Uri.fromFile 对于拍照程序和安装程序都不可以我们应用访问私有内部存储
             * FileProvider对于拍照程序可以我们应用访问私有内部存储（而安装程序却不可以）
             */
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            // 授予目录临时共享权限 //Intent中migrateExtraStreamToClipData有自动添加这两个权限
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (newFile == null) {
                        return;
                    }
                    image.setImageURI(Uri.fromFile(newFile));
                    break;
            }
        }
    }

    static class AdSwitchTask implements Runnable {

        private final WeakReference<Button> reference;

        AdSwitchTask(Button convenientBanner) {
            this.reference = new WeakReference<Button>(convenientBanner);
        }

        @Override
        public void run() {
            Button convenientBanner = reference.get();
            if (convenientBanner != null) {
                int count = Integer.valueOf(convenientBanner.getText().toString());
                convenientBanner.setText(String.valueOf(++count));
                LogUtil.e("count=" + count);
                convenientBanner.postDelayed(this, 5000);
            }
        }
    }

    final static class MyDiffUtil extends DiffUtil.Callback {
        private List<TestDiffBean> oldList, newList;

        public MyDiffUtil(List<TestDiffBean> oldList, List<TestDiffBean> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            // 返回旧数据的长度
            return oldList == null ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            // 返回新数据的长度
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // 返回两个item是否相同
            // 例如：此处两个item的数据实体是User类，所以以id作为两个item是否相同的依据
            // 即此处返回两个user的id是否相同
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // 当areItemsTheSame返回true时，我们还需要判断两个item的内容是否相同
            // 此处以User的age作为两个item内容是否相同的依据
            // 即返回两个user的age是否相同
            return TextUtils.equals(oldList.get(oldItemPosition).getContent(), newList.get(newItemPosition).getContent());
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TestDiffAndHandlerActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        LogUtil.e("调用了onDestroy=" + getClass().getSimpleName());
        super.onDestroy();
    }

    private void printMemory() {

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //最大分配内存
        int memory = activityManager.getMemoryClass();
        System.out.println("memory: " + memory);
        //最大分配内存获取方法2
        float maxMemory = (Runtime.getRuntime().maxMemory() * 1.0f / (1024 * 1024));
        //当前分配的总内存
        float totalMemory = (Runtime.getRuntime().totalMemory() * 1.0f / (1024 * 1024));
        //剩余内存
        float freeMemory = (Runtime.getRuntime().freeMemory() * 1.0f / (1024 * 1024));
        System.out.println("maxMemory: " + maxMemory);
        System.out.println("totalMemory: " + totalMemory);
        System.out.println("freeMemory: " + freeMemory);
    }
}