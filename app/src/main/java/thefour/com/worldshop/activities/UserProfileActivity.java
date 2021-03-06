package thefour.com.worldshop.activities;
//TODO||Have a button "chat" to launch ChatActivity
//TODO||Show image profile
//TODO||All the requests request by that user (inactive, offer accepted, completed, pending)
//TODO||All offers made by that user
//TODO||

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
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
import thefour.com.worldshop.TypefaceCache;
import thefour.com.worldshop.api.OfferApi;
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


    public static Intent getIntent(Context c, User loggedUser, User profileUser) {
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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition a = null;
            a = TransitionInflater.from(this).inflateTransition(R.transition.app_bar_transition);
            getWindow().setEnterTransition(a);

            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
//                    Animator anim = ObjectAnimator.ofFloat(mBinding.content.scrollView, "alpha", 0f, 1f);
                    Animator anim = ObjectAnimator.ofFloat(mBinding.content.scrollView, "translationY", 500, 0);
                    anim.setInterpolator(new DecelerateInterpolator());
                    anim.setDuration(300);
                    mBinding.content.scrollView.setVisibility(View.VISIBLE);
                    anim.start();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }else{
            Animator anim = ObjectAnimator.ofFloat(mBinding.content.scrollView, "translationY", 500, 0);
            anim.setInterpolator(new AccelerateInterpolator());
            anim.setStartDelay(300);
            anim.setDuration(300);
            mBinding.content.scrollView.setVisibility(View.VISIBLE);
            anim.start();
        }
//        if(mBinding.content.scrollView.getVisibility()!=View.VISIBLE){
//            Animator anim = ObjectAnimator.ofFloat(mBinding.content.scrollView, "translationY", 500, 0);
//            anim.setInterpolator(new DecelerateInterpolator());
//            anim.setStartDelay(300);
//            anim.setDuration(300);
//            mBinding.content.scrollView.setVisibility(View.VISIBLE);
//            anim.start();
//        }



        setSupportActionBar(mBinding.toolbar);

        mBinding.toolbarLayout.setTitle(mUserProfile.getName());
        mBinding.toolbarLayout.setCollapsedTitleTypeface(TypefaceCache.get(this, TypefaceCache.HARMONIA_BOLD));
        mBinding.toolbarLayout.setExpandedTitleTypeface(TypefaceCache.get(this, TypefaceCache.HARMONIA_BOLD));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(mUserProfile.getProfileImageUrl())
                .into(mBinding.userProfileImage);

        mRequestFragment = RequestFragment.newInstance(1, mLoggedUser.getUserId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.latestUserRequestContainer, mRequestFragment)
                .commit();

        mUserOfferFragment = UserProfileOfferListFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.yourOffersContainer, mUserOfferFragment)
                .commit();


        loadUserOffer();

        if (mLoggedUser.getUserId().equals(mUserProfile.getUserId())) {
            mBinding.fab.setVisibility(View.GONE);
        }
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = ChatActivity.getIntent(UserProfileActivity.this, mLoggedUser, mUserProfile);
                startActivity(i);
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
    public void onListFragmentInteraction(Request item, View selectedView) {
        Intent i = RequestDetailActivity.getIntent(this, item.getRequestId(), mLoggedUser);
        Bundle options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, selectedView, getString(R.string.item_image_transition_name))
                .toBundle();
        startActivity(i, options);
    }

    @Override
    public void onUserMakeOffer(Request item) {

    }

    @Override
    public void onUserProfileClick(Request item, View view, View view2) {

    }

    @Override
    public void onListFragmentCreated() {
        mRequestFragment.getRecyclerView().setNestedScrollingEnabled(false);
        loadLatestUserRequest();
    }

    @Override
    public void onRequestListLoaded(List<Request> list) {
        if (list.size() <= NUMBER_OF_REQUEST) {
            mBinding.content.tvViewAllRequest.setVisibility(View.GONE);
        } else {
            mBinding.content.tvViewAllRequest.setVisibility(View.VISIBLE);
        }
    }

    private void loadLatestUserRequest() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(Contracts.USER_REQUESTS_LOCATION).child(mUserProfile.getUserId());
        mLatestUserRequestQuery = ref.orderByChild(Contracts.PRO_REQUEST_ID);
        mLatestUserRequestQuery = mLatestUserRequestQuery.limitToLast(NUMBER_OF_REQUEST + 1);


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

    private void loadUserOffer() {
        mUserOfferListener = OfferApi.loadUserOffers(mUserProfile.getUserId(), mUserOfferFragment);
    }

    @Override
    protected void onDestroy() {
        if (mRequestListener != null) {
            mLatestUserRequestQuery
                    .removeEventListener(mRequestListener);
        }
        mRequestListener = null;
        if (mUserOfferListener != null) {
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
        if (item.getRequestId() == null) {
            Toast.makeText(this, "requestId is null", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(RequestDetailActivity.getIntent(this, item.getRequestId(), mLoggedUser));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//        Animator anim = ObjectAnimator.ofFloat(mBinding.content.scrollView, "alpha", 1f, 0f);
//        anim.setInterpolator(new DecelerateInterpolator());
//        anim.setDuration(500);
//        mBinding.content.scrollView.setVisibility(View.INVISIBLE);
//        anim.start();
//        super.onBackPressed();
//    }
}
