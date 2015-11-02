package com.example.bahadir.myapplicationn;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageFragment2 extends Fragment implements View.OnClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    ImageButton buton1;
    ListView liste1;
    private int mPage;
    private Uri outputFileUri;
    Dialog dialog;

    public static PageFragment2 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment2 fragment = new PageFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cevrendepaylasilanlar, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buton1= (ImageButton) view.findViewById(R.id.imageButton8);
        liste1 = (ListView) view.findViewById(R.id.listView3);
        buton1.setOnClickListener(this);
    }

    public void onClick(View view) {

        switch(view.getId()){

            case R.id.imageButton8:
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.hangitur);
                dialog.getWindow().setDimAmount(0.7f);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                ImageView image1,image2,image3,image4;
                image1 = (ImageView) dialog.findViewById(R.id.imageView6);
                image2 = (ImageView) dialog.findViewById(R.id.imageView7);
                image3 = (ImageView) dialog.findViewById(R.id.imageView8);
                image4 = (ImageView) dialog.findViewById(R.id.imageView9);
                image1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        openImageIntent();
                    }
                });
                break;

        }
    }

    private void openImageIntent() {

// Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "BenimDir" + File.separator);
        root.mkdirs();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager =getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Sec onu");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent,11);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== -1) {
            if (requestCode == 11) {
                final boolean isCamera;
                Log.i("tago" , "iscamerayı olusturdu");
                if (data == null) {
                    isCamera = true;
                    Log.i("tago" , "iscamera = true");
                } else {
                    final String action = data.getAction();
                    Log.i("tago", "action= " + action);
                    if (action == null) {
                        isCamera = false;
                        Log.i("tago" , "iscamera = false");
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        Log.i("tago" , "iscamera action");
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    try {
                        Bitmap kameraresmi = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImageUri);
                        buton1.setImageBitmap(kameraresmi);
                        dialog.cancel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("tago" ,String.valueOf(selectedImageUri));
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                    try {
                        Bitmap galeriresmi = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , selectedImageUri);
                        buton1.setImageBitmap(galeriresmi);
                        dialog.cancel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("tago" , "ikincisi");
                    Log.i("tago" , String.valueOf(selectedImageUri));
                }
            }
        }
    }
}