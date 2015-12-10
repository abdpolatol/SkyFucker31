package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.ContextWrapper;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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
    Button buton1;
    Bitmap resim,kapakresmi;
    String kapakresmiurl;
    String isim;
    private int mPage;

    //Facebook part
    ProfileTracker protracker;
    AccessTokenTracker tracker;
    private CallbackManager mCallbackManager;
    AccessToken accessToken;
    Profile profile;
    String yenicoverfotourl;
    String yeniresimurlidsi;
    public void onStart() {
        super.onStart();
        Log.i("tago", "FragmentDonguleri frag 3 onStart");
    }


    public void onPause() {
        super.onPause();
        Log.i("tago", "FragmentDonguleri frag 3 onPause");
    }


    public void onDestroy() {
        super.onDestroy();
        Log.i("tago", "FragmentDonguleri frag 3 onDestroy");
    }
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
        mCallbackManager = CallbackManager.Factory.create();
    }
    private void tanimlar(View view) {
        image1 = (ImageView) view.findViewById(R.id.imageView);
        image2 = (ImageView) view.findViewById(R.id.imageView3);
        image3 = (ImageView) view.findViewById(R.id.imageView4);
        image4 = (ImageView) view.findViewById(R.id.imageviewkapak);
        tv1 = (TextView) view.findViewById(R.id.textView);
        tv2 = (TextView) view.findViewById(R.id.textView5);
        tv3 = (TextView) view.findViewById(R.id.textView6);
        buton1 = (Button) view.findViewById(R.id.button15);
        image1.setImageBitmap(resim);
        tv1.setText(isim);
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
        buton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Güncel profil fotografınız onaylanıyor", Toast.LENGTH_SHORT).show();
                tracker = new AccessTokenTracker() {
                    protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken nev) {
                        Log.i("tago", "accesstokendeğişti");
                        GraphRequest request = GraphRequest.newMeRequest(nev,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            yenicoverfotourl = object.getJSONObject("cover").getString("source");
                                            Log.i("tago", "cover photo = " + yenicoverfotourl);
                                            sharedcoverurlkaydet(yenicoverfotourl);
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                    }

                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,cover");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                };
                protracker = new ProfileTracker() {
                    protected void onCurrentProfileChanged(Profile oldpro, Profile newpro) {
                        Log.i("tago" , "profile degisti");
                        yeniresimurlidsi = newpro.getId();
                        if(yeniresimurlidsi!=null){
                            NettenIndir nettenIndir = new NettenIndir();
                            nettenIndir.execute(yeniresimurlidsi);
                        }
                    }
                };
                tracker.startTracking();
                protracker.startTracking();
            }
        });
        UrldenResim urldenResim = new UrldenResim(image4);
        urldenResim.execute(kapakresmiurl);
        image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                programdancik();
            }
        });
    }
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        Log.i("tago", "FragmentDonguleri frag 3 onResume");
    }
    public void onStop(){
        super.onStop();
        if(tracker!=null){
            tracker.stopTracking();
        }
        if(protracker!=null){
            protracker.stopTracking();
        }
        Log.i("tago", "FragmentDonguleri frag 3 onStop");
    }
    private void sharedcoverurlkaydet(String yenicoverfoto) {
        SharedPreferences sP =getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sP.edit();
        prefEditor.putString("coverurl", yenicoverfoto);
        prefEditor.commit();
    }
    private void sharedkullaniciciktikeydet(boolean x) {
        SharedPreferences sP =getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sP.edit();
        prefEditor.putBoolean("kullanicicikti", x);
        prefEditor.commit();
    }
    private void programdancik() {
        LoginManager.getInstance().logOut();
        kullanicicikti = true;
        sharedkullaniciciktikeydet(kullanicicikti);
        getActivity().finish();
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
    public class NettenIndir extends AsyncTask<String , Void , Bitmap>{

        Bitmap bitmap;

        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL("https://graph.facebook.com/" + params[0]+ "/picture?type=large");
                String urll = "https://graph.facebook.com/" + params[0]+ "/picture?type=large";
                sharedresimurlkaydet(urll);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                Log.i("tago" , "PageFragment3 connect sağladım");
                InputStream input = connection.getInputStream();
                bitmap  = BitmapFactory.decodeStream(input);
                bitmapiinternalkaydet(bitmap);
                Log.i("tago" , "PageFragment3 bitmap yaptım");
                if(bitmap == null){
                    Log.i("tago" , "PageFragment3 bitmap yok");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            Log.i("tago", "PageFragment3 bitmap yerleştirdim");
            image1.setImageBitmap(bitmap);
            }
        }

    private String bitmapiinternalkaydet(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(getActivity());
        File directory = cw.getDir("userpro", Context.MODE_PRIVATE);
        File mypath=new File(directory,"kullaniciresmi.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    private void sharedresimurlkaydet(String urll) {
        SharedPreferences sP =getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sP.edit();
        prefEditor.putString("resimurl", urll);
        prefEditor.commit();
    }
}