package com.example.michael.qloc;

import android.location.Location;

import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.model.WayPoint;
import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

import java.io.IOException;


public class JsonTest5 extends TestCase {

    public void testAdd() throws JsonProcessingException {
        String s = "{\"waypoint\":{\"answers\":[\"1\",\"2\",\"3\",\"-6\"],\"location\":[0.0,0.0],\"hint\":\"\",\"question\":\"1 + 1?\"}}";

        try {
            assertEquals(MyLittleSerializer.JSONStringToRoutesList(s).toWayPointList().get(0).toString(), "nix");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String ... args) {
        TestCase test = new JsonTest5() {
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