package com.example.qloc.controller.json_utils;

import android.location.Location;

import com.example.qloc.model.exceptions.ServerCommunicationException;
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

    /*public static String rangeQuery(double lon, double lat) throws IOException {

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
    }*/

    /*public static String rangeQuery(Location l) throws IOException {

        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = jFactory.createGenerator(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("longitude", l.getLongitude());
        jsonGenerator.writeNumberField("latitude", l.getLatitude());
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }*/
    public static String rangeQuery(Location l) throws IOException {
        return rangeQuery(l.getLatitude(), l.getLongitude());

    }

    public static String rangeQuery(double lat, double lon) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        jsonGenerator = jFactory.createGenerator(writer);
        jsonGenerator.writeStartObject(); // {
        jsonGenerator.writeFieldName("range_query"); // {"myData":
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("location");
        jsonGenerator.writeStartArray(); // [
        jsonGenerator.writeNumber(lat); // "someString" (preceded by comma if not 1st)
        jsonGenerator.writeNumber(lon);
        jsonGenerator.writeNumber(0.0);
        jsonGenerator.writeEndArray(); // ]
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject(); // }}
        jsonGenerator.close();
        return writer.toString();
    }


    public static String sendAnswer(String ans) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        jsonGenerator = jFactory.createGenerator(writer);
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
        jsonGenerator = jFactory.createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("route_next");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", next);
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }

    public static boolean evaluatedAnswer(String st) throws ServerCommunicationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(st);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if((actualObj.get("error"))!= null){
            String error = actualObj.get("error").toString();
            throw new ServerCommunicationException();
        }
        JsonNode aField = actualObj.get("route");
        System.out.println(aField.get("question").toString());

        return (aField.booleanValue());

    }

    public static boolean checkCreatedRoutes(String st) throws ServerCommunicationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(st);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if((actualObj.get("error"))!= null){
            String error = actualObj.get("error").toString();
            throw new ServerCommunicationException();
        }

        return true;

    }

    public static String register(String name, String password1, String password2) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        jsonGenerator = jFactory.createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("setpwd");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", name);
        jsonGenerator.writeStringField("password1", password1);//TODO check if server also wants "password1"...
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }


    public static String login(String name, String password) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        jsonGenerator = jFactory.createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("login");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", name);
        jsonGenerator.writeStringField("password", password);
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }
    /*public static boolean evaluatedAnswer(String st) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(st);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode aField = actualObj.get("evaluation");


        return (aField.booleanValue());*/




}
