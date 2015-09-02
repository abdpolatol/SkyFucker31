package com.example.bahadir.myapplicationn;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class InsannAdapter extends ArrayAdapter<Insann>{

    ArrayList<Insann> objects;
    int resource;
    Context context;
    LayoutInflater lala;
    public InsannAdapter(Context context, int resource, ArrayList<Insann> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        lala = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InsannHolder holder ;
        if ( convertView == null){
            convertView = lala.inflate(resource , null);
            holder = new InsannHolder();
            holder.image1 = (ImageView) convertView.findViewById(R.id.imgIcon);
            holder.text1 = (TextView) convertView.findViewById(R.id.txtTitle);
            convertView.setTag(holder);
        }else{
            holder = (InsannHolder)convertView.getTag();
        }
            holder.text1.setText(objects.get(position).getName());
            new urldenResim(holder.image1).execute(objects.get(position).getUrl());

        return convertView;
    }

    static class InsannHolder{
        public ImageView image1;
        public TextView text1;
    }

    public class urldenResim extends AsyncTask<String , Void , Bitmap>{

        ImageView bmImage;

        public urldenResim(ImageView bmImage){
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            Bitmap icon = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);;
                connection.connect();
                Log.i("tago" , "Insan Adapter connect sağladım");
                InputStream input = connection.getInputStream();
                icon = BitmapFactory.decodeStream(input);
                Log.i("tago" , "Insan Adapter bitmap yaptım");
                return icon;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return icon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bmImage.setImageBitmap(bitmap);
        }
    }
}
