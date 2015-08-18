package com.example.bahadir.myapplicationn;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

public class AnaAkim extends Activity {
    TabHost thost;
    TabHost.TabSpec tspec1,tspec2,tspec3,tspec4,tspec5;
    ListView list1;
    Bitmap bitmap;
    String isim;
    protected void onCreate(Bundle bambam){
        super.onCreate(bambam);
        setContentView(R.layout.cevrendekiler);
        tanimlar();
        Intent intent = getIntent();
        bitmap = (Bitmap) intent.getParcelableExtra("resim");
        isim =  intent.getStringExtra("isim");
        Log.i("tago", isim + " giriş yaptı");
        Intent i = new Intent(AnaAkim.this, TakipServisi.class);
        startService(i);
        Log.i("tago", "takibe aldı");
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

    public void tanimlar(){
        thost = (TabHost) findViewById(R.id.tabHost);
        thost.setup();
        tspec1 = thost.newTabSpec("Baslik1");
        tspec1.setContent(R.id.tab1);
        Drawable d = getResources().getDrawable(R.mipmap.ic_launcher , null);
        tspec1.setIndicator("", d);
        thost.addTab(tspec1);
        tspec2 = thost.newTabSpec("Baslik2");
        tspec2.setContent(R.id.tab2);
        tspec2.setIndicator("IkinciTab");
        thost.addTab(tspec2);
        tspec3 = thost.newTabSpec("Baslik3");
        tspec3.setContent(R.id.tab3);
        tspec3.setIndicator("UcuncuTab");
        thost.addTab(tspec3);
        tspec4= thost.newTabSpec("Baslik4");
        tspec4.setContent(R.id.tab4);
        tspec4.setIndicator("Dorduncu Tab");
        thost.addTab(tspec4);
        tspec5 = thost.newTabSpec("Baslik5");
        tspec5.setContent(R.id.tab5);
        tspec5.setIndicator("Besinci Tab");
        thost.addTab(tspec5);
        adapterkur();
    }
    public void adapterkur() {
        Insan[] insan_data = new Insan[]
                {
                        new Insan(R.mipmap.apoprof, "Apo"),
                        new Insan(R.mipmap.baranprof, "Showers"),
                        new Insan(R.mipmap.kursatprof, "Snow"),
                        new Insan(R.mipmap.ozerprof , "Ben Delay Remix"),
                        new Insan(R.mipmap.taylanprof , "Sis Atma Och"),
                        new Insan(R.mipmap.aliprof , "BigFoot"),
                        new Insan(R.mipmap.hasanprof , "Marlboro Light"),
                        new Insan(R.mipmap.bengisuprof , "Operation"),
                        new Insan(R.mipmap.beyzaprof, "Bana yok mu"),
                        new Insan(R.mipmap.seloprof , "mega")
                };

        InsanAdapter adapter = new InsanAdapter(this,
                R.layout.listview_item, insan_data);


        list1 = (ListView) findViewById(R.id.listView);
        View header = getLayoutInflater().inflate(R.layout.listview_header, null);
        list1.addHeaderView(header);
        list1.setAdapter(adapter);
    }
}
