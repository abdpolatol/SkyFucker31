package com.example.bahadir.myapplicationn;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class InsannAdapter extends ArrayAdapter<Insann>{

    public LruCache<String,Bitmap> memoryCache;
    ArrayList<Insann> objects;
    int resource;
    Context context;
    LayoutInflater lala;
    Bitmap icon = null;
    public InsannAdapter(Context context, int resource, ArrayList<Insann> objects) {
        super(context, resource, objects);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        Log.i("tago", String.valueOf(cacheSize));
        memoryCache = new LruCache<String, Bitmap>(cacheSize){

            protected int sizeOf(String key, Bitmap bitmap) {
                Log.i("tago" , String.valueOf(bitmap.getByteCount()/1024));
                return bitmap.getByteCount()/1024 ;
            }
        };
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        lala = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        InsannHolder holder ;
        final int pozisyon = position;
        if ( convertView == null){
            convertView = lala.inflate(resource , null);
            holder = new InsannHolder();
            holder.image1 = (ImageView) convertView.findViewById(R.id.imgIcon);
            holder.text1 = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.likebutonu=(ImageButton) convertView.findViewById(R.id.imageButton2);
            holder.reportbutonu = (ImageButton) convertView.findViewById(R.id.imageButton3);
            holder.esasbolge = (LinearLayout) convertView.findViewById(R.id.esasbolge);
            convertView.setTag(holder);
        }else{
            holder = (InsannHolder)convertView.getTag();
        }
            holder.image1.setImageResource(R.mipmap.ins);
            holder.text1.setText(objects.get(position).getName());
            new urldenResim(holder.image1).execute(objects.get(position).getUrl());
            holder.likebutonu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tago", "like butonu tıklandı");
                }
            });
            holder.reportbutonu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tago" , "report butonu tıklandı");
                }
            });
            holder.esasbolge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tago" , "esas bölge tıklandı");
                    String name = objects.get(pozisyon).getName();
                    String resim = objects.get(pozisyon).getUrl();
                    String id = objects.get(pozisyon).getId();
                    Intent intent = new Intent(context , Mesajlasma.class);
                    intent.putExtra("id" , id);
                    intent.putExtra("isim" , name);
                    intent.putExtra("resimurl" , resim);
                    Bundle xxx = new Bundle();
                    xxx.putParcelable("iccon" , icon);
                    intent.putExtra("icon" , xxx);
                    context.startActivity(intent);
                }
            });
        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tago" , "kullanıcı resmi tıklandı");
            }
        });

        return convertView;
    }

    static class InsannHolder{
        public ImageView image1;
        public TextView text1;
        public ImageButton likebutonu;
        public ImageButton reportbutonu;
        public LinearLayout esasbolge;
    }

    public class urldenResim extends AsyncTask<String , Void , Bitmap>{

        Bitmap bitmape;
        boolean eldevar;
        ImageView bmImage;
        String fotoid;
        boolean hafizadayok = true;
        public urldenResim(ImageView bmImage){
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... params) {
            loadBitmapFromCache(params[0]);

            if(hafizadayok==true) {
                URL url = null;
                try {
                    url = new URL(params[0]);
                    fotoid = params[0];
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    Log.i("tago", "Insan Adapter connect sağladım");
                    InputStream input = new BufferedInputStream(connection.getInputStream());
                    icon = BitmapFactory.decodeStream(input);
                    //icon = decodeBitmapFromResources(input,65,65);
                    Log.i("tago", "Insan Adapter bitmap yaptım");
                    return icon;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return icon;
            }else{
                Log.i("tago" , "yanlış yapıyosun");
                return null;
            }
        }

        private Bitmap decodeBitmapFromResources(InputStream input , int istenilengenislik , int istenilenboy) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input,null,options);
                options.inSampleSize = calculateInSampleSize(options, 2, 2);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(input,null,options);
        }
        private int calculateInSampleSize(BitmapFactory.Options options, int istenilencozunurlukx , int istenilencozunurluky){
            final int boy = options.outHeight;
            final int genislik = options.outWidth;
            int sampleSize = 1;

            if(boy>istenilencozunurluky || genislik >istenilencozunurlukx) {
                final int yariboy = boy / 2;
                final int yarigenislik = genislik / 2;

                while ((yariboy / sampleSize) > istenilencozunurluky && (yarigenislik / sampleSize) > istenilencozunurlukx) {
                    sampleSize = sampleSize * 2;
                    Log.i("tago" , "sample sizee = " + sampleSize);
                }
            }
            return sampleSize;
        }
        private void loadBitmapFromCache(String key) {
            bitmape = getBitmapFromMemoryCache(key);
            if(bitmape!=null){
                eldevar=true;
                Log.i("tago" , "olabilirolabilirolabilir");
                hafizadayok = false;
            }else{
                eldevar = false;
                Log.i("tago" , "bitmap is null");
                hafizadayok = true;
            }
        }
        protected void onPostExecute(Bitmap bitmap) {
            if(eldevar==true){
                Bitmap yuvarlakbitmape = getCircleBitmap(bitmape);
                bmImage.setImageBitmap(yuvarlakbitmape);
            }if(eldevar==false){
                addBitmapToMemoryCache(fotoid, bitmap);
                Bitmap yuvarlakbitmap = getCircleBitmap(bitmap);
                bmImage.setImageBitmap(yuvarlakbitmap);
            }
        }
        private Bitmap getCircleBitmap(Bitmap b) {
            final Bitmap output = Bitmap.createBitmap(b.getWidth(),b.getHeight(),Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);
            final int color = Color.RED;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0,0,b.getWidth(),b.getHeight());
            final RectF rectf = new RectF(rect);

            paint.setAntiAlias(true);
            paint.setColor(color);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawOval(rectf, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(b, rect, rect, paint);
            return output;
        }
        private void addBitmapToMemoryCache(String key , Bitmap bitmap) {
            Log.i("tago" ,"addBitmapToMemoryCache" );
            if(getBitmapFromMemoryCache(key)==null){
                memoryCache.put(key,bitmap);
                Log.i("tago" ,"memory cache key :" + key);
            }
        }
        private Bitmap getBitmapFromMemoryCache(String key) {
            Log.i("tago" , "getBitmapFromMemoryCache");
            Log.i("tago", "getbitmap keyi :" + key);
            return memoryCache.get(key);
        }
    }
}
