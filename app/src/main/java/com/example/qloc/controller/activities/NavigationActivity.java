package com.example.qloc.controller.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qloc.R;
import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.example.qloc.controller.dialogs.AlertDialogUtility;
import com.example.qloc.controller.fragments.CompassFragment;
import com.example.qloc.controller.fragments.CompassMapParent;
import com.example.qloc.controller.fragments.MapsModeFragment;
import com.example.qloc.controller.fragments.StatusFragment;
import com.example.qloc.location.GPSTracker;
import com.example.qloc.model.data.Data;
import com.example.qloc.model.data.HttpFacade;
import com.example.qloc.model.exceptions.ServerCommunicationException;
import com.google.android.gms.maps.model.LatLng;

/**
 * This activity shows the compass and starts QuestionActivities when waypoints are reached
 * initially a compass is shown
 * There is the possibility to change to the map fragment
 * @author uli,michael,alex
 */
public class NavigationActivity extends Activity implements CompassMapParent {

    private final String TAG = "NavigationActivity";
    public static final String KEY = "Waypoint2";
    public static final String KEY_POINTS = "Key_points";
    public static final String KEY_WPCOUNT = "WpCount";
    public static final int REQUEST_ID_NEXT = 1;
    private int points = 0;
    private int waypointCounter = 0;

    private enum activeFragments{
        COMPASS, MAPS;
    }

    private ImageButton modeButton;
    private TextView loc_name;
    private TextView distance;
    private TextView pointsTxt;

