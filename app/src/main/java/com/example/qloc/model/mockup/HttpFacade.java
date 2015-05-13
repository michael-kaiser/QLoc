package com.example.qloc.model.mockup;

import android.location.Location;

import com.example.qloc.model.WayPoint;
import com.example.qloc.model.communication.HttpConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 04/05/2015.
 */
public class HttpFacade {
    private HttpConnection conn = null;
    private static HttpFacade instance = null;

    public static HttpFacade getInstance(){
        return null;
    }

    private HttpFacade(){
        conn = null;
    }

    public List<WayPoint> getWayPointList(Location currentLocation){
        WayPoint wp = new WayPoint(new Location(""), "GoldenRoof", "n nettes Dach", "Wer baute das Goldene Dachl", "Maximilan I.", "Max und Moritz", "Max Payne", "KlaxMAx");
        wp.setLatitude(47.268646d);
        wp.setLongitude(11.393268d);
        wp. setNextId("next");
        List<WayPoint> w =new ArrayList<WayPoint>();
        w.add(wp);

        return w;
    }

    public WayPoint getNextWayPoint(WayPoint currentWayPoint){
        WayPoint wp = new WayPoint(new Location(""), "GoldenRoof", "n nettes Dach", "Wer baute das Goldene Dachl", "Maximilan I.", "Max und Moritz", "Max Payne", "KlaxMAx");
        wp.setLatitude(47.268646d);
        wp.setLongitude(11.393268d);
        wp. setNextId("next");
        return wp;
    }
}
