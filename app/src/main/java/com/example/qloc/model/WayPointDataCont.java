package com.example.qloc.model;

import com.example.qloc.model.WayPoint;

/**
 * Created by uli on 17.04.15.
 */
public class WayPointDataCont {

    private String id;
    private String name;
    private String desc;
    private String question;
    private String nextId;
    private String answer01;
    private String answer2;
    private String answer3;
    private String answer4;
    private double latitude;
    private double longitude;

    public WayPointDataCont() {
    }

    public WayPointDataCont(String id, String name, String desc, String question, String answer01, String answer2, String answer3, String answer4, String nextId, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.question = question;
        this.answer01 = answer01;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.nextId = nextId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public WayPointDataCont(WayPoint wp){
        this.name= wp.getName();
        this.desc = wp.getDesc();
        this.question = wp.getQuestion();
        this.answer01 = wp.getAnswer01();
        this.answer2 = wp.getAnswer2();
        this.answer3 = wp.getAnswer3();
        this.answer4 = wp.getAnswer4();
        this.id = wp.getId();
        this.nextId = wp.getNextId();
        this.setLongitude(wp.getLongitude());
        this.setLatitude(wp.getLatitude());
    }
    public WayPoint toWayPoint(){
       return new WayPoint(latitude,  longitude, id,  name, desc,  question, answer01, answer2, answer3, answer4, nextId);
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
        return answer01;
    }

    public void setAnswer01(String answer01) {
        this.answer01 = answer01;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
