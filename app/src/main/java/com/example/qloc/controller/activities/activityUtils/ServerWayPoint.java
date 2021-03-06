package com.example.qloc.controller.activities.activityUtils;

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

    public void setAnswerTrue(String answerTrue) {
        this.answerTrue = answerTrue;
    }

    public void setFalseAnswers(String... falseAnswers) {
        this.falseAnswers = falseAnswers;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

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

    public String getQuestion() {
        return question;
    }

    public String getHint() {
        return hint;
    }

    public String[] getFalseAnswers() {
        return falseAnswers;
    }

    public String getAnswerTrue() {
        return answerTrue;
    }

    public void setLocation(double lat, double lon){
        Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        this.location = loc;
    }
}
