package com.example.qloc.controller.json_utils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by uli on 12.05.15.
 */
public class SaveRouteContainer2 {
    @JsonProperty("route_create")
    SaveRouteContainer routes_create;

    public SaveRouteContainer2(SaveRouteContainer src) {
        this.routes_create = src;
    }


    public SaveRouteContainer getRoutes_create() {
        return routes_create;
    }


}
