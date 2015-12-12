package com.example.bahadir.myapplicationn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Insann {

        public String name;
        public String url;
        public String id;
        public String uzaklik;

        public Insann() {

        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            Log.i("tago" , "Insann url " + url );
            this.url=url;

        }
        public String getUzaklik() {
            return uzaklik;
        }
        public void setUzaklik(String uzaklik) {
            this.uzaklik = uzaklik;
        }


    public class urldenResim extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... params) {
            URL url;
            Bitmap icon = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                Log.i("tago", "Insan Adapter connect sağladım");
                InputStream input = connection.getInputStream();
                icon = BitmapFactory.decodeStream(input);
                Log.i("tago", "Insan Adapter bitmap yaptım");
                return icon;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap bitmap) {
            Log.i("tago" , "InsanClassi resim indirme islemi yapildi");

        }
    }
}


