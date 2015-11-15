package com.example.bahadir.myapplicationn;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class YaziPaylas extends Activity {

    EditText etv1;
    Button buton1;
    String yazi;
    String query;
    String charset;
    String veritabaniid;
    ArkadanYaziGonder aYG;
    PaylasilanlariCekk pC;
    ArrayList<Paylasilanlar> PaylasilanlarListesi;
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.yazipaylas);
        etv1 = (EditText) findViewById(R.id.editText4);
        veritabaniid= sharedPrefIdAl();
        buton1 = (Button) findViewById(R.id.button3);
        buton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                yazi = etv1.getText().toString();
                yaziyiServeraGonder(yazi);
                //ListView liste1 = (ListView) findViewById(R.id.listView3);
                //liste1.removeAllViews();
                //pC = new PaylasilanlariCekk();
                //pC.execute(veritabaniid);
            }
        });
    }
    private String sharedPrefIdAl() {
        SharedPreferences sp = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sp.getString("veritabani_id", "default id");
        Log.i("tago", "YaziPaylas veritabani id = " + veritabani_id);
        return veritabani_id;
    }
    private void yaziyiServeraGonder(String yazi){
        aYG = new ArkadanYaziGonder();
        aYG.execute(yazi);
    }

    public class ArkadanYaziGonder extends AsyncTask<String,Void,String>{

        protected String doInBackground(String... params) {
            String param1 = "id";
            String param2 = "type";
            String param3 = "text";
            charset = "UTF-8";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                        URLEncoder.encode(param3, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i("tago", "VeriTabani arkadan yazi gonderme başlatıldı");
            try{
                return yaziyigonder(params[0]);
            }catch(Exception e){
                e.printStackTrace();
                return "olmadı";
            }
        }

        private String yaziyigonder(String yazii) {
            URLConnection connection = null;

            try{
                Log.i("tago" , yazii);
                Log.i("tago" , veritabaniid);
                connection = new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/paylas.php?userid="+veritabaniid
                        +"&type=text"+"&text="+yazii).openConnection();
                Log.i("tago" ,"Arkadan Yazi Gonder bagı kurdum");
            }catch(IOException e){
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try{
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                InputStream response = connection.getInputStream();
                Log.i("tago" , "Arkadan Yazi Gonder yazdım");
            }catch(IOException e){
                e.printStackTrace();
                Log.i("tago" , "Arkadan Yazi Gonder yazamadım");
            }
            return "alabama";
        }
    }
    public class PaylasilanlariCekk extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String param1 = "id";
            charset = "UTF-8";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i("tago", "YaziPaylas paylasilanlari alma baÅŸlatÄ±ldÄ±");
            try {
                return paylasilanlaricek(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadÄ±";
            }
        }

        private String paylasilanlaricek(String veritabaniidd) {
            HttpURLConnection connection = null;

            try {
                Log.i("tago", veritabaniidd);
                connection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/paylasilanlari_al?id=" + veritabaniidd).openConnection();
                Log.i("tago", "Paylasinlari alma bagÄ± kurdum");
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                try {
                    int a = connection.getResponseCode();
                    String b = connection.getResponseMessage();
                    Log.i("tago", "rerere" + a + " " + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader in;
                if (connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i("tago", "InputStream");
                    String inputline = null;

                    for (int i = 0; i < 3; i++) {
                        inputline = in.readLine();
                        Log.i("tago", "" + i + " for inputline= " + inputline);
                    }
                    PaylasilanlarListesi = new ArrayList();
                    JSONArray jsono = new JSONArray(inputline);
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        Paylasilanlar paylasilan = new Paylasilanlar();
                        paylasilan.setVeriid(object.optString("id"));
                        paylasilan.setGonderenid(object.optString("user_id"));
                        paylasilan.setCesit(object.optString("type"));
                        paylasilan.setYaziveyaurl(object.optString("text"));
                        paylasilan.setDate(object.optString("date"));
                        PaylasilanlarListesi.add(paylasilan);
                    }
                } else {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    Log.i("tago", "Error Stream");
                    String inputline;
                    while ((inputline = in.readLine()) != null) {
                        Log.i("tago", "camcamcam" + inputline);
                    }
                    PaylasilanlarListesi = new ArrayList();
                    JSONArray jsono = new JSONArray(inputline);
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        Paylasilanlar paylasilan = new Paylasilanlar();
                        paylasilan.setVeriid(object.optString("id"));
                        paylasilan.setGonderenid(object.optString("user_id"));
                        paylasilan.setCesit(object.optString("type"));
                        paylasilan.setYaziveyaurl(object.optString("text"));
                        paylasilan.setDate(object.optString("date"));
                        PaylasilanlarListesi.add(paylasilan);
                    }
                }
                in.close();
                Log.i("tago", "Page Fragment cevredekileri gor inputline yazdï¿½m");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Page Fragment cevredekileri gor yazamadï¿½m");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tago", "json Exception");
            }
            return "inputline";
        }

        protected void onPostExecute(String s) {
            //PaylasilanlarAdapter adapter = new PaylasilanlarAdapter(getActivity() , PaylasilanlarListesi);
            //liste1.setAdapter(adapter);
        }
    }

}