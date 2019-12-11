package com.example.retrofitframemwork.update.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.framework.common.BaseApi;
import com.framework.common.BuildConfig;
import com.framework.common.manager.NetWorkManager;
import com.framework.common.net.RxNet;
import com.framework.common.retrofit.SchedulerProvider;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * @author zhangzhiqiang
 * @date 2019/12/11.
 * description：
 */
public class DownLoadWorker extends Worker {

    public DownLoadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String filePath = data.getString("filePath");
        String url = data.getString("downUrl");
        final boolean[] ok = {false};
        NetWorkManager.getInstance().getRetorfit(BuildConfig.GLOBAL_HOST).create(BaseApi.class)
                .downloadFile(url)
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        ok[0] = saveFile(body,new File(filePath));
                    }

                    @Override
                    public void onError(Throwable e) {
                        ok[0] = false;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return ok[0] ? Result.success():Result.retry();
    }

    private boolean saveFile(ResponseBody response,File destFile) {
        boolean ok;
        InputStream is = null;
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;
        File tempFile = null;
        try {
            is = response.byteStream();
            final long total = response.contentLength();

            long sum = 0,oldSum = 0;

            File dir = new File(destFile.getParent());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            tempFile = File.createTempFile("tmep_",destFile.getName(),dir);
            fos = new FileOutputStream(tempFile);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                if(isStopped()){
                    throw new IOException("worker停止了工作");
                }
            }
            fos.flush();
            File file = new File(dir, destFile.getName());
            if(file.exists()){//先清除旧的文件
                file.delete();
            }
            /**
             * file对象代表一种虚拟路径  ,
             * file.renameTo(file2),file本身表示的路径不会变，实际是file表示的真实文件变成了file2的
             */
            tempFile.renameTo(file);
            ok = true;
        } catch (final IOException e) {//发生异常了就删除这个文件
            if(tempFile!=null&&tempFile.exists()){
                tempFile.delete();
            }
            ok = false;
        } finally {
            try {
                response.close();
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
        return ok;
    }
}
