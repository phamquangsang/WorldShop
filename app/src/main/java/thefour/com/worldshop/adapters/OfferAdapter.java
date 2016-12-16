package thefour.com.worldshop.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.FragmentOfferItemBinding;
import thefour.com.worldshop.fragments.OfferListFragment.OnListOfferInteractListener;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private final List<Offer> mOffers;
    private final OnListOfferInteractListener mListener;
    private final Request mRequest;
    private User mLoggedUser;

    private final int TYPE_PENDING = 0;
    private final int TYPE_ACCEPTED = 1;

    public OfferAdapter(User loggedUser, Request request, OnListOfferInteractListener listener) {
        mOffers = new ArrayList<Offer>();
        mListener = listener;
        mLoggedUser = loggedUser;
        mRequest = request;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_offer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mOffer = mOffers.get(position);
        holder.mBinding.setOffer(holder.mOffer);
        holder.mBinding.setRequest(mRequest);
        holder.mBinding.setLoggedUser(mLoggedUser);
    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public FragmentOfferItemBinding mBinding;
        public Offer mOffer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = DataBindingUtil.bind(view);
            mBinding.btnAccept.setOnClickListener(this);
            mBinding.btnCancelOffer.setOnClickListener(this);
            mBinding.btnUpdateOffer.setOnClickListener(this);
            mBinding.btnDeleteOffer.setOnClickListener(this);
            mBinding.btnCompleteOffer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Offer offer = mOffers.get(getAdapterPosition());
            switch (v.getId()){
                case R.id.btnAccept:
                    mListener.onAcceptClick(offer);
                    break;
                case R.id.btnCancelOffer:
                    mListener.onCancelOffer(offer);
                    break;
                case R.id.btnUpdateOffer:
                    mListener.onUpdateClick(offer);
                    break;
                case R.id.btnDeleteOffer:
                    mListener.onDeleteClick(offer);
                    break;
                case R.id.btnCompleteOffer:
                    mListener.onCompleteOffer(offer);
                    break;
            }
        }
    }

    public void addOffer(Offer offer){
        mOffers.add(offer);
        notifyDataSetChanged();
    }

    public void removeOffer(Offer offer){
        for (int i=0; i< mOffers.size();++i){
            Offer item = mOffers.get(i);
            if(offer.getOfferId().equals(item.getOfferId())){
                mOffers.remove(item);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void updateOffer(Offer offer){
        for (int i=0; i< mOffers.size();++i){
            Offer item = mOffers.get(i);
            if(offer.getOfferId().equals(item.getOfferId())){
                mOffers.set(mOffers.indexOf(item),offer);
                notifyDataSetChanged();
            }
        }
    }

}
