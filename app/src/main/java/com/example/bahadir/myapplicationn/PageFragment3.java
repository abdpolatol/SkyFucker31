package com.example.bahadir.myapplicationn;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PageFragment3 extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    ImageView image1 , image2;
    TextView tv1 , tv2 ,tv3 ;
    Bitmap resim;
    String isim;
    private int mPage;

    public static PageFragment3 newInstance(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, page);
        PageFragment3 fragment = new PageFragment3();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getActivity().getIntent().getExtras();
        isim = b.getString("isim");
        resim = b.getParcelable("resim");
        View view = inflater.inflate(R.layout.profil, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tanimlar(view);
    }

    private void tanimlar(View view) {
        image1 = (ImageView) view.findViewById(R.id.imageView);
        image2 = (ImageView) view.findViewById(R.id.imageView3);
        tv1 = (TextView) view.findViewById(R.id.textView);
        tv2 = (TextView) view.findViewById(R.id.textView5);
        tv3 = (TextView) view.findViewById(R.id.textView6);
        image1.setImageBitmap(resim);
        tv1.setText(isim);
    }
}