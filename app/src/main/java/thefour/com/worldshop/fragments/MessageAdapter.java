package thefour.com.worldshop.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.FragmentMessageLeftBinding;
import thefour.com.worldshop.databinding.FragmentMessageRightBinding;
import thefour.com.worldshop.fragments.MessageFragment.OnListMessageFragmentInteractionListener;
import thefour.com.worldshop.fragments.dummy.DummyContent.DummyItem;
import thefour.com.worldshop.models.Message;
import thefour.com.worldshop.models.User;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link MessageFragment.OnListMessageFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static int TYPE_LEFT = 1;
    public static int TYPE_RIGHT = 2;
    private User mLoggedUser;
    private final List<Message> mValues;
    private final MessageFragment.OnListMessageFragmentInteractionListener mListener;

    public MessageAdapter(List<Message> items, User loggedUser, OnListMessageFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mLoggedUser = loggedUser;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mValues.get(position);
        if(mLoggedUser.getUserId().equals(message.getFromUser().getUserId())){
            return TYPE_RIGHT;
        }else{
            return TYPE_LEFT;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_RIGHT){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_message_right, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_message_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int viewType = getItemViewType(position);
        if(viewType == TYPE_RIGHT){
            FragmentMessageRightBinding binding = (FragmentMessageRightBinding)holder.mBinding;
            binding.setMessage(holder.mItem);
        }else{
            FragmentMessageLeftBinding binding = (FragmentMessageLeftBinding) holder.mBinding;
            binding.setMessage(holder.mItem);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
//        return 50;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Message mItem;
        public ViewDataBinding mBinding;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = DataBindingUtil.bind(view);
        }

    }

    public void addMessage(Message message){
        mValues.add(message);
        notifyDataSetChanged();
    }

    public void removeMessage(Message message){
        for (Message item :
                mValues) {
            if(message.getMessageId().equals(item.getMessageId())){
                mValues.remove(item);
                notifyDataSetChanged();
            }
        }
    }

    public void updateMessage(Message message){
        for (Message item :
                mValues) {
            if(message.getMessageId().equals(item.getMessageId())){
                mValues.set(mValues.indexOf(item),message);
                notifyDataSetChanged();
            }
        }
    }
}
