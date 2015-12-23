package com.example.bahadir.myapplicationn;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class KarsilastirmaliFotoPaylas extends Activity {

    ImageButton image1, image2;
String deneme="deneme";
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.karsilastirmalifotopaylas);
        image1 = (ImageButton) findViewById(R.id.imageButton9);
        image2 = (ImageButton) findViewById(R.id.imageButton10);
        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Fotograf alma olayı
                // Alınan fotograf tekrardan image1 e bastırılır..
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Fotograf alma olayı
                // Alınan fotograf tekrardan image2 e bastırılır..
            }
        });
    }
}