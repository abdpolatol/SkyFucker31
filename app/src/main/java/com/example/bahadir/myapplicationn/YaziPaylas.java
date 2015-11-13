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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class YaziPaylas extends Activity {

    EditText etv1;
    Button buton1;
    String yazi;
    String query;
    String charset;
    String veritabaniid;
    ArkadanYaziGonder aYG;
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.yazipaylas);
        etv1 = (EditText) findViewById(R.id.editText4);
        veritabaniid= sharedPrefİdAl();
        buton1 = (Button) findViewById(R.id.button3);
        buton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                yazi = etv1.getText().toString();
                yaziyiServeraGonder(yazi);
            }
        });
    }
    private String sharedPrefİdAl() {
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

    }


