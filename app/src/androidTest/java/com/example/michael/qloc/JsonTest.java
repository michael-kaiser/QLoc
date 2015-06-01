package com.example.michael.qloc;

import android.location.Location;

import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;


/**
 * Created by michael on 13.04.15.
 */
public class JsonTest extends TestCase {
    WayPoint wp;
    protected void setUp() {
        wp = new WayPoint(new Location(""), "GoldenRoof", "n nettes Dach", "Wer baute das Goldene Dachl", "Maximilan I.", "Max und Moritz", "Max Payne", "KlaxMAx");
        wp.setLatitude(47.268646d);
        wp.setLongitude(11.393268d);
        wp. setNextId("next");

    }
    public void testAdd() throws JsonProcessingException {
        //assertEquals(MyLittleSerializer.toJSON(wp),"Somethubg");
        String s = MyLittleSerializer.WayPointToJSON(wp);
      //  WayPoint p =MyLittleSerializer.JSONStringToWayPoint(s);
        assertEquals(s, "{\"waypoint\":{\"desc\":\"n nettes Dach\",\"id\":\"GoldenRoof\",\"name\":\"Einziger\",\"nextId\":\"next\",\"question\":\"Wer baute das Goldene Dachl\",\"answers\":[\"Maximilan I.\",\"Maximilan I.\",\"Max Payne\",\"KlaxMAx\"],\"location\":[47.268646,11.393268]}}");
    }

    public static void main(String ... args) {
        TestCase test = new JsonTest() {
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
