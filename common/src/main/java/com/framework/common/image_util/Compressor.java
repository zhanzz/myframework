package com.framework.common.image_util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.framework.common.image_select.utils.FileManager;
import com.framework.common.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class Compressor {
    //max width and height values of the compressed image is taken as 612x816
    private float maxWidth = 612.0f;
    private float maxHeight = 816.0f;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 80;
    private Bitmap.Config config = Bitmap.Config.ARGB_8888;
    private File destinationDirectoryPath;

    public Compressor() {
        destinationDirectoryPath = FileManager.getTempFileDir();
    }

    public Compressor setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public Compressor setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public Compressor setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public Compressor setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public Compressor setConfig(Bitmap.Config config) {
        this.config = config;
        return this;
    }

    public Compressor setDestinationDirectoryPath(File destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
        return this;
    }

    public Uri compressToFile(File imageFile) throws IOException {
        return compressToFile(imageFile, imageFile.getName());
    }

    public Uri compressToFile(File imageFile, String compressedFileName) throws IOException {
        File filePath = new File(destinationDirectoryPath,compressedFileName);
        return ImageUtil.compressImage(Uri.fromFile(imageFile), maxWidth, maxHeight, compressFormat, quality,config,
                filePath);
    }

    public Uri compressToFile(Uri imageFile) throws IOException {
        File filePath = File.createTempFile("IMG_",".jpg",destinationDirectoryPath);
        return ImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,config,
                 filePath);
    }

    public Bitmap compressToBitmap(File imageFile) throws IOException {
        return ImageUtil.decodeSampledBitmapFromFile(Uri.fromFile(imageFile), maxWidth, maxHeight,config);
    }

    public Bitmap compressToBitmap(Uri uri) throws IOException {
        return ImageUtil.decodeSampledBitmapFromFile(uri, maxWidth, maxHeight,config);
    }

    public Observable<Uri> compressToFileAsObservable(final File imageFile) {
        return compressToFileAsObservable(imageFile, imageFile.getName());
    }

    public Observable<Uri> compressToFileAsObservable(final File imageFile, final String compressedFileName) {
        return Observable.defer(new Callable<Observable<Uri>>() {
            @Override
            public Observable<Uri> call() {
                try {
                    return Observable.just(compressToFile(imageFile, compressedFileName));
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    public Single<List<Uri>> compressToFileAsFlowableSingle(List<File> rawFileList) {
        return Flowable.fromIterable(rawFileList)
                .map(new Function<File, Uri>() {
                    @Override
                    public Uri apply(File file) throws Exception {
                        return compressToFile(file,file.getName());
                    }
                })
                .toList();
    }

    public Single<List<Uri>> compressToFileAsFlowableSingleV2(List<File> rawFileList, List<String> compressedFileName) {
        return Flowable.zip(Flowable.fromIterable(rawFileList), Flowable.fromIterable(compressedFileName), new BiFunction<File, String, Uri>() {
                    @Override
                    public Uri apply(File file, String compressedFileName) throws Exception {
                        return compressToFile(file, compressedFileName);
                    }
                })
                .toList();
    }

    public Single<List<Uri>> compressToFileAsObservableSingle(List<File> rawFileList) {
        return Observable.fromIterable(rawFileList)
                .map(new Function<File, Uri>() {
                    @Override
                    public Uri apply(File file) throws Exception {
                        return compressToFile(file,file.getName());
                    }
                })
                .toList();
    }

    public Observable<Map<String,Object>> compressToFileAsObservable(final Map<String,Object> params) {
        return Observable.create(new ObservableOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, Object>> emitter) throws Exception {
                Map<String,Object> result = new HashMap<>();
                for(String key : params.keySet()) {
                    if(!emitter.isDisposed()){
                        Object value = params.get(key);
                        if (value instanceof File) {
                            Uri file = compressToFile((File)value,((File)value).getName());
                            if(file!=null){
                                result.put(key,file);
                            }else {
                                result.put(key,value);
                            }
                        }else if(value instanceof Uri){
                            Uri file = compressToFile((Uri)value);
                            if(file!=null){
                                result.put(key,file);
                            }else {
                                result.put(key,value);
                            }
                        }else {
                            result.put(key,value);
                        }
                    }else {
                        break;
                    }
                }
                if(!emitter.isDisposed()){
                    emitter.onNext(result);
                }
            }
        });
    }

    public Single<List<Uri>> compressToFileAsObservableSingleV2(List<File> rawFileList, List<String> compressedFileName) {
        return Observable.zip(Observable.fromIterable(rawFileList), Observable.fromIterable(compressedFileName), new BiFunction<File, String, Uri>() {
                    @Override
                    public Uri apply(File file, String compressedFileName) throws Exception {
                        return compressToFile(file, compressedFileName);
                    }
                })
                .toList();
    }

    public Observable<Bitmap> compressToBitmapAsFlowable(final File imageFile) {
        return Observable.defer(new Callable<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                try {
                    return Observable.just(compressToBitmap(imageFile));
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
        });
    }
}
