package com.example.michael.qloc;

import android.location.Location;

import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.model.WayPoint;
import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

import java.io.IOException;


public class JsonTest4 extends TestCase {

    public void testAdd() throws JsonProcessingException {
        String s = "{\"evaluation\":true}";

        try {
            assertTrue(MyLittleSerializer.EvaluateAnswer(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String ... args) {
        TestCase test = new JsonTest4() {
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