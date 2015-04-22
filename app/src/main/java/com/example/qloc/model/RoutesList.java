package com.example.qloc.model;

import com.example.qloc.model.Route;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by uli on 17.04.15.
 */
public class RoutesList {
    @JsonProperty("routes")
    private static ArrayList<Route> routes = new ArrayList<>();

    public ArrayList<Route> getRoutes() {
        return routes;
    }
    public void addRoute(Route r){
        routes.add(r);
    }
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}
