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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;

public class AnaAkim extends AppCompatActivity {
    Bitmap bitmap;
    int hangifragmentuzerindesin;
    SampleFragmentPagerAdapter sFPA;
    String isim;
    String resimurl;
    boolean notificationbas;
    public Toolbar toolbar;
    ViewPager viewPager;
    ImageButton imagesimge;
    ImageButton imagearama;
    ImageView logo;
    EditText etv1;
    Toolbar.LayoutParams normalfirstaramaparams;
    Toolbar.LayoutParams normaletvparams;
    Toolbar.LayoutParams normallogoparams;

    protected void onCreate(Bundle bambam) {
        super.onCreate(bambam);
        setContentView(R.layout.genelaltplan);
        sFPA = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        sharedkullaniciciktikaydet(false);
        notificationbas = true;
        notificationSharedPrefKaydet();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        etv1 = (EditText) findViewById(R.id.aramaalani);
        imagesimge = (ImageButton) findViewById(R.id.imageButton14);
        imagearama = (ImageButton) findViewById(R.id.aramabutonu);
        logo = (ImageView) findViewById(R.id.imageView13);
        normalfirstaramaparams =(Toolbar.LayoutParams)imagearama.getLayoutParams();
        normaletvparams = new Toolbar.LayoutParams(0,0);
        normallogoparams = (Toolbar.LayoutParams) logo.getLayoutParams();
        imagearama.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("tago", "onclicktıklandı");
                InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                etv1.requestFocus();
                iMM.showSoftInput(etv1, InputMethodManager.SHOW_IMPLICIT);
                editTextAyarla(etv1, imagesimge, logo, imagearama);
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
    private void editTextAyarla(final EditText etv, final ImageButton simge,final ImageView logo,final ImageButton imagefirstarama) {
        final float scale = getResources().getDisplayMetrics().density;
        simge.setImageResource(R.mipmap.aramam);
        Toolbar.LayoutParams firstaramaparams = new Toolbar.LayoutParams(0,0);
        imagefirstarama.setLayoutParams(firstaramaparams);
        Toolbar.LayoutParams logoparams = new Toolbar.LayoutParams(0,0);
        logo.setLayoutParams(logoparams);
        Toolbar.LayoutParams etvparams = new Toolbar.LayoutParams((int) (300 * scale + 0.5f),(int) (50 * scale + 0.5f));
        etv.setLayoutParams(etvparams);
        etv.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String arananveri = etv.getText().toString();
                    Log.i("tago", arananveri);
                    etv.setText("");
                    etv.setLayoutParams(normaletvparams);
                    imagefirstarama.setLayoutParams(normalfirstaramaparams);
                    logo.setLayoutParams(normallogoparams);
                    simge.setImageResource(R.mipmap.owll_3);
                    View view = getCurrentFocus();
                    InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    iMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if (viewPager.getCurrentItem() == 0) {
                        PageFragment frag1 = (PageFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                        frag1.aramaYap(arananveri);
                        Log.i("tago", "ilk fragmenttesin");
                    } else if (viewPager.getCurrentItem() == 1) {
                        PageFragment1 frag2 = (PageFragment1) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                        frag2.aramaYap(arananveri);
                        Log.i("tago", "ikinci fragmenttesin");
                    } else if (viewPager.getCurrentItem() == 2) {
                        PageFragment2 frag3 = (PageFragment2) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                        frag3.aramaYap(arananveri);
                        Log.i("tago", "ucuncu fragmenttesin");
                    } else if (viewPager.getCurrentItem() == 3) {
                        Log.i("tago", "dorduncu fragmenttesin");
                    }
                }

                return false;
            }
        });
        simge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String arananveri = etv.getText().toString();
                Log.i("tago", arananveri);
                etv.setText("");
                etv.setLayoutParams(normaletvparams);
                imagefirstarama.setLayoutParams(normalfirstaramaparams);
                logo.setLayoutParams(normallogoparams);
                simge.setImageResource(R.mipmap.owll_3);
                View view = getCurrentFocus();
                InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                iMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (viewPager.getCurrentItem() == 0) {
                    PageFragment frag1 = (PageFragment) viewPager.getAdapter().instantiateItem(viewPager,viewPager.getCurrentItem());
                    frag1.aramaYap(arananveri);
                    Log.i("tago", "ilk fragmenttesin");
                } else if (viewPager.getCurrentItem() == 1) {
                    PageFragment1 frag2 = (PageFragment1) viewPager.getAdapter().instantiateItem(viewPager,viewPager.getCurrentItem());
                    frag2.aramaYap(arananveri);
                    Log.i("tago", "ikinci fragmenttesin");
                } else if (viewPager.getCurrentItem() == 2) {
                    PageFragment2 frag3 = (PageFragment2) viewPager.getAdapter().instantiateItem(viewPager,viewPager.getCurrentItem());
                    frag3.aramaYap(arananveri);
                    Log.i("tago", "ucuncu fragmenttesin");
                } else if (viewPager.getCurrentItem() == 3) {
                    Log.i("tago", "dorduncu fragmenttesin");
                }
            }
        });
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
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                hangifragmentuzerindesin = position;
                Log.i("tago", "onPageSelected position" + position);
            }

            public void onPageScrollStateChanged(int state) {
                Log.i("tago", "onPageScrollStateChanged " + state);
            }
        });
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
    public void onBackPressed(){
        etv1.setText("");
        etv1.setLayoutParams(normaletvparams);
        imagearama.setLayoutParams(normalfirstaramaparams);
        logo.setLayoutParams(normallogoparams);
        imagesimge.setImageResource(R.mipmap.owll_3);
        Log.i("tago" , "back pressed basıldı");
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


}
