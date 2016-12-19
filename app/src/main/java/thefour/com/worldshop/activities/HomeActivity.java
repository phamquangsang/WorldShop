package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import thefour.com.worldshop.Contracts;
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

public class HomeActivity extends AppCompatActivity
        implements RequestFragment.OnListFragmentInteractionListener{

    private static final String ARG_USER_ID = "arg_user_id";
    private static final String TAG = HomeActivity.class.getSimpleName();

    private RequestFragment mRequestFragment;
    private ChildEventListener mChildEventListener;
    private ActivityHomeBinding mBinding;
    private String mUserId;
    private User mLoggedUser;

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
    }

    private void loadRequest() {
        mChildEventListener = RequestApi.loadLatestRequestInCity(mRequestFragment, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });
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
            case R.id.action_shop: {
                if (mLoggedUser != null)
                    startActivity(ShoppingActivity.getIntent(this, mLoggedUser));
                break;
            }
            case R.id.action_travel: {
                if(mLoggedUser != null){
                    City travelTo = Util.loadSelectedCity(this);
                    if(travelTo == null){
                        startActivity(SelectTravelingCityActivity.getIntent(this, mLoggedUser));
                    }else
                        startActivity(TravellingActivity.getIntent(this, mLoggedUser, travelTo));
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Request item) {

    }

    @Override
    public void onUserMakeOffer(Request item) {

    }

    @Override
    public void onUserProfileClick(Request item) {

    }

    @Override
    public void onListFragmentCreated() {
        loadRequest();
    }

    @Override
    public void onRequestListLoaded(List<Request> list) {
        //do nothing
    }

    @Override
    protected void onDestroy() {
        if(mChildEventListener!=null){
            FirebaseDatabase.getInstance().getReference().child(Contracts.REQUESTS_LOCATION)
                    .removeEventListener(mChildEventListener);
        }
        super.onDestroy();
    }
}
