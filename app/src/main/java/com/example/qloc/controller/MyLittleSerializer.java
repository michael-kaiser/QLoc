package com.example.qloc.controller;

import com.example.qloc.model.RoutesList;
import com.example.qloc.model.WayPoint;
import com.example.qloc.model.WayPointDataCont;
//import com.example.qloc.model.WayPointList2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by uli on 17.04.15.
 */
public class MyLittleSerializer {

    private static final ObjectMapper OBJECT_MAPPER;
    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    public static String WayPointToJSON(WayPoint wp){
        WayPointDataCont cont = new WayPointDataCont(wp);
        String ret = null;
        try {
            ret = OBJECT_MAPPER.writeValueAsString(cont);
        } catch (JsonProcessingException e) {
            ret = null;
        }
        return ret;
    }
    public static WayPoint JSONStringToWayPoint(String jsonString){
        WayPointDataCont wpdc;
        try {
            wpdc = OBJECT_MAPPER.readValue(jsonString, WayPointDataCont.class);
        } catch (IOException e) {
            return null;
        }
        return wpdc.toWayPoint();
    }

    public static RoutesList JSONStringToRoutesList(String jsonString){
        RoutesList rl;
        try {
            rl = OBJECT_MAPPER.readValue(jsonString, RoutesList.class);
        } catch (IOException e) {
            return null;
        }
        return rl;
    }

    public static String RoutesListToJSONString(RoutesList rl){

        String ret = null;
        try {
            ret = OBJECT_MAPPER.writeValueAsString(rl);
        } catch (JsonProcessingException e) {
            ret = null;
        }
        return ret;
    }

}
