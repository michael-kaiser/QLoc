package com.example.qloc.controller.activities;

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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qloc.R;
import com.example.qloc.controller.json_utils.JsonTool;
import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.controller.fragments.StatusFragment;
import com.example.qloc.model.BitMapWorkerTask;
import com.example.qloc.model.DisableEnableGPSListener;
import com.example.qloc.model.GPSTracker;
import com.example.qloc.model.WayPoint;
import com.example.qloc.model.communication.HttpConnection;

import java.io.IOException;

/**
 * This activity shows the compass and starts QuestionActivities when waypoints are reached
 * @author uli
 * TODO add compass
 */
public class Navigation_Activity_neu extends Activity {

    private ImageView compass;
    private TextView loc_name;
    private ImageView waypoint;
    private float currentDegree = 0f;
    private float currentDegreeWaypoint = 0f;
    private TextView distance;
    private ImageView background;
    private float degree;
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
    private HttpConnection conn = HttpConnection.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.pull_out_to_left);
        setContentView(R.layout.activity_navigation_new);
        background = (ImageView)findViewById(R.id.background_compass);
        loadBitmap(R.drawable.back_jpeg,background);
        distance = (TextView) findViewById(R.id.txt_distance);
        compass = (ImageView) findViewById(R.id.inner_compass);
        waypoint = (ImageView) findViewById(R.id.waypoint);
        loc_name = (TextView) findViewById(R.id.current_waypoint_name);
        degree =0;


        start = getWayPoint();
        loc_name.setText(start.getName());
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
                    azimut = (float) Math.toDegrees(orientation[0]);
                    degree = (int) Math.round(azimut);


                    float dir = calculateHeading();

                    RotateAnimation rotateAnimation = new RotateAnimation(
                            currentDegree, -azimut,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                    );
                    rotateAnimation.setDuration(250);
                    //rotationAnimation.setFillAfter(true);
                    compass.startAnimation(rotateAnimation);
                    currentDegree = -azimut;

                    RotateAnimation rotateAnimation2 = new RotateAnimation(
                            currentDegreeWaypoint, dir,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                    );
                    rotateAnimation2.setDuration(250);
                    //rotationAnimation.setFillAfter(true);
                    waypoint.startAnimation(rotateAnimation2);
                    currentDegreeWaypoint = dir;
                }

            }

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
            if(location.distanceTo(start)>=0 && location.distanceTo(start)<10 && !inQuestion) {
                inQuestion = true;
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra(KEY,start);
                startActivityForResult(intent, REQUEST_ID_NEXT);

            }
            distance.setText("dist: "+Float.toString(location.distanceTo(start)));
            calculateHeading();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }

    public float calculateHeading(){
        float helpDegree;
        while(tracker.getLocation()==null){
        }
        float bea = tracker.getLocation().bearingTo(start);
        float dir = bea;
        if (degree <0){
            helpDegree=360+degree;
        }else{
            helpDegree=degree;
        }
        if(bea<0){
            dir=360+bea;
        }
        dir = (helpDegree-dir);

        return dir;



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

        if(start != null) {
            tracker.startTracking();
            mSensorManager.registerListener(mysensor,accelerometer,1000);
            mSensorManager.registerListener(mysensor,magnetometer,1000);
            loc_name.setText(start.getName());
        }
    }

    private WayPoint getWayPoint(){
        WayPoint nextWaypoint = null;
        //The first time you get the next waypoint from the PlayGameActivity
        if(nextWaypointId.equals("unset")){
            nextWaypoint = getIntent().getParcelableExtra(PlayGameActivity.KEY);

        //otherwise request next from server
        }else{
            String temp = null;
            try {
                temp = JsonTool.requestNext(nextWaypointId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String temp2 = conn.sendAndRecive(temp);

            try {
                nextWaypoint = MyLittleSerializer.JSONStringToWayPoint(temp2);
            }catch(Exception e){
                nextWaypoint = null;
            }

            //there is no next waypoint
            if(nextWaypoint == null){
                           /*
            TODO make usefull stuff when over
             */
                Log.d(TAG,"finishing");
                tracker.stopTracking();
                mSensorManager.unregisterListener(mysensor,accelerometer);
                mSensorManager.unregisterListener(mysensor,magnetometer);
                this.finish();
                return null;
            }

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

    public void loadBitmap(int resId, ImageView imageView) {
        BitMapWorkerTask task = new BitMapWorkerTask(imageView,this);
        task.execute(resId);
    }

    public void remember(View v){
        Location s = tracker.getLocation();

        start.setLongitude(s.getLongitude());
        start.setLatitude(s.getLatitude());
        mylistener.onLocationChanged(s);

        Toast.makeText(Navigation_Activity_neu.this, ("Latitude: " + String.valueOf(s.getLatitude()) +
                "\nLongitude: " + String.valueOf(start.getLongitude())), Toast.LENGTH_LONG).show();

    }

}