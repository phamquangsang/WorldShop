package thefour.com.worldshop.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.TypefaceCache;
import thefour.com.worldshop.api.MessageApi;
import thefour.com.worldshop.databinding.ActivityChatBinding;
import thefour.com.worldshop.fragments.MessageFragment;
import thefour.com.worldshop.models.Message;
import thefour.com.worldshop.models.User;

public class ChatActivity extends AppCompatActivity
                            implements MessageFragment.OnListMessageFragmentInteractionListener{

    private static final String ARG_CHAT_WITH = "arg_chat_with_user";
    private static final String ARG_LOGGED_USER = "arg_logged_user";
    private static final String TAG = ChatActivity.class.getSimpleName();

    private User mLoggedUser;
    private User mChatWith;
    private MessageFragment mFragment;
    private ActivityChatBinding mBinding;
    private ChildEventListener mMessageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);
        mChatWith = getIntent().getParcelableExtra(ARG_CHAT_WITH);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        setSupportActionBar(mBinding.toolbar);

        mBinding.toolbarLayout.setTitle(mChatWith.getName());
        mBinding.toolbarLayout.setCollapsedTitleTypeface(TypefaceCache.get(this, TypefaceCache.HARMONIA_BOLD));
        mBinding.toolbarLayout.setExpandedTitleTypeface(TypefaceCache.get(this, TypefaceCache.HARMONIA_BOLD));

        mFragment = MessageFragment.newInstance(1, mLoggedUser);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listMessageContainer, mFragment)
                .commit();

        handleEvent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(mChatWith.getProfileImageUrl())
                .placeholder(R.drawable.ic_person_black_48px)
                .into(mBinding.userProfileImage);

        loadMessages();

    }

    private void handleEvent() {
        mBinding.messageComposer.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mBinding.messageComposer.etMessage.getText().toString().trim();
                if(message.isEmpty()){
                    return;
                }else{
                    mBinding.messageComposer.etMessage.setText("");
                    Message m = new Message();
                    m.setContent(message);
                    m.setFromUser(mLoggedUser);
                    m.setTime(System.currentTimeMillis());
                    MessageApi.sendMessage(m, mLoggedUser, mChatWith, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Log.i(TAG, "onComplete: send message succeed");
                        }
                    });
                }
            }
        });
    }

    private void loadMessages(){
        mMessageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mFragment.addMessage(dataSnapshot.getValue(Message.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mFragment.updateMessage(dataSnapshot.getValue(Message.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mFragment.removeMessage(dataSnapshot.getValue(Message.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference()
                .child(Contracts.CONVERSATION_LOCATION)
                .child(mLoggedUser.getUserId())
                .child(mChatWith.getUserId())
                .addChildEventListener(mMessageListener);
    }

    public static Intent getIntent(Context context, User loggedUser, User chatWith) {
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        i.putExtra(ARG_CHAT_WITH, chatWith);
        return i;
    }

    @Override
    public void onListFragmentInteraction(Message item) {
        //todo do nothing
    }

    @Override
    protected void onDestroy() {
        if(mMessageListener!=null)
            FirebaseDatabase.getInstance().getReference()
                .child(Contracts.CONVERSATION_LOCATION)
                .child(mLoggedUser.getUserId())
                .child(mChatWith.getUserId())
                .removeEventListener(mMessageListener);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
