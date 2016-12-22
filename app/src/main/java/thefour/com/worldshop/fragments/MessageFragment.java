package thefour.com.worldshop.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;

import thefour.com.worldshop.R;
import thefour.com.worldshop.adapters.MessageAdapter;
import thefour.com.worldshop.databinding.FragmentMessageListBinding;
import thefour.com.worldshop.models.Message;
import thefour.com.worldshop.models.User;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListMessageFragmentInteractionListener}
 * interface.
 */
public class MessageFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LOGGED_USER = "logged-user";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListMessageFragmentInteractionListener mListener;
    private User mLoggedUser;
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessageFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MessageFragment newInstance(int columnCount, User loggedUser) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelable(ARG_LOGGED_USER, loggedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mLoggedUser = getArguments().getParcelable(ARG_LOGGED_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        FragmentMessageListBinding binding = DataBindingUtil.bind(view);
        // Set the adapter
        Context context = view.getContext();
        mRecyclerView = binding.list;
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mAdapter = new MessageAdapter(new ArrayList<Message>(),mLoggedUser, mListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(false);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListMessageFragmentInteractionListener) {
            mListener = (OnListMessageFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListMessageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnListMessageFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Message item);
    }

    public MessageAdapter getAdapter(){

        return mAdapter;
    }
    public void addMessage(Message message){
        mAdapter.addMessage(message);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
    }
    public void updateMessage(Message message){
        mAdapter.updateMessage(message);
    }

    public void removeMessage(Message message){
        mAdapter.removeMessage(message);
    }
}
