package com.example.qloc.controller.json_utils;

import com.example.qloc.model.Answer;
import com.example.qloc.model.RoutesList;
import com.example.qloc.model.RoutesNext;
import com.example.qloc.model.SaveRoute;
import com.example.qloc.model.WayPoint;
import com.example.qloc.model.WayPointDataCont;
import com.example.qloc.model.exceptions.ServerCommunicationException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

//import com.example.qloc.model.WayPointList2;

/**
 * Created by uli on 17.04.15.
 */
public class MyLittleSerializer {

    private static final ObjectMapper OBJECT_MAPPER;
    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    public static String WayPointToJSON(WayPoint wp) throws JsonProcessingException {
        RoutesNext cont = new RoutesNext(new WayPointDataCont(wp));
        String ret = null;
        ret = OBJECT_MAPPER.writeValueAsString(cont);
        return ret;
    }

    public static WayPoint JSONStringToWayPoint(String jsonString) throws IOException, ServerCommunicationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if((actualObj.get("error"))!= null){
            String error = actualObj.get("error").toString();
            throw new ServerCommunicationException();
        }
        RoutesNext wpdc;

            wpdc = OBJECT_MAPPER.readValue(jsonString, RoutesNext.class);

        return wpdc.toWayPoint();
    }

    public static RoutesList JSONStringToRoutesList(String jsonString) throws IOException, ServerCommunicationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if((actualObj.get("error"))!= null){
            String error = actualObj.get("error").toString();
            throw new ServerCommunicationException();
        }
        RoutesList rl;

            rl = OBJECT_MAPPER.readValue(jsonString, RoutesList.class);

        return rl;
    }

    public static String RoutesListToJSONString(RoutesList rl) throws JsonProcessingException {

        String ret = null;

            ret = OBJECT_MAPPER.writeValueAsString(rl);

        return ret;
    }

    public static boolean EvaluateAnswer(String s) throws IOException {
        boolean b;
        Answer a = null;

            a = OBJECT_MAPPER.readValue(s, Answer.class);

        return a.isEvaluation();
    }

    public static String SaveRouteToJson(SaveRoute svr) throws IOException {
        SaveRouteContainer container = new SaveRouteContainer(svr);

        String ret = null;
        SaveRouteContainer2 container2 =container.prepareJson();
        ret = OBJECT_MAPPER.writeValueAsString(container2);
        return ret;

    }

}
