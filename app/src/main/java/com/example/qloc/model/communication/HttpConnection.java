package com.example.qloc.model.communication;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by Alex on 26.04.2015.
 */
public class HttpConnection {
    private URL url;
    private final String URL_NAME = "192.168.1.73";
    private HttpURLConnection connection;
    private static HttpConnection instance;
    public static HttpConnection getInstance(){
        if(instance == null){
            try {
                return new HttpConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    private HttpConnection() throws MalformedURLException {
        this.url = new URL("http", "192.168.1.73", 3000,"");
        Log.d("Http", url.toString());
    }
    public String sendAndRecive(String request) {
        String response = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            Log.d("Http", connection.toString());
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
//deactivate that servers use gzip compression
            connection.setRequestProperty("Accept-Encoding", "identity");
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            writeStream(out, request);
            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return response;
    }
    private void writeStream(OutputStream out, String request)throws IOException{
        out.write(request.getBytes("UTF-8"));
    }
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
//check character size of longest response
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}