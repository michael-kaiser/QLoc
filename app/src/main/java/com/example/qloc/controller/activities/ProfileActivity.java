package com.example.qloc.controller.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.qloc.R;
import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.example.qloc.model.data.HttpFacade;
import com.example.qloc.model.exceptions.ServerCommunicationException;

import java.util.ArrayList;

public class ProfileActivity extends Activity {

    private TextView usernameTv;
    private TextView routeCount;
    private TextView points;
    private HttpFacade facade = HttpFacade.getInstance();
    private static String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        usernameTv = (TextView) findViewById(R.id.profile_name);
        routeCount = (TextView) findViewById(R.id.profile_route_count);
        points = (TextView) findViewById(R.id.profile_points);
        update();

    }

    public void update(){
        ArrayList<WayPoint> list = null;
        int userPoints = 0;
        try {
            list = facade.getUserRoutes();
            userPoints = facade.getUserPoints();
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
        }
        usernameTv.setText(username);
        routeCount.setText(list.size());
        points.setText(userPoints);


    }

    public static void setUsername(String uname){
        username = uname;
    }
}
