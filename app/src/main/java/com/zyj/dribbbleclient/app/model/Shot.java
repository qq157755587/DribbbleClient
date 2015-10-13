package com.zyj.dribbbleclient.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Shot implements Parcelable {
    public int id;
    public String title;
    public String description;
    public int width;
    public int height;
    public Images images;
    public int views_count;
    public int likes_count;
    public int comments_count;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeParcelable(this.images, 0);
        dest.writeInt(this.views_count);
        dest.writeInt(this.likes_count);
        dest.writeInt(this.comments_count);
    }

    public Shot() {
    }

    protected Shot(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.images = in.readParcelable(Images.class.getClassLoader());
        this.views_count = in.readInt();
        this.likes_count = in.readInt();
        this.comments_count = in.readInt();
    }

    public static final Parcelable.Creator<Shot> CREATOR = new Parcelable.Creator<Shot>() {
        public Shot createFromParcel(Parcel source) {
            return new Shot(source);
        }

        public Shot[] newArray(int size) {
            return new Shot[size];
        }
    };
}
