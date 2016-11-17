package thefour.com.worldshop.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;


public class Animation {
    private View view;


    public static AnimatorSet getZoomAnimation(View v, long duration, TimeInterpolator timeInterpolator, float from, float to)
    {
        ObjectAnimator scaleX = (ObjectAnimator) ObjectAnimator.ofFloat(v, "scaleX", from, to);
        ObjectAnimator scaleY = (ObjectAnimator) ObjectAnimator.ofFloat(v, "scaleY", from, to);
        scaleX.setDuration(duration);
        scaleX.setInterpolator(timeInterpolator);

        scaleY.setDuration(duration);
        scaleY.setInterpolator(timeInterpolator);
        AnimatorSet result = new AnimatorSet();
        result.playTogether(scaleX,scaleY);
        return result;
    }

    public static ObjectAnimator getRotateAnimation(View v, String type, long duration, float fromDegrees, float toDegrees)
    {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(v, type, fromDegrees, toDegrees);
        rotate.setDuration(duration);
        rotate.setInterpolator(new LinearInterpolator());
        return rotate;
    }



    public static ObjectAnimator getMoveAnimation(View v, boolean isMoveX, long duration, TimeInterpolator timeInterpolator, float fromX, float toX, float fromY, float toY)
    {
        ObjectAnimator animator = null;
        if(isMoveX)
            animator = ObjectAnimator.ofFloat(v,"X",fromX,toX);
        else
            animator = ObjectAnimator.ofFloat(v,"Y",fromY,toY);
        animator.setDuration(duration);
        animator.setInterpolator(timeInterpolator);
        return animator;
    }

    public static ObjectAnimator getFadeAnimation(View v, long duration, TimeInterpolator timeInterpolator, float fromAlpha, float toAlpha)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ALPHA,fromAlpha,toAlpha);
        animator.setDuration(duration);
        animator.setInterpolator(timeInterpolator);
        return animator;
    }

    public static ScaleAnimation getScaleOutAnimation()
    {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f,1.5f,1f,1.5f, android.view.animation.Animation.RELATIVE_TO_SELF,0.5f, android.view.animation.Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(250);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    public static ScaleAnimation getScaleInAnimation()
    {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f,1f,1.5f,1f, android.view.animation.Animation.RELATIVE_TO_SELF,0.5f, android.view.animation.Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(250);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }







}
