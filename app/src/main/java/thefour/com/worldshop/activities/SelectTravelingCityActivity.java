package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Random;

import thefour.com.worldshop.R;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.api.CityApi;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.User;

/* TODO-------------------------------------------------------------------------
-- todo this allow traveler select a city to travel
-- todo after user select a city, call onUserSelectedCity() to save to sharedPreference
-- todo call CityApi.loadCities() to load all the supported cites from server
--------------------------------------------------------------------------------*/
public class SelectTravelingCityActivity extends AppCompatActivity {

    private static final String ARG_TRAVELER = "arg_traveler";
    public static final String ARG_CITY_RETURN = "arg_city_return";
    private User mTraveler;
    private ArrayList<City> mCities;
    private String TAG = SelectTravelingCityActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTraveler = getIntent().getParcelableExtra(ARG_TRAVELER);
        setContentView(R.layout.activity_select_traveling_city);
        CityApi.loadCities(new CityApi.OnLoadCompleted() {
            @Override
            public void onLoadCompleted(ArrayList<City> cities) {
                mCities = cities;
                Random random = new Random();
                int randomInt = random.nextInt(cities.size());
                onUserSelectedCity(cities.get(randomInt));
            }

            @Override
            public void onLoadCancelled(DatabaseError error) {

            }
        });
    }



    public static Intent getIntent(Context c, User traveler){
        Intent i = new Intent(c, SelectTravelingCityActivity.class);
        i.putExtra(ARG_TRAVELER, traveler);
        return i;
    }

    private void onUserSelectedCity(City city){
        Util.saveTravelingCity(this, city);
        getIntent().putExtra(ARG_CITY_RETURN, city);
        setResult(RESULT_OK);
        finish();
        startActivity(TravelingActivity.getIntent(this, mTraveler, city)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        );
        Log.i(TAG, "onUserSelectedCity: "+ city.getName());
    }
}
