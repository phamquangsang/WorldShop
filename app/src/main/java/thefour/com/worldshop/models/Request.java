package thefour.com.worldshop.models;

import java.util.HashMap;

/**
 * Created by Quang Quang on 11/17/2016.
 */

public class Request {
    private String requestId;
    private String status;
    private double reward;
    private int quantity;
    private User fromUser;
    private Item item;
    private City deliverTo;
    private HashMap<String, Offer> offers;
    private long time;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
}
