package com.example.qloc.controller.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qloc.R;
import com.example.qloc.model.data.Data;
import com.example.qloc.model.data.HttpFacade;

public class FinalStatus extends Activity {

    private TextView motivation;
    private TextView pointsTxt;
    private TextView percentageTxt;
    private int points;
    private int wpCounter;
    private float percentage;
    private ImageView starMiddle;
    private ImageView starRight;
    private ImageView starLeft;
    private ImageView backToMain;
    private float x1, x2; //needed for the touch event
    static final int MIN_DISTANCE = 100; //the minimal distance of the swipe event to be recognized
    private int movingStars = 0;
    private Data facade = HttpFacade.getInstance();



    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d("haha", "here");
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.pull_out_to_left);
        setContentView(R.layout.activity_final_status);
        points = getIntent().getIntExtra(NavigationActivity.KEY_POINTS,0);
        wpCounter = getIntent().getIntExtra(NavigationActivity.KEY_WPCOUNT,0);
        percentageTxt = (TextView)findViewById(R.id.text_percentageFinal);
        motivation = (TextView) findViewById(R.id.text_finalRank);
        pointsTxt = (TextView) findViewById(R.id.text_pointsFinal);
        starMiddle = (ImageView)findViewById(R.id.star_middle_final);
        starRight = (ImageView) findViewById(R.id.star_right_final);
        starLeft = (ImageView) findViewById(R.id.star_left_final);
        backToMain = (ImageView) findViewById(R.id.back_to_main);
        calcPercentage();
        //set the text according to the tries the user needed to get the right answer
        if(percentage >= 90) {
            motivation.setText(getResources().getString(R.string.excellent));
            movingStars = 3;
        }
        else if(percentage >= 70) {
            motivation.setText(getResources().getString(R.string.good));
            starLeft.setImageResource(R.drawable.star_adult_cry);
            movingStars = 2;
        }
        else if(percentage >= 50) {
            motivation.setText(getResources().getString(R.string.fair));
            starLeft.setImageResource(R.drawable.star_adult_cry);
            starRight.setImageResource(R.drawable.star_adult_cry);
            movingStars = 1;
        }
        else{
                motivation.setText(getResources().getString(R.string.bad));
                starLeft.setImageResource(R.drawable.star_adult_cry);
                starMiddle.setImageResource(R.drawable.star_adult_cry);
                starRight.setImageResource(R.drawable.star_adult_cry);
        }

        pointsTxt.setText("Your final score is: " + points);
        percentageTxt.setText("You earned " + percentage + "% of all points");

        setBackAnimation();
        setStarAnimation();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float delta = x1 - x2;
                if (delta > MIN_DISTANCE) {
                    sendPointsToServer();
                    Intent i = new Intent(this , MainScreen.class);
                    startActivity(i);

                }
                break;
        }
        return super.onTouchEvent(event);

    }

    public void setBackAnimation(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(backToMain,"translationX", -100f);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(backToMain,"translationX", 0f);
        anim2.setRepeatCount(Animation.INFINITE);
        anim2.setDuration(100);

        AnimatorSet set = new AnimatorSet();
        set.play(anim).before(anim2);
        set.start();

    }

    public void setStarAnimation(){
        ObjectAnimator animStarMiddle1 = ObjectAnimator.ofFloat(starMiddle,"rotation", 90f);
        animStarMiddle1.setDuration(1000);
        ObjectAnimator animStarMiddle2 = ObjectAnimator.ofFloat(starMiddle,"rotation",-90f);
        animStarMiddle2.setDuration(1000);
        ObjectAnimator animStarMiddle3 = ObjectAnimator.ofFloat(starMiddle,"rotation",0f);
        animStarMiddle3.setDuration(1000);


        ObjectAnimator animStarLeft1 = ObjectAnimator.ofFloat(starLeft,"rotation", 90f);
        animStarLeft1.setDuration(1000);
        ObjectAnimator animStarLeft2 = ObjectAnimator.ofFloat(starLeft,"rotation",-90f);
        animStarLeft2.setDuration(1000);
        ObjectAnimator animStarLeft3 = ObjectAnimator.ofFloat(starLeft,"rotation",0f);
        animStarLeft3.setDuration(1000);

        ObjectAnimator animStarRight1 = ObjectAnimator.ofFloat(starRight,"rotation", 90f);
        animStarRight1.setDuration(1000);
        ObjectAnimator animStarRight2 = ObjectAnimator.ofFloat(starRight,"rotation",-90f);
        animStarRight2.setDuration(1000);
        ObjectAnimator animStarRight3 = ObjectAnimator.ofFloat(starRight,"rotation",0f);
        animStarRight3.setDuration(1000);


        final AnimatorSet set = new AnimatorSet();
        if(movingStars == 1) {
            set.play(animStarMiddle1).before(animStarMiddle2);
            set.play(animStarMiddle2).before(animStarMiddle3);
        }
        else if(movingStars == 2){
            set.play(animStarMiddle1).before(animStarMiddle2);
            set.play(animStarMiddle2).before(animStarMiddle3);
            set.play(animStarRight1).after(500);
            set.play(animStarRight2).after(animStarRight1);
            set.play(animStarRight3).after(animStarRight2);
        }
        else if(movingStars == 3){
            set.play(animStarMiddle1).before(animStarMiddle2);
            set.play(animStarMiddle2).before(animStarMiddle3);
            set.play(animStarLeft1).after(500);
            set.play(animStarLeft2).after(animStarLeft1);
            set.play(animStarLeft3).after(animStarLeft2);
            set.play(animStarRight1).after(500);
            set.play(animStarRight2).after(animStarRight1);
            set.play(animStarRight3).after(animStarRight2);
        }else{
            return;
        }

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                    set.start();


            }
        });
        set.start();

    }

    private void calcPercentage(){
        int maxPoints = wpCounter * 100;
        percentage = points * 100 / maxPoints;
    }

    private void sendPointsToServer(){
        ///facade.setPoints(points);
    }
}
