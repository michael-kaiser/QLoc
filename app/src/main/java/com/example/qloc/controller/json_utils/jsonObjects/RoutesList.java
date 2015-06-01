package com.example.qloc.controller.json_utils.jsonObjects;

import android.location.Location;

import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by uli on 17.04.15.
 */
public class RoutesList {
    @JsonProperty("routes")
    private ArrayList<Route> routes;

    public RoutesList() {
        this.routes =  new ArrayList<Route>();

    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }
    public void addRoute(Route r){
        routes.add(r);
    }
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public ArrayList<WayPoint> toWayPointList(){
        ArrayList<WayPoint> wp = new ArrayList<>();
        for(Route r : routes){
            Location loc = new Location(" ");
            loc.setLatitude(r.getLocation().get(0));
            loc.setLongitude(r.getLocation().get(1));
            WayPoint temp = new WayPoint(loc, r.getId(), r.getName(), r.getDescription(), "dummy", "dummy", "dummy","dummy", "", r.getId());
            wp.add(temp);
        }
        return wp;
    }

}
