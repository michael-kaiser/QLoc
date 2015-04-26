package com.example.qloc.model.Listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by Alex on 31.03.2015.
 */
public class updateSensorListener implements SensorEventListener {
    private float values[];
    private boolean update = false;

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        values = sensorEvent.values;
        update = true;
        Log.d("Sensor", " " + sensorEvent.values[0]);
    }

    public float[] getValues() {
        return values;
    }

    public boolean isUpdate() {
        return update;
    }

    public void resetUpdate(){
        update = false;
    }
}
