package com.example.qloc.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/*import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;*/

/**
 * Created by uli on 06.04.15.
 * TODO make compatible with Haskell-Server
 * TODO answers to array
 */
public class WayPoint extends Location implements Parcelable{

    private String id;
    private String name;
    private String desc;
    private String question;
    private String answer01; //TODO mock
    private String answer2;
    private String answer3;
    private String answer4;
    private String nextId;
    public String getQuestion() {
        return question;
    }



    public WayPoint(Location l) {
        super(l);
    }

    public WayPoint(Location l, String id, String desc, String q, String a1, String a2, String a3, String a4){
        super(l);
        this.name= "Einziger";
        this.question = q;
        this.answer01 = a1;
        this.answer2 = a2;
        this.answer3 = a3;
        this.answer4 = a4;
        this.desc = desc;
        this.id = id;

    }

    public WayPoint(WayPoint wp){
        super(wp);
        this.name= wp.getName();
        this.desc = wp.getDesc();
        this.question = wp.getQuestion();
        this.answer01 = wp.getAnswer01();
        this.answer2 = wp.getAnswer2();
        this.answer3 = wp.getAnswer3();
        this.answer4 = wp.getAnswer4();
        this.id = wp.getId();
        this.nextId = wp.getNextId();
    }

    public WayPoint(Location l, String id, String n, String desc,  String q, String a1, String a2, String a3, String a4, String nextId){
        super(l);
        this.name= n;
        this.question = q;
        this.answer01 = a1;
        this.answer2 = a2;
        this.answer3 = a3;
        this.answer4 = a4;
        this.desc = desc;
        this.id = id;
        this.nextId = nextId;

    }
    public WayPoint(Double latitude, Double longitude, String id, String n, String desc,  String q, String a1, String a2, String a3, String a4, String nextId){
        super("");
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.name= n;
        this.question = q;
        this.answer01 = a1;
        this.answer2 = a2;
        this.answer3 = a3;
        this.answer4 = a4;
        this.desc = desc;
        this.id = id;
        this.nextId = nextId;

    }


    public List<String> getAnswerList(){
        ArrayList<String> answers = new ArrayList<>();
        answers.add(answer01);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
        Collections.shuffle(answers);
        return answers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer01;
    }

    public void setAnswer1(String answer1) {
        this.answer01 = answer1;
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


    //TODO send answer request
    public boolean checkAnswer(String givenAnswer){
        return (givenAnswer.equals(answer01));
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAnswer01() {
        return answer01;
    }

    public void setAnswer01(String answer01) {
        this.answer01 = answer01;
    }


    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeString(question);
        parcel.writeString(answer01);
        parcel.writeString(answer2);
        parcel.writeString(answer3);
        parcel.writeString(answer4);
        parcel.writeString(nextId);

    }

   private WayPoint(Parcel in){
       super(Location.CREATOR.createFromParcel(in));
       this.id = in.readString();
       this.name = in.readString();
       this.desc = in.readString();
       this.question = in.readString();
       this.answer01 = in.readString();
       this.answer2 = in.readString();
       this.answer3 = in.readString();
       this.answer4 = in.readString();
       this.nextId = in.readString();
   }

    public static final Parcelable.Creator<WayPoint> CREATOR = new Parcelable.Creator<WayPoint>(){

        @Override
        public WayPoint createFromParcel(Parcel source) {
            return new WayPoint(source);
        }

        @Override
        public WayPoint[] newArray(int size) {
            return new WayPoint[size];
        }
    };

    @Override
    public String toString() {
        return "WayPoint{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", question='" + question + '\'' +
                ", answer01='" + answer01 + '\'' +
                ", answer2='" + answer2 + '\'' +
                ", answer3='" + answer3 + '\'' +
                ", answer4='" + answer4 + '\'' +
                ", long='" + getLongitude() + '\'' +
                ", lat='" + getLatitude() + '\'' +
                ", nextId='" + nextId + '\'' +
                '}';
    }


/*    public String toJSON(){
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObject value = factory.createObjectBuilder()
                .add("id", this.id)
                .add("name", this.name)
                .add("desc", this.desc)
                .add("answer01", this.answer01)
                .add("answer2", this.answer2)
                .add("answer3", this.answer3)
                .add("answer4", this.answer4)
                .add("nextID", this. nextId)
                .add("latitude", this.getLatitude())
                .add("longitude", this.getLongitude())
                .build();
        return value.toString();
    }*/
}
