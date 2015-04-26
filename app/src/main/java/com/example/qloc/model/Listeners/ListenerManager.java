package com.example.qloc.model.Listeners;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;



/**
 * Created by Alex on 01.04.2015.
 */
public class ListenerManager implements SensorEventListener {
    private SensorManager sensorManager;
    private updateSensorListener updateSensorMagnetic;
    private updateSensorListener updateSensorAccelerator;

    float[] R = new float[9];
    float[] I = null;
    float[] newRadians = new float[3];
    float currentDegree = 0f;
    private ImageView compass;


    public ListenerManager(SensorManager manager, ImageView compass) {
        sensorManager = manager;
        updateSensorMagnetic = new updateSensorListener();
        updateSensorAccelerator = new updateSensorListener();
        this.compass = compass;
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
            boolean success = SensorManager.getRotationMatrix(R, I, updateSensorAccelerator.getValues(), updateSensorMagnetic.getValues());
            if(success){
                SensorManager.getOrientation(R, newRadians);
                float azimuth = (float) Math.toDegrees(newRadians[0]);
                RotateAnimation rotateAnimation = new RotateAnimation(
                        currentDegree, -azimuth,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
                rotateAnimation.setDuration(250);
                //rotationAnimation.setFillAfter(true);
                compass.startAnimation(rotateAnimation);
                currentDegree = -azimuth;
            }
        }
    }

}
