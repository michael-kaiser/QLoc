package com.example.qloc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

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
    private String[] answers = new String[4];
    @JsonProperty("locations")
    private Double[] locations = new Double[2];


    public WayPointDataCont() {
    }

    public WayPointDataCont(String id, String name, String desc, String question, String answer01, String answer2, String answer3, String answer4, String nextId, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.question = question;
        this.answers[0]=answer01;
        this.answers[1]=answer2;
        this.answers[2]=answer3;
        this.answers[3]=answer4;
        /*
        this.answers.add(answer01);
        this.answers.add(answer2);
        this.answers.add(answer3);
        this.answers.add(answer4);

*/
        this.nextId = nextId;

  /*      this.locations.add( latitude);
        this.locations.add(longitude);*/
        this.locations[0]=latitude;
        this.locations[1] =longitude;
    }

    public WayPointDataCont(WayPoint wp){
        this.name= wp.getName();
        this.desc = wp.getDesc();
        this.question = wp.getQuestion();

        this.answers[0]=wp.getAnswer01();
        this.answers[1]=wp.getAnswer1();
        this.answers[2]=wp.getAnswer3();
        this.answers[3]=wp.getAnswer4();

/*        this.answers.add(wp.getAnswer01());
        this.answers.add(wp.getAnswer2());
        this.answers.add(wp.getAnswer3());
        this.answers.add(wp.getAnswer4());*/
        this.id = wp.getId();
        this.nextId = wp.getNextId();
        this.locations[0] =wp.getLatitude();
        this.locations[1] =wp.getLongitude();
        /*this.locations.add(wp.getLatitude());
        this.locations.add(wp.getLongitude());*/

    }
    public WayPoint toWayPoint(){
        return new WayPoint(locations[0],  locations[1], id,  name, desc,  question, answers[0], answers[1], answers[2], answers[3], nextId);
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

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public Double[] getLocations() {
        return locations;
    }

    public void setLocations(Double[] locations) {
        this.locations = locations;
    }
}
