package com.example.qloc.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.qloc.R;

/**
 * Created by michael on 18.04.15.
 * singleton class of a GPS-Tracker
 * tracks the users position and selects the GPS-service
 */
public final class GPSTracker extends DisableEnableGPSListener {

    private final String TAG = "GPSTracker";
    private Context context;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private static GPSTracker instance = null;
    private Location currentLocation;
    private boolean canGetLocation;

    private GPSTracker(Context context) {
        this.context = context;
    }

    public static GPSTracker getInstance(Context context){
        if(instance == null){
            instance = new GPSTracker(context);
            return instance;
        }else{
            instance.context = context;
            return instance;
        }
    }
    //TODO check if GPS is enabled otherwise use the Network-provider
    public void init(){
        super.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "got location manager");

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled && !isNetworkEnabled){
            Toast.makeText(this.context, R.string.no_location_service_available, Toast.LENGTH_LONG).show();
            currentProvider = NO_PROVIDER;
            this.canGetLocation = false;
        }
        else {

            this.canGetLocation = true;
            if (isNetworkEnabled) {
                currentLocation = null;
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DIST_CHANGE_FOR_UPDATES, this);
                Log.d(TAG, "Network");
                if (locationManager != null) {
                    currentLocation = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (currentLocation != null) {
                        currentLocation.setLatitude(currentLocation.getLatitude());
                        currentLocation.setLongitude(currentLocation.getLongitude());
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                currentLocation = null;
                if (currentLocation == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DIST_CHANGE_FOR_UPDATES, this);
                    Log.d(TAG, "GPS Enabled");
                    if (locationManager != null) {
                        currentLocation = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (currentLocation != null) {
                            currentLocation.setLatitude(currentLocation.getLatitude());
                            currentLocation.setLongitude(currentLocation.getLongitude());
                        }
                    }
                }
            }
        }
    }
    public Location getLocation(){
        return currentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void stopTracking(){
        locationManager.removeUpdates(listener);
    }

    public void startTracking(){
        if(!currentProvider.equals(NO_PROVIDER)) {
            locationManager.requestLocationUpdates(currentProvider, MIN_TIME_BW_UPDATES, MIN_DIST_CHANGE_FOR_UPDATES, listener);

        }
    }

    public void addListener(LocationListener list){
        locationManager.requestLocationUpdates(currentProvider, MIN_TIME_BW_UPDATES, MIN_DIST_CHANGE_FOR_UPDATES, list);

    }

    public void removeListener(LocationListener list){
        locationManager.removeUpdates(list);
    }

    public LocationManager getLocationManager(){
        return locationManager;
    }

}