package com.example.qloc.controller;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qloc.R;
import com.example.qloc.model.CustomAdapter;
import com.example.qloc.model.GPSTracker;
import com.example.qloc.model.RowItem;
import com.example.qloc.model.WayPoint;
import com.example.qloc.model.WayPointList;
import com.example.qloc.model.communication.HttpConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The screen to launch a new route
 * @author michael
 * TODO Start nearest game
 */
public class PlayGameActivity extends Activity implements AdapterView.OnItemClickListener {

    private final String TAG = "PlayGameActivity";
    public static final String KEY = "Waypoint";
    private List<RowItem> rowItems;
    private ListView myListview;
    private GPSTracker tracker;
    private Location currentLocation;
    private  HttpConnection conn = null;

    @Override
    protected void onRestart() {
        super.onRestart();
        updateList();
    }


    private void updateList(){
        Log.d(TAG,"update list");
        tracker = GPSTracker.getInstance(this);
        tracker.setListener(tracker);
        tracker.init();
        tracker.startTracking();
        currentLocation = tracker.getLocation();
        String answer = null;
        try {
            answer = conn.sendAndRecive(JsonTool.rangeQuery(currentLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<WayPoint> wpList = null;
        try {
            wpList = (MyLittleSerializer.JSONStringToRoutesList(answer)).toWayPointList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // if no location is available go to the GPS menu
        if(currentLocation == null){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }else {

            //feeding list with data
            rowItems = new ArrayList<>();

            WayPointList.setContext(this);
            for (WayPoint wp : wpList) {
                Log.d(TAG, wp.toString());
                RowItem item = new RowItem(wp, currentLocation);
                rowItems.add(item);
            }

            //sort the list according to the distance
            Collections.sort(rowItems, new Comparator<RowItem>() {
                public int compare(RowItem o1, RowItem o2) {
                    if (o1.getDistance() == null || o2.getDistance() == null)
                        return 0;
                    return (int) (Double.parseDouble(o1.getDistance()) - Double.parseDouble(o2.getDistance()));
                }
            });

            CustomAdapter adapter = new CustomAdapter(this, rowItems);
            myListview.setAdapter(adapter);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_right,R.anim.pull_out_to_left);
        setContentView(R.layout.activity_play);
        conn = HttpConnection.getInstance();
        myListview = (ListView) findViewById(R.id.list);
        View view = View.inflate(this, R.layout.headerview, null);
        myListview.addHeaderView(view);
        myListview.setOnItemClickListener(this);
        updateList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this,Navigation_Activity_neu.class);
        WayPoint wp = new WayPoint(((RowItem) parent.getAdapter().getItem(position)).getWaypoint());
        Log.d(TAG+"test",wp.getName() + wp.getDesc() + wp.getAnswer4());
        String msg = null;
        try {
            msg = JsonTool.requestNext(wp.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        WayPoint new_wp = null;
        try {
            new_wp = MyLittleSerializer.JSONStringToWayPoint(conn.sendAndRecive(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        i.putExtra(KEY, new_wp); //give the waypoint to the next Activity
        startActivity(i);
    }

    /**
     * starts the activity when you click on the green arrow
     */
    public void onClickStartButton(View v){
        Intent i = new Intent(this,Navigation_Activity_neu.class);
        WayPoint wp = new WayPoint(rowItems.get(0).getWaypoint());
        Log.d(TAG+"test",wp.getName() + wp.getDesc() + wp.getAnswer4());
        i.putExtra(KEY, wp); //give the waypoint to the next Activity
        startActivity(i);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.pull_out_to_right);
    }
}
