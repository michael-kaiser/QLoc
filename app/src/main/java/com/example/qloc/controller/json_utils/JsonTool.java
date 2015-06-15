package com.example.qloc.controller.json_utils;

import android.location.Location;

import com.example.qloc.model.exceptions.ServerCommunicationException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by uli on 18.04.15.
 */
public class JsonTool {


    public static String rangeQuery(Location l) throws IOException {
        return rangeQuery(l.getLatitude(), l.getLongitude());

    }

    private static String rangeQuery(double lat, double lon) throws IOException {
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
        jsonGenerator.writeFieldName("distance");
        jsonGenerator.writeNumber(1000000000);
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
    //TODO Test
    public static boolean checkError(String st) {
        if(st==null){
            return false;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(st);
        } catch (JsonParseException e) {
            return false;
        } catch (JsonProcessingException e) {
            return false;
        } catch (IOException e){
            return false;
        }
        if((actualObj.get("error"))!= null){
            String error = actualObj.get("error").toString();
            return false;
        }

        return true;

    }

    public static boolean checkCreatedRoutes(String st) throws ServerCommunicationException {
        if(st==null){
            return false;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(st);
        } catch (JsonParseException e) {
            return false;
        } catch (JsonProcessingException e) {
            return false;
        } catch (IOException e){
            return false;
        }
        if((actualObj.get("error"))!= null){
            String error = actualObj.get("error").toString();
            throw new ServerCommunicationException();
        }

        return true;

    }

    public static String register(String name, String password1) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        jsonGenerator = jFactory.createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("setpwd");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", name);
        jsonGenerator.writeStringField("password", password1);
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

    public static String sendPoints(int points) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        jsonGenerator = jFactory.createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("savedPoints");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("points", points);
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }

    public static String routesPerUser() throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        jsonGenerator = jFactory.createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("routes_list");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("user", null);
        jsonGenerator.writeFieldName("location");
        jsonGenerator.writeStartArray(); // [
        jsonGenerator.writeNumber(0.0); // "someString" (preceded by comma if not 1st)
        jsonGenerator.writeNumber(0.0);
        jsonGenerator.writeNumber(0.0);
        jsonGenerator.writeEndArray(); // ]
        jsonGenerator.writeFieldName("distance");
        jsonGenerator.writeNumber(1000000000);
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }

    public static String pointsOfUser() throws IOException {
        JsonFactory jFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        jsonGenerator = jFactory.createGenerator(writer);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("user_points");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("user", null);
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
        return writer.toString();
    }

    public static int getPoints(String st) throws ServerCommunicationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(st);
        } catch (JsonParseException e) {
            return -3;
        } catch (JsonProcessingException e) {
            return -2;
        } catch (IOException e){
            return -1;
        }

        if((actualObj.get("error"))!= null){
            String error = actualObj.get("error").toString();
            throw new ServerCommunicationException();
        }

        JsonNode aField = actualObj.get("user_points");
        return aField.get("points").asInt();


    }

    public static int routeCount(String st) throws ServerCommunicationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(st);
        } catch (JsonParseException e) {
            return -3;
        } catch (JsonProcessingException e) {
            return -2;
        } catch (IOException e){
            return -1;
        }

        if((actualObj.get("error"))!= null){
            String error = actualObj.get("error").toString();
            throw new ServerCommunicationException();
        }

        JsonNode aField = actualObj.get("userRoutes");
        return aField.get("routes").asInt();


    }
}
