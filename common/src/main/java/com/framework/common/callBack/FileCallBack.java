package com.framework.common.callBack;

import com.framework.common.utils.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.ResponseBody;

/**
 * Created by zhy on 15/12/15.
 */
public abstract class FileCallBack{
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;


    public FileCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    public abstract void onFile(File file);

    public abstract void onFail(Throwable e);

    public void saveFile(ResponseBody response) {
        InputStream is = null;
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;
        File tempFile = null;
        try {
            is = response.byteStream();
            final long total = response.contentLength();

            long sum = 0,oldSum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            tempFile = File.createTempFile("tmep_",destFileName,dir);
            fos = new FileOutputStream(tempFile);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                if((sum-oldSum)*1.0f/total>0.009){ //调节一下消息的频率
                    oldSum = sum;
                    Platform.get().defaultCallbackExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            inProgress(finalSum * 1.0f / total, total);
                        }
                    });
                }
            }
            fos.flush();
            File file = new File(dir, destFileName);
            if(file.exists()){//先清除旧的文件
                file.delete();
            }
            /**
             * file对象代表一种虚拟路径  ,
             * file.renameTo(file2),file本身表示的路径不会变，实际是file表示的真实文件名变成了file2的
             */
            tempFile.renameTo(file);
            final File finalFile = file;//此处为file
            Platform.get().defaultCallbackExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    onFile(finalFile);
                }
            });
        } catch (final IOException e) {//发生异常了就删除这个文件
            if(tempFile!=null&&tempFile.exists()){
                tempFile.delete();
            }
            Platform.get().defaultCallbackExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    onFail(e);
                }
            });
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
    }

    /**
     * UI Thread
     *
     * @param progress  [0,1]
     */
    protected void inProgress(float progress, long total){

    }
}
