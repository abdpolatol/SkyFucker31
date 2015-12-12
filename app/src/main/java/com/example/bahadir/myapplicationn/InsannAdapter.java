package com.example.bahadir.myapplicationn;


import android.app.Dialog;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
                public void onClick(View v) {
                    kullanicilikela(objects.get(pozisyon).getId());
                    Log.i("tago", "like butonu tıklandı");
                }
            });
            holder.reportbutonu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    kullanıcıreportet(objects.get(pozisyon).getId());
                    Log.i("tago" , "report butonu tıklandı");
                }
            });
            holder.esasbolge.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("tago" , "esas bölge tıklandı");
                    String name = objects.get(pozisyon).getName();
                    String resim = objects.get(pozisyon).getUrl();
                    String id = objects.get(pozisyon).getId();
                    try {
                        icon = new urldenResimm().execute(objects.get(pozisyon).getUrl()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context , Mesajlasma.class);
                    intent.putExtra("id" , id);
                    intent.putExtra("isim" , name);
                    intent.putExtra("resimurl" , resim);
                    intent.putExtra("intentname", "InsanAdapter");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("kullaniciresmi" , icon);
                    intent.putExtra("kullaniciresmi" , bundle);
                    context.startActivity(intent);
                }
            });
        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tago", "kullanıcı resmi tıklandı");
            }
        });

        return convertView;
    }
    private void kullanıcıreportet(final String id) {
        final String idd = id;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogreport);
        dialog.getWindow().setDimAmount(0.7f);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        final EditText etv1 = (EditText) dialog.findViewById(R.id.editText9);
        Button buton1 = (Button) dialog.findViewById(R.id.button14);
        buton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String reportyazisi = etv1.getText().toString();
                OndenReportEt oRE = new OndenReportEt(reportyazisi);
                oRE.execute(idd);
                dialog.dismiss();
            }
        });
    }
    private void kullanicilikela(String userid) {
        OndenLikeAt oLA = new OndenLikeAt();
        oLA.execute(userid);
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
                Log.i("tago" , "resmi gecici hafizadan cektim");
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
                if(bitmap!=null){
                    addBitmapToMemoryCache(fotoid, bitmap);
                    Log.i("tago", "nasıl null değil");
                    Bitmap yuvarlakbitmap = getCircleBitmap(bitmap);
                    bmImage.setImageBitmap(yuvarlakbitmap);
                }
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
    public class OndenLikeAt extends AsyncTask<String , Void , String>{

        String charset;
        String query;
        public OndenLikeAt(){
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
        protected String doInBackground(String... strings) {
            URLConnection connection = null;
            Log.i("tago","likeidsi :"+strings[0]);
            try {
                connection = new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/update_location.php?id=").openConnection();
                Log.i("tago" , "InsanAdapter like atma işlemi başladı");
            } catch (IOException e) {
                e.printStackTrace();
            }

            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(query.getBytes(charset));
                InputStream response = connection.getInputStream();
                Log.i("tago" , "Insan Adapter like atma veri tabanına gönderildi");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago" , "Insan Adapter like atma veri tabanına gönderilemedi");
            }
            return "mavifacebook";
        }

        protected void onPostExecute(String result){

        }

    }
    public class OndenReportEt extends AsyncTask<String , Void , String>{

        String charset , query;
        public OndenReportEt(String reportyazisi){
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
        protected String doInBackground(String... strings) {
            URLConnection connection = null;
            Log.i("tago", "reportidsi :" + strings[0]);
            try {
                connection = new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/update_location.php?id=d").openConnection();
                Log.i("tago" , "InsanAdapter report etme işlemi başladı");
            } catch (IOException e) {
                e.printStackTrace();
            }

            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(query.getBytes(charset));
                InputStream response = connection.getInputStream();
                Log.i("tago" , "InsanAdapter report etme işlemi yapıldı");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago" , "Report etme işlemi yapılamadı");
            }
            return "wifii";
        }
    }
    public class urldenResimm extends AsyncTask<String, Void, Bitmap> {


        public urldenResimm() {
        }

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

        @Override
        protected void onPostExecute(Bitmap bitmap) {
        }
    }
}
