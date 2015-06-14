package com.example.qloc.controller.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qloc.R;
import com.example.qloc.controller.activities.QuestionActivity;

/**
 * Created by michael on 31.03.15.
 */
public class StatusFragment extends Fragment {

    public static final String RETVAL_KEY = "NEXT_ID";
    public static final String RETVAL_KEY_POINTS = "NEXT_POINTS";
    private View view;
    private StatusDeliverer deliverer; //activity which manages the status, the QuestionActivity
    private TextView motivation;
    private TextView points;
    private ImageView starMiddle;
    private ImageView starRight;
    private ImageView starLeft;
    private ImageView backToCompass;
    private float x1, x2; //needed for the touch event
    static final int MIN_DISTANCE = 100; //the minimal distance of the swipe event to be recognized



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_status,container,false);
        motivation = (TextView) view.findViewById(R.id.text_motivation);
        points = (TextView) view.findViewById(R.id.text_points);
        starMiddle = (ImageView) view.findViewById(R.id.star_middle);
        starRight = (ImageView) view.findViewById(R.id.star_right);
        starLeft = (ImageView) view.findViewById(R.id.star_left);
        backToCompass = (ImageView) view.findViewById(R.id.back_to_compass);

        //set the text according to the tries the user needed to get the right answer
        switch(deliverer.getStatus().getTries()){
            case 1:
                motivation.setText(getResources().getString(R.string.excellent));
                break;
            case 2:
                motivation.setText(getResources().getString(R.string.good));
                starLeft.setImageResource(R.drawable.sleeping_star);
                break;
            case 3:
                motivation.setText(getResources().getString(R.string.fair));
                starLeft.setImageResource(R.drawable.sleeping_star);
                starRight.setImageResource(R.drawable.sleeping_star);
                break;
            case 4:
                motivation.setText(getResources().getString(R.string.bad));
                starLeft.setImageResource(R.drawable.sleeping_star);
                starMiddle.setImageResource(R.drawable.sleeping_star);
                starRight.setImageResource(R.drawable.sleeping_star);
                break;
        }

        points.setText(getResources().getString(R.string.earned_points) + " " + deliverer.getStatus().getPoints() + " points");

        //add right2left swipe gesture detection
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float delta = x1 - x2;
                        if (delta > MIN_DISTANCE) {
                            Intent i = new Intent();
                            i.putExtra(RETVAL_KEY,deliverer.getStatus().getNext());
                            i.putExtra(RETVAL_KEY_POINTS,deliverer.getStatus().getPoints());
                            deliverer.quit(i);
                        }
                        break;
                }
                return true;
            }
        });
        setBackAnimation();
        return view;
    }

    public interface StatusDeliverer {
        public QuestionActivity.Status getStatus();
        public void quit(Intent i);
    }

    @Override
    public void onAttach(Activity activity){
        if(activity instanceof StatusDeliverer){
            super.onAttach(activity);
            deliverer = (StatusDeliverer) activity;
        }else{
            throw new ClassCastException(activity.toString() + "must implement StatusFragment.StatusDeliverer");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    /**
     * animation of the moving arrow
     */
    public void setBackAnimation(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(backToCompass,"translationX", -100f);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(backToCompass,"translationX", 0f);
        anim2.setRepeatCount(Animation.INFINITE);
        anim2.setDuration(100);

        AnimatorSet set = new AnimatorSet();
        set.play(anim).before(anim2);
        set.start();

    }


}
