package com.zyj.dribbbleclient.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhaoyuanjie on 15/10/10.
 */
public class Images implements Parcelable {
    public String hidpi;
    public String normal;
    public String teaser;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hidpi);
        dest.writeString(this.normal);
        dest.writeString(this.teaser);
    }

    public Images() {
    }

    protected Images(Parcel in) {
        this.hidpi = in.readString();
        this.normal = in.readString();
        this.teaser = in.readString();
    }

    public static final Parcelable.Creator<Images> CREATOR = new Parcelable.Creator<Images>() {
        public Images createFromParcel(Parcel source) {
            return new Images(source);
        }

        public Images[] newArray(int size) {
            return new Images[size];
        }
    };
}
