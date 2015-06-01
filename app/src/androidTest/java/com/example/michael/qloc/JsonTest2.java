package com.example.michael.qloc;

import com.example.qloc.controller.json_utils.JsonTool;
import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

import java.io.IOException;


public class JsonTest2 extends TestCase {

    public void testAdd() throws JsonProcessingException {
        String s=null;

        try {
           s= JsonTool.sendAnswer("Testantwort");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(s, "{\"answer\":\"Testantwort\"}");
    }

    public static void main(String ... args) {
        TestCase test = new JsonTest2() {
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