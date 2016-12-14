package thefour.com.worldshop.api;

import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

/**
 * Created by Quang Quang on 12/8/2016.
 */

public class OfferApi {
    public static void makeOffer(Request request, Offer offer, @Nullable final DatabaseReference.CompletionListener listener) {
        if(request.getFromUser().getUserId().equals(offer.getFromUser().getUserId())){
            throw new IllegalStateException("user can not make offer for their own request");
        }
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

    public static void acceptOffer(final Request request, final Offer offer, User loggedUser, @Nullable final DatabaseReference.CompletionListener listener) {
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = offer.getOfferId();
        if (key == null) {
            throw new NullPointerException("Request ID is null");
        }
        if(request.getStatus().equals(Request.STATUS_OFFER_ACCEPTED)){
            throw new IllegalStateException("Request can only accept one offer");
        }
        if(!loggedUser.getUserId().equals(request.getFromUser().getUserId())){
            throw new IllegalStateException(loggedUser.getName()+" with UID: "
                    + loggedUser.getUserId() + " - don't have permission to accept offer");
        }


        offer.setStatus(Offer.STATUS_ACCEPTED);
        childUpdate.put("/" + Contracts.OFFERS_LOCATION + "/" + key, offer);
        childUpdate.put("/" + Contracts.REQUEST_OFFERS_LOCATION + "/" + request.getRequestId() + "/" + key, offer);
        childUpdate.put("/" + Contracts.USER_OFFERS_LOCATION + "/" + offer.getFromUser().getUserId() + "/" + key, offer);

        request.setStatus(Request.STATUS_OFFER_ACCEPTED);

        childUpdate.put("/" + Contracts.REQUESTS_LOCATION + "/" + request.getRequestId(), request);
        childUpdate.put("/" + Contracts.USER_REQUESTS_LOCATION + "/" + request.getFromUser().getUserId() + "/" + request.getRequestId(), request);
        childUpdate.put("/" + Contracts.CITY_REQUESTS_LOCATION + "/" + request.getDeliverTo().getCityId() + "/" + request.getRequestId(), request);
        //TODO minus amount of money (total request price + traveler award)
        ref.updateChildren(childUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                double money = request.getQuantity() * request.getItem().getPrice() + offer.getFee();
                UserApi.withdrawal(money + money * Contracts.SERVICE_FEE, request.getFromUser(), listener);
            }
        });
    }

    public static void deleteOffer(Request request,
                                   Offer offer,
                                   @Nullable final DatabaseReference.CompletionListener listener) {
        if (offer.getStatus().equals(Offer.STATUS_ACCEPTED)) {
            throw new IllegalStateException("You have to cancel accepted offer before delete it");
        }
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = offer.getOfferId();
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
        if(!request.getStatus().equals(Request.STATUS_PENDING)){
            throw new IllegalStateException("Request status is not pending for udpate Offer");
        }
        if (offer.getStatus().equals(Offer.STATUS_ACCEPTED)) {
            throw new IllegalStateException("You have to cancel accepted offer before update it");
        }
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = offer.getOfferId();
        if (key == null) {
            throw new NullPointerException("Request ID is null");
        }
        offer.setLastTimeEdited(System.currentTimeMillis());
        childUpdate.put("/" + Contracts.OFFERS_LOCATION + "/" + key, offer);
        childUpdate.put("/" + Contracts.REQUEST_OFFERS_LOCATION + "/" + request.getRequestId() + "/" + key, offer);
        childUpdate.put("/" + Contracts.USER_OFFERS_LOCATION + "/" + offer.getFromUser().getUserId() + "/" + key, offer);
        ref.updateChildren(childUpdate, listener);
    }

    public interface OfferEventListener{
        void onOfferAdded(Offer offer, String previousCityId);
        void onOfferChanged(Offer newOffer, String previousCityId);
        void onOfferRemoved(Offer offer);
        void onOfferMoved(Offer offer, String s);
        void onError(DatabaseError error);
    }

    public static ChildEventListener loadOffers(String requestId, final OfferEventListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(Contracts.REQUEST_OFFERS_LOCATION).child(requestId);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Offer offer = dataSnapshot.getValue(Offer.class);
                listener.onOfferAdded(offer, s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Offer offer = dataSnapshot.getValue(Offer.class);
                listener.onOfferChanged(offer, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Offer offer = dataSnapshot.getValue(Offer.class);
                listener.onOfferRemoved(offer);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Offer offer = dataSnapshot.getValue(Offer.class);
                listener.onOfferMoved(offer, s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError);
            }
        };
        ref.addChildEventListener(childEventListener);
        return childEventListener;
    }

    public static void undoAccepOffer(final Request request, final Offer offer, User loggedUser, @Nullable final DatabaseReference.CompletionListener listener){
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = offer.getOfferId();
        if (key == null) {
            throw new NullPointerException("Request ID is null");
        }
        if(!request.getStatus().equals(Request.STATUS_OFFER_ACCEPTED)){
            throw new IllegalStateException("Request status is: "+request.getStatus()+" only valid if status is "+Request.STATUS_OFFER_ACCEPTED);
        }
        if(!loggedUser.getUserId().equals(offer.getFromUser().getUserId())){
            throw new IllegalStateException(offer.getFromUser().getName()+" with UID: "
                    + loggedUser.getUserId() + " - don't have permission to accept offer");
        }


        offer.setStatus(Offer.STATUS_PENDING);
        childUpdate.put("/" + Contracts.OFFERS_LOCATION + "/" + key, offer);
        childUpdate.put("/" + Contracts.REQUEST_OFFERS_LOCATION + "/" + request.getRequestId() + "/" + key, offer);
        childUpdate.put("/" + Contracts.USER_OFFERS_LOCATION + "/" + offer.getFromUser().getUserId() + "/" + key, offer);

        request.setStatus(Request.STATUS_PENDING);

        childUpdate.put("/" + Contracts.REQUESTS_LOCATION + "/" + request.getRequestId(), request);
        childUpdate.put("/" + Contracts.USER_REQUESTS_LOCATION + "/" + request.getFromUser().getUserId() + "/" + request.getRequestId(), request);
        childUpdate.put("/" + Contracts.CITY_REQUESTS_LOCATION + "/" + request.getDeliverTo().getCityId() + "/" + request.getRequestId(), request);
        //TODO minus amount of money (total request price + traveler award)
        ref.updateChildren(childUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                double money = request.getQuantity() * request.getItem().getPrice() + offer.getFee();
                UserApi.recharge(money + money * Contracts.SERVICE_FEE, request.getFromUser(), listener);
            }
        });
    }

    public static void offerCompleted(final Request request, final Offer offer, User loggedUser, @Nullable final DatabaseReference.CompletionListener listener){
        //todo|set request state to Request.STATUS_COMPLETE
        //todo|pay money for traveler (request fee + reward)
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = request.getRequestId();
        if (key == null) {
            throw new NullPointerException("Request ID is null");
        }
        if(!request.getStatus().equals(Request.STATUS_OFFER_ACCEPTED)){
            throw new IllegalStateException("Request can only complete when in status "+Request.STATUS_OFFER_ACCEPTED);
        }
        if(!loggedUser.getUserId().equals(request.getFromUser().getUserId())){
            throw new IllegalStateException(loggedUser.getName()+" with UID: "
                    + loggedUser.getUserId() + " - don't have permission to accept offer");
        }



        request.setStatus(Request.STATUS_COMPLETE);

        childUpdate.put("/" + Contracts.REQUESTS_LOCATION + "/" + key, request);
        childUpdate.put("/" + Contracts.USER_REQUESTS_LOCATION + "/" + request.getFromUser().getUserId() + "/" + key, request);
        childUpdate.put("/" + Contracts.CITY_REQUESTS_LOCATION + "/" + request.getDeliverTo().getCityId() + "/" + key, request);
        //TODO minus amount of money (total request price + traveler award)
        ref.updateChildren(childUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                double money = request.getQuantity() * request.getItem().getPrice() + offer.getFee();
                UserApi.recharge(money, offer.getFromUser(), listener);
            }
        });
    }
}
