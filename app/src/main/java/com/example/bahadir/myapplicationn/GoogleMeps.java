package com.example.bahadir.myapplicationn;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.text.DateFormat;
import java.util.Date;

public class GoogleMeps extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks ,
        GoogleApiClient.OnConnectionFailedListener , LocationListener , View.OnClickListener{
    Location mCurrentLocation;
    GoogleApiClient googleclient;
    Button buton1;

    protected void onCreate(Bundle bambam){
        super.onCreate(bambam);
        setContentView(R.layout.fragment_main);
        googleclient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        Log.i("tago", "clienti yaptım");
        buton1 = (Button) findViewById(R.id.button);
        buton1.setOnClickListener(this);
    }
    public void onStart(){
        super.onStart();
        googleclient.connect();
    }
    protected void onStop() {
        googleclient.disconnect();
        super.onStop();
    }
    public void onMapReady(GoogleMap googleMap) {
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
            LocationServices.FusedLocationApi.requestLocationUpdates(googleclient,locrequest ,this);
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
        Log.i("tago" , mLastUpdateTime);
    }
    public void onClick(View v) {
        Intent i = new Intent(GoogleMeps.this , AnaAkim.class );
        startActivity(i);
    }
}
