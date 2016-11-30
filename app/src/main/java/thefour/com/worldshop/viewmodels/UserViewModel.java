package thefour.com.worldshop.viewmodels;

import android.content.Context;

import thefour.com.worldshop.models.User;

/**
 * Created by Quang Quang on 11/30/2016.
 */

public class UserViewModel {
    private String userId;
    private double money;
    private String name;
    private String email;
    private String profileImageUrl;
    private long timeJoined;

    private Context mContext;

    public UserViewModel(User user, Context context){
        userId = user.getUserId();
        money = user.getMoney();
        name = user.getName();
        email = user.getEmail();
        profileImageUrl = user.getProfileImageUrl();
        timeJoined = user.getTimeJoined();

        mContext = context;
    }

    public String getUserId() {
        return userId;
    }

    public double getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public long getTimeJoined() {
        return timeJoined;
    }

    public Context getmContext() {
        return mContext;
    }
}
