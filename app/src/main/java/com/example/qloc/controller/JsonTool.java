package com.example.qloc.controller;

import com.example.qloc.model.Route;
import com.example.qloc.model.RoutesList;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by uli on 18.04.15.
 */
public class JsonTool {

    public static String rangeQuery(double lon, double lat) throws IOException {

        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = jFactory.createGenerator(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("longitude", lon);
        jsonGenerator.writeNumberField("latitude", lat);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }

    public static String sendAnswer(String ans) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = jFactory.createGenerator(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("answer", ans);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }

    public static String requestNext(String next) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = jFactory.createGenerator(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("nextID", next);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }

    public static boolean evaluatedAnswer(String st) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(st);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode aField = actualObj.get("evaluation");


        return "true".equals(aField.toString());

    }



}
