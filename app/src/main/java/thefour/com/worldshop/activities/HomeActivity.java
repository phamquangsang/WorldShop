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

import thefour.com.worldshop.R;
import thefour.com.worldshop.api.UserApi;
import thefour.com.worldshop.models.User;

public class HomeActivity extends AppCompatActivity {

    private static final String ARG_USER_ID = "arg_user_id";
    private User mLoggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String userId = getIntent().getStringExtra(ARG_USER_ID);
        if(userId!=null){
            UserApi.retrieveUserById(userId, new UserApi.IsUserExistCallback() {
                @Override
                public void onUserExist(@Nullable User user) {
                    if(user!=null)
                        mLoggedUser = user;
                    else{
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_shop:{
                //TODO start shopping activity
                startActivity(ShoppingActivity.getIntent(this, mLoggedUser));
                break;
            }
            case R.id.action_travel:{
                //TODO start travel activity
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public static Intent getIntent(Context c, String userId){
        Intent i = new Intent(c, HomeActivity.class);
        i.putExtra(ARG_USER_ID, userId);
        return i;
    }
}
