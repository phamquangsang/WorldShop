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

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import thefour.com.worldshop.R;
import thefour.com.worldshop.api.OfferApi;
import thefour.com.worldshop.models.Offer;


public class UserProfileOfferListFragment extends Fragment implements OfferApi.OfferEventListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = UserProfileOfferListFragment.class.getSimpleName();
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;
    private UserOfferAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserProfileOfferListFragment() {
    }

    // TODO: Customize parameter initialization
    public static UserProfileOfferListFragment newInstance(int columnCount) {
        UserProfileOfferListFragment fragment = new UserProfileOfferListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_offer_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new UserOfferAdapter(new ArrayList<Offer>(), mListener);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onOfferAdded(Offer offer, String previousCityId) {
        mAdapter.addOffer(offer);
    }

    @Override
    public void onOfferChanged(Offer newOffer, String previousCityId) {
        mAdapter.updateOffer(newOffer);
    }

    @Override
    public void onOfferRemoved(Offer offer) {
        mAdapter.removeOffer(offer);
    }

    @Override
    public void onOfferMoved(Offer offer, String s) {
        Log.i(TAG, "onOfferMoved: onOffer moved " + offer.getOfferId());
    }

    @Override
    public void onError(DatabaseError error) {
        Log.e(TAG, "onError: ", error.toException());
    }

    public UserOfferAdapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Offer item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
