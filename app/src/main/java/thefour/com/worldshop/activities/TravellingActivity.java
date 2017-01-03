package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.TypefaceCache;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.databinding.ActivityTravelingBinding;
import thefour.com.worldshop.fragments.RequestFragment;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class TravellingActivity extends AppCompatActivity
        implements RequestFragment.OnListFragmentInteractionListener {

    private static final String ARG_TRAVELER = "arg_traveler";
    private static final String ARG_TRAVEL_TO = "arg_travel_to";
    private final int REQUEST_CODE_CHANGE_CITY = 1;
    private User mTraveler;
    private City mCity;
    private ChildEventListener mListener;
    private String TAG = TravellingActivity.class.getSimpleName();
    private RequestFragment mFragmentList;

    public static Intent getIntent(Context c, User traveler, City travelTo) {
        Intent i = new Intent(c, TravellingActivity.class);
        i.putExtra(ARG_TRAVELER, traveler);
        i.putExtra(ARG_TRAVEL_TO, travelTo);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        ActivityTravelingBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_traveling);

        mTraveler = getIntent().getParcelableExtra(ARG_TRAVELER);
        mCity = getIntent().getParcelableExtra(ARG_TRAVEL_TO);

        setSupportActionBar(mBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBinding.toolbar.textViewTitle.setTypeface(TypefaceCache.get(this,TypefaceCache.TITLE_FONT));
        mBinding.toolbar.textViewTitle.setText(mCity.getName());

        Toast.makeText(this, "city: " + mCity.getName(), Toast.LENGTH_SHORT).show();

        if (mTraveler == null || mCity == null) {
            Toast.makeText(this, "Traveler or selected City is null", Toast.LENGTH_SHORT).show();
            finish();
        }


        mFragmentList = RequestFragment.newInstance(1, mTraveler.getUserId());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mFragmentList).commit();

    }

    private void loadRequest() {
        mListener = RequestApi.loadRequestInCity(mCity, mFragmentList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_traveling, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_city:
                startActivityForResult(SelectTravelingCityActivity.getIntent(this, mTraveler)
                        , REQUEST_CODE_CHANGE_CITY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mListener != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child(Contracts.CITY_REQUESTS_LOCATION)
                    .child(mCity.getCityId())
                    .removeEventListener(mListener);
        }
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }


    @Override
    protected void onNewIntent(Intent intent) {

        mTraveler = intent.getParcelableExtra(ARG_TRAVELER);
        City newCity = intent.getParcelableExtra(ARG_TRAVEL_TO);
        if(newCity.getCityId().equalsIgnoreCase(mCity.getCityId())){

        }else{
            setIntent(intent);
            mCity = newCity;
            getSupportActionBar().setTitle(mCity.getName());
            mFragmentList.clearData();
            loadRequest();
        }


        Log.i(TAG, "onNewIntent: city = " + mCity.getName() + " - traveler = " + mTraveler.getName());
        super.onNewIntent(intent);
    }

    @Override
    public void onListFragmentInteraction(Request item, View selectedView) {
        //TODO enter Request detail activity
        Intent i = RequestDetailActivity.getIntent(this, item.getRequestId(), mTraveler);
        Bundle options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, selectedView, getString(R.string.item_image_transition_name))
                .toBundle();
        startActivity(i, options);
//        Toast.makeText(this, "onListInteraction: "+item.getItem().getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserMakeOffer(Request item) {
        //TODO enter make offer activity
        Intent intent = MakeOfferActivity.getIntent(this, mTraveler, item, null);
        startActivity(intent);
//        Toast.makeText(this, "onUserMakeOffer: "+item.getRequestId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserProfileClick(Request item, View userProfileImageView, View userNameTv) {
        //TODO enter profile activity
//        Toast.makeText(this, "onuserProfileClick: "+item.getFromUser().getName(), Toast.LENGTH_SHORT).show();
//        Pair<View, String> p1 = Pair.create(userProfileImageView, getString(R.string.profile_image));
//        Pair<View, String> p2 = Pair.create(userNameTv, getString(R.string.profile_name));
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this);
        Intent i = UserProfileActivity.getIntent(this, mTraveler, item.getFromUser());
        startActivity(i, options.toBundle());
    }

    @Override
    public void onListFragmentCreated() {
        loadRequest();
    }

    @Override
    public void onRequestListLoaded(List<Request> list) {
        //do nothing
    }
}
