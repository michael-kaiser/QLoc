package com.example.qloc.controller.activities;


import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;


import com.example.qloc.R;
import com.example.qloc.model.Listeners.ListenerManager;

/**
 * Created by Alex on 26.04.2015.
 */
public class Navigation2Activity extends Activity {
    private SensorManager sensorManager;
    private ListenerManager listenerManager;

    private ImageView compass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //override animation when changing to this activity
        setContentView(R.layout.activity_navigation2);
        compass = (ImageView) findViewById(R.id.compass);
        compass.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                                                 LayoutParams.WRAP_CONTENT,
                                                    0.5f));

        listenerManager = new ListenerManager(sensorManager, compass);
    }

    @Override
    public void onResume(){
        super.onResume();
        listenerManager.registerListeners();

    }

    @Override
    public void onPause(){
        super.onPause();
        listenerManager.unregisterListeners();
    }

}
