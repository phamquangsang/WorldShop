package thefour.com.worldshop.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.adapters.OfferAdapter;
import thefour.com.worldshop.api.OfferApi;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListOfferInteractListener}
 * interface.
 */
public class OfferListFragment extends Fragment implements OfferApi.OfferEventListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_COLUMN_LOGGED_USER = "column-logged-user";
    private static final String ARG_COLUMN_REQUEST = "column-request";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private User mLoggedUser;
    private OnListOfferInteractListener mListener;
    private OfferAdapter mAdapter;
    private String TAG = OfferListFragment.class.getSimpleName();
    private Request mRequest;
    private ValueEventListener mRequestListener;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OfferListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OfferListFragment newInstance(int columnCount, User loggedUser, String requestId) {
        OfferListFragment fragment = new OfferListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelable(ARG_COLUMN_LOGGED_USER, loggedUser);
        args.putString(ARG_COLUMN_REQUEST, requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mLoggedUser = getArguments().getParcelable(ARG_COLUMN_LOGGED_USER);
            String requestId = getArguments().getString(ARG_COLUMN_REQUEST);
            setUpRequestListener(requestId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setUpRequestListener(String requestId) {
        mRequestListener = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Request request = dataSnapshot.getValue(Request.class);
                mRequest = new Request();
                mRequest.setRequest(request);
                mAdapter = new OfferAdapter(mLoggedUser, mRequest, mListener);
                mRecyclerView.setAdapter(mAdapter);
                mListener.onFragmentCreated();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Contracts.REQUESTS_LOCATION)
                .child(requestId);
        ref.addValueEventListener(mRequestListener);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListOfferInteractListener) {
            mListener = (OnListOfferInteractListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListOfferInteractListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListOfferInteractListener {
        // TODO: Update argument type and name
        void onDatasetChange(List<Offer> list);
        void onItemClick(Offer item);
        void onAcceptClick(Offer item);
        void onUpdateClick(Offer item);
        void onDeleteClick(Offer item);
        void onCancelOffer(Offer item);
        void onFragmentCreated();
        void onCompleteOffer(Offer item);
        void onUserProfileClick(Offer item);
    }

    @Override
    public void onOfferAdded(Offer offer, String previousCityId) {
        if(mAdapter==null){
            throw new IllegalStateException("OfferListFragment is not ready, only call onOfferAdded() after onFragmentCreated() is called");
        }
        mAdapter.addOffer(offer);
    }

    @Override
    public void onOfferChanged(Offer newOffer, String previousCityId) {
        if(mAdapter==null){
            throw new IllegalStateException("OfferListFragment is not ready, only call onOfferAdded() after onFragmentCreated() is called");
        }
        mAdapter.updateOffer(newOffer);
    }

    @Override
    public void onOfferRemoved(Offer offer) {
        if(mAdapter==null){
            throw new IllegalStateException("OfferListFragment is not ready, only call onOfferAdded() after onFragmentCreated() is called");
        }
        mAdapter.removeOffer(offer);
    }

    @Override
    public void onOfferMoved(Offer offer, String s) {
        Log.i(TAG, "onOfferMoved: onOffer moved "+offer.getOfferId());
    }

    @Override
    public void onError(DatabaseError error) {
        Log.e(TAG, "onError: ", error.toException());
    }

    public OfferAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onDestroy() {
        if(mRequestListener!=null && mRequest != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child(Contracts.REQUESTS_LOCATION)
                    .child(mRequest.getRequestId());
            ref.removeEventListener(mRequestListener);
        }
        super.onDestroy();
    }
}
