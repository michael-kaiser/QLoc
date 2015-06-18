package com.example.qloc.model.communication;

import android.util.Log;
import android.webkit.CookieManager;

import com.example.qloc.model.communication.InputStream.RequestInputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Alex on 26.04.2015.
 */

public class CallableHttpConnection implements Callable<String> {
    private final String URL_NAME = "192.168.1.73";

    private final URL url;
    private final String request;

    public CallableHttpConnection(String request) throws MalformedURLException {
        url = new URL("http", "darkboxed.org", 3000,"api");
        //url = new URL("http", "192.168.1.87", 3000,"");

        this.request =request;
    }

    public String call() {
        String response = null;
        if(request == null) return null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            // Set cookies in requests
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(connection.getURL().toString());
            if (cookie != null) {
                connection.setRequestProperty("Cookie", cookie);
                Log.d("Cookie", cookie.toString());
            }

            setStandardHttpProperty(connection);

            connection.connect();

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

            response = new RequestInputStream(connection).readStream();
            Log.d("http", response.toString());
        } catch (IOException e) {
            Log.d("httpExc", e.getMessage());
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
    private void setStandardHttpProperty(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(0);
        System.setProperty("http.keepAlive", "false");
        connection.setRequestProperty("Connection", "close");
        //deactivate that servers use gzip compression
        //connection.setRequestProperty("Accept-Encoding", "identity");
    }

}
