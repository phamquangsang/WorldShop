package thefour.com.worldshop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Quang Quang on 12/21/2016.
 */

public class Friend extends BaseObservable {
    private User user;
    private Message latestMessage;

    public Friend() {
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyChange();
    }

    @Bindable
    public Message getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(Message latestMessage) {
        this.latestMessage = latestMessage;
        notifyChange();
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
