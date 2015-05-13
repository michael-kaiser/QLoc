package com.example.qloc.model.Listeners;


/**
 * Created by Alexander on 04/05/2015.
 */
public class SensorDataContainer {
    private float[] acc;
    private float[] mag;

    public SensorDataContainer(float[] a, float[] m){
        acc = a;
        mag=m;
    }

    public float[] getAcc() {
        return acc;
    }

    public float[] getMag() {
        return mag;
    }
}
