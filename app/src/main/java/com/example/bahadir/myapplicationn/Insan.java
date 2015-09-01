package com.example.bahadir.myapplicationn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Insan {
    public String url;
    public String title;
    public int icon;
    Bitmap myBitmap;

    public Insan(){
        super();
    }
    public Insan(int icon , String title){
        super();
        //this.url = url;
        this.title = title;
        this.icon = icon;
        //UrlToBitmap a = new UrlToBitmap();
        //a.execute(url);
    }

    public class UrlToBitmap extends AsyncTask<String , Void , Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
            }
            return null;
        }
    }
}

