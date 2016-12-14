package thefour.com.worldshop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;

import thefour.com.worldshop.R;

/**
 * Created by Quang Quang on 11/18/2016.
 */

public class Offer extends BaseObservable implements Parcelable  {
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ACCEPTED = "accepted";
    private String offerId;
    private String requestId;
    private long time;
    private long lastTimeEdited;
    private double fee;
    private City deliverFrom;
    private String status;
    private User fromUser;
    private Item item;
    private long deliveryDate;
    private String note;

    @Bindable
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
        notifyChange();
    }

    @Bindable
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
        notifyChange();
    }

    @Bindable
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        notifyChange();
    }

    @Bindable
    public long getLastTimeEdited() {
        return lastTimeEdited;
    }

    public void setLastTimeEdited(long lastTimeEdited) {
        this.lastTimeEdited = lastTimeEdited;
        notifyChange();
    }

    @Bindable
    public double getFee() {
        return fee;
    }

    public String getFeeInString(){
        return "$"+getFee();
    }

    public void setFee(double fee) {
        this.fee = fee;
        notifyChange();
    }

    @Bindable
    public City getDeliverFrom() {
        return deliverFrom;
    }

    public void setDeliverFrom(City deliverFrom) {
        this.deliverFrom = deliverFrom;
        notifyChange();
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyChange();
    }

    @Bindable
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
        notifyChange();
    }

    @Bindable
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        notifyChange();
    }

    @Bindable
    public long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(long deliveryDate) {
        this.deliveryDate = deliveryDate;
        notifyChange();
    }

    @Bindable
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyChange();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.offerId);
        dest.writeString(this.requestId);
        dest.writeLong(this.time);
        dest.writeLong(this.lastTimeEdited);
        dest.writeDouble(this.fee);
        dest.writeParcelable(this.deliverFrom, flags);
        dest.writeString(this.status);
        dest.writeParcelable(this.fromUser, flags);
        dest.writeParcelable(this.item, flags);
        dest.writeLong(this.deliveryDate);
        dest.writeString(this.note);
    }

    public Offer() {
    }

    protected Offer(Parcel in) {
        this.offerId = in.readString();
        this.requestId = in.readString();
        this.time = in.readLong();
        this.lastTimeEdited = in.readLong();
        this.fee = in.readDouble();
        this.deliverFrom = in.readParcelable(City.class.getClassLoader());
        this.status = in.readString();
        this.fromUser = in.readParcelable(User.class.getClassLoader());
        this.item = in.readParcelable(Item.class.getClassLoader());
        this.deliveryDate = in.readLong();
        this.note = in.readString();
    }

    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel source) {
            return new Offer(source);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
}
