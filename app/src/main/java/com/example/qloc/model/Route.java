package com.example.qloc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by uli on 17.04.15.
 */
public class Route {
    private String name;
    private String id;
    private String description;
    @JsonProperty("start_loc")
    private ArrayList<Double> location;
    private double distance;

    public Route() {
    }

    public Route(String name, String id, String description, double longitude, double latidue, double distance) {
        location = new ArrayList<>();
        this.name = name;
        this.id = id;
        this.description = description;
        this.location.add(longitude);
        this.location.add(latidue);
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Double> getLocation() {
        return location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
