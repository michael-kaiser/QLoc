package com.example.qloc.model;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11.05.15.
 */
public class SaveRoute {

    private String name;
    private String description;
    private List<ServerWayPoint> wayPointList = new ArrayList<>();

    public SaveRoute(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public void setWayPointList(List<ServerWayPoint> wayPointList) {
        this.wayPointList = wayPointList;
    }


    @Override
    public String toString() {
        return "SaveRoute{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", wayPointList=" + wayPointList +
                '}';
    }

    //Location of route is location of first waypoint
    public Location getLocation(){
        return wayPointList.get(0).getLocation();
    }

    public int size(){
        return wayPointList.size();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ServerWayPoint> getWayPointList() {
        return wayPointList;
    }
}
