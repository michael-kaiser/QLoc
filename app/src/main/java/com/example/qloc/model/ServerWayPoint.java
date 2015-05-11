package com.example.qloc.model;

import android.location.Location;

/**
 * Created by michael on 11.05.15.
 */
public class ServerWayPoint {

    private String answerTrue;
    private String [] falseAnswers;
    private Location location;
    private String hint;
    private String question;

    public ServerWayPoint(String answerTrue, String[] falseAnswers, Location location, String hint, String question) {
        this.answerTrue = answerTrue;
        this.falseAnswers = falseAnswers;
        this.location = location;
        this.hint = hint;
        this.question = question;
    }

    public Location getLocation() {
        return location;
    }
}
