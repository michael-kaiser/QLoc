package com.example.qloc.model;

import java.util.ArrayList;

/**
 * Created by uli on 24.04.15.
 */
public class RoutesList2 {
    private static ArrayList<Route> routes = new ArrayList<>();

    public RoutesList2(Route... rs) {
        Route[] liste = rs;
        for(int i =0; i<rs.length; i++) {
            routes.add(liste[i]);
        }
    }
}
