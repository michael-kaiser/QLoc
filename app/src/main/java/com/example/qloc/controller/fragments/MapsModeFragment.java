package com.example.qloc.controller.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qloc.R;
import com.example.qloc.location.DisableEnableGPSListener;
import com.example.qloc.model.communication.GoogleMapsCommunication;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsModeFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private CompassMapParent provider;
    private GoogleMapsCommunication googleMapsCommunication;
    private LocationListener listener;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_maps_mode_fragment, container,false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMapsCommunication = new GoogleMapsCommunication(googleMap);
        googleMap.setMyLocationEnabled(true);

        listener = new MyLocationListener();
        listener.onLocationChanged(provider.getTracker().getLocation());
        provider.getTracker().addListener(listener);
        googleMapsCommunication.drawRoute(getCurrentPositionFromTracker(), provider.getTarget());
        setMarker();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        googleMapsCommunication.drawRoute(getCurrentPositionFromTracker(), provider.getTarget());
        setMarker();
        provider.getTracker().addListener(listener);

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        googleMap.clear();
        provider.getTracker().removeListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        provider.getTracker().removeListener(listener);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.provider = (CompassMapParent)activity;
    }

    private void setMarker(){
        int bmpWidth = 70; //width of the icon
        float ratio = 1.26f; //the ratio between width and height of the icon
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inScaled = false;
        Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.wp3,options);
        source = Bitmap.createScaledBitmap(source,bmpWidth,(int)(bmpWidth*ratio),false);
        googleMap.addMarker(new MarkerOptions().position(provider.getTarget()).icon(BitmapDescriptorFactory.fromBitmap(source)));
    }

    private LatLng getCurrentPositionFromTracker(){
        return new LatLng(provider.getTracker().getLocation().getLatitude(),provider.getTracker().getLocation().getLongitude());
    }



    private class MyLocationListener extends DisableEnableGPSListener {


        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            LatLng latLng = new LatLng(lat, lon);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            Log.d(TAG, "location changed in fragment");
            googleMap.clear();
            googleMapsCommunication.drawRoute(getCurrentPositionFromTracker(), provider.getTarget());
            setMarker();
            provider.callListener(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }

}