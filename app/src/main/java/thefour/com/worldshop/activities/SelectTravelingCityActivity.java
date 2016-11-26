package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import thefour.com.worldshop.R;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.User;

/* TODO-------------------------------------------------------------------------
-- todo this allow traveler select a city to travel
-- todo after user select a city, call onUserSelectedCity() to save to sharedPreference
-- todo call CityApi.loadCities() to load all the supported cites from server
--------------------------------------------------------------------------------*/
public class SelectTravelingCityActivity extends AppCompatActivity {

    private static final String ARG_TRAVELER = "arg_traveler";
    private User mTraveler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTraveler = getIntent().getParcelableExtra(ARG_TRAVELER);
        setContentView(R.layout.activity_select_traveling_city);
    }

    public static Intent getIntent(Context c, User traveler){
        Intent i = new Intent(c, SelectTravelingCityActivity.class);
        i.putExtra(ARG_TRAVELER, traveler);
        return i;
    }

    private void onUserSelectedCity(City city){
        Util.saveTravelingCity(this, city);
        finish();
        startActivity(TravelingActivity.getIntent(this, mTraveler, city));
    }
}
