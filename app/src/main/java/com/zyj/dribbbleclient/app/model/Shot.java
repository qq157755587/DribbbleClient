package com.zyj.dribbbleclient.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Shot extends BaseModel implements Parcelable {
    public int id;
    public String title;
    public String url;
    public String short_url;
    public String image_url;
    public String image_teaser_url;
    public int width;
    public int height;
    public int views_count;
    public int likes_count;
    public int comments_count;
    public int rebounds_count;
    public int rebound_source_id;
    public String created_at;
    public Player player;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.short_url);
        dest.writeString(this.image_url);
        dest.writeString(this.image_teaser_url);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.views_count);
        dest.writeInt(this.likes_count);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.rebounds_count);
        dest.writeInt(this.rebound_source_id);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.player, 0);
    }

    public Shot() {
    }

    private Shot(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.short_url = in.readString();
        this.image_url = in.readString();
        this.image_teaser_url = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.views_count = in.readInt();
        this.likes_count = in.readInt();
        this.comments_count = in.readInt();
        this.rebounds_count = in.readInt();
        this.rebound_source_id = in.readInt();
        this.created_at = in.readString();
        this.player = in.readParcelable(Player.class.getClassLoader());
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
