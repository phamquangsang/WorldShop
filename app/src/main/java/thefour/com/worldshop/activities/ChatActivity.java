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
import thefour.com.worldshop.models.User;

public class ChatActivity extends AppCompatActivity {

    private static final String ARG_CHAT_WITH = "arg_chat_with_user";
    private static final String ARG_LOGGED_USER = "arg_logged_user";

    private User mLoggedUser;
    private User mChatWith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoggedUser = getIntent().getParcelableExtra(ARG_LOGGED_USER);
        mChatWith = getIntent().getParcelableExtra(ARG_CHAT_WITH);

        setContentView(R.layout.activity_chat);
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

    public static Intent getIntent(Context context, User loggedUser, User chatWith) {
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra(ARG_LOGGED_USER, loggedUser);
        i.putExtra(ARG_CHAT_WITH, chatWith);
        return i;
    }
}
