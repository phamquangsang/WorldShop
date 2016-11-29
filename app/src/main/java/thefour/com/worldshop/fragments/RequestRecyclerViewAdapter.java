package thefour.com.worldshop.fragments;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.FragmentRequestItemBinding;
import thefour.com.worldshop.models.Request;

import java.util.List;


public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.ViewHolder> {

    private final List<Request> mValues;
    private final RequestFragment.OnListFragmentInteractionListener mListener;

    public RequestRecyclerViewAdapter(List<Request> items, RequestFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        Glide.with(holder.mView.getContext())
                .load(holder.mItem.getItem().getFirstImage())
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .into(holder.mBinding.requestItemImageView);
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

    public List<Request> getValues() {
        return mValues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
