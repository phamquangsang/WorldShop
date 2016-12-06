package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import thefour.com.worldshop.R;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class MakeOfferActivity extends AppCompatActivity {

    private static final String ARG_USER = "arg_user";
    private static final String ARG_REQUEST = "arg_request";


    public static Intent getItent(Context c, User user, Request request){
        Intent i = new Intent(c, MakeOfferActivity.class);
        i.putExtra(ARG_USER, user);
        i.putExtra(ARG_REQUEST, request);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);
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
