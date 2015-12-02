package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PageFragment3 extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    boolean kullanicicikti=false;
    ImageView image1 , image2,image3,image4;
    TextView tv1 , tv2 ,tv3 ;
    Bitmap resim,kapakresmi;
    String kapakresmiurl;
    String isim;
    private int mPage;

    public static PageFragment3 newInstance(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, page);
        PageFragment3 fragment = new PageFragment3();
        fragment.setArguments(bundle);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedkullaniciciktikeydet(kullanicicikti);
        mPage = getArguments().getInt(ARG_PAGE);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    private void sharedkullaniciciktikeydet(boolean x) {
        SharedPreferences sP =getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sP.edit();
        prefEditor.putBoolean("kullanicicikti" , x);
        prefEditor.commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getActivity().getIntent().getExtras();
        isim = b.getString("isim");
        resim = b.getParcelable("resim");
        kapakresmiurl = b.getString("kapakresmiurl");
        View view = inflater.inflate(R.layout.profil, container, false);
        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tanimlar(view);
        UrldenResim urldenResim = new UrldenResim(image4);
        urldenResim.execute(kapakresmiurl);
        image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                programdancik();
            }
        });
    }

    private void programdancik() {
        LoginManager.getInstance().logOut();
        kullanicicikti = true;
        sharedkullaniciciktikeydet(kullanicicikti);
        getActivity().finish();
    }

    private void tanimlar(View view) {
        image1 = (ImageView) view.findViewById(R.id.imageView);
        image2 = (ImageView) view.findViewById(R.id.imageView3);
        image3 = (ImageView) view.findViewById(R.id.imageView4);
        image4 = (ImageView) view.findViewById(R.id.imageviewkapak);
        tv1 = (TextView) view.findViewById(R.id.textView);
        tv2 = (TextView) view.findViewById(R.id.textView5);
        tv3 = (TextView) view.findViewById(R.id.textView6);
        image1.setImageBitmap(resim);
        tv1.setText(isim);
    }



    public class UrldenResim extends AsyncTask<String , Void , Bitmap> {

        ImageView bmImage;

        public UrldenResim(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... params) {
            URL url;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                ;
                connection.connect();
                Log.i("tago", "Insan Adapter connect sağladım");
                InputStream input = connection.getInputStream();
                kapakresmi = BitmapFactory.decodeStream(input);
                Log.i("tago", "Insan Adapter bitmap yaptım");
                return kapakresmi;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return kapakresmi;
        }

        protected void onPostExecute(Bitmap bitmap) {
            bmImage.setImageBitmap(bitmap);
        }
    }
}