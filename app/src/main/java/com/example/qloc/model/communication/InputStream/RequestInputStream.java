package com.example.qloc.model.communication.InputStream;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by Alexander on 01/06/2015.
 */
public class RequestInputStream {
    private InputStream httpInputStream;
    private int httpStatus;

    public RequestInputStream(HttpURLConnection connection) throws IOException {
        httpStatus = connection.getResponseCode();
        if(httpStatus != 200){
            httpInputStream = new BufferedInputStream(connection.getErrorStream());
        }else {
            httpInputStream = new BufferedInputStream(connection.getInputStream());
        }
    }

    public int getStreamType(){
        return httpStatus;
    }
    public InputStream getInputStream(){
        return httpInputStream;
    }

    public String readStream() throws IOException {
        StringBuilder sb = new StringBuilder();
        //check character size of longest response
        BufferedReader r = new BufferedReader(new InputStreamReader(httpInputStream),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        httpInputStream.close();
        return sb.toString();
    }
}
