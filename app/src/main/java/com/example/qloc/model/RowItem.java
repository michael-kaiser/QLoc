package com.example.qloc.model;

import android.location.Location;

/**
 * A item of the ListView on the PlayGameActivity
 * Created by michael on 25.03.15.
 */
public class RowItem {
    private WayPoint waypoint;
    private  String distance;

    public RowItem(WayPoint wp, Location currentLocation){
        this.waypoint = wp;
        this.distance = String.valueOf(currentLocation.distanceTo(wp));
    }

    public RowItem(Route route, Location currentLocation){

    }
    public String getDesc() {
        return waypoint.getDesc();
    }

    public String getLocation() {
        return waypoint.getName();
    }
    public String getDistance() {
        return distance;
    }

    public WayPoint getWaypoint(){
        return waypoint;
    }




}
