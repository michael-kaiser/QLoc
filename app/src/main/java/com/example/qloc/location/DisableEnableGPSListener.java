package com.example.qloc.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by michael on 18.04.15.
 * Implements function for selection of Gps-services when the services are enabled/disabled
 * GPS is preferred, but when GPS returns no locations then network is used
 */
public abstract class DisableEnableGPSListener implements LocationListener{

    public static final String NO_PROVIDER = "no_provider";
    protected String currentProvider = LocationManager.NETWORK_PROVIDER;
    protected final String TAG = "DisEnGPS";
    protected LocationManager locationManager;
    protected final int MIN_TIME_BW_UPDATES = 1;
    protected final int MIN_DIST_CHANGE_FOR_UPDATES = 1;
    protected  LocationListener listener;

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "enabled: " + provider);
        if(provider.equals(LocationManager.GPS_PROVIDER)){
            if(!enableGPS()){
                if(enableNetwork()){
                    currentProvider = LocationManager.NETWORK_PROVIDER;
                }else{
                    currentProvider = NO_PROVIDER;
                }
            }else{
                currentProvider=LocationManager.GPS_PROVIDER;
            }
        }
        //only the network is enabled
        else if(provider.equals(LocationManager.NETWORK_PROVIDER) && !enableGPS()){
            if(enableNetwork()){
                currentProvider = LocationManager.NETWORK_PROVIDER;
            }else{
                currentProvider = NO_PROVIDER;
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG,"disabled: " + provider);
        if(provider.equals(LocationManager.GPS_PROVIDER) &&  locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            if(enableNetwork()){
                currentProvider = LocationManager.NETWORK_PROVIDER;
            }else{
                currentProvider = NO_PROVIDER;
            }
        }
        //only the network is enabled
        else if(provider.equals(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if(!enableGPS()){
                currentProvider = NO_PROVIDER;
            }else{
                currentProvider = LocationManager.GPS_PROVIDER;
            }
        }
    }

    public boolean enableNetwork(){
        Location location;
        Log.d(TAG,"network");
        if(locationManager != null){
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return (location != null);
        }
        return false;
    }

    public boolean enableGPS(){
        Location location;
        Log.d(TAG,"gps");
        if(locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return (location != null);

        }
        return false;
    }

    public void setListener(LocationListener listener){
        this.listener = listener;
    }

    public String getProvider(){
        return currentProvider;
    }

}
