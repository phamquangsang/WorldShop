package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.databinding.ActivityTravelingBinding;
import thefour.com.worldshop.databinding.ContentTravelingBinding;
import thefour.com.worldshop.fragments.RequestFragment;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class TravelingActivity extends AppCompatActivity
        implements RequestFragment.OnListFragmentInteractionListener {

    private static final String ARG_TRAVELER = "arg_traveler";
    private static final String ARG_TRAVEL_TO = "arg_travel_to";
    private final int REQUEST_CODE_CHANGE_CITY = 1;
    private User mTraveler;
    private City mCity;
    private ActivityTravelingBinding mBinding;
    private ChildEventListener mListener;
    private String TAG = TravelingActivity.class.getSimpleName();
    private RequestFragment mFragmentList;

    public static Intent getIntent(Context c, User traveler, City travelTo) {
        Intent i = new Intent(c, TravelingActivity.class);
        i.putExtra(ARG_TRAVELER, traveler);
        i.putExtra(ARG_TRAVEL_TO, travelTo);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_traveling);

        mTraveler = getIntent().getParcelableExtra(ARG_TRAVELER);
        mCity = getIntent().getParcelableExtra(ARG_TRAVEL_TO);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setTitle(mCity.getName());

        Toast.makeText(this, "city: " + mCity.getName(), Toast.LENGTH_SHORT).show();

        if (mTraveler == null || mCity == null) {
            Toast.makeText(this, "Traveler or selected City is null", Toast.LENGTH_SHORT).show();
            finish();
        }


        mFragmentList = RequestFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.container, mFragmentList).commit();

        loadRequest();

        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
    public void onListFragmentInteraction(Request item) {

    }

    @Override
    protected void onNewIntent(Intent intent) {

        mTraveler = intent.getParcelableExtra(ARG_TRAVELER);
        City newCity = intent.getParcelableExtra(ARG_TRAVEL_TO);
        if(newCity.getCityId().equalsIgnoreCase(mCity.getCityId())){

        }else{
            setIntent(intent);
            mCity = newCity;
            loadRequest();
            getSupportActionBar().setTitle(mCity.getName());
            mFragmentList.clearData();
        }


        Log.i(TAG, "onNewIntent: city = " + mCity.getName() + " - traveler = " + mTraveler.getName());
        super.onNewIntent(intent);
    }
}
