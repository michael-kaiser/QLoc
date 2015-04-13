package com.example.qloc.controller;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qloc.R;
import com.example.qloc.model.WayPoint;

import java.util.List;

/**
 * The Fragment which manages the core quiz game
 *
 * @author michael
 * Created by michael on 31.03.15.
 */
public class QuestionFragment extends Fragment {

    private QuestionDeliverer questionDeliverer; //the activity which manages the currentQuestion
    private View view;
    private TextView question;
    private TextView answerA;
    private TextView answerB;
    private TextView answerC;
    private TextView answerD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_question,container,false);
        initializeQuestion();
        return view;
    }

    public interface QuestionDeliverer {
        public WayPoint getCurrentQuestion();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof QuestionDeliverer){
            questionDeliverer = (QuestionDeliverer) activity;
        }else{
            throw new ClassCastException(activity.toString() + "must implement QuestionFragment.QuestionDeliverer");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        questionDeliverer = null;
    }

    /**
     * setup the question
     */
    public void initializeQuestion(){
        question = (TextView) view.findViewById(R.id.text_question);
        answerA = (TextView) view.findViewById(R.id.text_answerA);
        answerB = (TextView) view.findViewById(R.id.text_answerB);
        answerC = (TextView) view.findViewById(R.id.text_answerC);
        answerD = (TextView) view.findViewById(R.id.text_answerD);
        WayPoint q = questionDeliverer.getCurrentQuestion();
        List<String> answers = q.getAnswerList();
        question.setText(q.getQuestion());
        answerA.setText(answers.get(0));
        answerB.setText(answers.get(1));
        answerC.setText(answers.get(2));
        answerD.setText(answers.get(3));
    }

}
