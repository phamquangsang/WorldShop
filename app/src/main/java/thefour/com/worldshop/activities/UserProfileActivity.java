package thefour.com.worldshop.activities;
//TODO||Have a button "chat" to launch ChatActivity
//TODO||Show image profile
//TODO||All the requests request by that user (inactive, offer accepted, completed, pending)
//TODO||All offers made by that user
//TODO||

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.tool.DataBindingBuilder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.adapters.RequestAdapter;
import thefour.com.worldshop.api.OfferApi;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.databinding.ActivityUserProfileBinding;
import thefour.com.worldshop.fragments.RequestFragment;
import thefour.com.worldshop.fragments.UserProfileOfferListFragment;
import thefour.com.worldshop.models.Offer;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class UserProfileActivity extends AppCompatActivity
        implements RequestFragment.OnListFragmentInteractionListener,
        UserProfileOfferListFragment.OnListFragmentInteractionListener {
    private static final String ARG_LOGGED_USER = "arg-logged-user";
    private static final String ARG_PROFILE_USER = "arg-profile-user";
    private static final int NUMBER_OF_REQUEST = 3;
    private ActivityUserProfileBinding mBinding;
    private User mLoggedUser;
    private User mUserProfile;
    private RequestFragment mRequestFragment;
    private UserProfileOfferListFragment mUserOfferFragment;
    private ValueEventListener mRequestListener;
    private ChildEventListener mUserOfferListener;
    private Query mLatestUserRequestQuery;


    public static Intent getIntent(Context c, User loggedUser, User profileUser){
        Intent i = new Intent(c, UserProfileActivity.class);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        i.putExtra(ARG_PROFILE_USER, profileUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);
        mUserProfile = getIntent().getParcelableExtra(ARG_PROFILE_USER);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);



        setSupportActionBar(mBinding.toolbar);

        mBinding.toolbarLayout.setTitle(mUserProfile.getName());

        mRequestFragment = RequestFragment.newInstance(1, mLoggedUser.getUserId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.latestUserRequestContainer, mRequestFragment)
                .commit();

        mUserOfferFragment = UserProfileOfferListFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.yourOffersContainer, mUserOfferFragment)
                .commit();


        loadUserOffer();
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mBinding.content.tvViewAllRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = UserViewAllRequestActivity.getIntent(UserProfileActivity.this, mUserProfile, mLoggedUser);
                startActivity(i);
            }
        });
    }


    @Override
    public void onListFragmentInteraction(Request item) {
        Intent i = RequestDetailActivity.getIntent(this, item.getRequestId(), mLoggedUser);
        startActivity(i);
    }

    @Override
    public void onUserMakeOffer(Request item) {

    }

    @Override
    public void onUserProfileClick(Request item) {

    }

    @Override
    public void onListFragmentCreated() {
        mRequestFragment.getRecyclerView().setNestedScrollingEnabled(false);
        loadLatestUserRequest();
    }

    @Override
    public void onRequestListLoaded(List<Request> list) {
        Toast.makeText(this, "onRequestLoaded: "+list.size(), Toast.LENGTH_LONG).show();
        if(list.size()<=NUMBER_OF_REQUEST){
            mBinding.content.tvViewAllRequest.setVisibility(View.GONE);
        }else{
            mBinding.content.tvViewAllRequest.setVisibility(View.VISIBLE);
        }
    }

    private void loadLatestUserRequest() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(Contracts.USER_REQUESTS_LOCATION).child(mUserProfile.getUserId());
        mLatestUserRequestQuery = ref.orderByChild(Contracts.PRO_REQUEST_ID);
        mLatestUserRequestQuery = mLatestUserRequestQuery.limitToLast(NUMBER_OF_REQUEST+1);


        mRequestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Request> listRequest = new ArrayList<>();
                for (DataSnapshot requestNS :
                        dataSnapshot.getChildren()) {
                    listRequest.add(requestNS.getValue(Request.class));
                }
                mRequestFragment.onDataChange(listRequest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mRequestFragment.onDatabaseError(databaseError);
            }
        };
        mLatestUserRequestQuery.addValueEventListener(mRequestListener);
    }

    private void loadUserOffer(){
        mUserOfferListener = OfferApi.loadUserOffers(mUserProfile.getUserId(), mUserOfferFragment);
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "User Profile Fragment Destroy", Toast.LENGTH_SHORT).show();
        if(mRequestListener!=null){
            mLatestUserRequestQuery
                    .removeEventListener(mRequestListener);
        }
        mRequestListener = null;
        if(mUserOfferListener!=null){
            FirebaseDatabase.getInstance().getReference()
                    .child(Contracts.USER_OFFERS_LOCATION)
                    .child(mLoggedUser.getUserId())
                    .removeEventListener(mUserOfferListener);
        }
        mUserOfferListener = null;
        mUserOfferFragment = null;
        mRequestFragment = null;
        super.onDestroy();
    }

    @Override
    public void onListFragmentInteraction(Offer item) {
        if(item.getRequestId()==null){
            Toast.makeText(this, "requestId is null", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(RequestDetailActivity.getIntent(this, item.getRequestId(), mLoggedUser));
    }
}
