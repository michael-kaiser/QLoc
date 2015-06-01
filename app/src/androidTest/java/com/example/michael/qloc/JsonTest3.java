package com.example.michael.qloc;

import android.location.Location;

import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.example.qloc.model.exceptions.ServerCommunicationException;
import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

import java.io.IOException;


public class JsonTest3 extends TestCase {
    WayPoint wp;
    WayPoint p;
    protected void setUp() {
        wp = new WayPoint(new Location(""), "GoldenRoof", "n nettes Dach", "Wer baute das Goldene Dachl", "Maximilan I.", "Max und Moritz", "Max Payne", "KlaxMAx");
        wp.setLatitude(47.268646d);
        wp.setLongitude(11.393268d);
        wp. setNextId("next");

    }
    public void testAdd() throws JsonProcessingException {
        String s = MyLittleSerializer.WayPointToJSON(wp);
        s = "{\"error\":\"ICh bin ein error\"}";

        try {
            p =MyLittleSerializer.JSONStringToWayPoint(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
        }
        assertEquals(p.toString(), wp.toString());
    }

    public static void main(String ... args) {
        TestCase test = new JsonTest3() {
            public void runTest() {
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