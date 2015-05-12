package com.example.qloc.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.qloc.R;
import com.example.qloc.controller.json_utils.JsonTool;

import java.io.InputStream;

public class MainScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ImageView back1 = (ImageView) findViewById(R.id.map_background);
        ImageView back2 = (ImageView) findViewById(R.id.map_background2);
        loadBitmap(R.drawable.map_top,back1);
        loadBitmap(R.drawable.map_lower,back2);

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

    public void loadBitmap(int resId, ImageView imageView) {
        JsonTool.BitMapWorkerTask task = new JsonTool.BitMapWorkerTask(imageView,this);
        task.execute(resId);
    }

}
