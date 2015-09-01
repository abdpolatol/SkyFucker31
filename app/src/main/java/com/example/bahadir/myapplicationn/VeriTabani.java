package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class VeriTabani {

    JSONObject jsonObject;
    String url;
    String query;
    String charset;
    ArkadanKaynat aK;
    ArkadanVurdur aV;
    ArkadanCevir aC;
    String is;
    String urrl;
    String lat;
    String longi;
    String id;
    String lato;
    String longio;
    String veritabani_id;
    Context context;

    public VeriTabani(Context context){}
    public VeriTabani(Context context , String is , String urrl , String lat , String longi){
        this.is=is;
        this.urrl = urrl;
        this.lat=lat;
        this.longi=longi;
        this.context = context;
        tanımlar();
        Log.i("tago" , "beşli veritabanı objesi oluşturuldu");
    }
    public VeriTabani(Context context , String id , String lato , String longio){
        this.id=id;
        this.lato=lato;
        this.longio = longio;
        this.context = context;
        yenilemetanimlar();
    }

    public void yenilemetanimlar(){
        url = "http://185.22.184.103/project/connection.php?name=bahadirturk&url=http://www.google.com&long=33.2132123&lat=33.2322322";
        charset = "UTF-8";
        String param1 = "id";
        String param2 = "long";
        String param3 ="lat";
        try {
            query = String.format("param1=%s&param2=%s&param3=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                    URLEncoder.encode(param3, charset) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void tanımlar() {
        url = "http://185.22.184.103/project/connection.php?name=bahadirturk&url=http://www.google.com&long=33.2132123&lat=33.2322322";
        charset = "ISO-8859-9";
        String param1 = "name";
        String param2 = "url";
        String param3 ="long";
        String param4 ="lat";
        try {
            query = String.format("param1=%s&param2=%s&param3=%s&param4=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                    URLEncoder.encode(param3, charset) ,URLEncoder.encode(param4, charset) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void firehttprequest() {
        URLConnection connection = null;
        try {
            connection = new URL("http://185.22.184.103/project/connection.php?name=bahadirturk&url=url&long=33.2132123&lat=33.2322322" + "?" + query).openConnection();
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
    public void lokasyonuyenile(){
        aV = new ArkadanVurdur();
        aV.execute(url);
    }
    public void cevredekilerigoster(){
        aC = new ArkadanCevir();
        aC.execute();
    }
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class ArkadanKaynat extends AsyncTask<String , Void , String> {

        protected String doInBackground(String... params) {
            Log.i("tago", "VeriTabani Arkadan Kaynat arkadan işlem başlatıldı");
            try{
                return bilgiyigonder();
            }catch(Exception e){
                e.printStackTrace();
                return "olmadı";
            }
        }

        public String bilgiyigonder() {
            HttpURLConnection connection = null;
            try{
                Log.i("tago" ,"Veri Tabani bilgiyi gonder" + is.trim());
                Log.i("tago" , "Veri Tabani bilgiyi gonder" + urrl);
                Log.i("tago" , "VeriTabani bilgiyi gonder" + longi);
                Log.i("tago" ,"VeriTabani bilgiyi gonder" + lat);
                connection = (HttpURLConnection)new URL("http://185.22.184.103/project/connection.php?name="+is+"&url="+urrl+"&long="+longi+"&lat="+lat).openConnection();
                Log.i("tago" ,"VeriTabani bagı kurdum");
            }catch(IOException e){
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try(OutputStream output = connection.getOutputStream()){
                output.write(query.getBytes(charset));
                //BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //while((inputline=in.readLine()) != null){
                 //   Log.i("tago" , inputline);
                //}in.close();
                //InputStream response = connection.getInputStream();
                Log.i("tago", "VeriTabani yazdım");
            }catch(IOException e){
                e.printStackTrace();
                Log.i("tago" , "VeriTabani yazamadım");
            }
            try {
                int status = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String inputline;
                while((inputline=in.readLine()) != null){
                    Log.i("tago" , inputline);
                    JsondanCevir(inputline);
                }in.close();
                Log.i("tago" , "VeriTabani status= " +status);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "alabama";
        }

        public void JsondanCevir(String x) {
            JSONArray jsonarray = null;
            String data = "";
            try {
                jsonarray = new JSONArray(x);
                Log.i("tago" , x);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("tago", "" + jsonarray.length());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonarray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                veritabani_id = jsonObject.optString("id").toString();
                Log.i("tago", "tur attım");
                data = data +  " id= " + veritabani_id;
            }
            Log.i("tago", data);
            Log.i("tago" ,veritabani_id );
            idSharedPreferenceKaydet(context);
        }

        private void idSharedPreferenceKaydet(Context a) {
            SharedPreferences sP = a.getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sP.edit();
            prefEditor.putString("veritabani_id", veritabani_id);
            Log.i("tago" , "Telefon hafızasına aldım veritabanı id = " + veritabani_id);
            prefEditor.commit();
        }


    }

    public class ArkadanVurdur extends AsyncTask<String , Void , String>{

        protected String doInBackground(String... params) {

            Log.i("tago" , "VeriTabani arkadan vurdurma başlatıldı");
            try{
                return lokasyonuyenilee();
            }catch(Exception e){
                e.printStackTrace();
                return "olmadı";
            }
        }

        private String lokasyonuyenilee() {
            URLConnection connection = null;
            try{
                connection = new URL("http://185.22.184.103/project/update_location.php?id="+id+
                        "&long="+longio+"&lat="+lato).openConnection();
                Log.i("tago" ,"VeriTabani Arkadan vurdurma bagı kurdum");
            }catch(IOException e){
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try(OutputStream output = connection.getOutputStream()){
                output.write(query.getBytes(charset));
                //InputStream response = connection.getInputStream();
                Log.i("tago" , "VeriTabani Arkadan vurdurma yazdım");
            }catch(IOException e){
                e.printStackTrace();
                Log.i("tago" , "VeriTabani Arkadan vurdurma yazamadım");
            }
            return "alabama";
        }
    }

    public class ArkadanCevir extends AsyncTask<String , Void , String> {
        protected String doInBackground(String... params) {

            Log.i("tago", "VeriTabani arkadan cevirme başlatıldı");
            try {
                return cevredekilerigor();
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadı" ;
            }
        }

        public String cevredekilerigor() {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://185.22.184.103/project/near_users.php?id=" + "98" +
                        "&long=" + "33.3332233" + "&lat=" + "33.3332233").openConnection();
                Log.i("tago", "VeriTabani Arkadan cevirme bagı kurdum");
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
                int status = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String inputline;
                while ((inputline = in.readLine()) != null) {
                    Log.i("tago", inputline);

                }
                in.close();
                Log.i("tago", "VeriTabani status= " + status);
                Log.i("tago", "VeriTabani Arkadan cevirme yazdım");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "VeriTabani Arkadan cevirme yazamadım");
            }
            return "alabama";
        }

    }
}
