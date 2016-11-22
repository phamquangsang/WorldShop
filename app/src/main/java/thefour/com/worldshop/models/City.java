package thefour.com.worldshop.models;

/**
 * Created by Quang Quang on 11/17/2016.
 */

public class City {
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
}
