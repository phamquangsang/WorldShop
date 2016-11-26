package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.Util;
import thefour.com.worldshop.models.City;
import thefour.com.worldshop.models.User;

public class TravelingActivity extends AppCompatActivity {

    private static final String ARG_TRAVELER = "arg_traveler";
    private static final String ARG_TRAVEL_TO = "arg_travel_to";
    
    private User mTraveler;
    private City mCity;

    public static Intent getIntent(Context c, User traveler, City travelTo){
        Intent i = new Intent(c, TravelingActivity.class);
        i.putExtra(ARG_TRAVELER, traveler);
        i.putExtra(ARG_TRAVEL_TO,travelTo);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveling);

        mTraveler = getIntent().getParcelableExtra(ARG_TRAVELER);
        mCity = getIntent().getParcelableExtra(ARG_TRAVEL_TO);
        
        if(mTraveler == null || mCity == null){
            Toast.makeText(this, "Traveler or selected City is null", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    
}
