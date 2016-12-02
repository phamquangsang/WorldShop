package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.firebase.database.DatabaseError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import thefour.com.worldshop.R;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.adapters.CityAdapter;
import thefour.com.worldshop.api.CityApi;
import thefour.com.worldshop.databinding.ActivitySelectTravelingCityBinding;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.User;

/* TODO-------------------------------------------------------------------------
-- todo this allow traveler select a city to travel
-- todo after user select a city, call onUserSelectedCity() to save to sharedPreference
-- todo call CityApi.loadCities() to load all the supported cites from server
--------------------------------------------------------------------------------*/
public class SelectTravelingCityActivity extends AppCompatActivity {
    public static final String ARG_CITY_RETURN = "arg_city_return";
    private static final String ARG_TRAVELER = "arg_traveler";
    private static final String TAG = SelectTravelingCityActivity.class.getSimpleName();

    private User mTraveler;
    private CityAdapter mAdapter;
    private ArrayList<City> mCities;
    private ActivitySelectTravelingCityBinding mBiding;

    public static Intent getIntent(Context c, User traveler) {
        Intent i = new Intent(c, SelectTravelingCityActivity.class);
        i.putExtra(ARG_TRAVELER, traveler);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTraveler = getIntent().getParcelableExtra(ARG_TRAVELER);
        mBiding = DataBindingUtil.setContentView(this, R.layout.activity_select_traveling_city);
        setUpViews();
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CityAdapter.CitySelectedEvent event) {
        Log.i(TAG, "onMessageEvent: " + event.getmCity());
        onUserSelectedCity(event.getmCity());
        Intent i = TravelingActivity.getIntent(this, mTraveler, event.getmCity());
        startActivity(i);
    }

    private void setUpViews() {
        setSupportActionBar(mBiding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBiding.recyclerViewCities.setHasFixedSize(true);
        mBiding.recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        CityApi.loadCities(new CityApi.OnLoadCompleted() {
            @Override
            public void onLoadCompleted(ArrayList<City> cities) {
                mCities = cities;
                for (City c : mCities) {
                    Log.i(TAG, "onLoadCompleted: " + c.toString());
                }
                mAdapter = new CityAdapter(SelectTravelingCityActivity.this, mCities);
                mBiding.recyclerViewCities.setAdapter(mAdapter);
            }

            @Override
            public void onLoadCancelled(DatabaseError error) {

            }
        });
    }

    private void onUserSelectedCity(City city) {
        Util.saveTravelingCity(this, city);
        getIntent().putExtra(ARG_CITY_RETURN, city);
        setResult(RESULT_OK);
        finish();
        startActivity(TravelingActivity.getIntent(this, mTraveler, city)
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        );
        Log.i(TAG, "onUserSelectedCity: " + city.getName());
    }


}
