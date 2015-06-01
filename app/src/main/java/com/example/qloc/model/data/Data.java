package com.example.qloc.model.data;

import android.location.Location;

import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.example.qloc.model.exceptions.ServerCommunicationException;

import java.util.List;

/**
 * Created by michael on 17.05.15.
 */
public interface Data {
    public List<WayPoint> getWayPointList(Location currentLocation) throws ServerCommunicationException;
    public WayPoint getNextWayPoint(String nextWayPointID) throws ServerCommunicationException;
    public boolean checkAnswer(String givenAnswer) throws ServerCommunicationException;
}
