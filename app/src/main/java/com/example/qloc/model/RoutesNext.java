package com.example.qloc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by uli on 26.04.15.
 */
public class RoutesNext {
    @JsonProperty("waypoint")
    private WayPointDataCont wp;

    public RoutesNext() {

    }
    public RoutesNext(WayPointDataCont wp) {
        this.wp = wp;
    }

    public WayPointDataCont getWp() {
        return wp;
    }

    public void setWp(WayPointDataCont wp) {
        this.wp = wp;
    }

    public WayPoint toWayPoint(){
        return this.getWp().toWayPoint();
    }
}
