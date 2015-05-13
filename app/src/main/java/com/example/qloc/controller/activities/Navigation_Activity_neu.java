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
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qloc.R;
import com.example.qloc.controller.fragments.StatusFragment;
import com.example.qloc.model.BitMapWorkerTask;
import com.example.qloc.model.DisableEnableGPSListener;
import com.example.qloc.model.GPSTracker;
import com.example.qloc.model.WayPoint;
import com.example.qloc.model.communication.HttpFacade;
import com.example.qloc.model.exceptions.ServerCommunicationException;

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
    private HttpFacade facade = HttpFacade.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.pull_out_to_left);
        setContentView(R.layout.activity_navigation_new);
        facade = HttpFacade.getInstance();
        background = (ImageView)findViewById(R.id.background_compass);
        loadBitmap(R.drawable.back_jpeg,background);
        distance = (TextView) findViewById(R.id.txt_distance);
        compass = (ImageView) findViewById(R.id.inner_compass);
        waypoint = (ImageView) findViewById(R.id.waypoint);
        loc_name = (TextView) findViewById(R.id.current_waypoint_name);
        degree =0;


        start = getWayPoint();
        loc_name.setText(start.getDesc());
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
                    float goalDegree;
                    RotateAnimation rotateAnimation = new RotateAnimation(
                            currentDegree, -azimut,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                    );
                    rotateAnimation.reset();
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    rotateAnimation.setDuration(400);
                    rotateAnimation.setFillAfter(true);
                    compass.startAnimation(rotateAnimation);
                    currentDegree = -azimut;

                    goalDegree = currentDegree + dir;
                    RotateAnimation rotateAnimation2 = new RotateAnimation(
                            currentDegreeWaypoint, goalDegree,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                    );
                    rotateAnimation2.reset();
                    rotateAnimation2.setInterpolator(new LinearInterpolator());
                    rotateAnimation2.setDuration(400);
                    rotateAnimation2.setFillAfter(true);
                    waypoint.startAnimation(rotateAnimation2);
                    currentDegreeWaypoint = goalDegree;
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
        float bea = tracker.getLocation().bearingTo(start);
        return bea;
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
            //TODO change to server
            try {
                nextWaypoint = facade.getNextWayPoint(nextWaypointId);
            } catch (ServerCommunicationException e) {
                e.printStackTrace();
                //TODO addDialog
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