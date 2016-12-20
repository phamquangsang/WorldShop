package thefour.com.worldshop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Quang Quang on 12/20/2016.
 */

public class Message extends BaseObservable {
    private String messageId;
    private User fromUser;
    private String content;
    private long time;

    public Message(){

    }

    @Bindable
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
        notifyChange();
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
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @BindingAdapter(value = {"bind:imageUrl", "bind:placeholder"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url, Drawable placeHolder) {
        if (url == null) {
            imageView.setImageDrawable(placeHolder);
        } else {
            Glide.with(imageView.getContext()).load(url).placeholder(placeHolder).into(imageView);
        }

    }
}
