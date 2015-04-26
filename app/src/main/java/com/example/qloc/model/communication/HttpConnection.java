package com.example.qloc.model.communication;

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
    private HttpURLConnection connection;

    public  HttpConnection(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public String sendAndRecive(String request) {
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            writeStream(out, request);

            InputStream in = new BufferedInputStream(connection.getInputStream());
            readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }


    private void writeStream(OutputStream out, String request)throws IOException{
        out.write(request.getBytes());
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
