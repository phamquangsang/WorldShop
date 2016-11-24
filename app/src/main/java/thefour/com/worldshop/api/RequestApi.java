package thefour.com.worldshop.api;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.Request;

/**
 * Created by Quang Quang on 11/24/2016.
 */

public class RequestApi {
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
}
