package com.framework.common.image_select.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class TakeBean implements Parcelable {
    private Uri uri;
    private String path;

    public TakeBean(){

    }

    protected TakeBean(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        path = in.readString();
    }

    public static final Creator<TakeBean> CREATOR = new Creator<TakeBean>() {
        @Override
        public TakeBean createFromParcel(Parcel in) {
            return new TakeBean(in);
        }

        @Override
        public TakeBean[] newArray(int size) {
            return new TakeBean[size];
        }
    };

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
        dest.writeString(path);
    }
}