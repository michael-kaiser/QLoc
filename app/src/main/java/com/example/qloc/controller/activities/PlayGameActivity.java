package com.example.qloc.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qloc.R;
import com.example.qloc.model.CustomAdapter;
import com.example.qloc.model.GPSTracker;
import com.example.qloc.model.RowItem;
import com.example.qloc.model.WayPoint;
import com.example.qloc.model.communication.HttpFacade;
import com.example.qloc.model.exceptions.ServerCommunicationException;
import com.example.qloc.model.mockup.Mockup;

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
    private HttpFacade httpFacade;

    @Override
    protected void onRestart() {
        super.onRestart();
        updateList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_right,R.anim.pull_out_to_left);
        setContentView(R.layout.activity_play);
        httpFacade = HttpFacade.getInstance();
        myListview = (ListView) findViewById(R.id.list);
        View view = View.inflate(this, R.layout.headerview, null);
        myListview.addHeaderView(view);
        myListview.setOnItemClickListener(this);
        updateList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this,Navigation_Activity_neu.class);
        WayPoint wp = new WayPoint(((RowItem) parent.getAdapter().getItem(position)).getWaypoint());
        Log.d(TAG+"test",wp.getName() + wp.getDesc() + wp.getAnswer4());
        Log.d(TAG,wp.getId() + " ");
        //TODO change to server !!!!!!!!!!!!!!!!!!!!!
        WayPoint nextWayPoint = null;
        try {
            nextWayPoint = Mockup.getNextWayPoint(wp.getId());
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
            ////TODO addDialog
        }

        Log.d(TAG+"test",nextWayPoint.getId() + nextWayPoint.getDesc() + nextWayPoint.getAnswer4());
        i.putExtra(KEY, nextWayPoint); //give the waypoint to the next Activity
        startActivity(i);
    }

    /**
     * starts the activity when you click on the green arrow
     */
    public void onClickStartButton(View v){
        Intent i = new Intent(this,Navigation_Activity_neu.class);
        WayPoint wp = new WayPoint(rowItems.get(0).getWaypoint());
        //TODO change to server
        WayPoint nextWayPoint = null;
        try {
            nextWayPoint = httpFacade.getNextWayPoint(wp.getId());
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
            //TODO addDialog
        }
        Log.d(TAG+"test",nextWayPoint.getName() + nextWayPoint.getDesc() + nextWayPoint.getAnswer4());
        i.putExtra(KEY, nextWayPoint); //give the waypoint to the next Activity
        startActivity(i);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //override animation when changing to this activity
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.pull_out_to_right);
    }

    /**
     * fill the scrollList with the routes
     */
    private void updateList(){
        List<WayPoint> wpList = null;
        tracker = GPSTracker.getInstance(this);
        tracker.setListener(tracker);
        tracker.init();
        currentLocation = tracker.getLocation();

        while (currentLocation == null) {
            try {
                Log.d(TAG, "no location");
                Thread.sleep(1000);
                currentLocation = tracker.getLocation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "got location");

        //TODO change to server !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        try {
            wpList = Mockup.getWayPointList(currentLocation);
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
            //TODO addDialog
        }
        rowItems = new ArrayList<>();

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

        final CustomAdapter adapter = new CustomAdapter(this, rowItems);
        myListview.setAdapter(adapter);

    }


}
