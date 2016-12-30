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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.adapters.RequestAdapter;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RequestFragment extends Fragment
        implements  RequestApi.RequestValueEventListener, RequestApi.RequestChildEventListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_USER_ID = "arg_logged_user_id";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    protected RequestAdapter mAdapter;
    private String TAG = RequestFragment.class.getSimpleName();
    private User mLoggedUser;
    private String mLoggedUserId;
    private ValueEventListener mUserListener;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RequestFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RequestFragment newInstance(int columnCount, String userid) {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_USER_ID, userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mLoggedUser = new User();
            mLoggedUserId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

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
        loadLoggedUser();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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

    @Override
    public void onDestroy() {
        if(mUserListener!=null){
            FirebaseDatabase.getInstance().getReference()
                    .child(Contracts.USERS_LOCATION).child(mLoggedUserId)
                    .removeEventListener(mUserListener);
        }
        super.onDestroy();
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
        void onListFragmentInteraction(Request item);
        void onUserMakeOffer(Request item);
        void onUserProfileClick(Request item);
        void onListFragmentCreated();
        void onRequestListLoaded(List<Request> list);
    }

    public void clearData(){
        mAdapter.getValues().clear();
        mAdapter.notifyDataSetChanged();
    }

    private void loadLoggedUser(){
        mUserListener = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    mLoggedUser = new User();
                    mLoggedUser.setUser(user);
                    mAdapter = new RequestAdapter(getActivity(),new ArrayList<Request>(), mLoggedUser, mListener);
                    mRecyclerView.setAdapter(mAdapter);
                    mListener.onListFragmentCreated();
                }
                else {
                    throw new IllegalStateException("userId not found exception");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: load user error", databaseError.toException());
            }
        };
        FirebaseDatabase.getInstance().getReference()
                .child(Contracts.USERS_LOCATION).child(mLoggedUserId)
                .addValueEventListener(mUserListener);
    }

    public RequestAdapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    @Override
    public void onDataChange(List<Request> requestList) {
        mAdapter.getValues().clear();
        mAdapter.getValues().addAll(requestList);
        mAdapter.notifyDataSetChanged();
        mListener.onRequestListLoaded(requestList);
    }

    @Override
    public void onDatabaseError(DatabaseError error) {
        Log.e(TAG, "onDatabaseError: ", error.toException());
    }

    @Override
    public void onRequestAdded(Request request, String previousCityId) {
        mAdapter.addRequest(request);
    }

    @Override
    public void onRequestChanged(Request newRequest, String previousCityId) {
        mAdapter.updateRequest(newRequest);
    }

    @Override
    public void onRequestRemoved(Request request) {
        mAdapter.removeRequest(request);
    }

    @Override
    public void onRequestMoved(Request request, String s) {

    }

    @Override
    public void onError(DatabaseError error) {
        Log.e(TAG, "onError: ", error.toException());
    }
}
