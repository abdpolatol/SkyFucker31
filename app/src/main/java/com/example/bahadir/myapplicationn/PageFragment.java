package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ListView list1;
    View view;
    VeriTabani v ;
    String url;
    String charset;
    String query;
    CevredekileriGoster cG;
    ArrayList<Insann> InsanListesi;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }
    private String idSharedPreferenceAl() {
        SharedPreferences sP = getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sP.getString("veritabani_id", "default id");
        Log.i("tago", "Page Fragment haf�zadan ula�t�m veritaban� id = " + veritabani_id);
        return veritabani_id;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab1, container, false);
        list1 = (ListView) view.findViewById(R.id.listView);
        Log.i("tago", "Page Fragment onCreateView");
        String veritabani_id = idSharedPreferenceAl();
        if (veritabani_id != "default") {
            cevredekilericek(veritabani_id);
        } else if(veritabani_id=="default"){
            Log.i("tago", "default ald�n �u anda 800 milisaniye kaybettin");
            Thread idyibeklepic = new Thread() {
                public void run(){
                    try {
                        sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            idyibeklepic.start();
            String veritabani_idd= idSharedPreferenceAl();
            cevredekilericek(veritabani_idd);
        }else{
            Log.i("tago" , "Anan� avrad�n� sikerim senin");
        }
            return view;
    }
    private void cevredekilericek(String veritabani_id) {
        cG = new CevredekileriGoster();
        cG.execute(veritabani_id);
    }

    private class CevredekileriGoster extends AsyncTask<String , Void , String>{

        protected String doInBackground(String... params) {
            url = "http://185.22.184.103/project/connection.php?name=bahadirturk&url=http://www.google.com&long=33.2132123&lat=33.2322322";
            charset = "ISO-8859-9";
            String param1 = "id";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tago", "Page Fragment cevredekileri goster ba�lat�ld�");
            try {
                return cevredekilerigor(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmad�" ;
            }
        }

        public String cevredekilerigor(String id) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://185.22.184.103/project/near_users.php?id="+ id).openConnection();
                Log.i("tago", "Page Fragment cevredekileri gor bag� kuruldu");
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
                    InsanListesi = new ArrayList<Insann>();
                    JSONArray jsono = new JSONArray(inputline);
                    for(int i = 0 ; i < jsono.length() ; i++){
                        JSONObject object = jsono.getJSONObject(i);
                        Insann insann = new Insann();
                        insann.setId(object.optString("id"));
                        insann.setName(object.optString("name"));
                        insann.setUrl(object.optString("profile_picture"));
                        insann.setUzaklik(object.optString("distance"));
                        InsanListesi.add(insann);
                    }
                }
                in.close();
                Log.i("tago", "VeriTabani status= " + status);
                Log.i("tago", "Page Fragment cevredekileri gor inputline yazd�m");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Page Fragment cevredekileri gor yazamad�m");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tago" , "json Exception");
            }
            return "inputline";
        }

        @Override
        protected void onPostExecute(String s) {
            InsannAdapter adapter = new InsannAdapter (getActivity() , R.layout.listview_item , InsanListesi);
            list1.setAdapter(adapter);
        }
    }
}