package com.framework.common.convert;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.framework.common.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class UriRequestBody {
    /** Returns a new request body that transmits the content of {@code Uri}. */
    public static RequestBody create(final @Nullable MediaType contentType, final Uri uri) {
        if (uri == null) throw new NullPointerException("uri == null");

        return new RequestBody() {
            @Override public @Nullable MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                InputStream in = null;
                try {
                    in = BaseApplication.getApp().getContentResolver().openInputStream(uri);
                    if(in!=null){
                        return in.available();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(in!=null){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return 0;
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    InputStream in = BaseApplication.getApp().getContentResolver().openInputStream(uri);
                    source = Okio.source(in);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}
