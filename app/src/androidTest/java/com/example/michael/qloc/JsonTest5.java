package com.example.michael.qloc;

import android.location.Location;

import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.model.WayPoint;
import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

import java.io.IOException;


public class JsonTest5 extends TestCase {

    public void testAdd() throws IOException {
        String s ="{\"waypoint\":{\"answers\":[\"A\",\"B\",\"C\",\"D\"],\"location\":[47.264077,11.345851],\"hint\":\"mein Hint\",\"question\":\"Zeichensäle\"}}";


            assertEquals(MyLittleSerializer.JSONStringToWayPoint(s).toString(), "nix");
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