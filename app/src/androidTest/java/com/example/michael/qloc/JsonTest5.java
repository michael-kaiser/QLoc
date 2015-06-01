package com.example.michael.qloc;

import android.location.Location;

import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.controller.activities.activityUtils.SaveRoute;
import com.example.qloc.controller.activities.activityUtils.ServerWayPoint;
import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JsonTest5 extends TestCase {

    public void testAdd() throws IOException {
        String[] false1= new String[]{"eins", "zwei", "drei"};
        Location l1= new Location("");
        l1.setLatitude(1);
        l1.setLongitude(2);
        ServerWayPoint p1 = new ServerWayPoint("richtig0", false1, l1, "hint1","frage1");

        String[] false2= new String[]{"eins2", "zwei2", "drei2"};
        Location l2= new Location("");
        l2.setLatitude(3);
        l2.setLongitude(4);
        ServerWayPoint p2 = new ServerWayPoint("richtig1", false1, l2, "hint2","frage2");

        List<ServerWayPoint> liste = new ArrayList<ServerWayPoint>();
        liste.add(p1);
        liste.add(p2);


        SaveRoute sr1 = new SaveRoute("Name", "Description");
        sr1.setWayPointList(liste);


            assertEquals(MyLittleSerializer.SaveRouteToJson(sr1), "nix");
    }

    public static void main(String ... args) {
        TestCase test = new JsonTest5() {
            public void runTest() throws IOException {
                try {
                    testAdd();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        };
        test.run();
    }

}