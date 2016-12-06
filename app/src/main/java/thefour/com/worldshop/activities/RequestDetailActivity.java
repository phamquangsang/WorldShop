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

public class RequestDetailActivity extends AppCompatActivity {
    private static final String ARG_LOGGED_USER = "arg_logged_user";
    private static final String ARG_REQUEST = "arg_request";

    private User mLoggedUser;
    private Request mRequest;

    //TODO|| to see request in detail, All item images, All offer (pending, accepted),
    //TODO|| User who make offer could edit their offer,
    //TODO|| User who make request can accept other user's offer,

    //TODO|| Action Make offer button.
    //TODO|| Action Chat.
    //TODO|| Action accept offer.

    //TODO|| UI-UX example https://grabr.io/en/grabs/52007


    public static Intent getIntent(Context c, Request request, User loggedUser){
        Intent i = new Intent(c, RequestDetailActivity.class);
        i.putExtra(ARG_REQUEST, request);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);
        mRequest = getIntent().getParcelableExtra(ARG_REQUEST);

        setContentView(R.layout.activity_request_detail);
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
