package thefour.com.worldshop.api;

import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;

/**
 * Created by Quang Quang on 12/8/2016.
 */

public class OfferApi {
    public static void makeOffer(Request request, Offer offer, @Nullable final DatabaseReference.CompletionListener listener) {
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = ref.child(Contracts.REQUEST_OFFERS_LOCATION).child(request.getRequestId())
                .push().getKey();
        offer.setOfferId(key);
        childUpdate.put("/" + Contracts.OFFERS_LOCATION + "/" + key, offer);
        childUpdate.put("/" + Contracts.REQUEST_OFFERS_LOCATION + "/" + request.getRequestId() + "/" + key, offer);
        childUpdate.put("/" + Contracts.USER_OFFERS_LOCATION + "/" + offer.getFromUser().getUserId() + "/" + key, offer);
        ref.updateChildren(childUpdate, listener);
    }

    public static void acceptOffer(Request request, Offer offer, @Nullable final DatabaseReference.CompletionListener listener) {
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = request.getRequestId();
        if (key == null) {
            throw new NullPointerException("Request ID is null");
        }
        offer.setStatus(Offer.STATUS_ACCEPTED);
        childUpdate.put("/" + Contracts.OFFERS_LOCATION + "/" + key, offer);
        childUpdate.put("/" + Contracts.REQUEST_OFFERS_LOCATION + "/" + request.getRequestId() + "/" + key, offer);
        childUpdate.put("/" + Contracts.USER_OFFERS_LOCATION + "/" + offer.getFromUser().getUserId() + "/" + key, offer);

        request.setStatus(Request.STATUS_OFFER_ACCEPTED);

        childUpdate.put("/" + Contracts.REQUESTS_LOCATION + "/" + key, request);
        childUpdate.put("/" + Contracts.USER_REQUESTS_LOCATION + "/" + request.getFromUser().getUserId() + "/" + key, request);
        childUpdate.put("/" + Contracts.CITY_REQUESTS_LOCATION + "/" + request.getDeliverTo().getCityId() + "/" + key, request);

        ref.updateChildren(childUpdate, listener);
    }

    public static void deleteOffer(Request request,
                                   Offer offer,
                                   @Nullable final DatabaseReference.CompletionListener listener) {
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = request.getRequestId();
        if (key == null) {
            throw new NullPointerException("Request ID is null");
        }
        childUpdate.put("/" + Contracts.OFFERS_LOCATION + "/" + key, null);
        childUpdate.put("/" + Contracts.REQUEST_OFFERS_LOCATION + "/" + request.getRequestId() + "/" + key, null);
        childUpdate.put("/" + Contracts.USER_OFFERS_LOCATION + "/" + offer.getFromUser().getUserId() + "/" + key, null);
        ref.updateChildren(childUpdate, listener);
    }

    public static void updateOffer(Request request,
                                   Offer offer,
                                   @Nullable final DatabaseReference.CompletionListener listener) {
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = request.getRequestId();
        if (key == null) {
            throw new NullPointerException("Request ID is null");
        }
        childUpdate.put("/" + Contracts.OFFERS_LOCATION + "/" + key, offer);
        childUpdate.put("/" + Contracts.REQUEST_OFFERS_LOCATION + "/" + request.getRequestId() + "/" + key, offer);
        childUpdate.put("/" + Contracts.USER_OFFERS_LOCATION + "/" + offer.getFromUser().getUserId() + "/" + key, offer);
        ref.updateChildren(childUpdate, listener);
    }
}