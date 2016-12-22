package thefour.com.worldshop.api;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

/**
 * Created by Quang Quang on 11/24/2016.
 */

public class RequestApi {
    private static String TAG = RequestApi.class.getSimpleName();

    public static void createRequest(Request request, DatabaseReference.CompletionListener completionListener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = ref.child(Contracts.REQUESTS_LOCATION).push().getKey();
        request.setRequestId(key);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Contracts.REQUESTS_LOCATION + "/" + key, request);
        childUpdates.put("/" + Contracts.USER_REQUESTS_LOCATION + "/" + request.getFromUser().getUserId() + "/" + key, request);
        childUpdates.put("/" + Contracts.CITY_REQUESTS_LOCATION + "/" + request.getDeliverTo().getCityId() + "/" + key, request);
        ref.updateChildren(childUpdates, completionListener);
    }

    public static void updateRequest(Request request, DatabaseReference.CompletionListener completionListener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = request.getRequestId();
        request.setRequestId(key);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Contracts.REQUESTS_LOCATION + "/" + key, request);
        childUpdates.put("/" + Contracts.USER_REQUESTS_LOCATION + "/" + request.getFromUser().getUserId() + "/" + key, request);
        childUpdates.put("/" + Contracts.CITY_REQUESTS_LOCATION + "/" + request.getDeliverTo().getCityId() + "/" + key, request);
        ref.updateChildren(childUpdates, completionListener);
    }

    public interface RequestChildEventListener {
        abstract void onRequestAdded(Request request, String previousCityId);
        abstract void onRequestChanged(Request newRequest, String previousCityId);
        abstract void onRequestRemoved(Request request);
        abstract void onRequestMoved(Request request, String s);
        abstract void onError(DatabaseError error);
    }

    public interface RequestValueEventListener{
        void onDataChange(List<Request> requestList);
        void onDatabaseError(DatabaseError error);
    }

    public static ChildEventListener loadRequestInCity(City city, final RequestChildEventListener eventListener){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(Contracts.CITY_REQUESTS_LOCATION).child(city.getCityId());
//        Query pendingRequest = ref.orderByChild(Contracts.PRO_REQUEST_STATUS).equalTo(Request.STATUS_PENDING);
        final ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded: "+dataSnapshot.toString());
                eventListener.onRequestAdded(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                eventListener.onRequestChanged(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                eventListener.onRequestRemoved(dataSnapshot.getValue(Request.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                eventListener.onRequestMoved(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventListener.onError(databaseError);
            }

        };

        ref.addChildEventListener(listener);
//        pendingRequest.addChildEventListener(listener);
        return listener;
    }

    public static ChildEventListener loadLatestUserRequestChildEvent(User user, int limitedSize, final RequestChildEventListener eventListener){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(Contracts.USER_REQUESTS_LOCATION).child(user.getUserId());
        Query query = ref.orderByChild(Contracts.PRO_REQUEST_ID);
        if(limitedSize>0){
            query = query.limitToLast(limitedSize);
        }
        final ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded: "+dataSnapshot.toString());
                eventListener.onRequestAdded(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                eventListener.onRequestChanged(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                eventListener.onRequestRemoved(dataSnapshot.getValue(Request.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                eventListener.onRequestMoved(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventListener.onError(databaseError);
            }

        };

        query.addChildEventListener(listener);
        return listener;
    }


    ////using this method cause Memory leak.
//    public static ValueEventListener loadLatestUserRequestValueEvent(User user, final int limitedSize, final RequestValueEventListener eventListener){
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref = ref.child(Contracts.USER_REQUESTS_LOCATION).child(user.getUserId());
//        Query query = ref.orderByChild(Contracts.PRO_REQUEST_ID);
//        if(limitedSize>0){
//            query = query.limitToLast(limitedSize);
//        }
//
//        ValueEventListener valueListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Request> listRequest = new ArrayList<>();
//                for (DataSnapshot requestNS :
//                        dataSnapshot.getChildren()) {
//                    listRequest.add(requestNS.getValue(Request.class));
//                }
//                eventListener.onDataChange(listRequest);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                eventListener.onDatabaseError(databaseError);
//            }
//        };
//        query.addValueEventListener(valueListener);
//        return valueListener;
//    }

    public static ChildEventListener loadLatestRequest(final RequestChildEventListener eventListener
            , final DatabaseReference.CompletionListener completionListener){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(Contracts.REQUESTS_LOCATION);
        final ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded: "+dataSnapshot.toString());
                eventListener.onRequestAdded(dataSnapshot.getValue(Request.class),s);
                completionListener.onComplete(null, null);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                eventListener.onRequestChanged(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                eventListener.onRequestRemoved(dataSnapshot.getValue(Request.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                eventListener.onRequestMoved(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventListener.onError(databaseError);
            }

        };

        ref.addChildEventListener(listener);
        return listener;
    }
}
