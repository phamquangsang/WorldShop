package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.api.UserApi;
import thefour.com.worldshop.fragments.RequestFragment;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class HomeActivity extends AppCompatActivity
        implements RequestFragment.OnListFragmentInteractionListener{

    private static final String ARG_USER_ID = "arg_user_id";
    private User mLoggedUser;
    private RequestFragment mRequestFragment;
    private ChildEventListener mChildEventListener;

    public static Intent getIntent(Context c, String userId) {
        Intent i = new Intent(c, HomeActivity.class);
        i.putExtra(ARG_USER_ID, userId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String userId = getIntent().getStringExtra(ARG_USER_ID);
        if (userId != null) {
            UserApi.retrieveUserById(userId, new UserApi.IsUserExistCallback() {
                @Override
                public void onUserExist(@Nullable User user) {
                    if (user != null){
                        mLoggedUser = user;
                        mRequestFragment = RequestFragment.newInstance(1,mLoggedUser);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, mRequestFragment)
                                .commit();
                        loadRequest();
                    }
                    else {
                        Toast.makeText(HomeActivity.this, "Please Login again!", Toast.LENGTH_SHORT).show();
                        startActivity(LoginActivity.getIntent(HomeActivity.this));
                    }
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadRequest() {
        mChildEventListener = RequestApi.loadLatestRequestInCity(mRequestFragment);
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
                        startActivity(TravelingActivity.getIntent(this, mLoggedUser, travelTo));
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
    protected void onDestroy() {
        if(mChildEventListener!=null){
            FirebaseDatabase.getInstance().getReference().child(Contracts.REQUESTS_LOCATION)
                    .removeEventListener(mChildEventListener);
        }
        super.onDestroy();
    }
}
