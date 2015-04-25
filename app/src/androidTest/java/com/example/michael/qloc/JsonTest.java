package com.example.michael.qloc;

import android.location.Location;

import com.example.qloc.controller.MyLittleSerializer;
import com.example.qloc.model.WayPoint;
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
        assertEquals(s, "GoldenRoof");
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
