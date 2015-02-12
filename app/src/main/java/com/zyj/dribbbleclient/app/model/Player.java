package com.zyj.dribbbleclient.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Player extends BaseModel implements Parcelable {
    public int id;
    public String name;
    public String username;
    public String url;
    public String avatar_url;
    public String location;
    public String twitter_screen_name;
    public String drafted_by_player_id;
    public int shots_count;
    public int draftees_count;
    public int followers_count;
    public int following_count;
    public int comments_count;
    public int comments_received_count;
    public int likes_count;
    public int likes_received_count;
    public int rebounds_count;
    public int rebounds_received_count;
    public String created_at;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.url);
        dest.writeString(this.avatar_url);
        dest.writeString(this.location);
        dest.writeString(this.twitter_screen_name);
        dest.writeString(this.drafted_by_player_id);
        dest.writeInt(this.shots_count);
        dest.writeInt(this.draftees_count);
        dest.writeInt(this.followers_count);
        dest.writeInt(this.following_count);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.comments_received_count);
        dest.writeInt(this.likes_count);
        dest.writeInt(this.likes_received_count);
        dest.writeInt(this.rebounds_count);
        dest.writeInt(this.rebounds_received_count);
        dest.writeString(this.created_at);
    }

    public Player() {
    }

    private Player(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.username = in.readString();
        this.url = in.readString();
        this.avatar_url = in.readString();
        this.location = in.readString();
        this.twitter_screen_name = in.readString();
        this.drafted_by_player_id = in.readString();
        this.shots_count = in.readInt();
        this.draftees_count = in.readInt();
        this.followers_count = in.readInt();
        this.following_count = in.readInt();
        this.comments_count = in.readInt();
        this.comments_received_count = in.readInt();
        this.likes_count = in.readInt();
        this.likes_received_count = in.readInt();
        this.rebounds_count = in.readInt();
        this.rebounds_received_count = in.readInt();
        this.created_at = in.readString();
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
