package com.example.bahadir.myapplicationn;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class AnaAkim extends AppCompatActivity {
    Bitmap bitmap;
    String isim;
    String resimurl;
    boolean notificationbas;
    public Toolbar toolbar;
    public MaterialSearchView searchView;
    protected void onCreate(Bundle bambam) {
        super.onCreate(bambam);
        setContentView(R.layout.genelaltplan);
        sharedkullaniciciktikaydet(false);
        notificationbas = true;
        notificationSharedPrefKaydet();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageButton imagesimge,imagearama;
        ImageView logo;
        final EditText etv1;
        etv1 = (EditText) findViewById(R.id.aramaalani);
        imagesimge = (ImageButton) findViewById(R.id.imageButton14);
        imagearama = (ImageButton) findViewById(R.id.aramabutonu);
        logo = (ImageView) findViewById(R.id.imageView13);
        imagearama.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("tago", "onclicktıklandı");
                editTextAyarla(etv1);
               // final float scale = getResources().getDisplayMetrics().density;
                // etv1.setWidth((int) (200 * scale + 0.5f));
            }
        });
        /*Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("resim");
        isim =  intent.getStringExtra("isim");
        resimurl = intent.getStringExtra("resimurl");;
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        Intent inte = new Intent(AnaAkim.this , TakipServisi.class);
        inte.putExtra("isim" , isim);
        inte.putExtra("resimurl" , resimurl);
        inte.putExtra("firstname" , firstname);
        inte.putExtra("lastname", lastname);
        startService(inte);*/
        tanimlar();
        nickAl();
       // yerolustur();
        //mesafeyiBul(x, y);
    }

    private void editTextAyarla(EditText etv) {
        Toolbar.LayoutParams lparams = new Toolbar.LayoutParams(50,30);
        etv.setLayoutParams(lparams);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_anakim,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void sharedkullaniciciktikaydet(boolean b) {
            SharedPreferences sP =getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sP.edit();
            prefEditor.putBoolean("kullanicicikti" , b);
            prefEditor.commit();
    }
    private void notificationSharedPrefKaydet() {
        SharedPreferences sP = getSharedPreferences("notification" , Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sP.edit();
        prefEditor.putBoolean("notificationver", notificationbas);
        prefEditor.commit();
    }

    protected void onResume() {
        super.onResume();
        Log.i("tago", "AnaAkim " + isim + " giriş yaptı");

    }
    public void tanimlar(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);
    }
    public void nickAl(){
        final Dialog dialog = new Dialog(AnaAkim.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialognickal);
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
                nickiSharedKeydet(nick);
            }
        });

    }

    private void nickiSharedKeydet(String nick) {
        SharedPreferences sP = getSharedPreferences("kullaniciverileri",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString("kullanicinick" , nick);
        editor.commit();
    }

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
