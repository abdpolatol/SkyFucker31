package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class KanalAdapter extends BaseAdapter {

    Context context;
    String charset;
    String query;
    String kanaladi;
    String veritabani_id;
    ArrayList<OfficialKanal> officialKanals;
    ArrayList<NormalKanal> normalKanals;
    ArrayList<Object> kanallar = new ArrayList();
    ArrayList<Kanal> channelbaba;
    int OFFICIAL_KANAL = 0;
    int NORMAL_KANAL= 1;
    LayoutInflater lala;
    KanalaElemanEkle kEE;
    public KanalAdapter(Context context , ArrayList<OfficialKanal> officiallar , ArrayList<NormalKanal> normaller
            , ArrayList<Kanal> channelbaba){
        this.context = context;
        officialKanals = officiallar;
        normalKanals = normaller;
        this.channelbaba = channelbaba;
        for(int i = 0 ; i < officiallar.size() ; i++){
            kanallar.add(officiallar.get(i));
        }
        for(int i = 0 ; i < normaller.size() ; i++){
            kanallar.add(normaller.get(i));
        }
        Log.i("tago" , "tagtag");
        veritabani_id = SharedIdCek();
        lala = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    private String SharedIdCek() {
        SharedPreferences sp = context.getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sp.getString("veritabani_id", "default id");
        return veritabani_id;
    }
    public int getCount() {
        return (channelbaba.size());
    }
    public Object getItem(int i) {
            Log.i("tago" , "getItem = " + channelbaba.get(i));
        return channelbaba.get(i);

    }
    public long getItemId(int i) {
        Log.i("tago" , "getItemId =" + i);
        return i;
    }
    public int getItemViewType(int position) {
        Object item = getItem(position-1);
        Kanal kanal = (Kanal)item;
        if(kanal.official){
            return OFFICIAL_KANAL;
        }else if(!kanal.official){
            return NORMAL_KANAL;
        }
        return -1;
    }
    public int getViewTypeCount() {
        return 2;
    }
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        KanalHolder holder = null;
        final int pozisyon = position;

        Object currentKanal = getItem(position);
        Kanal kanal = (Kanal) currentKanal;
        if(convertView==null) {
            holder = new KanalHolder();
            if (kanal.official) {
                convertView = lala.inflate(R.layout.officialkanal, null);
                holder.image2 = (ImageView) convertView.findViewById(R.id.imageView5);
                holder.image3 = (ImageButton) convertView.findViewById(R.id.button7);
                holder.tv3 = (TextView) convertView.findViewById(R.id.textView4);
                holder.tv4 = (TextView) convertView.findViewById(R.id.textView8);
                holder.buton1 = (ImageButton) convertView.findViewById(R.id.button7);
                holder.buton2 = (ImageButton) convertView.findViewById(R.id.button8);
                holder.buton3 = (ImageButton) convertView.findViewById(R.id.imageButton7);
                Log.i("tago", "tagtagatagtagtagatg");
            }
            if (!kanal.official) {
                convertView = lala.inflate(R.layout.normalkanal, null);
                holder.image1 = (ImageView) convertView.findViewById(R.id.imageView5);
                holder.tv1 = (TextView) convertView.findViewById(R.id.textView4);
                holder.tv2 = (TextView) convertView.findViewById(R.id.textView8);
                Log.i("tago", "tagtagtag");
            }

            convertView.setTag(holder);
        }else{
            holder = (KanalHolder)convertView.getTag();
        }
            if(kanal.official){
                holder.image2.setBackgroundResource(R.mipmap.cropped);
                Log.i("tago", "kanaladapter position = " + channelbaba.get(position).getLikedurumu());
                holder.tv3.setText(channelbaba.get(position).getKanaladi());
                holder.image2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        kanaladi = channelbaba.get(position).getKanaladi();
                        Log.i("tago", kanaladi);
                        kEE = new KanalaElemanEkle();
                        kEE.execute(kanaladi);
                        Intent intent = new Intent(context, GrupSohbeti.class);
                        intent.putExtra("kanaladi", kanaladi);
                        context.startActivity(intent);
                    }
                });
                if(channelbaba.get(position).getLikedurumu()== 1){
                    //holder.image3.setImageResource(R.mipmap.heartson);
                    Log.i("tago" , "likedurumu1");
                }else{
                    holder.image3.setImageResource(R.mipmap.heartsonn);
                    Log.i("tago", "likedurumu2");
                }
                final ImageButton image1 = holder.image3;
                holder.image3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        channelbaba.get(pozisyon).setLikedurumu(1);
                        //image1.setImageResource(R.mipmap.heartson);
                        image1.setClickable(false);
                        Log.i("tago" , "butona tıklanıldı");
                        String channelid = channelbaba.get(pozisyon).getId();
                        Log.i("tago" , "channel id= " + channelid);
                        officialkanalilikela(channelbaba.get(pozisyon).getId());
                    }
                });
            }
            if(!kanal.official){
                holder.image1.setBackgroundResource(R.mipmap.odtu);
                holder.tv1.setText(channelbaba.get(position).getKanaladi());
                holder.image1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        kanaladi = channelbaba.get(pozisyon).getKanaladi();
                        Log.i("tago" , kanaladi);
                        kEE = new KanalaElemanEkle();
                        kEE.execute(kanaladi);
                        Intent intent = new Intent(context , GrupSohbeti.class);
                        intent.putExtra("kanaladi" , kanaladi);
                        context.startActivity(intent);
                    }
                });
            }
        return convertView;
    }
    private void officialkanalilikela(String kanalid) {
        OfficialKanaliLike oKL = new OfficialKanaliLike();
        oKL.execute(kanalid);
    }

    static class KanalHolder{
        public ImageView image1,image2;
        public ImageButton image3,image4;
        public TextView tv1,tv2,tv3,tv4;
        public ImageButton buton1,buton2,buton3;
    }

    private class KanalaElemanEkle extends AsyncTask<String,Void,String>{
        protected String doInBackground(String... params) {
            charset = "utf-8";
            String param1 = "id";
            String param2 = "name";
            try {
                query = String.format("param1=%s&param2=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tago", "KanalAdapter kanala eleman ekleme ba�lat�ld�");
            try {
                return kanaliekle(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadi";
            }
        }

        private String kanaliekle(String kanaladi) {
            HttpURLConnection sconnection = null;
            try {
                sconnection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/join_us.php?id=" + veritabani_id + "&name=" + kanaladi).openConnection();
                Log.i("tago", "KanalAdapter kanali ekleme bağı kuruldu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sconnection.setDoOutput(true);
            sconnection.setDoInput(true);
            sconnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            sconnection.setRequestProperty("Accept", "* /*");
            sconnection.setRequestProperty("Accept-Charset", charset);
            sconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream output = new BufferedOutputStream(sconnection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                InputStream response = sconnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "alabama";
        }
    }
    private class OfficialKanaliLike extends AsyncTask<String,Void,String>{

        protected String doInBackground(String... strings) {
            charset = "utf-8";
            String param1 = "kanalid";
            String param2 = "ekleyenid";
            String param3 = "type";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset)
                , URLEncoder.encode(param3,charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tago", "KanalAdapter official kanali like etme başlatıldı");
            try {
                return officiallike(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadi";
            }
        }

        private String officiallike(String string) {
            HttpURLConnection vconnection = null;
            String kullaniciid= SharedIdCek();
            Log.i("tago" , "kanalid= "+string);
            Log.i("tago", "kullaniciid= " + kullaniciid);
            try {
                vconnection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/like_channel.php?id=" + string + "&userid="
                        + kullaniciid+"&type=2").openConnection();
                Log.i("tago", "KanalAdapter official like etme bağı kuruldu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            vconnection.setDoOutput(true);
            vconnection.setRequestProperty("Accept-Charset", charset);
            vconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream output = new BufferedOutputStream(vconnection.getOutputStream());
                output.write(query.getBytes(charset));
                InputStream response = vconnection.getInputStream();
                Log.i("tago" , "olması lazımm");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "alabama";
        }
    }
}
