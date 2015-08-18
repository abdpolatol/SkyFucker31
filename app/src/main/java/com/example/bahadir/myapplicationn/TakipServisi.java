package com.example.bahadir.myapplicationn;

import android.app.Service;
import android.content.Intent;
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
    GoogleApiClient googleclient;
    public void onCreate() {
        super.onCreate();
        Log.i("tago" , "onCreate çağırıldı");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("tago", "onStartCommand çağırıldı");
        Runnable r = new Runnable() {
            public void run() {
                googleclient = new GoogleApiClient.Builder(TakipServisi.this).addApi(LocationServices.API).addConnectionCallbacks(TakipServisi.this)
                        .addOnConnectionFailedListener(TakipServisi.this).build();
                Log.i("tago", "clienti yaptım");
                googleclient.connect();
                Log.i("tago" , "baglandım");
            }
        };
        Thread islem = new Thread(r);
        islem.start();
        return Service.START_STICKY;
    }

    public void onDestroy() {
        Log.i("tago", "onDestroy çağırıldı");
        googleclient.disconnect();
        Log.i("tago" , "baglantı bitirildi");
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleclient);
        if (mCurrentLocation != null) {
            Log.i("tago" ,String.valueOf(mCurrentLocation.getLatitude())+ String.valueOf(mCurrentLocation.getLongitude()) );
        }
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
        Log.i("tago" ,  String.valueOf(mCurrentLocation.getLatitude())+ " " + String.valueOf(mCurrentLocation.getLongitude()) );
        Log.i("tago", mLastUpdateTime);
    }

}

