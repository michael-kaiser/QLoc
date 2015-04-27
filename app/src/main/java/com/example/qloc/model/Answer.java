package com.example.qloc.model;

/**
 * Created by uli on 27.04.15.
 */
public class Answer {
   private boolean evaluation;

    public Answer() {

    }
    public Answer(boolean answer) {
        this.evaluation = answer;
    }

    public boolean isEvaluation() {
        return evaluation;
    }

    public void setEvaluation(boolean evaluation) {
        this.evaluation = evaluation;
    }
}
