package com.example.qloc.controller.json_utils.jsonObjects;

import com.example.qloc.controller.activities.activityUtils.ServerWayPoint;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by uli on 12.05.15.
 */
public class ServerWayPointContainer {

    @JsonProperty("location")
    private Double[] locations = new Double[2];
    @JsonProperty("answers")
    private String[] answers = new String[4];
    private String hint;
    private String question;

    public ServerWayPointContainer(ServerWayPoint swp) {
        this.locations[0]= swp.getLocation().getLatitude();
        this.locations[1]= swp.getLocation().getLongitude();
        this.answers[0]=swp.getAnswerTrue();
        this.answers[1]=swp.getFalseAnswers()[0];
        this.answers[2]=swp.getFalseAnswers()[1];
        this.answers[3]=swp.getFalseAnswers()[2];
        this.hint = swp.getHint();
        this.question = swp.getQuestion();
    }

    public Double[] getLocations() {
        return locations;
    }

    public void setLocations(Double[] locations) {
        this.locations = locations;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
