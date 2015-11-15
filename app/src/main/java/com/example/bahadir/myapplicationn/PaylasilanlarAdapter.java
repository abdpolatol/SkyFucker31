package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class PaylasilanlarAdapter extends BaseAdapter {
    Context context;
    ArrayList<Paylasilanlar> paylasilanlarListesi ;
    LayoutInflater lala;
    Bitmap resim = null;
    int PAYLASILANYAZI = 0;
    int PAYLASILANTEKLIFOTO = 1;
    int PAYLASILANIKILIFOTO = 2;
    int PAYLASILANANKET = 3;

    public PaylasilanlarAdapter(Context context, ArrayList<Paylasilanlar> paylasilanlarListesi) {
        this.context = context;
        this.paylasilanlarListesi = paylasilanlarListesi;
        lala = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public int getCount() {
        return paylasilanlarListesi.size();
    }
    public Object getItem(int i) {
        return paylasilanlarListesi.get(i);
    }
    public long getItemId(int i) {
        return i;
    }
    public int getItemViewType(int position){
        Object item = getItem(position);
        Paylasilanlar paylasilan = (Paylasilanlar) item;
        if(paylasilan.cesit.equals("text")){
            return PAYLASILANYAZI;
        }else if(paylasilan.cesit.equals("teklifoto")){
            return PAYLASILANTEKLIFOTO;
        }else if(paylasilan.cesit.equals("ikilifoto")){
            return PAYLASILANIKILIFOTO;
        }else if(paylasilan.cesit.equals("anket")){
            return PAYLASILANANKET;
        }
        return -1;
    }


    public int getViewTypeCount() {
        return 4;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        PaylasilanHolder holder;
        final int pozisyon = position;
        Object currentpaylasilan = getItem(position);
        Paylasilanlar paylasilan = (Paylasilanlar) currentpaylasilan;
        if(convertView==null){
            holder = new PaylasilanHolder();
            if(paylasilan.cesit.equals("text")){

                convertView = lala.inflate(R.layout.paylasilanyazi,null);
                holder.tv1 = (TextView) convertView.findViewById(R.id.textView11);
                holder.tv2 = (TextView) convertView.findViewById(R.id.textView12);
                Log.i("tago" , "texttexttext");
            }else if(paylasilan.cesit.equals("teklifoto")){

                convertView = lala.inflate(R.layout.paylasilanresim,null);
                holder.tv3 = (TextView) convertView.findViewById(R.id.textView13);
                holder.image1 = (ImageButton) convertView.findViewById(R.id.imageButton11);
                Log.i("tago" , "tekliteklitekli");
            }else if(paylasilan.cesit.equals("ikilifoto")){
                Log.i("tago" , "ikiliikiliikili");
            }else if(paylasilan.cesit.equals("anket")){
                Log.i("tago" , "anketanketanket");
            }

            convertView.setTag(holder);
        }else{
            holder = (PaylasilanHolder)convertView.getTag();
        }

        if(paylasilan.cesit.equals("text")){
            holder.tv1.setText(paylasilanlarListesi.get(position).getGonderenid());
            holder.tv2.setText(paylasilanlarListesi.get(position).getYaziveyaurl());
        }else if(paylasilan.cesit.equals("teklifoto")){
            holder.tv3.setText(paylasilanlarListesi.get(position).getGonderenid());
            new urldenResim(holder.image1).execute(paylasilanlarListesi.get(position).getYaziveyaurl());
        }else if(paylasilan.cesit.equals("ikilifoto")){

        }else if(paylasilan.cesit.equals("anket")){

        }
        return convertView;
    }




    static class PaylasilanHolder{
        TextView tv1,tv2,tv3;
        ImageButton image1;
    }

    public class urldenResim extends AsyncTask<String , Void , Bitmap> {

        ImageView bmImage;

        public urldenResim(ImageView bmImage){
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);;
                connection.connect();
                Log.i("tago" , "Paylasilanlar Adapter connect sağladım");
                InputStream input = connection.getInputStream();
                resim = BitmapFactory.decodeStream(input);
                Log.i("tago" , "Paylasilanlar Adapter bitmap yaptım");
                return resim;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resim;
        }
        protected void onPostExecute(Bitmap bitmap) {
            bmImage.setImageBitmap(bitmap);
        }
    }
}
