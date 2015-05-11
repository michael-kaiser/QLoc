package com.example.qloc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by uli on 11.05.15.
 */
public class CreatedRoute {

    @JsonProperty("waypoints")
    private ArrayList<WayPoint> waypoints;
}
