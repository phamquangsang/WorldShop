package thefour.com.worldshop.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.FragmentRequestItemBinding;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

import java.util.List;


public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.ViewHolder> {

    private final List<Request> mValues;
    private final RequestFragment.OnListFragmentInteractionListener mListener;
    private final User mLoggedUser;

    public RequestRecyclerViewAdapter(List<Request> items, User loggedUser, RequestFragment.OnListFragmentInteractionListener listener) {
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
        Context c = holder.mView.getContext();
        holder.mItem = mValues.get(position);
        holder.mBinding.setRequest(holder.mItem);
        Glide.with(c)
                .load(holder.mItem.getItem().getFirstImage())
                .placeholder(R.drawable.cicor_progessbar)
                .into(holder.mBinding.requestItemImageView);
        Glide.with(c)
                .load(holder.mItem.getFromUser().getProfileImageUrl())
                .placeholder(R.drawable.cicor_progessbar)
                .into(holder.mBinding.header.imageViewProfile);

        String deliverTo = "Deliver to "+holder.mItem.getDeliverTo().getName();
        holder.mBinding.requestItemDeliverTo.setText(deliverTo);
        String price = c.getString(R.string.item_price_format)+holder.mItem.getItem().getPrice();
        holder.mBinding.footer.itemRequestPrice.setText(price);
        holder.mBinding.footer.reward.setText(("$"+ holder.mItem.getReward()));

        //user don't make offer for their own request.
        if(mLoggedUser.getUserId().equalsIgnoreCase(holder.mItem.getFromUser().getUserId())){
            holder.mBinding.footer.btnMakeOffer.setVisibility(View.INVISIBLE);
        }else {
            holder.mBinding.footer.btnMakeOffer.setVisibility(View.VISIBLE);
        }

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
        holder.mBinding.footer.btnMakeOffer.setOnClickListener(new View.OnClickListener() {
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
            if(request.getRequestId().equalsIgnoreCase(item.getRequestId())){
                mValues.remove(item);
                notifyDataSetChanged();
            }
        }
    }

    public void updateRequest(Request request){
        for (Request item :
                mValues) {
            if(request.getRequestId().equalsIgnoreCase(item.getRequestId())){
                mValues.set(mValues.indexOf(item),request);
                notifyDataSetChanged();
            }
        }
    }
}
