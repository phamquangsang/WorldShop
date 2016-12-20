package thefour.com.worldshop.api;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.Exchanger;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.Message;
import thefour.com.worldshop.models.User;

/**
 * Created by Quang Quang on 12/20/2016.
 */



public class MessageApi {
    static class IllegalChatException extends RuntimeException{
        public IllegalChatException(String message) {
            super(message);
        }
    }

    public static void sendMessage(Message message, User loggedUser, User chatWith
            , DatabaseReference.CompletionListener listener){
        if(loggedUser.getUserId().equals(chatWith.getUserId())){
            throw new IllegalChatException("two User is the same");
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Contracts.REQUEST_CONVERSATION_LOCATION)
                .getRef();
        String key = ref.child(loggedUser.getUserId()).child(chatWith.getUserId()).push().getKey();
        message.setMessageId(key);
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("/" + loggedUser.getUserId() + "/" + chatWith.getUserId()+ "/" +key, message);
        updates.put("/" + chatWith.getUserId() + "/" + loggedUser.getUserId() + "/" +key, message);
        ref.updateChildren(updates, listener);
    }


}
