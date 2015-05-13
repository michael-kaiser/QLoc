package com.example.qloc.model.Listeners;

import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by Alexander on 04/05/2015.
 */
public class AsyncCompasComputation extends AsyncTask<SensorDataContainer, Integer, Integer> {
    float[] R = new float[9];
    float[] I = null;
    float[] newRadians = new float[3];
    private ListenerManager listenManager;

    public AsyncCompasComputation(ListenerManager l){
        this.listenManager = l;
    }

    @Override
    protected Integer doInBackground(SensorDataContainer...params) {

        SensorDataContainer cont = params[0];

        boolean success = SensorManager.getRotationMatrix(R, I, cont.getAcc(), cont.getMag());
        if(success){
            SensorManager.getOrientation(R, newRadians);
            float azimuth = (float) Math.toDegrees(newRadians[0]);
            RotateAnimation rotateAnimation = new RotateAnimation(
                    listenManager.getCurrentDegree(), -azimuth,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            rotateAnimation.setDuration(250);
            //rotationAnimation.setFillAfter(true);
            listenManager.getCompass().startAnimation(rotateAnimation);
            listenManager.setCurrentDegree(-azimuth);
        }
        return null;
    }
}
