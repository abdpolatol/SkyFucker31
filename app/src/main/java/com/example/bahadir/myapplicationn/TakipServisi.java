package com.example.bahadir.myapplicationn;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

public class TakipServisi extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
,LocationListener{
    Location mCurrentLocation;
    Location mFirstLocation;
    VeriTabani v;
    String veritabani_id;
    String firstname;
    String lastname;
    String issim;
    String isim;
    String resimurl;
    GoogleApiClient googleclient;

    public void onCreate() {
        super.onCreate();
        Log.i("tago" , "Takip Servisi onCreate çağırıldı");
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("tago", "Takip Servisi onStartCommand çağırıldı");
        Runnable r = new Runnable() {
            public void run() {
                googleclient = new GoogleApiClient.Builder(TakipServisi.this).addApi(LocationServices.API).addConnectionCallbacks(TakipServisi.this)
                        .addOnConnectionFailedListener(TakipServisi.this).build();
                Log.i("tago", "Takip servisi google clienti yaptım");
                googleclient.connect();
                Log.i("tago" , "Takip Servisi baglandım");
            }
        };
        Thread islem = new Thread(r);
        islem.start();
        isim = intent.getStringExtra("isim");
        resimurl = intent.getStringExtra("resimurl");
        firstname = intent.getStringExtra("firstname");
        lastname = intent.getStringExtra("lastname");
        Log.i("tago" , "Takip Servisi firstname= " + firstname);
        Log.i("tago" , "Takip Servisi lastname= " + lastname);
        Log.i("tago" , "burası Takip servisi" + isim);
        firstname = firstname.replace(" " ,".");
        Log.i("tago", "Takip servisi yenilenmis firstname = " + firstname);
        issim = firstname+"-"+lastname;
        return Service.START_STICKY;
    }
    public void onDestroy() {
        googleclient.disconnect();
        Log.i("tago", "Takip Servisi onDestroy çağırıldı");
        super.onDestroy();
        Log.i("tago", "Takip Servisi baglantı bitirildi");
    }
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onConnected(Bundle bundle) {
        mFirstLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleclient);
        if (mFirstLocation != null) {
            Log.i("tago" ,"TakipServisi OnConnected " + "First Latitude = " + String.valueOf(mFirstLocation.getLatitude())+
                    " First Longitude" +String.valueOf(mFirstLocation.getLongitude()) );
        }
        VeriTabani c = new VeriTabani( this ,issim, resimurl ,String.valueOf(mFirstLocation.getLatitude()), String.valueOf(mFirstLocation.getLongitude()));
        c.firehttprequestwithqueryparameters();
        Log.i("tago" , "Takip servisi veritabanı objesi veritabanına kaydı yaptı");
        LocationRequest locrequest = new LocationRequest();
        locrequest.setInterval(10000);
        locrequest.setFastestInterval(5000);
        locrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(true){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleclient,locrequest,this);
        }
    }
    public void onConnectionSuspended(int i) {

    }
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public void onLocationChanged(Location location) {
        mCurrentLocation = location ;
        String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.i("tago" , "Takip Servisi Current Location " + String.valueOf(mCurrentLocation.getLatitude())+ " " + String.valueOf(mCurrentLocation.getLongitude()) );
        Log.i("tago", mLastUpdateTime);
        veritabani_id= idSharedPreferenceAl();
        Log.i("tago" , "Takip Servisi Location Change id = " + veritabani_id);
        if(veritabani_id!=null){
            VeriTabani vRefresh = new VeriTabani(this , veritabani_id,String.valueOf(mCurrentLocation.getLatitude()),
                    String.valueOf(mCurrentLocation.getLongitude()));
            vRefresh.lokasyonuyenile();
        }
    }
    private String idSharedPreferenceAl() {
           Thread bekle = new Thread(){
                public void run(){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        bekle.start();
            SharedPreferences sP = getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
            String veritabani_id = sP.getString("veritabani_id","default id");
            Log.i("tago" , "Takip Servisi hafızadan ulaştım veritabanı id = " + veritabani_id);
            return veritabani_id;
        }
}

