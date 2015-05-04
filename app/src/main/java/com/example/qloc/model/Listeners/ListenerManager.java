package com.example.qloc.model.Listeners;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.qloc.model.GPSTracker;
import com.example.qloc.model.WayPoint;


/**
 * Created by Alex on 01.04.2015.
 */
public class ListenerManager implements SensorEventListener{
    private SensorManager sensorManager;
    private updateSensorListener updateSensorMagnetic;
    private updateSensorListener updateSensorAccelerator;

    public ImageView getCompass() {
        return compass;
    }

    float currentDegree = 0f;

    public float getCurrentDegree() {
        return currentDegree;
    }

    public synchronized void setCurrentDegree(float currentDegree) {
        this.currentDegree = currentDegree;
    }

    private ImageView compass;


    private GPSTracker tracker;
    private WayPoint start;
    private ImageView outer;

    public ListenerManager(SensorManager manager, ImageView compass, ImageView pointer, WayPoint p, GPSTracker gps) {
        sensorManager = manager;
        updateSensorMagnetic = new updateSensorListener();
        updateSensorAccelerator = new updateSensorListener();
        this.compass = compass;
        this.outer = pointer;
        this.start = p;
        this.tracker=gps;

    }

    /**
     *  This method will register the listeners in a different thread
     */
    public void registerListeners(){
                sensorManager.registerListener(updateSensorAccelerator, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
                sensorManager.registerListener(updateSensorMagnetic, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
                sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
    }
    /**
     *  This method will unregister the listeners
     */
    public void unregisterListeners(){
         sensorManager.unregisterListener(updateSensorAccelerator);
         sensorManager.unregisterListener(updateSensorMagnetic);
         sensorManager.unregisterListener(this);
    }
    /**
     *  Getter for magnetic field listener
     * @return the magnetic field listener object
     */
    public updateSensorListener getUpdateSensorMagnetic() {
        return updateSensorMagnetic;
    }
    /**
     *  Getter for accelerator  listener
     * @return the accelerator listener object
     */
    public updateSensorListener getUpdateSensorAccelerator() {
        return updateSensorAccelerator;
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(updateSensorMagnetic.isUpdate() && updateSensorAccelerator.isUpdate()){
            updateSensorAccelerator.resetUpdate();
            updateSensorMagnetic.resetUpdate();
            //DO do make rotation in a thread
            new AsyncCompasComputation(this).execute(new SensorDataContainer(updateSensorAccelerator.getValues(), updateSensorMagnetic.getValues()));

        }
    }

    public float calculateHeading(){
        float helpDegree;
        while(tracker.getLocation()==null){
        }
        float bea = tracker.getLocation().bearingTo(start);
        float dir = bea;
        if (currentDegree <0){
            helpDegree=360+currentDegree;
        }else{
            helpDegree=currentDegree;
        }
        if(bea<0){
            dir=360+bea;
        }
        dir = (helpDegree-dir);

        return dir;



    }


}
