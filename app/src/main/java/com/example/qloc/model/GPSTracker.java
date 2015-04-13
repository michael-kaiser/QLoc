package com.example.qloc.model;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public final class GPSTracker{

    private Context context;

    public GPSTracker(Context context) {
        this.context = context;
    }

    public Location getLocation(){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        String provider = locationManager.getBestProvider(criteria,false);
        Location location = locationManager.getLastKnownLocation(provider);
        return location;
    }

    public double getDistanceTo(Location to){
        Location from = getLocation();
        return from.distanceTo(to);
    }



}