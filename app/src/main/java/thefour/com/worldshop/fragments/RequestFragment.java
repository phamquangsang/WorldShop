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
import thefour.com.worldshop.adapters.RequestRecyclerViewAdapter;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RequestFragment extends Fragment implements RequestApi.RequestEventListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LOGGED_USER = "arg_logged_uesr";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RequestRecyclerViewAdapter mAdapter;
    private String TAG = RequestFragment.class.getSimpleName();
    private User mLoggedUser;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RequestFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RequestFragment newInstance(int columnCount, User loggedUser) {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelable(ARG_LOGGED_USER, loggedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mLoggedUser = getArguments().getParcelable(ARG_LOGGED_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new RequestRecyclerViewAdapter(getActivity(),new ArrayList<Request>(), mLoggedUser, mListener);
            recyclerView.setAdapter(mAdapter);
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
    }

    public void clearData(){
        mAdapter.getValues().clear();
        mAdapter.notifyDataSetChanged();
    }
}
