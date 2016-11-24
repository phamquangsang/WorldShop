package thefour.com.worldshop.models;

/**
 * Created by Quang Quang on 11/18/2016.
 */

public class Offer {
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

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLastTimeEdited() {
        return lastTimeEdited;
    }

    public void setLastTimeEdited(long lastTimeEdited) {
        this.lastTimeEdited = lastTimeEdited;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public City getDeliverFrom() {
        return deliverFrom;
    }

    public void setDeliverFrom(City deliverFrom) {
        this.deliverFrom = deliverFrom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
