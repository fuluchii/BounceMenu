package me.fuluchii.bouncemenu.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import me.fuluchii.bouncemenu.ReverseBounceIntepolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fuluchii.zhao
 */
public class BounceMenu extends ViewGroup {

    private List<ImageButton> buttonList;

    private int buttonCount;

    private int buttonWidth;

    private int interval;

    private float leftwipe;

    private List<PopupWindow> popupWindowList;

    private final Map<Integer,AnimatorSet> waveAnimatorList = new HashMap<Integer, AnimatorSet>();

    private Map<Integer,Boolean> statemap = new HashMap<Integer, Boolean>();



    public BounceMenu(Context context) {
        super(context);
    }

    public BounceMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //calculate interval
        interval = ((r-l)-buttonCount*buttonWidth)/(buttonCount+1);

        //init button positioin
        for (ImageButton imageButton : buttonList) {
            int left= buttonList.indexOf(imageButton)*buttonWidth+(buttonList.indexOf(imageButton)+1)*interval;
            imageButton.layout(l + left, b, l + left + buttonWidth, b+buttonWidth);

            popupWindowList.get(buttonList.indexOf(imageButton)).getContentView().layout(0,b,r,b+popupWindowList.get(buttonList.indexOf(imageButton)).getHeight());
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        buttonCount = getChildCount();
        if(buttonList == null){
            buttonList = new ArrayList<ImageButton>();
        }
        for (int i =0;i<buttonCount ;i++){
            buttonList.add((ImageButton) findViewById(getContext().getResources().getIdentifier("bounce_btn_" + i, "id", getContext().getPackageName())));
        }
        if(buttonList.size()>0){
            buttonWidth = buttonList.get(0).getLayoutParams().width;
            Log.i("width","+"+buttonWidth);
        }else{
            throw new NullPointerException("No button found");
        }

    }

    public void bounce(){
        for (ImageButton imageButton : buttonList) {

            bounceOpen(imageButton,buttonList.indexOf(imageButton));
        }
    }

    private void bounceOpen(final ImageButton imageButton, final int index){
        ObjectAnimator up = ObjectAnimator.ofFloat(imageButton,"translationY",-buttonWidth*1.2f);
        final ObjectAnimator bounceOpen = ObjectAnimator.ofFloat(imageButton,"translationY",-buttonWidth*2);
        up.setDuration(1000);
        bounceOpen.setDuration(2000);
        bounceOpen.setInterpolator(new ReverseBounceIntepolator());
        final AnimatorSet bounceSet = new AnimatorSet();
        bounceSet.playSequentially(up,bounceOpen);
        statemap.put(index,false);
        bounceSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!statemap.get(index)){
                    if(waveAnimatorList.size()-1< index || waveAnimatorList.get(index) == null) {

                        ObjectAnimator toUp = ObjectAnimator.ofFloat(imageButton, "translationY", -buttonWidth * 1.35f);
                        toUp.setRepeatCount(Animation.INFINITE);
                        toUp.setInterpolator(new CycleInterpolator(1));
                        toUp.setDuration(3200);
                        AnimatorSet set = new AnimatorSet();
                        set.playSequentially(toUp);
                        set.setStartDelay(index * 800);
                        waveAnimatorList.put(index, set);
                        set.start();
                    }else{
                        waveAnimatorList.get(index).start();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        bounceSet.start();
        //add listener
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow window = popupWindowList.get(index);
//                imageButton.clearAnimation();
                if(!bounceSet.isRunning()) {
                    statemap.put(index,false);
                    if(waveAnimatorList.get(buttonList.indexOf(imageButton))!=null) {
                        waveAnimatorList.get(buttonList.indexOf(imageButton)).cancel();
                    }
                }else{
                    statemap.put(index,true);
                    bounceSet.cancel();
                }
                ObjectAnimator title = ObjectAnimator.ofFloat(imageButton,"translationY",-window.getHeight()-buttonWidth);
                title.setDuration(700);
                title.start();
                title.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        window.getContentView().setBackgroundColor(Color.rgb(256,138,138));
                        window.showAtLocation(imageButton, Gravity.BOTTOM,0,0);
                        leftwipe = imageButton.getTranslationX();
                        ObjectAnimator pop = ObjectAnimator.ofFloat(imageButton,"translationX",-imageButton.getLeft()+20);
                        pop.setDuration(1000);
                        pop.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                //set invisible
                for (ImageButton button : buttonList) {
                    if(buttonList.indexOf(button)!=index){
                        button.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });

    }

    public void setButtonCount(int buttonCount) {
        this.buttonCount = buttonCount;
    }

    public void setPopupWindowList(final List<PopupWindow> popupWindowList) {

        this.popupWindowList = popupWindowList;
        for (final PopupWindow window : popupWindowList) {
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    dismissP(window);
                }
            });
        }
    }

    public void dismissP(final PopupWindow window){
        for (ImageButton imageButton : buttonList) {
            imageButton.setVisibility(View.VISIBLE);
        }
        ImageButton button = buttonList.get(popupWindowList.indexOf(window));
        button.forceLayout();
        ObjectAnimator pop = ObjectAnimator.ofFloat(button,"translationX",leftwipe);
        pop.setDuration(1000);
        pop.start();
        pop.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bounceOpen(buttonList.get(popupWindowList.indexOf(window)), popupWindowList.indexOf(window));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
