package thefour.com.worldshop.activities;
//List all recent conversation
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.databinding.ActivityConversationBinding;
import thefour.com.worldshop.fragments.FriendsListFragment;
import thefour.com.worldshop.models.Friend;
import thefour.com.worldshop.models.User;

public class ConversationActivity extends AppCompatActivity
        implements FriendsListFragment.OnListFragmentInteractionListener {

    private static final String ARG_LOGGED_USER = "arg-logged-user";
    private FriendsListFragment mFragment;

    private User mLoggedUser;
    private ValueEventListener mFriendsListener;
    private ActivityConversationBinding mBinding;

    public static Intent getIntent(Context c, User loggedUser){
        Intent i = new Intent(c, ConversationActivity.class);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragment = FriendsListFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();
        loadFriendList();
    }


    public void loadFriendList(){
        mFriendsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Friend> list = new ArrayList<>();
                for (DataSnapshot friend :
                        dataSnapshot.getChildren()) {
                    Friend f = friend.getValue(Friend.class);
                    list.add(f);
                }
                mFragment.addValues(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConversationActivity.this, "load friend list error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        FirebaseDatabase.getInstance().getReference()
                .child(Contracts.FRIENDS_LIST_LOCATION)
                .child(mLoggedUser.getUserId()).addValueEventListener(mFriendsListener);
    }

    @Override
    public void onListFragmentInteraction(Friend item) {
        Intent i = ChatActivity.getIntent(this, mLoggedUser, item.getUser());
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        if(mFriendsListener!=null){
            FirebaseDatabase.getInstance().getReference()
                    .child(Contracts.CONVERSATION_LOCATION)
                    .child(mLoggedUser.getUserId())
                    .removeEventListener(mFriendsListener);
        }
        super.onDestroy();
    }
}
