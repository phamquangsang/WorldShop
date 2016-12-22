package thefour.com.worldshop.api;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.models.Friend;
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
                .getRef();
        String key = ref.child(loggedUser.getUserId()).child(chatWith.getUserId()).push().getKey();
        message.setMessageId(key);
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Contracts.CONVERSATION_LOCATION + "/" + loggedUser.getUserId() + "/" + chatWith.getUserId()+ "/" +key, message);
        updates.put(Contracts.CONVERSATION_LOCATION + "/" + chatWith.getUserId() + "/" + loggedUser.getUserId() + "/" +key, message);

        Friend friend = new Friend();
        friend.setLatestMessage(message);
        friend.setUser(chatWith);

        updates.put(Contracts.FRIENDS_LIST_LOCATION + "/" + loggedUser.getUserId() + "/" + chatWith.getUserId()  + "/", friend);

        updates.put(Contracts.FRIENDS_LIST_LOCATION + "/"  + chatWith.getUserId()+ "/" + loggedUser.getUserId()  + "/" +Contracts.PRO_FRIEND_LIST_USER, loggedUser);
        updates.put(Contracts.FRIENDS_LIST_LOCATION + "/"  + chatWith.getUserId() + "/" + loggedUser.getUserId()  + "/" +Contracts.PRO_FRIEND_LIST_LATEST_MESSAGE, message);

        ref.updateChildren(updates, listener);
    }


}
