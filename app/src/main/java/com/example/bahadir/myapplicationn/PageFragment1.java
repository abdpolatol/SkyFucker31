package com.example.bahadir.myapplicationn;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felipecsl.quickreturn.library.AbsListViewQuickReturnAttacher;
import com.felipecsl.quickreturn.library.QuickReturnAttacher;
import com.felipecsl.quickreturn.library.widget.AbsListViewScrollTarget;
import com.felipecsl.quickreturn.library.widget.QuickReturnAdapter;
import com.felipecsl.quickreturn.library.widget.QuickReturnTargetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class PageFragment1 extends Fragment implements AbsListView.OnScrollListener,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    ArrayList<OfficialKanal> officialKanalListesi;
    ArrayList<NormalKanal> normalKanalListesi;
    ArrayList<Kanal> ArananKanalListesi;
    KanallariCek kC;
    KanallariYenidenCek kYC;
    ArrayList<Kanal> channelbaba = new ArrayList<>();
    String charset;
    String query;
    ListView liste1;
    String veritabani_id;
    YeniKanalEkle yenikanalekle;
    boolean kanaleklemebitti = false;
    private int mPage;

    ViewGroup viewGroup;
    View view;
    AbsListView absListView;
    RelativeLayout lay1;
    ImageButton buton1,buton2,buton3;
    TextView bottomTextView;
    private QuickReturnTargetView topTargetView;;



    public void onStart() {
        super.onStart();
        Log.i("tago", "FragmentDonguleri frag 1 onStart");
    }

    public void onResume() {
        super.onResume();
        Log.i("tago", "FragmentDonguleri frag 1 onResume");
    }

    public void onPause() {
        super.onPause();
        Log.i("tago", "FragmentDonguleri frag 1 onPause");
    }

    public void onStop() {
        super.onStop();
        Log.i("tago", "FragmentDonguleri frag 1 onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i("tago", "FragmentDonguleri frag 1 onDestroy");
    }

    public static PageFragment1 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment1 fragment = new PageFragment1();
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.kanallar, container, false);
        ImageButton buton3 = (ImageButton) view.findViewById(R.id.button9);
        buton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                kanalekle();
            }
        });
        liste1 = (ListView) view.findViewById(R.id.listView);
        veritabani_id = idSharedPrefAl();
        initializeQuickReturn();
        return view;
    }
    private void initializeQuickReturn() {
        viewGroup = (ViewGroup) view.findViewById(R.id.listView);
        absListView = (AbsListView) viewGroup;
        lay1 = (RelativeLayout) view.findViewById(R.id.quickReturnTopTarget);
        buton1 = (ImageButton) view.findViewById(R.id.button);
        buton2 = (ImageButton) view.findViewById(R.id.button2);
        buton3 = (ImageButton) view.findViewById(R.id.imageButton12);
        buton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.i("tago" , "Buton 1 tıklandı");
                channelbaba.clear();
                cevredekikanallaricek(veritabani_id);
            }
        });

        buton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.i("tago", "Buton 2 tıklandı");
                channelbaba.clear();
                DatabaseClassKonusulanKanallar dB = new DatabaseClassKonusulanKanallar(getActivity());
                dB.open();
                List<String> kanallar = dB.databasedenkanalcek();;
                dB.close();
                for(int i = 0 ; i<kanallar.size() ; i++){
                    Kanal kanal = new Kanal(true);
                    kanal.setKanaladi(kanallar.get(i));
                    Log.i("tago", kanallar.get(i));
                    channelbaba.add(kanal);
                }

                KanalAdapter adapter = new KanalAdapter(getActivity() ,officialKanalListesi , normalKanalListesi, channelbaba);
                if (viewGroup instanceof AbsListView) {
                    absListView.setAdapter(new QuickReturnAdapter(adapter));
                }

                QuickReturnAttacher quickReturnAttacher = QuickReturnAttacher.forView(viewGroup);
                quickReturnAttacher.addTargetView(bottomTextView, AbsListViewScrollTarget.POSITION_BOTTOM);
                topTargetView = quickReturnAttacher.addTargetView(lay1,
                        AbsListViewScrollTarget.POSITION_TOP,
                        dpToPx(getActivity(), 50));

                if (quickReturnAttacher instanceof AbsListViewQuickReturnAttacher) {
                    AbsListViewQuickReturnAttacher
                            attacher =
                            (AbsListViewQuickReturnAttacher) quickReturnAttacher;
                    attacher.addOnScrollListener(PageFragment1.this);
                    attacher.setOnItemClickListener(PageFragment1.this);
                    attacher.setOnItemLongClickListener(PageFragment1.this);
                }
            }
        });

        bottomTextView = (TextView) view.findViewById(R.id.quickReturnBottomTarget);
        if (!veritabani_id.equals("default")) {
            if(officialKanalListesi!=null){
                officialKanalListesi.clear();
            }if(normalKanalListesi!=null){
                normalKanalListesi.clear();
            }if(channelbaba!=null){
                channelbaba.clear();
            }
            cevredekikanallaricek(veritabani_id);
        } else if (veritabani_id.equals("default")) {
            do {
                idSharedPrefAl();
            } while (!veritabani_id.equals("default"));
            if(officialKanalListesi!=null){
                officialKanalListesi.clear();
            }if(normalKanalListesi!=null){
                normalKanalListesi.clear();
            }if(channelbaba!=null){
                channelbaba.clear();
            }
            cevredekikanallaricek(veritabani_id);
        } else {
            Log.i("tago", "Anan� avrad�n� sikerim senin");
        }

    }
    private void kanalekle() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.kanalikur);
        dialog.getWindow().setDimAmount(0.7f);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final EditText etv1 = (EditText) dialog.findViewById(R.id.editText3);
        Button buton1 = (Button) dialog.findViewById(R.id.button10);
        buton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String kurulacakkanaladi = etv1.getText().toString();
                yenikanalekle = new YeniKanalEkle();
                yenikanalekle.execute(kurulacakkanaladi);
                kYC = new KanallariYenidenCek();
                kYC.execute(veritabani_id);
                dialog.cancel();
                }
    });
    }
    private void cevredekikanallaricek(String veritabani_id) {
        kC = new KanallariCek();
        kC.execute(veritabani_id);
    }
    private String idSharedPrefAl() {
        SharedPreferences sP = getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sP.getString("veritabani_id", "default id");
        Log.i("tago", "Page Fragment1 haf�zadan ula�t�m veritaban� id = " + veritabani_id);
        this.veritabani_id = veritabani_id;
        return veritabani_id;
    }
    public int dpToPx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }
    public void onClick(View view) {
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
    }

    public void aramaYap(String arananveri) {
        ArananKanalListesi = new ArrayList();
        for (int i = 0; i < channelbaba.size(); i++) {
            Log.i("tago", channelbaba.get(i).getKanaladi());
            if ((channelbaba.get(i).getKanaladi().equals(arananveri))) {
                ArananKanalListesi.add(channelbaba.get(i));
                Log.i("tago", "aramayla eşleşen öge bulundu: " + ArananKanalListesi.get(i).getKanaladi());
            } else {
                Log.i("tago", "aramayla eşleşen öge bulunmadı");
            }
        }

        KanalAdapter arananadapter = new KanalAdapter(getActivity(), officialKanalListesi, normalKanalListesi, ArananKanalListesi);
        if (viewGroup instanceof AbsListView) {
            int numColumns = (viewGroup instanceof GridView) ? 3 : 1;
            absListView.setAdapter(new QuickReturnAdapter(arananadapter, numColumns));
        }
        QuickReturnAttacher quickReturnAttacher = QuickReturnAttacher.forView(viewGroup);
        quickReturnAttacher.addTargetView(bottomTextView, AbsListViewScrollTarget.POSITION_BOTTOM);
        topTargetView = quickReturnAttacher.addTargetView(lay1,
                AbsListViewScrollTarget.POSITION_TOP,
                dpToPx(getActivity(), 50));

        if (quickReturnAttacher instanceof AbsListViewQuickReturnAttacher) {
            AbsListViewQuickReturnAttacher
                    attacher =
                    (AbsListViewQuickReturnAttacher) quickReturnAttacher;
            attacher.addOnScrollListener(PageFragment1.this);
            attacher.setOnItemClickListener(PageFragment1.this);
            attacher.setOnItemLongClickListener(PageFragment1.this);
        }
    }


    private class KanallariCek extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            charset = "utf-8";
            String param1 = "id";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tago", "Page Fragment1 kanallari goster ba�lat�ld�");
            try {
                return kanallarigor(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadi";
            }
        }

        private String kanallarigor(String id) {
            HttpURLConnection sconnection = null;
            try {
                sconnection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy//get_official_channels.php?id=" + id).openConnection();
                Log.i("tago", "Page Fragment1 official kanalları gor bagı kuruldu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sconnection.setDoOutput(true);
            sconnection.setDoInput(true);
            sconnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            sconnection.setRequestProperty("Accept", "* /*");
            sconnection.setRequestProperty("Accept-Charset", charset);
            sconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream output = new BufferedOutputStream(sconnection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                try {
                    int a = sconnection.getResponseCode();
                    String b = sconnection.getResponseMessage();
                    Log.i("tago", "rerere" + a + " " + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader in;
                if (sconnection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(sconnection.getInputStream()));
                    Log.i("tago", "InputStream");
                    String inputline = null;

                    inputline = in.readLine();
                    Log.i("tago", "inputline= " + inputline);
                    JSONArray jsono = new JSONArray(inputline);
                    officialKanalListesi = new ArrayList();
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        /*OfficialKanal officialKanal = new OfficialKanal();
                        officialKanal.setKanaladi(object.optString("name"));
                        officialKanal.setDate(object.optString("date"));
                        officialKanal.setId(object.optString("id"));
                        officialKanal.setLikedurumu(object.optInt("like_status"));
                        officialKanalListesi.add(officialKanal);*/
                        Kanal kanal = new Kanal(true);
                        kanal.setKanaladi(object.optString("name"));
                        kanal.setDate("date");
                        kanal.setId(object.optString("id"));
                        kanal.setLikedurumu(object.optInt("like_status"));
                        channelbaba.add(kanal);
                    }
                } else {
                    in = new BufferedReader(new InputStreamReader(sconnection.getErrorStream()));
                    Log.i("tago", "Error Stream");
                    String inputline;
                    inputline = in.readLine();
                    Log.i("tago", "inputline= " + inputline);
                    JSONArray jsono = new JSONArray(inputline);
                    officialKanalListesi = new ArrayList();
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        OfficialKanal officialKanal = new OfficialKanal();
                        officialKanal.setKanaladi(object.optString("name"));
                        officialKanal.setDate(object.optString("date"));
                        officialKanal.setId(object.optString("id"));
                        officialKanal.setLikedurumu(object.optInt("like_status"));
                        officialKanalListesi.add(officialKanal);
                        Kanal kanal = new Kanal(true);
                        kanal.setKanaladi(object.optString("name"));
                        kanal.setDate("date");
                        kanal.setId(object.optString("id"));
                        kanal.setLikedurumu(object.optInt("like_status"));
                        channelbaba.add(kanal);
                    }
                }
                in.close();
                Log.i("tago", "Page Fragment official gor inputline yazd�m");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Page Fragment official gor yazamad�m");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tago", "json Exception");
            }

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy//get_channels.php?id=" + id).openConnection();
                Log.i("tago", "Page Fragment1 kanalları gor bagı kuruldu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                try {
                    int a = connection.getResponseCode();
                    String b = connection.getResponseMessage();
                    Log.i("tago", "rerere" + a + " " + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader in;
                if (connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i("tago", "InputStream");
                    String inputline = null;

                    inputline = in.readLine();
                    Log.i("tago", "inputline= " + inputline);
                    JSONArray jsono = new JSONArray(inputline);
                    normalKanalListesi = new ArrayList();
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        /*NormalKanal normalKanal = new NormalKanal();
                        normalKanal.setKanaladi(object.optString("name"));
                        normalKanal.setDate(object.optString("date"));
                        normalKanal.setId(object.optString("id"));
                        normalKanalListesi.add(normalKanal);*/
                        Kanal kanal = new Kanal(false);
                        kanal.setKanaladi(object.optString("name"));
                        kanal.setDate("date");
                        kanal.setId(object.optString("id"));
                        channelbaba.add(kanal);
                    }
                } else {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    Log.i("tago", "Error Stream");
                    String inputline;
                    while ((inputline = in.readLine()) != null) {
                        Log.i("tago", "camcamcam" + inputline);
                    }
                    normalKanalListesi = new ArrayList();
                    JSONArray jsono = new JSONArray(inputline);
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        NormalKanal normalKanal = new NormalKanal();
                        normalKanal.setKanaladi(object.optString("name"));
                        normalKanal.setDate(object.optString("date"));
                        normalKanal.setId(object.optString("id"));
                        normalKanalListesi.add(normalKanal);
                        Kanal kanal = new Kanal(false);
                        kanal.setKanaladi(object.optString("name"));
                        kanal.setDate("date");
                        kanal.setId(object.optString("id"));
                        channelbaba.add(kanal);
                        Log.i("tago", "insaninsan");

                    }
                }
                in.close();
                Log.i("tago", "Page Fragment kanalları gor inputline yazd�m");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Page Fragment kanalları gor yazamad�m");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tago", "json Exception");
            }

            return "inputline";
        }

        protected void onPostExecute(String s) {

            KanalAdapter adapter = new KanalAdapter(getActivity(), officialKanalListesi, normalKanalListesi, channelbaba);
            if (viewGroup instanceof AbsListView) {
                int numColumns = (viewGroup instanceof GridView) ? 3 : 1;
                absListView.setAdapter(new QuickReturnAdapter(adapter, numColumns));
            }

            QuickReturnAttacher quickReturnAttacher = QuickReturnAttacher.forView(viewGroup);
            quickReturnAttacher.addTargetView(bottomTextView, AbsListViewScrollTarget.POSITION_BOTTOM);
            topTargetView = quickReturnAttacher.addTargetView(lay1,
                    AbsListViewScrollTarget.POSITION_TOP,
                    dpToPx(getActivity(), 50));

            if (quickReturnAttacher instanceof AbsListViewQuickReturnAttacher) {
                AbsListViewQuickReturnAttacher
                        attacher =
                        (AbsListViewQuickReturnAttacher) quickReturnAttacher;
                attacher.addOnScrollListener(PageFragment1.this);
                attacher.setOnItemClickListener(PageFragment1.this);
                attacher.setOnItemLongClickListener(PageFragment1.this);
            }
        }

    }
    private class YeniKanalEkle extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            charset = "utf-8";
            String param1 = "id";
            String param2 = "name";
            try {
                query = String.format("param1=%s&param2=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tago", "Page Fragment1 kanal ekleme ba�lat�ld�");
            try {
                return kanaliekle(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadi";
            }
        }

        private String kanaliekle(String kanaladi) {
            HttpURLConnection sconnection = null;
            try {
                sconnection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/add_channel.php?id=" + veritabani_id + "&name=" + kanaladi).openConnection();
                Log.i("tago", "Page Fragment1 yeni kanal kur bagı kuruldu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sconnection.setDoOutput(true);
            sconnection.setDoInput(true);
            sconnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            sconnection.setRequestProperty("Accept", "* /*");
            sconnection.setRequestProperty("Accept-Charset", charset);
            sconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream output = new BufferedOutputStream(sconnection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                int a = sconnection.getResponseCode();
                String b = sconnection.getResponseMessage();
                Log.i("tago", "rerere" + a + " " + b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Caxobaxo";
        }

        protected void onPostExecute(String s) {
            kanaleklemebitti=true;
        }
    }
    private class KanallariYenidenCek extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            charset = "utf-8";
            String param1 = "id";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tago", "Page Fragment1 kanallari yeniden goster ba�lat�ld�");
            try {
                return kanallarigor(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadi";
            }
        }

        private String kanallarigor(String id) {
            channelbaba.clear();
            HttpURLConnection sconnection = null;
            try {
                sconnection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy//get_official_channels.php?id=" + id).openConnection();
                Log.i("tago", "Page Fragment1 official kanalları gor bagı kuruldu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sconnection.setDoOutput(true);
            sconnection.setDoInput(true);
            sconnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            sconnection.setRequestProperty("Accept", "* /*");
            sconnection.setRequestProperty("Accept-Charset", charset);
            sconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream output = new BufferedOutputStream(sconnection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                try {
                    int a = sconnection.getResponseCode();
                    String b = sconnection.getResponseMessage();
                    Log.i("tago", "rerere" + a + " " + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader in;
                if (sconnection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(sconnection.getInputStream()));
                    Log.i("tago", "InputStream");
                    String inputline = null;

                    inputline = in.readLine();
                    Log.i("tago", "inputline= " + inputline);
                    JSONArray jsono = new JSONArray(inputline);
                    officialKanalListesi = new ArrayList();
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        OfficialKanal officialKanal = new OfficialKanal();
                        officialKanal.setKanaladi(object.optString("name"));
                        officialKanal.setDate(object.optString("date"));
                        officialKanalListesi.add(officialKanal);
                        Kanal kanal = new Kanal(true);
                        kanal.setKanaladi(object.optString("name"));
                        kanal.setDate("date");
                        channelbaba.add(kanal);
                    }
                } else {
                    in = new BufferedReader(new InputStreamReader(sconnection.getErrorStream()));
                    Log.i("tago", "Error Stream");
                    String inputline;
                    inputline = in.readLine();
                    Log.i("tago", "inputline= " + inputline);
                    JSONArray jsono = new JSONArray(inputline);
                    officialKanalListesi = new ArrayList();
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        OfficialKanal officialKanal = new OfficialKanal();
                        officialKanal.setKanaladi(object.optString("name"));
                        officialKanal.setDate(object.optString("date"));
                        officialKanalListesi.add(officialKanal);
                        Kanal kanal = new Kanal(true);
                        kanal.setKanaladi(object.optString("name"));
                        kanal.setDate("date");
                        channelbaba.add(kanal);
                    }
                }
                in.close();
                Log.i("tago", "Page Fragment yeniden official gor inputline yazd�m");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Page Fragment yeniden official gor yazamad�m");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tago", "json Exception");
            }

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy//get_channels.php?id=" + id).openConnection();
                Log.i("tago", "Page Fragment1 kanalları yeniden gor bagı kuruldu");
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "* /*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                output.close();
                try {
                    int a = connection.getResponseCode();
                    String b = connection.getResponseMessage();
                    Log.i("tago", "rerere" + a + " " + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader in;
                if (connection.getResponseCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i("tago", "InputStream");
                    String inputline = null;

                    inputline = in.readLine();
                    Log.i("tago", "inputline= " + inputline);
                    JSONArray jsono = new JSONArray(inputline);
                    normalKanalListesi = new ArrayList();
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        NormalKanal normalKanal = new NormalKanal();
                        normalKanal.setKanaladi(object.optString("name"));
                        normalKanal.setDate(object.optString("date"));
                        normalKanalListesi.add(normalKanal);
                        Kanal kanal = new Kanal(false);
                        kanal.setKanaladi(object.optString("name"));
                        kanal.setDate("date");
                        channelbaba.add(kanal);
                    }
                } else {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    Log.i("tago", "Error Stream");
                    String inputline;
                    while ((inputline = in.readLine()) != null) {
                        Log.i("tago", "camcamcam" + inputline);
                    }
                    normalKanalListesi = new ArrayList();
                    JSONArray jsono = new JSONArray(inputline);
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        NormalKanal normalKanal = new NormalKanal();
                        normalKanal.setKanaladi(object.optString("name"));
                        normalKanal.setDate(object.optString("date"));
                        normalKanalListesi.add(normalKanal);
                        Kanal kanal = new Kanal(false);
                        kanal.setKanaladi(object.optString("name"));
                        kanal.setDate("date");
                        channelbaba.add(kanal);
                        Log.i("tago", "insaninsan");

                    }
                }
                in.close();
                Log.i("tago", "Page Fragment yeniden kanalları gor inputline yazd�m");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Page Fragment yeniden kanalları gor yazamad�m");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tago", "json Exception");
            }

            return "inputline";
        }

        protected void onPostExecute(String s) {
            KanalAdapter adapter = new KanalAdapter(getActivity(), officialKanalListesi, normalKanalListesi, channelbaba);
            if (viewGroup instanceof AbsListView) {
                int numColumns = (viewGroup instanceof GridView) ? 3 : 1;
                absListView.setAdapter(new QuickReturnAdapter(adapter, numColumns));
            }

            QuickReturnAttacher quickReturnAttacher = QuickReturnAttacher.forView(viewGroup);
            quickReturnAttacher.addTargetView(bottomTextView, AbsListViewScrollTarget.POSITION_BOTTOM);
            topTargetView = quickReturnAttacher.addTargetView(lay1,
                    AbsListViewScrollTarget.POSITION_TOP,
                    dpToPx(getActivity(), 50));

            if (quickReturnAttacher instanceof AbsListViewQuickReturnAttacher) {
                AbsListViewQuickReturnAttacher
                        attacher =
                        (AbsListViewQuickReturnAttacher) quickReturnAttacher;
                attacher.addOnScrollListener(PageFragment1.this);
                attacher.setOnItemClickListener(PageFragment1.this);
                attacher.setOnItemLongClickListener(PageFragment1.this);
            }
        }
    }

}