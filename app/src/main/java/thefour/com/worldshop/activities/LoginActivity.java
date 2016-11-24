package thefour.com.worldshop.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import thefour.com.worldshop.NetworkUtil;
import thefour.com.worldshop.R;
import thefour.com.worldshop.api.UserApi;
import thefour.com.worldshop.databinding.ActivityLoginBinding;
import thefour.com.worldshop.models.User;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 11;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ActivityLoginBinding mBinding;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final long timeBeginCreate = System.currentTimeMillis();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        handleEvent();
        //initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "onAuthStateChanged: log time need to check user"+ (System.currentTimeMillis()-timeBeginCreate));
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in, check if Existing user, then load data else create new user
                    UserApi.retrieveUserById(user.getUid(), new UserApi.IsUserExistCallback() {
                        @Override
                        public void onUserExist(User existingUser) {
                            if(existingUser==null){//new User, create account into database
                                final User worldShopUser = new User();
                                worldShopUser.setUserId(user.getUid());
                                worldShopUser.setEmail(user.getEmail());
                                worldShopUser.setName(user.getDisplayName());
                                worldShopUser.setProfileImageUrl(user.getPhotoUrl().toString());
                                worldShopUser.setTimeJoined(System.currentTimeMillis());
                                UserApi.createNewUser(worldShopUser, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError==null){
                                            Log.i(TAG, "onComplete: create new User Completed: "
                                                    +worldShopUser.getEmail());
                                            startActivity(HomeActivity.getIntent(LoginActivity.this, worldShopUser.getUserId()));
                                        }else{
                                            Log.e(TAG, "onComplete: create new user failed",databaseError.toException());
                                        }
                                    }
                                });
                            }else{
                                Snackbar.make(mBinding.loginContainer
                                        ,"Welcome back "+existingUser.getName()
                                        ,Snackbar.LENGTH_INDEFINITE).show();
                                //Todo enter main Screen
                                startActivity(HomeActivity.getIntent(LoginActivity.this, user.getUid()));
                            }
                        }
                    });

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                mProgressDialog.dismiss();
            }
        };

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.web_client_id))
                .requestProfile()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mProgressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(R.string.title_login_dialog);
        mProgressDialog.setMessage(getString(R.string.message_login_dialog));
    }

    private void handleEvent() {
        mBinding.googleSignInButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(mBinding.loginContainer,"Cannot connect to Google!\n"
                + connectionResult.getErrorMessage(),Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                if(!NetworkUtil.isNetworkAvailable(this)){
                    Snackbar.make(mBinding.loginContainer,
                            R.string.no_internet_warning,
                            Snackbar.LENGTH_LONG)
                            .setAction("Turn on Wifi", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(
                                            Settings.ACTION_WIFI_SETTINGS));
                                }
                            })
                            .show();
                    break;
                }
                mProgressDialog.show();
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
//            Toast.makeText(this, "login succeed" + "\nName:"+acct.getDisplayName()
//                    +"\nEmail:"+acct.getEmail()
//                    +"\nProfileImage:"+acct.getPhotoUrl()
//                    , Toast.LENGTH_SHORT).show();
        } else {
//            // Signed out, show unauthenticated UI.
//            updateUI(false);
            Log.e(TAG, "handleSignInResult: sign in failed");
        }
    }
    public static Intent getIntent(Context c){
        Intent i = new Intent(c, LoginActivity.class);
        return i;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}
