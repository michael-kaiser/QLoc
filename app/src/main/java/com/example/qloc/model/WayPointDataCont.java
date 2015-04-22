package com.example.qloc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by uli on 17.04.15.
 */
public class WayPointDataCont {

    private String id;
    private String name;
    private String desc;
    private String question;
    private String nextId;
    @JsonProperty("answers")
    private ArrayList<String> answers = new ArrayList(4);
    @JsonProperty("locations")
    private ArrayList<Double> locations = new ArrayList(2);


    public WayPointDataCont() {
    }

    public WayPointDataCont(String id, String name, String desc, String question, String answer01, String answer2, String answer3, String answer4, String nextId, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.question = question;

        this.answers.add(answer01);
        this.answers.add(answer2);
        this.answers.add(answer3);
        this.answers.add(answer4);


        this.nextId = nextId;
        this.locations.add( latitude);
        this.locations.add(longitude);

    }

    public WayPointDataCont(WayPoint wp){
        this.name= wp.getName();
        this.desc = wp.getDesc();
        this.question = wp.getQuestion();

        this.answers.add(wp.getAnswer01());
        this.answers.add(wp.getAnswer2());
        this.answers.add(wp.getAnswer3());
        this.answers.add(wp.getAnswer4());
        this.id = wp.getId();
        this.nextId = wp.getNextId();
        this.locations.add(wp.getLatitude());
        this.locations.add(wp.getLongitude());

    }
    public WayPoint toWayPoint(){
        return new WayPoint(locations.get(0),  locations.get(1), id,  name, desc,  question, answers.get(0), answers.get(1), answers.get(2), answers.get(3), nextId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public String getAnswer01() {
        return answers.get(0);
    }

    public void setAnswer01(String answer01) {
        this.answers.add(answer01);
    }

    public String getAnswer2() {
        return answers.get(1);
    }

    public void setAnswer2(String answer2) {
        this.answers.add(answer2);
    }

    public String getAnswer3() {
        return answers.get(2);
    }

    public void setAnswer3(String answer3) {
        this.answers.add(answer3);
    }

    public String getAnswer4() {
        return answers.get(3);
    }

    public void setAnswer4(String answer4) {
        this.answers.add(answer4);
    }

    public double getLatitude() {
        return locations.get(0);
    }

    public void setLatitude(double latitude) {
        this.locations.add(latitude);
    }

    public double getLongitude() {
        return locations.get(1);
    }

    public void setLongitude(double longitude) {
        this.locations.add(longitude);
    }

}
