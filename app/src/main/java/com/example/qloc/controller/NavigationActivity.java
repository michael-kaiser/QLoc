package com.example.qloc.controller;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qloc.R;
import com.example.qloc.model.DisableEnableGPSListener;
import com.example.qloc.model.GPSTracker;
import com.example.qloc.model.WayPoint;
import com.example.qloc.model.WayPointList;

/**
 * This activity shows the compass and starts QuestionActivities when waypoints are reached
 * @author uli
 * TODO add compass
 */
public class NavigationActivity extends Activity {

    private TextView latitude;
    private TextView longitude;
    private TextView bearing;
    private TextView direction;
    private TextView distance;
    private float degree;
    private TextView provText;
    private WayPoint start;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private MySensorEventListener mysensor;
    private SensorManager mSensorManager;
    private MyLocationListener mylistener;
    private final String TAG = "NavigationActivity";
    public static final String KEY = "Waypoint2";
    public static final int REQUEST_ID_NEXT = 1;
    private String nextWaypointId = "unset";
    private GPSTracker tracker = GPSTracker.getInstance(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_right,R.anim.pull_out_to_left);
        setContentView(R.layout.activity_navigation);
        latitude = (TextView) findViewById(R.id.lat);
        longitude = (TextView) findViewById(R.id.lon);
        bearing = (TextView) findViewById(R.id.bea);
        provText = (TextView) findViewById(R.id.prov);
        direction = (TextView) findViewById(R.id.goldach);
        distance = (TextView) findViewById(R.id.golddist);
        degree =0;


        start = getWayPoint();
        Log.d(TAG,start.getName());
        mylistener = new MyLocationListener();
        tracker.setListener(mylistener);
        tracker.init();
        Location location = tracker.getLocation();


        mysensor = new MySensorEventListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        /*Checks, if GPS is enabled*/
        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        mSensorManager.registerListener(mysensor, accelerometer, 1000);
        mSensorManager.registerListener(mysensor, magnetometer, 1000);

    }

    private class MySensorEventListener implements SensorEventListener {
        float[] mGravity;
        float[] mGeomagnetic;
        float azimut;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    azimut = orientation[0];
                    degree = (int) Math.round(Math.toDegrees(azimut));
                    bearing.setText("Heading: " + Float.toString(degree) + " degrees");


                    calculateHeading();
                }

            }

            bearing.setText("Heading: " + Float.toString(degree) + " degrees");
            calculateHeading();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
    private class MyLocationListener extends DisableEnableGPSListener {
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
            if(location.distanceTo(start)>0 && location.distanceTo(start)<120 && !inQuestion) {
                inQuestion = true;
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra(KEY,start);
                startActivityForResult(intent, REQUEST_ID_NEXT);

            }
            latitude.setText("Latitude: "+String.valueOf(location.getLatitude()));
            longitude.setText("Longitude: "+String.valueOf(location.getLongitude()));
            distance.setText("Distance to Start: "+Float.toString(location.distanceTo(start)));
            provText.setText(tracker.getProvider() + " provider has been selected.");
            calculateHeading();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }

    public void calculateHeading(){
        float helpDegree;
        direction = (TextView) findViewById(R.id.goldach);
        while(tracker.getLocation()==null){
            direction.setText("Richtung: ??");
        }
        float bea = tracker.getLocation().bearingTo(start);
        float dir =bea;
        if (degree <0){
            helpDegree=360+degree;
        }else{
            helpDegree=degree;
        }
        if(bea<0){
            dir=360+bea;
        }
        dir = (helpDegree-dir);
        direction.setText("Direction from North" + Float.toString(bea) +
                " \n Direction to Start" + Float.toString(dir));



    }

    public void remember(View v){
        Location s = tracker.getLocation();

        start.setLongitude(s.getLongitude());
        start.setLatitude(s.getLatitude());


        Toast.makeText(NavigationActivity.this, ("Latitude: " + String.valueOf(s.getLatitude()) +
                "\nLongitude: " + String.valueOf(start.getLongitude())), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mylistener.setInQuestion(true);
        tracker.stopTracking();
        mSensorManager.unregisterListener(mysensor,accelerometer);
        mSensorManager.unregisterListener(mysensor,magnetometer);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        start = getWayPoint();
        mylistener.setInQuestion(false);
        mSensorManager.registerListener(mysensor,accelerometer,1000);
        mSensorManager.registerListener(mysensor,magnetometer,1000);
        tracker.startTracking();
    }

    private WayPoint getWayPoint(){
        WayPoint nextWaypoint = null;
        if(nextWaypointId.equals("unset")){
            nextWaypoint = getIntent().getParcelableExtra(PlayGameActivity.KEY);
        }else if(nextWaypointId.equals("finish")){
            /*
            TODO make usefull stuff when over
             */
            Log.d(TAG,"finishing");
            tracker.stopTracking();
            mSensorManager.unregisterListener(mysensor,accelerometer);
            mSensorManager.unregisterListener(mysensor,magnetometer);
            this.finish();
        }else{
            nextWaypoint = WayPointList.getWaypointById(nextWaypointId);
        }
        if (nextWaypoint != null) {
            Log.d(TAG, "got waypoint: " + nextWaypoint.toString());
        }
        return nextWaypoint;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_ID_NEXT){
            if(resultCode == RESULT_OK){
                nextWaypointId = data.getStringExtra(StatusFragment.RETVAL_KEY);
                Log.d(TAG,"++++++++++++++++++++++++++++++++++++++nextwaypoint = " + nextWaypointId);
            }
        }
    }

}
