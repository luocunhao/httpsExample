package com.pulan.wechat.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static String postRequest(String url, String body) {
        String result ;
        BufferedReader reader ;
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.connect();

            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            reader.close();
            result = buffer.toString();
            return result;
        } catch (Exception e) {
            return null;
        }

    }
}
