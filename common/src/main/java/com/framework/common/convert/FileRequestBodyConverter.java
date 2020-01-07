package com.framework.common.convert;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.common.util.UriUtil;
import com.framework.common.convert.CountingRequestBody;
import com.framework.common.net.UploadOnSubscribe;
import com.framework.common.utils.PictureUtils;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class FileRequestBodyConverter implements Converter<Map<String, Object>, RequestBody>{

    //进度发射器
    UploadOnSubscribe uploadOnSubscribe;

    @Override
    public RequestBody convert(Map<String, Object> params) throws IOException {
        uploadOnSubscribe = (UploadOnSubscribe) params.get("UploadOnSubscribe");
        return filesToMultipartBody(params);
    }

    /**
     * @return MultipartBody（retrofit 多文件文件上传）
     */
    public synchronized RequestBody filesToMultipartBody(Map<String, Object> params) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for(String key : params.keySet()){
            Object value = params.get(key);
            if(value instanceof Uri){
                Uri uri = (Uri) value;
                if(UriUtil.isLocalFileUri(uri) && !TextUtils.isEmpty(uri.getPath())){
                    value = new File(uri.getPath());
                }
            }
            if(value instanceof File){
                String fileName = PictureUtils.getFilName(((File)value).getName());
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)),(File)value);
                builder.addFormDataPart(key, fileName, fileBody);
            }else if(value instanceof String){
                builder.addFormDataPart(key,(String)value);
            }
        }
        MultipartBody multipartBody = builder.build();
        CountingRequestBody countingRequestBody = new CountingRequestBody(multipartBody, new CountingRequestBody.Listener(){
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength){
                uploadOnSubscribe.onRead(bytesWritten);
            }
        });
        try {
            uploadOnSubscribe.setmSumLength(multipartBody.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countingRequestBody;
    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}