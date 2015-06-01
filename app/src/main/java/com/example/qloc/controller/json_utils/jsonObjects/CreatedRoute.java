package com.example.qloc.controller.json_utils.jsonObjects;

import com.example.qloc.controller.json_utils.jsonObjects.WayPointHelper;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by uli on 11.05.15.
 */
public class CreatedRoute {
    private String name;
    private String description;

    @JsonProperty("waypoints")

    private ArrayList<WayPointHelper> waypoints;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<WayPointHelper> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<WayPointHelper> waypoints) {
        this.waypoints = waypoints;
    }
}
