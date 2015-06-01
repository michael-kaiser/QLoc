package com.example.qloc.controller.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.qloc.R;
import com.example.qloc.controller.activities.activityUtils.BitMapWorkerTask;

/**
 * This fragment shows the compass
 * Created by michael on 16.05.15.
 */
public class CompassFragment extends Fragment {

    private final static int DELAY_SENSOR = 10000;

    private ImageView compass;
    private ImageView waypoint;
    private View view;
    private ImageView background;

    private CompassMapParent parent;
    private float degree;
    private float currentDegree = 0f;
    private float currentDegreeWaypoint = 0f;

    private Sensor accelerometer;
    private Sensor magnetometer;
    private MySensorEventListener mysensor;
    private SensorManager mSensorManager;
    private LocationListener listener;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BitMapWorkerTask.loadBitmap(R.drawable.back_jpeg, background, getActivity());
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.compass_fragment,container,false);

        background = (ImageView) view.findViewById(R.id.background_compass);
        compass = (ImageView) view.findViewById(R.id.inner_compass);
        waypoint = (ImageView) view.findViewById(R.id.waypoint);

        degree =0;
        mysensor = new MySensorEventListener();
        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(mysensor, accelerometer, DELAY_SENSOR);
        mSensorManager.registerListener(mysensor, magnetometer, DELAY_SENSOR);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                parent.callListener(location);
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
        };
        parent.getTracker().addListener(listener);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(mysensor,accelerometer);
        mSensorManager.unregisterListener(mysensor, magnetometer);
        parent.getTracker().removeListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        parent.getTracker().addListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        parent.getTracker().removeListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mSensorManager.registerListener(mysensor,accelerometer,DELAY_SENSOR);
        mSensorManager.registerListener(mysensor,magnetometer,DELAY_SENSOR);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof CompassMapParent){
            parent = (CompassMapParent) activity;
        }else{
            throw new ClassCastException(activity.toString() + "must implement CompassFragment.HeadingUpdater");
        }
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
                    float dir = parent.calculateHeading();
                    float goalDegree;
                    RotateAnimation rotateAnimation = new RotateAnimation(
                            currentDegree, -azimut,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                    );
                    rotateAnimation.reset();
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    rotateAnimation.setDuration(DELAY_SENSOR);
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
                    rotateAnimation2.setDuration(DELAY_SENSOR);
                    rotateAnimation2.setFillAfter(true);
                    waypoint.startAnimation(rotateAnimation2);
                    currentDegreeWaypoint = goalDegree;
                }

            }

            parent.calculateHeading();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


}

