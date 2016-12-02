package thefour.com.worldshop.api;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Request;

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

    public interface RequestEventListener{
        abstract void onRequestAdded(Request request, String previousCityId);
        abstract void onRequestChanged(Request newRequest, String previousCityId);
        abstract void onRequestRemoved(Request request);
        abstract void onRequestMoved(Request request, String s);
        abstract void onError(DatabaseError error);
    }
    public static ChildEventListener loadRequestInCity(City city, final RequestEventListener eventListener){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(Contracts.CITY_REQUESTS_LOCATION).child(city.getCityId());
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
        return listener;
    }

    public static ChildEventListener loadLatestRequestInCity(final RequestEventListener eventListener
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
