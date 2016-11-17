package thefour.com.worldshop.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import thefour.com.worldshop.R;
import thefour.com.worldshop.animation.Animation;
import thefour.com.worldshop.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding mBinding;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private ImageView mFlashScreenImageView;
    private TextView mTitleTextView;

    private static final int DURATION = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mFlashScreenImageView = mBinding.imageViewFlashScreen;
        mTitleTextView = mBinding.textViewFlashScreenTitle;

        AnimatorSet zoomOutImage = Animation.getZoomAnimation(mFlashScreenImageView,DURATION,new AccelerateDecelerateInterpolator(),1f,3f);
        AnimatorSet zoomOutTextView = Animation.getZoomAnimation(mTitleTextView,DURATION,new AccelerateDecelerateInterpolator(),1f,3f);

        AnimatorSet finalZoom = new AnimatorSet();
        finalZoom.setDuration(DURATION);
        finalZoom.setInterpolator(new AccelerateDecelerateInterpolator());
        finalZoom.playTogether(zoomOutImage,zoomOutTextView);
        finalZoom.start();

        finalZoom.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

//                Gson gson = new Gson();
//                String userJson = Utilities.getSharedPreference(FlashScreenActivity.this)
//                        .getString(Constants.LOGGED_USER_JSON, "");
//                User loggedUser = gson.fromJson(userJson, User.class);
//                if (loggedUser == null || loggedUser.getEmail()== null) {
//                    User temporaryUser = User.getTemporaryUser(FlashScreenActivity.this);
//                    startActivity(IcychatActivity.newIntent(FlashScreenActivity.this, temporaryUser,true));
//                }else{
//                    startActivity(IcychatActivity.newIntent(FlashScreenActivity.this, loggedUser,true));
//                }

            }
        });

    }
}
