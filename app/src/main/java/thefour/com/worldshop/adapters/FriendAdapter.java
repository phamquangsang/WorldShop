package thefour.com.worldshop.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.FragmentFriendItemBinding;
import thefour.com.worldshop.fragments.FriendsListFragment.OnListFragmentInteractionListener;
import thefour.com.worldshop.models.Friend;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private final List<Friend> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FriendAdapter(List<Friend> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mBinding.setFriend(holder.mItem);
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
    }

    public void addValues(List<Friend> list) {
        mValues.addAll(list);
        notifyDataSetChanged();
    }

    public void removeValues() {
        mValues.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentFriendItemBinding mBinding;
        public Friend mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = DataBindingUtil.bind(view);
        }
    }
}
