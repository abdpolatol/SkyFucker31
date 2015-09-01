package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ListView list1;
    View view;
    VeriTabani v ;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    private String idSharedPreferenceAl() {
        SharedPreferences sP = getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sP.getString("veritabani_id", "default id");
        Log.i("tago", "Page Fragment hafýzadan ulaþtým veritabaný id = " + veritabani_id);
        return veritabani_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab1, container, false);
        adapterkur();
        Log.i("tago", "Page Fragment onCreateView");
        String veritabani_id = idSharedPreferenceAl();
        if (veritabani_id != "default") {
            v = new VeriTabani(getActivity(), veritabani_id, "32.3344343", "32.3223233");
            v.cevredekilerigoster();
        } else if(veritabani_id=="default"){
            Log.i("tago" , "default aldýn þu anda 800 milisaniye kaybettin");
            Thread idyibeklepic = new Thread() {
                public void run(){
                    try {
                        sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            idyibeklepic.start();
            String veritabani_idd= idSharedPreferenceAl();
            v = new VeriTabani(getActivity(), veritabani_idd, "32.3344343", "32.3223233");
            v.cevredekilerigoster();
        }else{
            Log.i("tago" , "Ananý avradýný sikerim senin");
        }
            return view;
    }

    private void adapterkur() {
        Insan[] insan_data = new Insan[]
                {
                        new Insan(R.mipmap.aliprof, "Apo"),
                        new Insan(R.mipmap.aliprof, "Showers"),
                        new Insan(R.mipmap.aliprof, "Snow"),
                        new Insan(R.mipmap.aliprof , "Ben Delay Remix"),
                        new Insan(R.mipmap.aliprof , "Sis Atma Och"),
                        new Insan(R.mipmap.aliprof , "BigFoot"),
                        new Insan(R.mipmap.aliprof, "Marlboro Light"),
                        new Insan(R.mipmap.aliprof , "Operation"),
                        new Insan(R.mipmap.aliprof, "Bana yok mu"),
                        new Insan(R.mipmap.aliprof, "mega")
                };

        InsanAdapter adapter = new InsanAdapter(getActivity(), R.layout.listview_item, insan_data);
        list1 = (ListView) view.findViewById(R.id.listView);
        View header = getActivity().getLayoutInflater().inflate(R.layout.listview_header, null);
        list1.addHeaderView(header);
        list1.setAdapter(adapter);
    }
}