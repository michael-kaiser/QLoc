package com.example.qloc.model.data;

import android.location.Location;
import android.util.Log;

import com.example.qloc.controller.json_utils.JsonTool;
import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.controller.activities.activityUtils.SaveRoute;
import com.example.qloc.model.ThreadControl.NetworkExecuter;
import com.example.qloc.controller.activities.activityUtils.WayPoint;
import com.example.qloc.model.communication.CallableHttpConnection;
import com.example.qloc.model.communication.HttpConnection;
import com.example.qloc.model.exceptions.ServerCommunicationException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.qloc.controller.json_utils.JsonTool.checkError;

/**
 * Created by michael on 03.05.15.
 */
public class HttpFacade implements Data {
    private NetworkExecuter<String> executer = new NetworkExecuter<String>();
    private HttpConnection conn;
    private static HttpFacade instance = null;

    public static HttpFacade getInstance(){
        if(instance == null){
            return new HttpFacade();
        }else{
            return instance;
        }
    }

    private HttpFacade(){
        conn = HttpConnection.getInstance();
    }

    public List<WayPoint> getWayPointList(Location currentLocation) throws ServerCommunicationException {
        String answer = null;
        try {
            getResponseFromServer("{\"reset\": true}");
            answer = getResponseFromServer((JsonTool.rangeQuery(currentLocation)));
        } catch (Exception e) {
            Log.d("Facade", e.getMessage());
        }
        Log.d("Facade", answer);


        ArrayList<WayPoint> wpList = null;
        try {
            wpList = (MyLittleSerializer.JSONStringToRoutesList(answer)).toWayPointList();
        } catch (IOException e) {
        }

        return wpList;
    }

    public void login(String name, String password){
        String answer = null;
        try {
            answer = getResponseFromServer(JsonTool.login(name, password));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Facade", answer);
    }

    public boolean register(String name, String password){
        String answer = null;
        try {
            answer = getResponseFromServer(JsonTool.register(name, password));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Facade", answer);
        return checkError(answer);
    }

    public WayPoint getNextWayPoint(String nextWayPointID) throws ServerCommunicationException {
        String msg = null;
        WayPoint nextWayPoint = null;

        try {
            msg = JsonTool.requestNext(nextWayPointID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String response = getResponseFromServer(msg);
            Log.d("Test", " " + response);
            nextWayPoint = MyLittleSerializer.JSONStringToWayPoint(response);
        } catch (IOException e) {
            nextWayPoint = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(nextWayPoint==null){
            return null; //TODO search better solution!
        }
        nextWayPoint.setName(nextWayPoint.getDesc());
        nextWayPoint.setNextId(nextWayPointID);
        nextWayPoint.setId(nextWayPointID);
        Log.d("id", nextWayPointID + " ");
        return nextWayPoint;
    }

    public boolean checkAnswer(String givenAnswer) throws ServerCommunicationException{
        String temp = null;
        boolean response = true;

        try {
            temp = JsonTool.sendAnswer(givenAnswer);

            temp = getResponseFromServer(temp);
            response = MyLittleSerializer.EvaluateAnswer(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getResponseFromServer(String request) throws MalformedURLException, ExecutionException, InterruptedException {
        String response;
        executer.execute(new CallableHttpConnection(request));
        response = executer.getResult();
        return response;
    }

    public boolean saveRoute(SaveRoute route) throws ServerCommunicationException {
        //TODO implement
        String JsonStringSavedRoute = null;
        String answer = null;
        try {
            JsonStringSavedRoute= MyLittleSerializer.SaveRouteToJson(route);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            executer.execute(new CallableHttpConnection(JsonStringSavedRoute));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            answer= executer.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        JsonTool.checkCreatedRoutes(answer);
        Log.d("SavedRouteJson", JsonStringSavedRoute);
        Log.d("Http", route.toString() + " " + route.size());


        return false;
    }

}
