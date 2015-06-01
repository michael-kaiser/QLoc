package com.example.qloc.controller.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.qloc.R;
import com.example.qloc.controller.activities.activityUtils.SaveRoute;
import com.example.qloc.controller.activities.activityUtils.ServerWayPoint;
import com.example.qloc.model.data.HttpFacade;
import com.example.qloc.model.exceptions.ServerCommunicationException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for creating new routes
 * @author michael
 * TODO logic for saving the paths
 */
public class MapsActivity extends FragmentActivity implements LocationListener, GoogleMap.OnMarkerClickListener{

    private final String TAG = "MapsActivity";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Criteria criteria;
    private Location currentLocation;
    private List<ServerWayPoint> waypointList = new ArrayList<>();
    private HttpFacade facade = HttpFacade.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_right,R.anim.pull_out_to_left);
        setContentView(R.layout.activity_maps);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setUpMapIfNeeded();
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

        currentLocation = locationManager.getLastKnownLocation(provider);

        if(currentLocation != null){
            onLocationChanged(currentLocation);
        }else{
            Log.d(TAG,"location is null");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
       // mMap.addMarker(new MarkerOptions().position(new LatLng(locListener.getLatitude(), locListener.getLongitude())).title("M"));
    }

    private void setMarker(String name){
        int bmpWidth = 70; //width of the icon
        float ratio = 1.26f; //the ratio between width and height of the icon
        BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inScaled = false;
        Bitmap source = BitmapFactory.decodeResource(getResources(),R.drawable.wp3,options);
        source = Bitmap.createScaledBitmap(source,bmpWidth,(int)(bmpWidth*ratio),false);


        Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title(name).icon(BitmapDescriptorFactory.fromBitmap(source)));
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        this.currentLocation = location;

        LatLng latLng = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Log.d(TAG,"location changed");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void startQuestionDialog(final View v){
        final float alpha = v.getAlpha();
        v.setAlpha(1.0f);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_question_dialog);
        final EditText description = (EditText) dialog.findViewById(R.id.dialog_description);
        final EditText question = (EditText) dialog.findViewById(R.id.dialog_question);
        final EditText correct = (EditText) dialog.findViewById(R.id.dialog_correct_answer);
        final EditText wrong1 = (EditText) dialog.findViewById(R.id.dialog_wrong_answer1);
        final EditText wrong2 = (EditText) dialog.findViewById(R.id.dialog_wrong_answer2);
        final EditText wrong3 = (EditText) dialog.findViewById(R.id.dialog_wrong_answer3);


        /* set alpha of button to origin again */
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                v.setAlpha(alpha);
            }
        });



        Button dialogButtonSave = (Button) dialog.findViewById(R.id.button_save_question);
        // if button is clicked, close the custom dialog
        dialogButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                // save waypoint
                Location loc = new Location("");
                loc.setLatitude(currentLocation.getLatitude());
                loc.setLongitude(currentLocation.getLongitude());
                ServerWayPoint wp = new ServerWayPoint(correct.getText().toString(), new String[]{wrong1.getText().toString(),wrong2.getText().toString(),wrong3.getText().toString()}, loc,description.getText().toString(), question.getText().toString());
                saveWaypoint(wp);


                setMarker(description.getText().toString());
                dialog.dismiss();
                Log.d(TAG,waypointList.size() + " size");
            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.button_dismiss_question);

        dialogButtonCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void startSaveRouteDialog(final View v){
        final float alpha = v.getAlpha();
        v.setAlpha(1.0f);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.save_route_dialog);
        final EditText description = (EditText) dialog.findViewById(R.id.dialog_save_desc);
        final EditText name = (EditText) dialog.findViewById(R.id.dialog_save_name);

        /* set alpha of button to origin again */
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                v.setAlpha(alpha);
            }
        });



        Button dialogButtonSave = (Button) dialog.findViewById(R.id.button_save_route);
        // if button is clicked, close the custom dialog
        dialogButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                /* TODO save the route */
                String routeName = name.getText().toString();
                String routeDesc = name.getText().toString();
                SaveRoute route = new SaveRoute(routeName,routeDesc);
                route.setWayPointList(waypointList);
                try {
                    facade.saveRoute(route);
                } catch (ServerCommunicationException e) {
                    //TODO make dialog
                    e.printStackTrace();
                }
                reset();
                dialog.dismiss();
            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.button_save_cancel);

        dialogButtonCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "marker clicked");
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.pull_out_to_right);
    }

    public void saveWaypoint(ServerWayPoint wp){
        waypointList.add(wp);
    }

    public void reset(){
        mMap.clear();
        waypointList.clear();
        Log.d(TAG, waypointList.size() + " ");
    }




}
