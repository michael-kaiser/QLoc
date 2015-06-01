package com.example.qloc.controller.json_utils.jsonObjects;

import com.example.qloc.controller.activities.activityUtils.SaveRoute;
import com.example.qloc.controller.activities.activityUtils.ServerWayPoint;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uli on 12.05.15.
 */
public class SaveRouteContainer {
    private String name;
    private String description;
    @JsonProperty("waypoints")
    private List<ServerWayPointContainer> wayPointList = new ArrayList<>();

    public SaveRouteContainer(SaveRoute route) {
        this.name = route.getName();
        this.description = route.getDescription();
        for(ServerWayPoint swp: route.getWayPointList()){
            wayPointList.add(new ServerWayPointContainer(swp));
        }
    }

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

    public List<ServerWayPointContainer> getWayPointList() {
        return wayPointList;
    }

    public void setWayPointList(List<ServerWayPointContainer> wayPointList) {
        this.wayPointList = wayPointList;
    }

    public SaveRouteContainerContainer prepareJson(){
        return new SaveRouteContainerContainer(this);
    }
}
