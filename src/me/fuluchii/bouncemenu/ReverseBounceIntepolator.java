package me.fuluchii.bouncemenu;

import android.view.animation.BounceInterpolator;

/**
 * @author fuluchii.zhao
 */
public class ReverseBounceIntepolator extends BounceInterpolator {

    @Override
    public float getInterpolation(float t) {
        if(t<=0.2){
            return 1-(1-t*5)*(1-t*5);
        }else{
            return (1-super.getInterpolation((t-0.2f)*1.25f));
        }

    }
}
