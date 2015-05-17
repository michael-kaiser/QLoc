package com.example.qloc.controller.activities;

import android.animation.Animator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qloc.R;
import com.example.qloc.controller.dialogs.AlertDialogUtility;
import com.example.qloc.controller.fragments.QuestionFragment;
import com.example.qloc.controller.fragments.StatusFragment;
import com.example.qloc.model.WayPoint;
import com.example.qloc.model.exceptions.ServerCommunicationException;

/**
 * the activity during the question game
 *
 * @author michael
 */
public class QuestionActivity extends Activity implements QuestionFragment.QuestionDeliverer, StatusFragment.StatusDeliverer {

    private final String TAG = "QuestionActicity";
    private FragmentManager fm;
    private FragmentTransaction ft;
    private WayPoint currentQuestion;
    private int numPoints = 100;
    private int numTries = 1;
    private String points = "p";
    private String noOfQuestions = "/4";
    private TextView text_points;
    private TextView text_noOfQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentQuestion = getWaypoint();
        overridePendingTransition(R.anim.pull_in_from_right,R.anim.pull_out_to_left);
        setContentView(R.layout.activity_question);
        text_points = (TextView) findViewById(R.id.points);
        text_noOfQuestions = (TextView) findViewById(R.id.questionCounter);
        text_points.setText(numPoints + points);
        text_noOfQuestions.setText(numTries + noOfQuestions);


        /* add the question fragment */
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.question_fragment, new QuestionFragment());
        ft.commit();
    }

    public boolean checkAnswer(final String answer) throws ServerCommunicationException{
        boolean check = false;
        check = currentQuestion.checkAnswer(answer);
        //throw new ServerCommunicationException();
        return check;
    }

    @Override
    public WayPoint getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * gets the question
     */
    public WayPoint getWaypoint() {
        Log.d(TAG, "got waypoint: " + (getIntent().getParcelableExtra(Navigation_Activity_neu.KEY)).toString());
        return (WayPoint) getIntent().getParcelableExtra(Navigation_Activity_neu.KEY);
    }


    /**
     * evaluates the answer
     * if there are connection problems a dialog is shown
     * if evaluates to true -> change to status fragment
     * else -> decrease points and increment tries counter
     * @param v
     */
    public void evaluateAnswer(View v){
        int caller = v.getId(); //which answer was given? needed to get the answer text
        final TextView txt;
        boolean eval = false;
        try {
            switch (caller) {
                case R.id.answer1:
                    txt = (TextView) findViewById(R.id.text_answerA);
                    eval = checkAnswer(txt.getText().toString());
                    break;
                case R.id.answer2:
                    txt = (TextView) findViewById(R.id.text_answerB);
                    eval = checkAnswer(txt.getText().toString());
                    break;
                case R.id.answer3:
                    txt = (TextView) findViewById(R.id.text_answerC);
                    eval = checkAnswer(txt.getText().toString());
                    break;
                case R.id.answer4:
                    txt = (TextView) findViewById(R.id.text_answerD);
                    eval = checkAnswer(txt.getText().toString());
                    break;
            }
        }catch(ServerCommunicationException e) {
            Log.d(TAG, "ServerCommunicationException");
            AlertDialogUtility.showAlertDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return;
        }
        if(eval){
            Log.d("Evaluation","True");

            /* the correct answer card-flip animation */
            ((ViewGroup) v).getChildAt(0).setVisibility(View.INVISIBLE); //make the circle in the upper left invisible
            ((ViewGroup) v).getChildAt(1).setVisibility(View.INVISIBLE); //make the answer-text invisible
            ((ViewGroup) v).getChildAt(2).setBackground(getResources().getDrawable(R.drawable.correct)); //change the background from the red cross to the green tick
            ((ViewGroup) v).getChildAt(2).setVisibility(View.VISIBLE); //make the green tick visible
            v.setBackground(getResources().getDrawable(R.drawable.correct_answer)); //make the background of the answer-card green
            v.animate().rotationY(180).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                /**
                 * change to the status fragment
                 * @param animation
                 */
                @Override
                public void onAnimationEnd(Animator animation) {
                    ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out);
                    ft.replace(R.id.question_fragment, new StatusFragment());
                    ft.commit();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();

        }else{
            Log.d("Evaluation","False");
            decreasePointsAndIncreaseGivenAnswers();
            /* the wrong answer card-flip animation */
            ((ViewGroup) v).getChildAt(0).setVisibility(View.INVISIBLE); //make the circle in the upper left invisible
            ((ViewGroup) v).getChildAt(1).setVisibility(View.INVISIBLE); //make the answer-text invisible
            ((ViewGroup) v).getChildAt(2).setVisibility(View.VISIBLE); //make the red cross visible
            v.setBackground(getResources().getDrawable(R.drawable.wrong_answer)); //make the background of the answer-card red
            v.animate().rotationY(180).start(); //rotate the answer card
        }
    }

    /**
     * decrease the points and increment the tries counter
     */
    public void decreasePointsAndIncreaseGivenAnswers(){
        numTries += 1;
        if(numTries == 4){
            numPoints = 0;
        }else{
            numPoints /= 2;
        }


        text_points.setText(numPoints + points);
        text_noOfQuestions.setText(numTries + noOfQuestions);
    }

    public Status getStatus(){
        return new Status(numPoints, numTries, currentQuestion.getNextId());
    }

    @Override
    public void quit(Intent i) {
        this.setResult(Activity.RESULT_OK, i);
        this.finish();
    }

    /**
     * holds the points and tries
     */
    public class Status {

        private int points;
        private int tries;
        private String next;

        public Status(int points, int answers, String next){
            this.points = points;
            this.tries = answers;
            this.next = next;
        }

        public int getTries() {
            return tries;
        }

        public int getPoints() {
            return points;
        }

        public String getNext(){return next;}


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.pull_out_to_right);
    }


}
