package thefour.com.worldshop.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Quang Quang on 11/17/2016.
 */

public class City implements Parcelable {
    private static final String defaulUrl = "https://www.vietnamsunshinetravel.com/wp-content/uploads/2015/04/Da-Lat-daily-Tours.jpg";

    public static String getDefaulUrl() {
        return defaulUrl;
    }

    private String cityId;
    private String name;
    private String image;

    public City() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        dest.writeString(this.image);
        dest.writeString(this.name);
        dest.writeString(this.cityId);
    }

    protected City(Parcel in) {
        this.image = in.readString();
        this.name = in.readString();
        this.cityId = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public String toString() {
        return "City{" +
                "cityId='" + cityId + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + image + '\'' +
                '}';
    }
}
