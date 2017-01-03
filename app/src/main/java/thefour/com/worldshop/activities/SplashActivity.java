package thefour.com.worldshop.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;

import thefour.com.worldshop.Contracts;
import thefour.com.worldshop.R;
import thefour.com.worldshop.animation.Animation;
import thefour.com.worldshop.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final int DURATION = 500;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (DatabaseException e){

        }

        ActivitySplashBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);


        //initialize Firebase Authentication
        final long timeBeginCreate = System.currentTimeMillis();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "onAuthStateChanged: log time need to check user: "
                        + (System.currentTimeMillis()-timeBeginCreate));
                mFirebaseUser = firebaseAuth.getCurrentUser();
            }
        };

        AnimatorSet zoomOutImage = Animation.getZoomAnimation(mBinding.imageViewFlashScreen
                ,DURATION, new AccelerateDecelerateInterpolator(),1f,3f);
        AnimatorSet zoomOutTextView = Animation.getZoomAnimation(mBinding.textViewFlashScreenTitle
                ,DURATION, new AccelerateDecelerateInterpolator(),1f,3f);

        AnimatorSet finalZoom = new AnimatorSet();
        finalZoom.setDuration(DURATION);
        finalZoom.setInterpolator(new AccelerateDecelerateInterpolator());
        finalZoom.playTogether(zoomOutImage,zoomOutTextView);
        finalZoom.start();

        finalZoom.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mFirebaseUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mFirebaseUser.getUid());
                    //Keep this user account information in synced
                    //
                    FirebaseDatabase.getInstance().getReference()
                            .child(Contracts.USERS_LOCATION).
                            child(mFirebaseUser.getUid())
                            .keepSynced(true);
//                    todo animation not work yet
//                    ActivityOptionsCompat optionsCompat =
//                            ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this);
//                    startActivity(HomeActivity.getIntent(SplashActivity.this, mFirebaseUser.getUid())
//                                                            , optionsCompat.toBundle());
                    startActivity(HomeActivity.getIntent(SplashActivity.this, mFirebaseUser.getUid()));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(LoginActivity.getIntent(SplashActivity.this));
                }
            }
        });

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
}
