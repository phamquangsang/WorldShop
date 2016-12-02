package thefour.com.worldshop.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;

import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Item;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

/**
 * Created by Quang Quang on 11/30/2016.
 */

public class RequestViewModel extends BaseObservable {
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_INACTIVE = "inactive";//when user de-active request
    public static final String STATUS_COMPLETE = "complete";
    public static final String STATUS_OFFER_ACCEPTED = "offerAccepted";

    private String requestId;
    private String status;
    private double reward;
    private int quantity;
    private UserViewModel fromUser;
    private ItemViewModel item;
    private CityViewModel deliverTo;
    private HashMap<String, OfferViewModel> offers;
    private long time;

    private Context mContext;

    public RequestViewModel(Request request, Context c) {
        mContext = c;
        this.requestId = request.getRequestId();
        this.status = request.getStatus();
        this.reward = request.getReward();
        this.quantity = request.getQuantity();
        this.fromUser = new UserViewModel(request.getFromUser(), c);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
        notifyChange();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyChange();
    }

    public String getReward() {

        return "$"+reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
        notifyChange();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        notifyChange();
    }





    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        notifyChange();
    }
}
