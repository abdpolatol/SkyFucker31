package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.pushy.sdk.Pushy;

public class MainActivity extends AppCompatActivity {

    private String sharedcoverurlal() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String coverurl = sP.getString("coverurl","defaultcoverurl");
        Log.i("tago", "Takip Servisi hafızadan ulaştım coverurl= " + coverurl);
        return coverurl;
    }
    private String sharedresimurlal() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String resimurl = sP.getString("resimurl","defaultresimurl");
        Log.i("tago", "Takip Servisi hafızadan ulaştım resimurl = " + resimurl);
        return resimurl;
    }
    private String sharedemailal() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String email = sP.getString("email","defaultemail");
        Log.i("tago", "Takip Servisi hafızadan ulaştım email = " + email);
        return email;
    }
    private String sharedcinsiyetal() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String cinsiyet = sP.getString("cinsiyet","defaultcinsiyet");
        Log.i("tago", "Takip Servisi hafızadan ulaştım cinsiyet = " + cinsiyet);
        return cinsiyet;
    }
    private String sharedlastnameal() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String lastname = sP.getString("lastname","defaultlastname");
        Log.i("tago", "Takip Servisi hafızadan ulaştım lastname = " + lastname);
        return lastname;
    }
    private String sharedfirstnameal() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String firstname = sP.getString("firstname","defaultfirstname");
        Log.i("tago", "Takip Servisi hafızadan ulaştım firstname = " + firstname);
        return firstname;
    }
    private String sharedtumisimal() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String tumisim = sP.getString("tumisim","defaulttumisim");
        Log.i("tago", "Takip Servisi hafızadan ulaştım tumisim = " + tumisim);
        return tumisim;
    }
    private String sharedPrefIdAl() {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sP.getString("veritabani_id","defaultid");
        Log.i("tago", "Takip Servisi hafızadan ulaştım veritabanı id = " + veritabani_id);
        return veritabani_id;
    }
    private Bitmap internalresimal(){

            try {
                File f=new File("/data/data/com.example.bahadir.myapplicationn/app_userpro", "kullaniciresmi.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                return b;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                return null;
            }
    }

    protected void onCreate(Bundle savedInstanceState) {
        Log.i("tago", "Main Activity onCreate");
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        new registerForPushNotificationsAsync().execute();
        String kullaniciid= sharedPrefIdAl();
        Log.i("tago", kullaniciid);
        setContentView(R.layout.activity_main);
        /*if(kullaniciid.equals("defaultid")){
            setContentView(R.layout.activity_main);
        }else{
            String tumisim = sharedtumisimal();
            String firstname = sharedfirstnameal();
            String lastname = sharedlastnameal();
            String cinsiyet = sharedcinsiyetal();
            String email = sharedemailal();
            String resimurl = sharedresimurlal();
            String coverurl = sharedcoverurlal();
            Bitmap resim = internalresimal();
            Intent i = new Intent(MainActivity.this , TakipServisi.class);
            i.putExtra("resim" , resim);
            i.putExtra("isim", tumisim);
            i.putExtra("resimurl" , resimurl);
            i.putExtra("firstname" , firstname);
            i.putExtra("lastname" , lastname);
            i.putExtra("email" , email);
            i.putExtra("gender" , cinsiyet);
            i.putExtra("kapakresmiurl" , coverurl);
            startService(i);
        }*/
        printHashKey();
    }

    private void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.bahadir.myapplicationn",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("tago","keyhash= "+  Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private class registerForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {
        protected Exception doInBackground(Void... params) {
            try {
                // Acquire a unique registration ID for this device
                String registrationId = Pushy.register(getApplicationContext());
                registrationIdSharedPrefKaydet(registrationId);
                Log.i("tago", registrationId);

                // Save the registration ID in your backend database:
                // You must implement this function
                //sendRegistrationIdToBackendServer(registrationId);
            } catch (Exception exc) {
                // Return exc to onPostExecute
                Log.i("tago", "register olmadı");
                Log.i("tago" , exc.getMessage());
                return exc;
            }
            // We're good
            return null;
        }

        private void registrationIdSharedPrefKaydet(String registrationId) {
            SharedPreferences sp = getSharedPreferences("kullaniciverileri" , Context.MODE_PRIVATE);
            SharedPreferences.Editor sedit = sp.edit();
            sedit.putString("registrationid" , registrationId);
            sedit.commit();
            Log.i("tago" , "sharedpreference registration id kaydedildi");
        }

        @Override
        protected void onPostExecute(Exception exc) {
            // Failed?
            if (exc != null) {
                // Show error in toast
                Log.i("tago" , exc.toString());
                return;
            }

            // Succeeded, do something
        }

        // Example implementation
        public void sendRegistrationIdToBackendServer(String registrationId) throws Exception {
            String charset = "UTF-8";
            String param1 = "is";
            String query = "";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Change this accordingly
            HttpURLConnection connection;
            //URL sendRegIdRequest = new URL("http://185.22.184.103/project/update_regid.php?id=4&regid="+registrationId);
            Log.i("tago" ,"gonder " + registrationId);
            connection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/update_regid.php?id=4&regid="+registrationId).openConnection();

            // Execute the request
            //sendRegIdRequest.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (Exception e){
                Log.i("tago" , "hillfe hillfe");
            }
        }
    }
    }

