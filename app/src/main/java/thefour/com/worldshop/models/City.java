package thefour.com.worldshop.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Quang Quang on 11/17/2016.
 */

public class City implements Parcelable {
    private String cityId;
    private String name;
    private String imageUrl;

    public City() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
    }

    protected City(Parcel in) {
        this.cityId = in.readString();
        this.name = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
