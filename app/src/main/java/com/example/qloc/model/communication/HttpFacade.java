package com.example.qloc.model.communication;

import android.location.Location;

import com.example.qloc.controller.json_utils.JsonTool;
import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.model.WayPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 03.05.15.
 */
public class HttpFacade {

    private HttpConnection conn;
    private static HttpFacade instance = null;

    public static HttpFacade getInstance(){
        if(instance == null){
            return new HttpFacade();
        }else{
            return instance;
        }
    }

    private HttpFacade(){
        conn = HttpConnection.getInstance();
    }

    public List<WayPoint> getWayPointList(Location currentLocation){
        String answer = null;
        try {
            answer = conn.sendAndRecive(JsonTool.rangeQuery(currentLocation));
        } catch (IOException e) {
        }


        ArrayList<WayPoint> wpList = null;
        try {
            wpList = (MyLittleSerializer.JSONStringToRoutesList(answer)).toWayPointList();
        } catch (IOException e) {
        }

        return wpList;
    }

    public WayPoint getNextWayPoint(WayPoint currentWayPoint){
        String msg = null;
        WayPoint nextWayPoint = null;

        try {
            msg = JsonTool.requestNext(currentWayPoint.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            nextWayPoint = MyLittleSerializer.JSONStringToWayPoint(conn.sendAndRecive(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nextWayPoint;
    }

}
