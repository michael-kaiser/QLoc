package com.example.qloc.model.data;

import android.location.Location;
import android.util.Log;

import com.example.qloc.controller.json_utils.JsonTool;
import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.example.qloc.model.exceptions.ServerCommunicationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 05.05.15.
 */
public class Mockup implements Data{
    private static final String TAG = "MOCKUP";
    private  List<WayPoint> wayPointList;
    private static Mockup instance;

    public static  Mockup getInstance(){
        if(instance == null){
            return new Mockup();
        }else{
            return instance;
        }
    }

    private Mockup(){
        wayPointList = new ArrayList<>();
        double [] lats = {47.268646d, 47.2559848d, 47.2644675d, 47.2588401d, 47.2633235d, 47.2641882d};
        double [] longs = {11.393268d, 11.4113074d, 11.4297771d, 11.3859263d, 11.3838469d, 11.3458574d};
        String [] locs = {"goldenes Dachl", "Tivoli", "Media Markt", "West Friedhof", "Hauptuni", "Technik"};
        String [] descs = {"n nettes Dachl", "Stadium", "medien und mehr", "tote Menschen", "Bücher", "ganz lustig"};

        for(int i = 0; i < descs.length; i++) {
            WayPoint start = new WayPoint(new Location(""), i + "", locs[i], descs[i], "Was ist die Hauptstadt von Österreich?", "Wien", "Belgien", "Innsbruck", "der Weihnachtsmann", "nexte");
            start.setLatitude(lats[i]);
            start.setLongitude(longs[i]);
            wayPointList.add(start);
        }
    }
    public List<WayPoint> getWayPointList(Location currentLocation) throws ServerCommunicationException{
        try {
            Log.d(TAG, JsonTool.rangeQuery(currentLocation).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wayPointList;
    }

    public WayPoint getNextWayPoint(String id) throws ServerCommunicationException{
        try {
            Log.d(TAG, JsonTool.requestNext(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wayPointList.get(0);
    }

    public boolean checkAnswer(String givenAnswer) throws ServerCommunicationException{
        Log.d(TAG, "answer: " + givenAnswer);
        return true;
    }

    @Override
    public boolean setPoints(int points) {
        return false;
    }


}
