package thefour.com.worldshop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

/**
 * Created by Quang Quang on 11/17/2016.
 */

public class User extends BaseObservable implements Parcelable {
    private String userId;
    private double money;
    private String name;
    private String email;
    private String profileImageUrl;
    private long timeJoined;

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", money=" + money +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", timeJoined=" + timeJoined +
                '}';
    }

    public User() {
    }

    @Bindable
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        notifyChange();
    }

    public void setMoney(double money) {
        this.money = money;
        notifyChange();
    }

    @Bindable
    public double getMoney() {
        return money;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange();
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyChange();
    }

    @Bindable
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        notifyChange();
    }

    @Bindable
    public long getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(long timeJoined) {
        this.timeJoined = timeJoined;
        notifyChange();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeDouble(this.money);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.profileImageUrl);
        dest.writeLong(this.timeJoined);
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.money = in.readDouble();
        this.name = in.readString();
        this.email = in.readString();
        this.profileImageUrl = in.readString();
        this.timeJoined = in.readLong();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
