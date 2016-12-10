package thefour.com.worldshop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

import thefour.com.worldshop.BR;

/**
 * Created by Quang Quang on 11/17/2016.
 */

public class Request extends BaseObservable implements Parcelable{
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_INACTIVE = "inactive";//when user de-active request
    public static final String STATUS_COMPLETE = "complete";
    public static final String STATUS_OFFER_ACCEPTED = "offerAccepted";

    private String requestId;
    private String status;
    private double reward;
    private int quantity;
    private User fromUser;
    private Item item;
    private City deliverTo;
    private HashMap<String, Offer> offers;
    private long time;

    @Bindable
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Bindable
    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    @Bindable
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public City getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(City deliverTo) {
        this.deliverTo = deliverTo;
    }

    public HashMap<String, Offer> getOffers() {
        return offers;
    }

    public void setOffers(HashMap<String, Offer> offers) {
        this.offers = offers;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.requestId);
        dest.writeString(this.status);
        dest.writeDouble(this.reward);
        dest.writeInt(this.quantity);
        dest.writeParcelable(this.fromUser, flags);
        dest.writeParcelable(this.item, flags);
        dest.writeParcelable(this.deliverTo, flags);
        dest.writeSerializable(this.offers);
        dest.writeLong(this.time);
    }

    public Request() {
    }

    protected Request(Parcel in) {
        this.requestId = in.readString();
        this.status = in.readString();
        this.reward = in.readDouble();
        this.quantity = in.readInt();
        this.fromUser = in.readParcelable(User.class.getClassLoader());
        this.item = in.readParcelable(Item.class.getClassLoader());
        this.deliverTo = in.readParcelable(City.class.getClassLoader());
        this.offers = (HashMap<String, Offer>) in.readSerializable();
        this.time = in.readLong();
    }

    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };


}
