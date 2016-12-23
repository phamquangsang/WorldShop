package thefour.com.worldshop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Quang Quang on 12/22/2016.
 */

public class Notification extends BaseObservable{
    public static final String ACTION_MAKE_OFFER = "action_make_offer";
    public static final String ACTION_ACCEPT_OFFER = "action_accept_offer";
    public static final String ACTION_DELETE_OFFER = "action_delete_offer";
    public static final String ACTION_UPDATE_OFFER = "action_update_offer";
    public static final String ACTION_CANCEL_OFFER = "action_cancel_offer";

    private User fromUser;
    private Request request;
    private Offer offer;
    private String action;
    private long time;

    public Notification() {
    }

    @Bindable
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
        notifyChange();
    }

    @Bindable
    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
        notifyChange();
    }

    @Bindable
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
        notifyChange();
    }

    @Bindable
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        notifyChange();
    }

    public String getMessage(){
        String message = "";
        message += "<b>"+getFromUser().getName()+"</b>";
        if(getAction().equals(Notification.ACTION_MAKE_OFFER)){
            message += " made a new offer to deliver your request ";
        }else if(getAction().equals(Notification.ACTION_UPDATE_OFFER)){
            message += " updated their offer to deliver your request ";
        }else if(getAction().equals(Notification.ACTION_DELETE_OFFER)){
            message += " deleted their offer to deliver your request ";
        } else if (getAction().equals(Notification.ACTION_CANCEL_OFFER)) {
            message += " canceled their offer to deliver your request ";
        } else if(getAction().equals(Notification.ACTION_ACCEPT_OFFER)){
            message += " accepted your offer to deliver their request ";
        }
        message += "<b>" +request.getItem().getName()+"</b>";
        return message;
    }

    @Bindable
    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
        notifyChange();
    }

    @BindingAdapter(value = {"bind:htmlMessage"})
    public static void bindingHtmlMessage(TextView textView, String message){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        }else{
            textView.setText(Html.fromHtml(message));
        }
    }

    @BindingAdapter(value = {"bind:imageUrl", "bind:placeholder"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url, Drawable placeHolder) {
        if (url == null) {
            imageView.setImageDrawable(placeHolder);
        } else {
            Glide.with(imageView.getContext()).load(url).placeholder(placeHolder).into(imageView);
        }
    }
}
