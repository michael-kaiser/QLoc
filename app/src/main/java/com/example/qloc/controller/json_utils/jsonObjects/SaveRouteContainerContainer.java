package com.example.qloc.controller.json_utils.jsonObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by uli on 12.05.15.
 */
public class SaveRouteContainerContainer {
    @JsonProperty("route_create")
    SaveRouteContainer routes_create;

    public SaveRouteContainerContainer(SaveRouteContainer src) {
        this.routes_create = src;
    }


    public SaveRouteContainer getRoutes_create() {
        return routes_create;
    }


}
