package com.example.qloc.model;

/**
 * Created by uli on 17.04.15.
 */
public class Route {
    private String name;
    private String id;
    private String description;
    private double longitude;
    private double latidue;
    private double distance;

    public Route() {
    }

    public Route(String name, String id, String description, double longitude, double latidue, double distance) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.longitude = longitude;
        this.latidue = latidue;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatidue() {
        return latidue;
    }

    public void setLatidue(double latidue) {
        this.latidue = latidue;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
