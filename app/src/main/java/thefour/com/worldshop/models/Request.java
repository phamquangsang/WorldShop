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
}
