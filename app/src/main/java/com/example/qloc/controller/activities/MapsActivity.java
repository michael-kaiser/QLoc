package com.example.qloc.controller.activities;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qloc.R;
import com.example.qloc.controller.activities.activityUtils.SaveRoute;
import com.example.qloc.controller.activities.activityUtils.ServerWayPoint;
import com.example.qloc.controller.dialogs.AlertDialogUtility;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Activity for creating new routes
 * @author michael
 * TODO logic for saving the paths
 */
    public class MapsActivity extends FragmentActivity implements LocationListener, GoogleMap.OnMarkerClickListener{

    private enum State
    { NORMAL, CHANGE_ORDER_MODE, EDIT_MODE, DELETE_MODE}

    private final String TAG = "MapsActivity";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Criteria criteria;
    private Location currentLocation;
    private List<ServerWayPoint> waypointList = new LinkedList<>();
    private HttpFacade facade = HttpFacade.getInstance();
    private State state = State.NORMAL;
    private int selected = 0;
    private Marker currentMarker;
    private ArrayList<Marker> markers = new ArrayList<>();


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

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                ServerWayPoint current = getWaypointByDescription(deleteOrderNumber(marker.getTitle().toString()));
                double lat = marker.getPosition().latitude;
                double lon = marker.getPosition().longitude;
                current.setLocation(lat, lon);
            }
        });


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
        m.setDraggable(true);
        markers.add(m);

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
        if(state == State.CHANGE_ORDER_MODE){
            return;
        }
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
                if(!IsEverythingSetInCreateQuestionDialog(dialog)){
                    return;
                }
                // save waypoint
                Location loc = new Location("");
                loc.setLatitude(currentLocation.getLatitude());
                loc.setLongitude(currentLocation.getLongitude());
                ServerWayPoint wp = new ServerWayPoint(correct.getText().toString(), new String[]{wrong1.getText().toString(),wrong2.getText().toString(),wrong3.getText().toString()}, loc,description.getText().toString(), question.getText().toString());
                saveWaypoint(wp);


                setMarker(waypointList.indexOf(wp) + " " + description.getText().toString());
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

        final Activity currentActivity = this;
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

                if(!IsEverythingSetInSaveRouteDialog(dialog)){
                    return;
                }

                String routeName = name.getText().toString();
                String routeDesc = description.getText().toString();
                SaveRoute route = new SaveRoute(routeName,routeDesc);
                route.setWayPointList(waypointList);
                try {
                    facade.saveRoute(route);
                    //Todo delete
                    for(ServerWayPoint wp : route.getWayPointList()){
                        Log.d(TAG, wp.getHint());
                    }
                    Toast.makeText(currentActivity, "Route saved successfully", Toast.LENGTH_SHORT).show();
                } catch (ServerCommunicationException e) {
                    AlertDialogUtility.showAlertDialog(currentActivity, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, "Can't save route!");
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

    private void setMarkerIcon(Marker m, int drawable){
        int bmpWidth = 70; //width of the icon
        float ratio = 1.26f; //the ratio between width and height of the icon
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap source = BitmapFactory.decodeResource(getResources(), drawable,options);
        source = Bitmap.createScaledBitmap(source,bmpWidth,(int)(bmpWidth*ratio),false);
        m.setIcon(BitmapDescriptorFactory.fromBitmap(source));
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(state == State.DELETE_MODE){
            ServerWayPoint current = getWaypointByDescription(deleteOrderNumber(marker.getTitle().toString()));
            waypointList.remove(current);
            markers.remove(marker);
            marker.remove();
            for(Marker m : markers){
                ServerWayPoint wp = getWaypointByDescription(deleteOrderNumber(m.getTitle().toString()));
                int index = waypointList.indexOf(wp);
                m.setTitle(index + " " + wp.getHint());
            }
        }
        if(state == State.CHANGE_ORDER_MODE){
            Log.d(TAG, "change order mode");
            marker.showInfoWindow();
            if(selected == 0){
                selected++;
                currentMarker = marker;
                setMarkerIcon(currentMarker, R.drawable.wp3_checked);

            }
            else if(selected == 1){
                ServerWayPoint wp1 = getWaypointByDescription(deleteOrderNumber(currentMarker.getTitle().toString()));
                int index1 = waypointList.indexOf(wp1);
                int index2 = waypointList.indexOf(getWaypointByDescription(deleteOrderNumber(marker.getTitle().toString())));
                waypointList.remove(index1);
                waypointList.add(index1, getWaypointByDescription(deleteOrderNumber(marker.getTitle().toString())));
                waypointList.remove(index2);
                waypointList.add(index2, wp1);
                marker.setTitle(index1 + " " + getWaypointByDescription(deleteOrderNumber(marker.getTitle().toString())).getHint());
                currentMarker.setTitle(index2 + " " + wp1.getHint());
                selected = 0;
                setMarkerIcon(currentMarker, R.drawable.wp3);
                currentMarker = null;
                marker.showInfoWindow();
            }
            return true;
        }

        else if(state == State.EDIT_MODE) {
            Log.d(TAG, "edit mode");
            final ServerWayPoint current = getWaypointByDescription(deleteOrderNumber(marker.getTitle().toString()));


            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.create_question_dialog);
            final EditText description = (EditText) dialog.findViewById(R.id.dialog_description);
            final EditText question = (EditText) dialog.findViewById(R.id.dialog_question);
            final EditText correct = (EditText) dialog.findViewById(R.id.dialog_correct_answer);
            final EditText wrong1 = (EditText) dialog.findViewById(R.id.dialog_wrong_answer1);
            final EditText wrong2 = (EditText) dialog.findViewById(R.id.dialog_wrong_answer2);
            final EditText wrong3 = (EditText) dialog.findViewById(R.id.dialog_wrong_answer3);

            //setting the data
            description.setText(current.getHint());
            question.setText(current.getQuestion());
            correct.setText(current.getAnswerTrue());
            wrong1.setText(current.getFalseAnswers()[0]);
            wrong2.setText(current.getFalseAnswers()[1]);
            wrong3.setText(current.getFalseAnswers()[2]);

            Button dialogButtonSave = (Button) dialog.findViewById(R.id.button_save_question);
            // if button is clicked, close the custom dialog
            dialogButtonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View w) {
                    if (!IsEverythingSetInCreateQuestionDialog(dialog)) {
                        return;
                    }
                    // save waypoint
                    current.setHint(description.getText().toString());
                    current.setAnswerTrue(correct.getText().toString());
                    current.setFalseAnswers(wrong1.getText().toString(), wrong2.getText().toString(), wrong3.getText().toString());
                    current.setQuestion(question.getText().toString());
                    marker.hideInfoWindow();
                    marker.setTitle(waypointList.indexOf(getWaypointByDescription(description.getText().toString())) + " " + description.getText().toString());
                    marker.showInfoWindow();
                    dialog.dismiss();
                    Log.d(TAG, waypointList.size() + " size");
                }
            });

            Button dialogButtonCancel = (Button) dialog.findViewById(R.id.button_dismiss_question);

            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        else{
            marker.showInfoWindow();
        }

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

    public ServerWayPoint getWaypointByDescription(String description){
        for(ServerWayPoint s : waypointList){
            if(s.getHint().equals(description)){
                return s;
            }
        }
        return null;
    }

    public boolean IsEverythingSetInCreateQuestionDialog(Dialog d){
        String desc = ((EditText)d.findViewById(R.id.dialog_description)).getText().toString();
        String question = ((EditText)d.findViewById(R.id.dialog_question)).getText().toString();
        String correct = ((EditText)d.findViewById(R.id.dialog_correct_answer)).getText().toString();
        String wrong1 = ((EditText)d.findViewById(R.id.dialog_wrong_answer1)).getText().toString();
        String wrong2 = ((EditText)d.findViewById(R.id.dialog_wrong_answer2)).getText().toString();
        String wrong3 = ((EditText)d.findViewById(R.id.dialog_wrong_answer3)).getText().toString();

        boolean noDesc = desc.isEmpty();
        boolean noQuestion = question.isEmpty();
        boolean noCorrect = correct.isEmpty();
        boolean noWrong1 = wrong1.isEmpty();
        boolean noWrong2 = wrong2.isEmpty();
        boolean noWrong3 = wrong3.isEmpty();

        if(noDesc){
            d.findViewById(R.id.dialog_description).setBackground(getResources().getDrawable(R.drawable.wrong_answer));
        }else{
            d.findViewById(R.id.dialog_description).setBackground(getResources().getDrawable(R.drawable.item_background));
        }
        if(noQuestion){
            d.findViewById(R.id.question).setBackground(getResources().getDrawable(R.drawable.wrong_answer));
        }else{
            d.findViewById(R.id.question).setBackground(getResources().getDrawable(R.drawable.item_background));
        }
        if(noCorrect){
            d.findViewById(R.id.answer1).setBackground(getResources().getDrawable(R.drawable.wrong_answer));
        }else{
            d.findViewById(R.id.answer1).setBackground(getResources().getDrawable(R.drawable.item_background));
        }
        if(noWrong1){
            d.findViewById(R.id.answer2).setBackground(getResources().getDrawable(R.drawable.wrong_answer));
        }else{
            d.findViewById(R.id.answer2).setBackground(getResources().getDrawable(R.drawable.item_background));
        }
        if(noWrong2){
            d.findViewById(R.id.answer3).setBackground(getResources().getDrawable(R.drawable.wrong_answer));
        }else{
            d.findViewById(R.id.answer3).setBackground(getResources().getDrawable(R.drawable.item_background));
        }
        if(noWrong3){
            d.findViewById(R.id.answer4).setBackground(getResources().getDrawable(R.drawable.wrong_answer));
        }else{
            d.findViewById(R.id.answer4).setBackground(getResources().getDrawable(R.drawable.item_background));
        }

        if(noDesc || noQuestion || noCorrect || noWrong1 || noWrong2 || noWrong3){
            return false;
        }else{
            return true;
        }

    }

    public boolean IsEverythingSetInSaveRouteDialog(Dialog d){
        String name = ((EditText)d.findViewById(R.id.dialog_save_name)).getText().toString();
        String desc = ((EditText)d.findViewById(R.id.dialog_save_desc)).getText().toString();


        boolean noDesc = desc.isEmpty();
        boolean noName = name.isEmpty();


        if(noDesc){
            d.findViewById(R.id.dialog_save_desc).setBackground(getResources().getDrawable(R.drawable.wrong_answer));
        }else{
            d.findViewById(R.id.dialog_save_desc).setBackground(getResources().getDrawable(R.drawable.item_background));
        }
        if(noName){
            d.findViewById(R.id.dialog_save_name).setBackground(getResources().getDrawable(R.drawable.wrong_answer));
        }else {
            d.findViewById(R.id.dialog_save_name).setBackground(getResources().getDrawable(R.drawable.item_background));
        }

        if(noDesc || noName){
            return false;
        }else{
            return true;
        }

    }


    private String deleteOrderNumber(String name){
        String [] arr = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < arr.length; i++){
            if(i == arr.length - 1){
                sb.append(arr[i]);
            }else {
                sb.append(arr[i] + " ");
            }
        }
        return sb.toString();
    }

    public void activateSwapMode(View v){
        if(state == State.NORMAL){
            ((ImageButton)this.findViewById(R.id.btnSwapWp)).setImageResource(R.drawable.swap_wp);
            state = State.CHANGE_ORDER_MODE;
        }
        else if(state == State.EDIT_MODE){
            ((ImageButton)this.findViewById(R.id.btnEditWp)).setImageResource(R.drawable.edit_wp_grey);
            ((ImageButton)this.findViewById(R.id.btnSwapWp)).setImageResource(R.drawable.swap_wp);
            state = State.CHANGE_ORDER_MODE;
        }
        else if(state == State.DELETE_MODE){
            ((ImageButton)this.findViewById(R.id.btnDeleteWp)).setImageResource(R.drawable.delete_wp_grey);
            ((ImageButton)this.findViewById(R.id.btnSwapWp)).setImageResource(R.drawable.swap_wp);
            state = State.CHANGE_ORDER_MODE;
        }else {
            ((ImageButton)this.findViewById(R.id.btnSwapWp)).setImageResource(R.drawable.swap_wp_grey);
            selected = 0;
            state = State.NORMAL;
        }
    }

    public void activateDeleteMode(View v){
        if(state == State.NORMAL){
            ((ImageButton)this.findViewById(R.id.btnDeleteWp)).setImageResource(R.drawable.delete_wp);
            state = State.DELETE_MODE;
        }
        else if(state == State.EDIT_MODE){
            ((ImageButton)this.findViewById(R.id.btnEditWp)).setImageResource(R.drawable.edit_wp_grey);
            ((ImageButton)this.findViewById(R.id.btnDeleteWp)).setImageResource(R.drawable.delete_wp);
            state = State.DELETE_MODE;
        }
        else if(state == State.CHANGE_ORDER_MODE) {
            ((ImageButton) this.findViewById(R.id.btnSwapWp)).setImageResource(R.drawable.swap_wp_grey);
            ((ImageButton) this.findViewById(R.id.btnDeleteWp)).setImageResource(R.drawable.delete_wp);
            state = State.DELETE_MODE;
        }else {
            ((ImageButton)this.findViewById(R.id.btnDeleteWp)).setImageResource(R.drawable.delete_wp_grey);
            selected = 0;
            state = State.NORMAL;
        }
    }

    public void activateEditMode(View v){
        if(state == State.NORMAL){
            ((ImageButton)this.findViewById(R.id.btnEditWp)).setImageResource(R.drawable.edit_wp);
            state = State.EDIT_MODE;
        }
        else if(state == State.CHANGE_ORDER_MODE){
            ((ImageButton)this.findViewById(R.id.btnEditWp)).setImageResource(R.drawable.edit_wp);
            ((ImageButton)this.findViewById(R.id.btnSwapWp)).setImageResource(R.drawable.swap_wp_grey);
            state = State.EDIT_MODE;
        }
        else if(state == State.DELETE_MODE){
            ((ImageButton)this.findViewById(R.id.btnEditWp)).setImageResource(R.drawable.edit_wp);
            ((ImageButton)this.findViewById(R.id.btnDeleteWp)).setImageResource(R.drawable.delete_wp_grey);
            state = State.EDIT_MODE;
        }else {
            ((ImageButton)this.findViewById(R.id.btnEditWp)).setImageResource(R.drawable.edit_wp_grey);
            state = State.NORMAL;
        }
    }

}
