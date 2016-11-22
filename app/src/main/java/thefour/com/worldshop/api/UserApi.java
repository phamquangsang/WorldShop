package thefour.com.worldshop.api;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.models.User;

/**
 * Created by Quang Quang on 11/18/2016.
 */

public class UserApi {
    private static final String TAG = UserApi.class.getSimpleName();

    public interface IsUserExistCallback{
        //callback return existing user,
        // or null if user is not existing
        void onUserExist(@Nullable User user);
    }


    public static void retrieveUserById(String Uid, final IsUserExistCallback callback){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child(Contracts.USER_LOCATION).child(Uid);
        userRef.keepSynced(true);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User();
                user = dataSnapshot.getValue(User.class);
                callback.onUserExist(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: isUserExist()", databaseError.toException());
            }
        });
    }

    public static void createNewUser(User user,@Nullable DatabaseReference.CompletionListener completeCallback){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child(Contracts.USER_LOCATION).child(user.getUserId());
        if(user.getEmail()!=null){
            //Firebase don't allow keys that contain ., $, #, [, ], /,
            // or ASCII control characters 0-31 or 127.
            user.setEmail(Util.encodeEmail(user.getEmail()));
        }
        userRef.setValue(user, completeCallback);
    }

    public static void retrieveUserByEmail(String email, final IsUserExistCallback callback){
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(Contracts.USER_LOCATION).orderByChild(Contracts.PRO_USER_EMAIL)
                .equalTo(Util.encodeEmail(email));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user :
                        dataSnapshot.getChildren()) {
                    callback.onUserExist(user.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
