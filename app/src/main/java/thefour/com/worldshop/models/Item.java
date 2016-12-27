package thefour.com.worldshop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Quang Quang on 11/17/2016.
 */

public class Item extends BaseObservable implements Parcelable{
    private String itemId;
    private String itemUrl;
    private String firstImage;
    private ArrayList<String> images;
    private String name;
    private String description;
    private double price;

    public Item() {
    }

    @Bindable
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
        notifyChange();
    }

    @Bindable
    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
        notifyChange();
    }

    @Bindable
    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
        notifyChange();
    }

    @Bindable
    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
        notifyChange();
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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyChange();
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        notifyChange();
    }

    public String getPriceString(){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return "$" + decimalFormat.format(getPrice());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemId);
        dest.writeString(this.itemUrl);
        dest.writeString(this.firstImage);
        dest.writeStringList(this.images);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeDouble(this.price);
    }

    protected Item(Parcel in) {
        this.itemId = in.readString();
        this.itemUrl = in.readString();
        this.firstImage = in.readString();
        this.images = in.createStringArrayList();
        this.name = in.readString();
        this.description = in.readString();
        this.price = in.readDouble();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
