package thefour.com.worldshop.adapters;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;

import thefour.com.worldshop.R;
import thefour.com.worldshop.TypefaceCache;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.databinding.FragmentRequestItemBinding;
import thefour.com.worldshop.fragments.RequestFragment;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

import java.util.List;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private final Activity mActivity;
    private final List<Request> mValues;
    private final RequestFragment.OnListFragmentInteractionListener mListener;
    private final User mLoggedUser;

    public RequestAdapter(Activity activity, List<Request> items, User loggedUser, RequestFragment.OnListFragmentInteractionListener listener) {
        mActivity = activity;
        mValues = items;
        mListener = listener;
        mLoggedUser = loggedUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBinding.setRequest(holder.mItem);
        holder.mBinding.setLoggedUser(mLoggedUser);
        holder.mBinding.header.textViewTime.setText(Util.relativeTimeFormat(holder.mItem.getTime()));


        String deliverTo = "Deliver to "+holder.mItem.getDeliverTo().getName();
        holder.mBinding.body.requestItemDeliverTo.setText(deliverTo);
        String price = mActivity.getString(R.string.item_price_format)+holder.mItem.getItem().getPrice();
        holder.mBinding.body.itemRequestPrice.setText(price);
        String reward = mActivity.getString(R.string.item_reward_format) + holder.mItem.getReward();
        holder.mBinding.body.reward.setText(reward);
        holder.mBinding.body.itemRequestPrice.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.HARMONIA_BOLD));
        holder.mBinding.body.requestItemItemName.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.HARMONIA_BOLD));
        holder.mBinding.body.requestItemDescription.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.HARMONIA_REGULAR));
        holder.mBinding.body.requestItemItemName.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.HARMONIA_REGULAR));
        holder.mBinding.body.requestItemDeliverTo.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.HARMONIA_REGULAR));
        holder.mBinding.body.reward.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.HARMONIA_REGULAR));
        holder.mBinding.header.textViewUserName.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.HARMONIA_BOLD));
        holder.mBinding.header.textViewTime.setTypeface(TypefaceCache.get(mActivity,TypefaceCache.HARMONIA_REGULAR));



        setOnClickListener(holder);
    }

    private void setOnClickListener(final ViewHolder holder) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        };
        holder.mView.setOnClickListener(clickListener);
        holder.mBinding.header.btnMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserMakeOffer(holder.mItem);
            }
        });

        holder.mBinding.header.imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserProfileClick(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Request> getValues() {
        return mValues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final View mView;
        public Request mItem;
        public FragmentRequestItemBinding mBinding;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = DataBindingUtil.bind(view);
        }

    }

    public void addRequest(Request request){
        mValues.add(request);
        notifyDataSetChanged();
    }

    public void removeRequest(Request request){
        for (Request item :
                mValues) {
            if(request.getRequestId().equals(item.getRequestId())){
                mValues.remove(item);
                notifyDataSetChanged();
            }
        }
    }

    public void updateRequest(Request request){
        for (Request item :
                mValues) {
            if(request.getRequestId().equals(item.getRequestId())){
                mValues.set(mValues.indexOf(item),request);
                notifyDataSetChanged();
            }
        }
    }
}
