package com.example.bahadir.myapplicationn;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class VeriTabaniServisi extends Service {

    String url;
    String query;
    String charset;
    ArkadanKaynat aK;
    public void onCreate() {
        super.onCreate();
        Log.i("tago" , "VeriTabani onCreate");
        tanımlar();
    }
    public void tanımlar() {
        url = "http://185.22.184.103/project/connection.php?id=15&name=deneme";
        charset = "UTF-8";
        String param1 = "id";
        String param2 = "name";
        try {
            query = String.format("param1=%s&param2=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void firehttprequest() {
        URLConnection connection = null;
        try {
            connection = new URL("http://185.22.184.103/project/connection.php?id=15&name=deneme" + "?" + query).openConnection();
            InputStream response = connection.getInputStream();
            Log.i("tago", "tart");

        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Accept-Charset", charset);

    }
    public void firehttprequestwithqueryparameters() {
        aK = new ArkadanKaynat();
        aK.execute(url);
    }
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class ArkadanKaynat extends AsyncTask<String , Void , String> {

        protected String doInBackground(String... params) {
            Log.i("tago" , "arkadan işlem başlatıldı");
            try{
                return   bilgiyigonder();
            }catch(Exception e){
                e.printStackTrace();
                return "olmadı";
            }
        }

        private String bilgiyigonder() {
            URLConnection connection = null;
            try{
                connection = new URL("http://185.22.184.103/project/connection.php?id=15&name=deneme").openConnection();
                Log.i("tago" ,"bagı kurdum");
            }catch(IOException e){
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try(OutputStream output = connection.getOutputStream()){
                output.write(query.getBytes(charset));
                InputStream response = connection.getInputStream();
                Log.i("tago" , "yazdım");
            }catch(IOException e){
                e.printStackTrace();
                Log.i("tago" , "yazamadım");
            }
            return "alabama";
        }
    }
}
