package com.example.bahadir.myapplicationn;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.astuetz.PagerSlidingTabStrip;

public class AnaAkim extends FragmentActivity {
    Bitmap bitmap;
    String isim;
    String resimurl;
    Location x = new Location("ababa");
    Location y = new Location("caccaca");
    protected void onCreate(Bundle bambam){
        super.onCreate(bambam);
        setContentView(R.layout.cevrendekiler);
        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("resim");
        isim =  intent.getStringExtra("isim");
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        resimurl = intent.getStringExtra("resimurl");;
        Intent inte = new Intent(AnaAkim.this , TakipServisi.class);
        inte.putExtra("isim" , isim);
        inte.putExtra("resimurl" , resimurl);
        inte.putExtra("firstname" , firstname);
        inte.putExtra("lastname", lastname);
        startService(inte);
        Log.i("tago", "Takip servisi başlatıldı");
        tanimlar();
        nickAl();
       // yerolustur();
        //mesafeyiBul(x, y);
    }

    protected void onResume() {
        super.onResume();
        Log.i("tago","AnaAkim " +  isim + " giriş yaptı");

    }
    public void tanimlar(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);
    }
    public void nickAl(){
        final Dialog dialog = new Dialog(AnaAkim.this);
        dialog.setTitle("nick ver");
        dialog.setContentView(R.layout.customdialog);
        dialog.getWindow().setDimAmount(0.7f);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final EditText etv1 = (EditText) dialog.findViewById(R.id.editText);
        Button onaylaButonu = (Button) dialog.findViewById(R.id.button5);
        onaylaButonu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nick = etv1.getText().toString();
                Log.i("tago", "nickin :" + nick);
                dialog.cancel();
            }
        });
    }
    public void mesafeyiBul(Location a , Location b){
        float distance = a.distanceTo(b);
        Log.i("tago", "" + distance);

    }
    public void yerolustur(){
        x.setLatitude(39.8998454);
        x.setLongitude(32.7949772);
        y.setLatitude(39.8973333);
        y.setLongitude(32.7924444);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

   /* protected void onDestroy() {
        unregisterReceiver(breceiver);
        stopService(new Intent(AnaAkim.this , TakipServisi.class));
        super.onDestroy();
    }
*/

}
