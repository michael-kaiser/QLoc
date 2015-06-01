package com.example.qloc.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.qloc.R;
import com.example.qloc.controller.activities.activityUtils.BitMapWorkerTask;

public class MainScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ImageView back1 = (ImageView) findViewById(R.id.map_background);
        ImageView back2 = (ImageView) findViewById(R.id.map_background2);
        BitMapWorkerTask.loadBitmap(R.drawable.map_top,back1, this);
        BitMapWorkerTask.loadBitmap(R.drawable.map_lower,back2, this);

    }

    public void onButtonCreateRoutes(View v){
        v.setAlpha(1.0f);
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);
    }

    public void onButtonPlay(View v){
        v.setAlpha(1.0f);
        Intent i = new Intent(this,PlayGameActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

}
