package thefour.com.worldshop.api;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.Notification;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;

/**
 * Created by Quang Quang on 12/22/2016.
 */

public class NotificationApi {
    public static void makeOfferNotify(Request request, Offer offer, DatabaseReference.CompletionListener listener){
        //this is for notification
        Notification noti = new Notification();
        noti.setRequest(request);
        noti.setFromUser(offer.getFromUser());
        noti.setAction(Notification.ACTION_MAKE_OFFER);
        noti.setTime(System.currentTimeMillis());
        noti.setOffer(offer);

        Map<String, Object> updates = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Contracts.NOTIFICATION_LOCATION);
        String key = ref.child(request.getFromUser().getUserId()).push().getKey();
        updates.put("/"+request.getFromUser().getUserId()+"/"+key, noti);

        ref.updateChildren(updates, listener);
    }


    public static void updateOfferNotify(Request request, Offer offer, DatabaseReference.CompletionListener listener){
        //this is for notification
        Notification noti = new Notification();
        noti.setRequest(request);
        noti.setFromUser(offer.getFromUser());
        noti.setAction(Notification.ACTION_UPDATE_OFFER);
        noti.setTime(System.currentTimeMillis());
        noti.setOffer(offer);

        Map<String, Object> updates = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Contracts.NOTIFICATION_LOCATION);
        String key = ref.child(request.getFromUser().getUserId()).push().getKey();
        updates.put("/"+request.getFromUser().getUserId()+"/"+key, noti);

        ref.updateChildren(updates, listener);
    }

    public static void deleteOfferNotify(Request request, Offer offer, DatabaseReference.CompletionListener listener){
        //this is for notification
        Notification noti = new Notification();
        noti.setRequest(request);
        noti.setFromUser(offer.getFromUser());
        noti.setAction(Notification.ACTION_DELETE_OFFER);
        noti.setTime(System.currentTimeMillis());
        noti.setOffer(offer);

        Map<String, Object> updates = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Contracts.NOTIFICATION_LOCATION);
        String key = ref.child(request.getFromUser().getUserId()).push().getKey();
        updates.put("/"+request.getFromUser().getUserId()+"/"+key, noti);

        ref.updateChildren(updates, listener);
    }

    public static void cancelOfferNotify(Request request, Offer offer, DatabaseReference.CompletionListener listener){
        //this is for notification
        Notification noti = new Notification();
        noti.setRequest(request);
        noti.setFromUser(offer.getFromUser());
        noti.setAction(Notification.ACTION_CANCEL_OFFER);
        noti.setTime(System.currentTimeMillis());
        noti.setOffer(offer);

        Map<String, Object> updates = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Contracts.NOTIFICATION_LOCATION);
        String key = ref.child(request.getFromUser().getUserId()).push().getKey();
        updates.put("/"+request.getFromUser().getUserId()+"/"+key, noti);

        ref.updateChildren(updates, listener);
    }


    //TODO this one different from above. User who make request notify user who make offer that they accept offer
    public static void acceptOfferNotify(Request request, Offer offer, DatabaseReference.CompletionListener listener){
        Notification noti = new Notification();
        noti.setRequest(request);
        noti.setFromUser(request.getFromUser());
        noti.setAction(Notification.ACTION_ACCEPT_OFFER);
        noti.setTime(System.currentTimeMillis());
        noti.setOffer(offer);

        Map<String, Object> updates = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Contracts.NOTIFICATION_LOCATION);
        String key = ref.child(offer.getFromUser().getUserId()).push().getKey();
        updates.put("/"+offer.getFromUser().getUserId()+"/"+key, noti);

        ref.updateChildren(updates, listener);
    }
}
