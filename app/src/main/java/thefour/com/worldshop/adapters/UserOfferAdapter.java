package thefour.com.worldshop.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.FragmentUserOfferItemBinding;
import thefour.com.worldshop.fragments.UserProfileOfferListFragment.OnListFragmentInteractionListener;
import thefour.com.worldshop.models.Offer;


public class UserOfferAdapter extends RecyclerView.Adapter<UserOfferAdapter.ViewHolder> {

    private final List<Offer> mValues;
    private final OnListFragmentInteractionListener mListener;

    public UserOfferAdapter(List<Offer> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_offer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBinding.setOffer(holder.mItem);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Offer mItem;
        FragmentUserOfferItemBinding mBinding;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = DataBindingUtil.bind(view);
        }
    }

    public void addOffer(Offer offer){
        mValues.add(offer);
        notifyDataSetChanged();
    }

    public void removeOffer(Offer offer){
        for (int i=0; i< mValues.size();++i){
            Offer item = mValues.get(i);
            if(offer.getOfferId().equals(item.getOfferId())){
                mValues.remove(item);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void updateOffer(Offer offer){
        for (int i=0; i< mValues.size();++i){
            Offer item = mValues.get(i);
            if(offer.getOfferId().equals(item.getOfferId())){
                mValues.set(mValues.indexOf(item),offer);
                notifyDataSetChanged();
            }
        }
    }
}
