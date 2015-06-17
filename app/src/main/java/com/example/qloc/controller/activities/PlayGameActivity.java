package com.example.qloc.controller.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qloc.R;
import com.example.qloc.controller.activities.activityUtils.CustomAdapter;
import com.example.qloc.controller.activities.activityUtils.RowItem;
import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.example.qloc.controller.dialogs.AlertDialogUtility;
import com.example.qloc.location.GPSTracker;
import com.example.qloc.model.data.Data;
import com.example.qloc.model.data.HttpFacade;
import com.example.qloc.model.exceptions.ServerCommunicationException;

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
    private Data httpFacade;

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
        final Activity thisOne = this;
        Intent i = new Intent(this,NavigationActivity.class);
        WayPoint wp = new WayPoint(((RowItem) parent.getAdapter().getItem(position)).getWaypoint());
        Log.d(TAG+"test",wp.getName() + wp.getDesc() + wp.getAnswer4());
        Log.d(TAG,wp.getId() + " ");
        //TODO change to server !!!!!!!!!!!!!!!!!!!!!
        WayPoint nextWayPoint = null;
        try {
            nextWayPoint = httpFacade.getNextWayPoint(wp.getId());
            if(nextWayPoint == null){
                throw new ServerCommunicationException();
            }
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
            AlertDialogUtility.showAlertDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(thisOne, MainScreen.class);
                    startActivity(i);
                }
            }, "Can't connect to Server!");
        }

        if(nextWayPoint != null) {
            Log.d(TAG + "test", nextWayPoint.getId() + nextWayPoint.getDesc() + nextWayPoint.getAnswer4());
            i.putExtra(KEY, nextWayPoint); //give the waypoint to the next Activity
            startActivity(i);
        }
    }

    /**
     * starts the activity when you click on the green arrow
     */
    public void onClickStartButton(View v){
        final Activity parent = this;
        Intent i = new Intent(this,NavigationActivity.class);
        WayPoint wp = new WayPoint(rowItems.get(0).getWaypoint());
        //TODO change to server
        WayPoint nextWayPoint = null;
        try {
            nextWayPoint = httpFacade.getNextWayPoint(wp.getId());
            if(nextWayPoint == null){
                throw new ServerCommunicationException();
            }
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
            AlertDialogUtility.showAlertDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(parent, MainScreen.class);
                    startActivity(i);
                }
            }, "Can't connect to Server!");
        }
        if(nextWayPoint != null) {
            Log.d(TAG + "test", nextWayPoint.getName() + nextWayPoint.getDesc() + nextWayPoint.getAnswer4());
            i.putExtra(KEY, nextWayPoint); //give the waypoint to the next Activity
            startActivity(i);
        }

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
        final Activity parent = this;
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
            wpList = httpFacade.getWayPointList(currentLocation);
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
            AlertDialogUtility.showAlertDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(parent, MainScreen.class);
                    startActivity(i);
                }
            }, "Can't connect to Server!");
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
