package com.example.qloc.controller.json_utils;

import com.example.qloc.controller.json_utils.jsonObjects.Answer;
import com.example.qloc.controller.json_utils.jsonObjects.RoutesList;
import com.example.qloc.controller.json_utils.jsonObjects.RoutesNext;
import com.example.qloc.controller.activities.activityUtils.SaveRoute;
import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.example.qloc.controller.json_utils.jsonObjects.SaveRouteContainer;
import com.example.qloc.controller.json_utils.jsonObjects.SaveRouteContainerContainer;
import com.example.qloc.controller.json_utils.jsonObjects.WayPointDataCont;
import com.example.qloc.model.exceptions.ServerCommunicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

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

        JsonNode actualObj = null;
        try {
            actualObj = OBJECT_MAPPER.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(actualObj == null || (actualObj.get("error"))!= null){
            throw new ServerCommunicationException();
        }
        if((actualObj.get("end"))!= null){
            return null;//TODO no other object null!
        }
        RoutesNext wpdc;

            wpdc = OBJECT_MAPPER.readValue(jsonString, RoutesNext.class);

        return wpdc.toWayPoint();
    }

    public static RoutesList JSONStringToRoutesList(String jsonString) throws IOException, ServerCommunicationException {
        JsonNode actualObj = null;
        try {
            actualObj = OBJECT_MAPPER.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(actualObj == null || (actualObj.get("error"))!= null){
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
        SaveRouteContainerContainer container2 =container.prepareJson();
        ret = OBJECT_MAPPER.writeValueAsString(container2);
        return ret;

    }

}
