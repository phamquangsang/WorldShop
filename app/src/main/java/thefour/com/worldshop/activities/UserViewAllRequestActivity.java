package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.adapters.RequestAdapter;
import thefour.com.worldshop.api.RequestApi;
import thefour.com.worldshop.databinding.ActivityUserViewAllRequestBinding;
import thefour.com.worldshop.fragments.RequestFragment;
import thefour.com.worldshop.models.Request;
import thefour.com.worldshop.models.User;

public class UserViewAllRequestActivity extends AppCompatActivity
        implements RequestFragment.OnListFragmentInteractionListener{

    private static final String ARG_USER = "arg-user";
    private static final String ARG_LOGGED_USER = "arg-logged-user";
    private ActivityUserViewAllRequestBinding mBinding;
    private User mUser;
    private User mLoggedUser;
    private RequestFragment mFragment;
    private ChildEventListener mRequestListener;

    public static Intent getIntent(Context c, User user, User loggedUser){
        Intent i = new Intent(c, UserViewAllRequestActivity.class);
        i.putExtra(ARG_USER, user);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getIntent().getParcelableExtra(ARG_USER);
        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_view_all_request);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("All Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFragment = RequestFragment.newInstance(1, mUser.getUserId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //handle the home button onClick event here.
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Request item, View selectedView) {
        Intent i = RequestDetailActivity.getIntent(this, item.getRequestId(), mUser);
        Bundle options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, selectedView, getString(R.string.item_image_transition_name))
                .toBundle();
        startActivity(i, options);
    }

    @Override
    public void onUserMakeOffer(Request item) {
        Intent intent = MakeOfferActivity.getIntent(this, mUser, item, null);
        startActivity(intent);
    }

    @Override
    public void onUserProfileClick(Request item, View profileImage, View profileName) {

    }

    @Override
    public void onListFragmentCreated() {
        loadAllUserRequest();
    }

    @Override
    public void onRequestListLoaded(List<Request> list) {

    }

    private void loadAllUserRequest() {
        mRequestListener = RequestApi.loadLatestUserRequestChildEvent(mUser, 0, mFragment);
    }

    @Override
    protected void onDestroy() {
        if(mRequestListener!=null) {
            FirebaseDatabase.getInstance().getReference()
                    .child(Contracts.USER_REQUESTS_LOCATION)
                    .child(mUser.getUserId())
                    .removeEventListener(mRequestListener);
        }
        super.onDestroy();
    }
}
