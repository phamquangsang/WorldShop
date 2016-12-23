package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.ActivityNotificationBinding;
import thefour.com.worldshop.fragments.NotificationFragment;
import thefour.com.worldshop.models.Notification;
import thefour.com.worldshop.models.User;

public class NotificationActivity extends AppCompatActivity
        implements NotificationFragment.OnListFragmentInteractionListener{

    private static final String ARG_LOGGED_USER = "arg-logged-user";
    private static final String TAG = NotificationActivity.class.getSimpleName();
    private User mLoggedUser;
    private ActivityNotificationBinding mBinding;
    private NotificationFragment mFragment;
    private ValueEventListener mNotificationListener;

    public static Intent getIntent(Context c, User loggedUser){
        Intent i = new Intent(c, NotificationActivity.class);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.notification);

        mFragment = NotificationFragment.newInstance(1);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();

        loadNotification();
    }

    private void loadNotification() {
        mNotificationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Notification> list = new ArrayList<>();
                for (DataSnapshot data :
                        dataSnapshot.getChildren()) {
                    list.add(data.getValue(Notification.class));
                }
                Log.i(TAG, "onDataChange: "+list.size()+ " notifications loaded");
                mFragment.updateDataset(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: load notification failed", databaseError.toException());
            }
        };
        FirebaseDatabase.getInstance().getReference()
                .child(Contracts.NOTIFICATION_LOCATION)
                .child(mLoggedUser.getUserId())
                .addValueEventListener(mNotificationListener);
    }

    @Override
    public void onListFragmentInteraction(Notification item) {
        Intent i = RequestDetailActivity.getIntent(this, item.getRequest().getRequestId(), mLoggedUser);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
