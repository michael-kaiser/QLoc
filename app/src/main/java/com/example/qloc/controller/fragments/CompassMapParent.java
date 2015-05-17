package com.example.qloc.controller.fragments;

import android.location.Location;

import com.example.qloc.location.GPSTracker;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by michael on 17.05.15.
 */
public interface CompassMapParent {
    public float calculateHeading();
    public void callListener(Location location);
    public GPSTracker getTracker();
    public LatLng getTarget();
}
