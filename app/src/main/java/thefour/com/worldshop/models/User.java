package thefour.com.worldshop.models;

import java.util.HashMap;

/**
 * Created by Quang Quang on 11/17/2016.
 */

public class User {
    private String userId;
    private double money;
    private String name;
    private String email;
    private String profileImageUrl;
    private long timeJoined;

    public String getUserId() {
        return userId;
    }


    public double getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public long getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(long timeJoined) {
        this.timeJoined = timeJoined;
    }
}
