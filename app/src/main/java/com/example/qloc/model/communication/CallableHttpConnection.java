package com.example.qloc.model.communication;

import android.util.Log;
import android.webkit.CookieManager;

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
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Alex on 26.04.2015.
 */

public class CallableHttpConnection implements Callable<String> {
    private final String URL_NAME = "192.168.1.87";

    private final URL url;
    private final String request;

    public CallableHttpConnection(String request) throws MalformedURLException {
        url = new URL("http", URL_NAME, 3000,"");
        this.request =request;
    }

    public String call() {
        int status;
        String response = null;
        if(request == null) return null;
        HttpURLConnection connection = null;
        try {
            // Set cookies in requests
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(connection.getURL().toString());
            if (cookie != null) {
                connection.setRequestProperty("Cookie", cookie);
            }
            connection = (HttpURLConnection) url.openConnection();
            Log.d("Http", connection.toString());
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            //deactivate that servers use gzip compression
            //connection.setRequestProperty("Accept-Encoding", "identity");
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            writeStream(out, request);
            out.close();
            // Get cookies from responses and save into the cookie manager
            List<String> cookieList = connection.getHeaderFields().get("Set-Cookie");
            if (cookieList != null) {
                for (String cookieTemp : cookieList) {
                    cookieManager.setCookie(connection.getURL().toString(), cookieTemp);
                }
            }
            Log.d("http", request.toString());
            status  = connection.getResponseCode();
            InputStream in;
            if(status != 200){
                in = new BufferedInputStream(connection.getErrorStream());
            }else {
                in = new BufferedInputStream(connection.getInputStream());
            }
            response = readStream(in);
        } catch (IOException e) {
            Log.d("http", e.getMessage());
            e.printStackTrace();
        } finally {
            if(connection != null)
                connection.disconnect();
        }

        return response;
    }
    private void writeStream(OutputStream out, String request)throws IOException{
        out.write(request.getBytes("UTF-8"));
        out.flush();
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
