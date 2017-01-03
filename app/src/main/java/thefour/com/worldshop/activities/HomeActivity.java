package thefour.com.worldshop.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.MetricUtil;
import thefour.com.worldshop.R;
import thefour.com.worldshop.TypefaceCache;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.api.UserApi;
import thefour.com.worldshop.databinding.ActivityHomeBinding;
import thefour.com.worldshop.fragments.RequestFragment;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class HomeActivity extends AppCompatActivity
        implements RequestFragment.OnListFragmentInteractionListener,
                    View.OnClickListener{

    private static final String ARG_USER_ID = "arg_user_id";
    private static final String TAG = HomeActivity.class.getSimpleName();

    private RequestFragment mRequestFragment;
    private ChildEventListener mChildEventListener;
    private ActivityHomeBinding mBinding;
    private String mUserId;
    private User mLoggedUser;
    private Query mRequestQuery;

    public static Intent getIntent(Context c, String userId) {
        Intent i = new Intent(c, HomeActivity.class);
        i.putExtra(ARG_USER_ID, userId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setSupportActionBar(mBinding.layoutToolbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBinding.layoutToolbar.textViewTitle.setTypeface(TypefaceCache.get(this,TypefaceCache.TITLE_FONT));
        mBinding.layoutToolbar.textViewTitle.setText(R.string.title_activity_home);
        setUpTwoMainButtonView();
//      todo Animation not work yet
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Transition a = null;
//            a = TransitionInflater.from(this).inflateTransition(R.transition.home_transition);
//            getWindow().setEnterTransition(a);
//
//
//        }

//        Load user from sharedPreference faster than firebase
        mLoggedUser = Util.loadLoggedUser(this);

        mUserId = getIntent().getStringExtra(ARG_USER_ID);
        if (mUserId != null) {
            loadLoggedUser();
        }else{
            throw new IllegalStateException("userID == null");
        }
        mRequestFragment = RequestFragment.newInstance(1,mUserId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mRequestFragment)
                .commit();
        mBinding.btnShopping.setOnClickListener(this);
        mBinding.btnTravelling.setOnClickListener(this);
    }

    private void setUpTwoMainButtonView() {
        //this method set up button traveling and shopping buttons to have the same size and fill up device width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int sizeX = metrics.widthPixels/2;
        LinearLayout.LayoutParams btnSizeShoppingParams = new LinearLayout.LayoutParams(sizeX, sizeX);
        int twoDp = (int) MetricUtil.convertDpToPixel(2f, this);
        btnSizeShoppingParams.setMargins(0, 0, twoDp, 0);
        mBinding.btnShopping.setLayoutParams(btnSizeShoppingParams);

        LinearLayout.LayoutParams btnSizeTravelingParams = new LinearLayout.LayoutParams(sizeX, sizeX);
        btnSizeTravelingParams.setMargins(twoDp, 0, 0, 0);
        mBinding.btnTravelling.setLayoutParams(btnSizeTravelingParams);
    }

    private void loadRequest() {
        mChildEventListener = loadLatestRequest(mRequestFragment, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public ChildEventListener loadLatestRequest(final RequestApi.RequestChildEventListener eventListener
            , final DatabaseReference.CompletionListener completionListener){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(Contracts.REQUESTS_LOCATION);
        mRequestQuery = ref.orderByChild(Contracts.PRO_REQUEST_STATUS).equalTo(Request.STATUS_PENDING);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded: "+dataSnapshot.toString());
                eventListener.onRequestAdded(dataSnapshot.getValue(Request.class),s);
                completionListener.onComplete(null, null);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                eventListener.onRequestChanged(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                eventListener.onRequestRemoved(dataSnapshot.getValue(Request.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                eventListener.onRequestMoved(dataSnapshot.getValue(Request.class),s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventListener.onError(databaseError);
            }

        };

        mRequestQuery.addChildEventListener(childEventListener);
        return childEventListener;
    }
    private void loadLoggedUser(){
        UserApi.retrieveUserById(mUserId, new UserApi.IsUserExistCallback() {
            @Override
            public void onUserExist(@Nullable User user) {
                if (user != null){
                    Util.saveLoggedUser(HomeActivity.this ,user);
                    mLoggedUser = user;
                }
                else {
                    Toast.makeText(HomeActivity.this, "Please Login again!", Toast.LENGTH_SHORT).show();
                    startActivity(LoginActivity.getIntent(HomeActivity.this));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_shop: {
//                if (mLoggedUser != null)
//                    startActivity(ShoppingActivity.getIntent(this, mLoggedUser));
//                break;
//            }
//            case R.id.action_travel: {
//                if(mLoggedUser != null){
//                    City travelTo = Util.loadSelectedCity(this);
//                    if(travelTo == null){
//                        startActivity(SelectTravelingCityActivity.getIntent(this, mLoggedUser));
//                    }else
//                        startActivity(TravellingActivity.getIntent(this, mLoggedUser, travelTo));
//                }
//                break;
//            }
            case R.id.action_conversation:{
                Intent i = ConversationActivity.getIntent(this, mLoggedUser);
                startActivity(i);
                break;
            }
            case R.id.action_notification:{
                Intent i = NotificationActivity.getIntent(this, mLoggedUser);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_shopping: {
                if (mLoggedUser != null)
                    startActivity(ShoppingActivity.getIntent(this, mLoggedUser));
                break;
            }
            case R.id.btn_travelling: {
                if (mLoggedUser != null) {
                    City travelTo = Util.loadSelectedCity(this);
                    if (travelTo == null) {
                        startActivity(SelectTravelingCityActivity.getIntent(this, mLoggedUser));
                    } else
                        startActivity(TravellingActivity.getIntent(this, mLoggedUser, travelTo));
                }
                break;
            }
        }
    }

    @Override
    public void onListFragmentInteraction(Request item, View selectedView) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, selectedView, getString(R.string.item_image_transition_name));
        Intent i = RequestDetailActivity.getIntent(this, item.getRequestId(), mLoggedUser);
        startActivity(i, options.toBundle());

    }

    @Override
    public void onUserMakeOffer(Request item) {
        Intent i = MakeOfferActivity.getIntent(this, mLoggedUser, item, null);
        startActivity(i);
    }

    @Override
    public void onUserProfileClick(Request item, View userProfileImageView, View userNameTv) {
//        Pair<View, String> p1 = Pair.create(userProfileImageView, getString(R.string.profile_image));
//        Pair<View, String> p2 = Pair.create(userNameTv, getString(R.string.profile_name));
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this);

        Intent i = UserProfileActivity.getIntent(this, mLoggedUser, item.getFromUser());
        startActivity(i, options.toBundle());
    }

    @Override
    public void onListFragmentCreated() {
        loadRequest();
        //recyclerView inside NestedScrollView
        mRequestFragment.getRecyclerView().setNestedScrollingEnabled(false);
    }

    @Override
    public void onRequestListLoaded(List<Request> list) {
        //do nothing
    }

    @Override
    protected void onDestroy() {
        if(mChildEventListener!=null){
            mRequestQuery.removeEventListener(mChildEventListener);
        }
        super.onDestroy();
    }
}
