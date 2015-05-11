package com.example.qloc.model.communication;

import android.location.Location;
import android.util.Log;

import com.example.qloc.controller.json_utils.JsonTool;
import com.example.qloc.controller.json_utils.MyLittleSerializer;
import com.example.qloc.model.SaveRoute;
import com.example.qloc.model.ThreadControl.NetworkExecuter;
import com.example.qloc.model.WayPoint;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by michael on 03.05.15.
 */
public class HttpFacade {
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

    public List<WayPoint> getWayPointList(Location currentLocation){
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

    public WayPoint getNextWayPoint(String nextWayPointID){
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
        nextWayPoint.setNextId(nextWayPointID);
        nextWayPoint.setId(nextWayPointID);
        Log.d("id", nextWayPointID + " ");
        return nextWayPoint;
    }

    public boolean checkAnswer(String givenAnswer){
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

    public boolean saveRoute(SaveRoute route){
        //TODO implement
        Log.d("Http", route.toString() + " " + route.size());
        return false;
    }
}
