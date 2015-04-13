package com.example.qloc.model;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.qloc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uli on 06.04.15.
 * just for testing
 * TODO this is in the end not needed
 *
 */
public class WayPointList {


    private static List<WayPoint> wayPointsList = new ArrayList<WayPoint>();

    private static Context context;

    static{
        /*WayPoint start = new WayPoint(new Location(""), "GoldenesDachl", "n nettes Dach", "Wer baute das Goldene Dachl", "Maximilan I.", "Max und Moritz", "Max Payne", "KlaxMAx");
        start.setLatitude(47.268646d);
        start.setLongitude(11.393268d);
        addToList(start);*/


    }


    public static void addToList(WayPoint wp){
        Log.d("here", wp.toString());
        wayPointsList.add(wp);
    }

    public static WayPoint getWaypoint(String name){
        List<WayPoint> wpl = new ArrayList<WayPoint>(wayPointsList);
        for(WayPoint el: wpl){
            if(el.getName().equals(name)) {
                return el;
            }
        }
        return null;
    }

    public static WayPoint getWaypointByName(String name){
        for(WayPoint el: wayPointsList){
            if(el.getName().equals(name)) {
                return el;
            }
        }
        return null;
    }

    public static WayPoint getWaypointById(String id){
        for(WayPoint el: wayPointsList){
            if(el.getId().equals(id)) {
                return el;
            }
        }
        return null;
    }

    public static WayPoint getDefaultElement(){
        for(WayPoint el: wayPointsList){
            if(el.getName().equals("GoldenesDachl")) {
                return el;
            }
        }
        return null;
    }


    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        WayPointList.context = context;
    }


    public static List<WayPoint> getWayPointsList() {
        return wayPointsList;
    }

    public static void setWayPointsList(ArrayList<WayPoint> wayPointsList) {
        WayPointList.wayPointsList = wayPointsList;
    }

    /**
     * adding dummy data and returning Iterable
     * @return Iterable with dummy data
     */
    public static Iterable<WayPoint> getIterable(){
        wayPointsList.clear();
        //goldenes Dachl, Tivoli, MediaMarkt, WestFriedhof, Uni, Technik
        double [] lats = {47.268646d, 47.2559848d, 47.2644675d, 47.2588401d, 47.2633235d, 47.2641882d};
        double [] longs = {11.393268d, 11.4113074d, 11.4297771d, 11.3859263d, 11.3838469d, 11.3458574d};
        String [] descs = context.getResources().getStringArray(R.array.descs);
        String [] locs = context.getResources().getStringArray(R.array.location);

        for(int i = 0; i < descs.length; i++) {
            WayPoint start = new WayPoint(new Location(""), i + "", locs[i], descs[i], "Was ist die Hauptstadt von Österreich?", "Wien", "Belgien", "Innsbruck", "der Weihnachtsmann");
            start.setLatitude(lats[i]);
            start.setLongitude(longs[i]);
            addToList(start);
            if (i == descs.length-1) {
                getWaypointById((i) + "").setNextId("finish");
                getWaypointById((i-1) + "").setNextId(i + "");
            } else if (i != 0) {
                getWaypointById((i - 1) + "").setNextId(i + "");
            }

        }
            return wayPointsList;

    }




}
