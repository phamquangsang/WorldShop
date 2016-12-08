package thefour.com.worldshop.api;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

/**
 * Created by Quang Quang on 12/8/2016.
 */

public class OfferApi {
    public static void makeOffer(Context c, Request request, User userMakeOffer, Offer offer, @Nullable final DatabaseReference.CompletionListener listener){
        Map<String, Object> childUpdate = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = ref.child(Contracts.REQUESTS_LOCATION).child(request.getRequestId())
                .child(Contracts.PRO_REQUEST_OFFERS).push().getKey();
        offer.setOfferId(key);
        childUpdate.put("/"+ Contracts.OFFERS_LOCATION+"/"+key, offer);
        childUpdate.put("/"+ Contracts.REQUESTS_LOCATION+"/" + request.getRequestId()+"/"+ Contracts.PRO_REQUEST_OFFERS+ "/"+key, offer);
        childUpdate.put("/"+ Contracts.USER_OFFERS_LOCATION+ "/"+ userMakeOffer.getUserId()+"/"+key, offer);
        ref.updateChildren(childUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(listener!=null){
                    listener.onComplete(databaseError, databaseReference);
                }
            }
        });
    }

//    public static void acceptOffer()
}
