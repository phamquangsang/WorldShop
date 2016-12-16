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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.adapters.RequestAdapter;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.databinding.ActivityUserProfileBinding;
import thefour.com.worldshop.fragments.RequestFragment;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class UserProfileActivity extends AppCompatActivity implements RequestFragment.OnListFragmentInteractionListener {
    private static final String ARG_LOGGED_USER = "arg-logged-user";
    private static final String ARG_PROFILE_USER = "arg-profile-user";
    private ActivityUserProfileBinding mBinding;
    private User mLoggedUser;
    private User mUserProfile;
    private RequestFragment mRequestFragment;
    private ChildEventListener mRequestListener;


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
        getSupportActionBar().setTitle(mUserProfile.getName());

        mRequestFragment = RequestFragment.newInstance(1, mLoggedUser.getUserId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.latestUserRequestContainer, mRequestFragment)
                .commit();

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
        Intent i = RequestDetailActivity.getIntent(this, item, mLoggedUser);
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

    private void loadLatestUserRequest() {
        mRequestListener = RequestApi.loadLatestUserRequest(mUserProfile, 5, mRequestFragment);
    }

    @Override
    protected void onDestroy() {
        if(mRequestListener!=null){
            FirebaseDatabase.getInstance().getReference()
                    .child(Contracts.USER_REQUESTS_LOCATION)
                    .child(mLoggedUser.getUserId())
                    .removeEventListener(mRequestListener);
        }
        super.onDestroy();
    }

}