    private activeFragments activeFragment = activeFragments.COMPASS;
    private WayPoint start;
    private MyLocationListener mylistener;
    private String nextWaypointId = "unset";
    private GPSTracker tracker = GPSTracker.getInstance(this);
    private Data facade = HttpFacade.getInstance();
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.pull_in_from_right, R.anim.pull_out_to_left);
        setContentView(R.layout.activity_navigation_new2);

        modeButton = (ImageButton) findViewById(R.id.modeButton);
        distance = (TextView) findViewById(R.id.txt_distance);
        pointsTxt = (TextView) findViewById(R.id.txt_points);
        pointsTxt.setText("points: " + points);

        loc_name = (TextView) findViewById(R.id.current_waypoint_name);

        start = getWayPoint();

        loc_name.setText(start.getDesc());
        mylistener = new MyLocationListener();
        tracker.setListener(mylistener);
        tracker.init();
        Location location = tracker.getLocation();

        /*Checks, if GPS is enabled*/
        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

         /* add the question fragment */
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.compass_fragment_layout, new CompassFragment());
        ft.commit();


    }

    @Override
    protected void onStop() {
        super.onStop();
        mylistener.setInQuestion(true);
        tracker.stopTracking();


    }

    @Override
    protected void onResume() {
        super.onResume();
        showModeButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showModeButton();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        start = getWayPoint();
        mylistener.setInQuestion(false);

        if(start != null) {
            tracker.startTracking();

            loc_name.setText(start.getName());
        }else{
            fm.beginTransaction().
            remove(fm.findFragmentById(R.id.compass_fragment_layout)).commit();
            Intent i = new Intent(this, FinalStatus.class);
            i.putExtra(KEY_POINTS, points);
            i.putExtra(KEY_WPCOUNT, waypointCounter);
            startActivity(i);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_ID_NEXT){
            if(resultCode == RESULT_OK){
                nextWaypointId = data.getStringExtra(StatusFragment.RETVAL_KEY);
                Log.d(TAG,"++++++++++++++++++++++++++++++++++++++nextwaypoint = " + nextWaypointId);
                points += data.getIntExtra(StatusFragment.RETVAL_KEY_POINTS,0);
                pointsTxt.setText("points: " + points);
                Log.d(TAG,"points = " + points);
                waypointCounter++;
            }
        }
    }

    @Override
    public GPSTracker getTracker() {
        return tracker;
    }


    @Override
    public float calculateHeading(){
        float bea = tracker.getLocation().bearingTo(start);
        return bea;
    }

    @Override
    public LatLng getTarget(){
        double lat = start.getLatitude();
        double lon = start.getLongitude();
        return new LatLng(lat,lon);
    }


    /**
     * returns the current Waypoint
     * Communicates with the server
     * @return the current waypoint
     */
    private WayPoint getWayPoint(){
        final Activity parent = this;
        WayPoint nextWaypoint = null;
        //The first time you get the next waypoint from the PlayGameActivity
        if(nextWaypointId.equals("unset")){
            nextWaypoint = getIntent().getParcelableExtra(PlayGameActivity.KEY);

        //otherwise request next from server
        }else{
            try {
                nextWaypoint = facade.getNextWayPoint(nextWaypointId);
            } catch (ServerCommunicationException e) {
                e.printStackTrace();
                AlertDialogUtility.showAlertDialog(this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(parent, MainScreen.class);
                        startActivity(i);
                    }
                }, "Can't connect to Server!");
            }

        }
        if (nextWaypoint != null) {
            Log.d(TAG, "got waypoint: " + nextWaypoint.toString());
        }
        return nextWaypoint;
    }

    /**
     * sets the target location to the currentLocation
     * needed for debugging
     * @param v
     */
    public void remember(View v){
        Location s = tracker.getLocation();

        start.setLongitude(s.getLongitude());
        start.setLatitude(s.getLatitude());
        mylistener.onLocationChanged(s);

        Toast.makeText(NavigationActivity.this, ("Latitude: " + String.valueOf(s.getLatitude()) +
                "\nLongitude: " + String.valueOf(start.getLongitude())), Toast.LENGTH_LONG).show();

    }

    /**
     * changes the Fragment to either map or compass
     * @param v
     */
    public void mapsMode(View v){
        setModeButtonAnimation();
        Fragment mode;
        if(activeFragment == activeFragments.COMPASS) {
            mode = new MapsModeFragment();
            activeFragment = activeFragments.MAPS;
        }else{
            mode = new CompassFragment();
            activeFragment = activeFragments.COMPASS;
        }
        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.animator.pull_in_from_right, R.animator.pull_out_to_left);
        ft.replace(R.id.compass_fragment_layout, mode);
        ft.commit();

    }

    @Override
    public void callListener(Location location){
        mylistener.onLocationChanged(location);
    }

    /**
     * shows the mode-button depending on the current fragment
     */
    private void showModeButton(){
        if(activeFragment == activeFragments.COMPASS){
            modeButton.setImageResource(R.drawable.map_icon);
        }else{
            modeButton.setImageResource(R.drawable.compass_icon);
        }
    }

    /**
     * animation of the mode button
     */
    private void setModeButtonAnimation(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(modeButton,"translationX", -200f);
        anim.setDuration(100);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(modeButton,"translationX", 0f);
        anim2.setDuration(100);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showModeButton();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(anim).before(anim2);
        set.start();

    }




    private class MyLocationListener implements LocationListener{

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        /*variable to check i the user is in a QuestionActivity then he shouldn't get the question again
                   the variable is controlled by the onStop() and onRestart() of the Activity
                 */
        private boolean inQuestion = false;

        public void setInQuestion(boolean inQuestion) {
            this.inQuestion = inQuestion;
        }

        @Override
        public void onLocationChanged(Location location) {
            // Initialize the location fields

            //Das ist natürlich Unsinn, aber es dient nur Testzwecken
            //float near;
            if(location.distanceTo(start)>=0 && location.distanceTo(start)<10 && !inQuestion) {
                inQuestion = true;
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra(KEY,start);
                startActivityForResult(intent, REQUEST_ID_NEXT);

            }
            Log.d(TAG, "location changed in activity");
            distance.setText("dist: "+Float.toString(location.distanceTo(start)));
            calculateHeading();



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }


}