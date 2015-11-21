package com.example.bahadir.myapplicationn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzhl.runOnUiThread;

public class PageFragment2 extends Fragment implements View.OnClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    ImageButton buton1;
    ListView liste1;
    private int mPage;
    private Uri outputFileUri;
    Dialog dialoge;
    String charset, query;
    public ArrayList<Paylasilanlar> PaylasilanlarListesi;
    PaylasilanlariCek pC;
    String veritabaniid;
    ArkadanYaziGonder aYG;
    //ali edited here
    private ImageView mImageView;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private String selectedImagePath = "";
    boolean GallaryPhotoSelected = false;
    public static String Finalmedia = "";

    public static PageFragment2 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment2 fragment = new PageFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        veritabaniid=sharedPreferenceIdAl();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cevrendepaylasilanlar, container, false);
        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buton1 = (ImageButton) view.findViewById(R.id.imageButton8);
        liste1 = (ListView) view.findViewById(R.id.listView3);
        buton1.setOnClickListener(this);
        pC = new PaylasilanlariCek();
        pC.execute(veritabaniid);
    }
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imageButton8:
                dialoge = new Dialog(getActivity());
                dialoge.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialoge.setContentView(R.layout.hangitur);
                dialoge.getWindow().setDimAmount(0.7f);
                dialoge.setCancelable(false);
                dialoge.setCanceledOnTouchOutside(false);
                dialoge.show();
                ImageView image1, image2, image3, image4;
                image1 = (ImageView) dialoge.findViewById(R.id.imageView6);
                image2 = (ImageView) dialoge.findViewById(R.id.imageView7);
                image3 = (ImageView) dialoge.findViewById(R.id.imageView8);
                image4 = (ImageView) dialoge.findViewById(R.id.imageView9);
                //ali edited here for image activity
                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Image");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) { // pick from
                        // camera
                        if (item == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                    "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                            try {
                                intent.putExtra("return-data", true);

                                startActivityForResult(intent, PICK_FROM_CAMERA);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else { // pick from file
                            Intent intent = new Intent();

                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);

                            startActivityForResult(Intent.createChooser(intent,
                                    "Complete action using"), PICK_FROM_FILE);
                        }
                    }
                });
                final AlertDialog dialog1 = builder.create();

                //
                image1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialoge.dismiss();
                        dialog1.show();
                        //Intent i= new Intent(getActivity(), resim_aktivitesi.class);
                        //startActivity(i);
                    }
                });
                image2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialoge.dismiss();
                        final Dialog dialog = new Dialog(getActivity(),R.style.DialogTheme);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.yazipaylas);
                        dialog.getWindow().setDimAmount(0.7f);
                        dialog.show();
                        final TextView tv1 = (TextView) dialog.findViewById(R.id.textView10);
                        final EditText etv1 = (EditText) dialog.findViewById(R.id.editText4);
                        Button onaylaButonu = (Button) dialog.findViewById(R.id.button3);
                        onaylaButonu.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String yazi = etv1.getText().toString();
                                yaziyiServeraGonder(yazi);
                                PaylasilanlarListesi.clear();
                                PaylasilanlariCek pc = new PaylasilanlariCek();
                                pc.execute(veritabaniid);
                                dialog.dismiss();
                            }

                            private void yaziyiServeraGonder(String yazi) {
                                aYG = new ArkadanYaziGonder();
                                aYG.execute(yazi);
                            }
                        });

                    }
                });
                image3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), KarsilastirmaliFotoPaylas.class);
                        startActivity(i);
                    }
                });

                image4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        final Dialog dialdial = new Dialog(getActivity(),R.style.DialogTheme);
                        dialdial.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialdial.setContentView(R.layout.anketpaylas);
                        dialdial.getWindow().setDimAmount(0.7f);
                        dialdial.show();
                        final EditText etv1 , etv2 , etv3 , etv4;
                        final Button buton1 , buton2,buton3;
                        final LinearLayout lay1 , lay2;
                        lay1 = (LinearLayout) dialdial.findViewById(R.id.lay3);
                        lay2 = (LinearLayout) dialdial.findViewById(R.id.lay4);
                        etv1 = (EditText) dialdial.findViewById(R.id.editText5);
                        etv2 = (EditText) dialdial.findViewById(R.id.editText6);
                        etv3 = (EditText) dialdial.findViewById(R.id.editText7);
                        etv4 = (EditText) dialdial.findViewById(R.id.editText8);
                        buton1 = (Button) dialdial.findViewById(R.id.button11);
                        buton2 = (Button) dialdial.findViewById(R.id.button12);
                        buton3 = (Button) dialdial.findViewById(R.id.button13);
                        buton1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                lay1.setVisibility(View.VISIBLE);
                            }
                        });
                        buton2.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                              lay2.setVisibility(View.VISIBLE);
                            }
                        });
                        buton3.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                String soru = etv1.getText().toString();
                                String ilksecenek = etv2.getText().toString();
                                String ikincisecenek = etv3.getText().toString();
                                String ucuncusecenek = etv4.getText().toString();

                            }
                        });
                    }
                });
                break;
        }
    }
    private void openImageIntent() {

// Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "BenimDir" + File.separator);
        root.mkdirs();
        final String fname = "img_" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
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

        startActivityForResult(chooserIntent, 11);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != -1)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                Log.i("TAG", "Inside PICK_FROM_CAMERA");
                doCrop();
                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();
                Log.i("TAG",
                        "After Crop mImageCaptureUri " + mImageCaptureUri.getPath());
                GallaryPhotoSelected = true;
                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                selectedImagePath = mImageCaptureUri.getPath();

                Log.i("TAG", "After Crop selectedImagePath " + selectedImagePath);

                if (GallaryPhotoSelected) {
                    selectedImagePath = getRealPathFromURI(mImageCaptureUri);
                    Log.i("TAG", "Absolute Path " + selectedImagePath);
                    GallaryPhotoSelected = true;
                }

                Finalmedia = selectedImagePath;

                if (extras != null) {
                    // Bitmap photo = extras.getParcelable("data");
                    Log.i("TAG", "Inside Extra " + selectedImagePath);
                    Bitmap photo = (Bitmap) extras.get("data");
                    //photo = getRoundedCroppedBitmap(photo, 300);
                    selectedImagePath = String.valueOf(System.currentTimeMillis())
                            + ".jpg";

                    Log.i("TAG", "new selectedImagePath before file "
                            + selectedImagePath);

                    File file = new File(Environment.getExternalStorageDirectory(),
                            selectedImagePath);

                    try {
                        file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file);
                        photo.compress(Bitmap.CompressFormat.PNG, 95, fos);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getActivity(),
                                "Sorry, Camera Crashed-Please Report as Crash A.",
                                Toast.LENGTH_LONG).show();
                    }

                    selectedImagePath = Environment.getExternalStorageDirectory()
                            + "/" + selectedImagePath;
                    Log.i("TAG", "After File Created  " + selectedImagePath);

                    Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);
                    Log.i("TAG", "After File Created  " + bm.toString());
                    getActivity().setContentView(R.layout.cropmain);
                    Button uploadbutton = (Button) getActivity().findViewById(R.id.uploadbutton);
                    mImageView = (ImageView) getActivity().findViewById(R.id.iv_photo);
                    mImageView.setImageBitmap(bm);
                    uploadbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                        }
                                    });
                                    try {
                                        uploadFile(selectedImagePath);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });
                }

                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) f.delete();


                break;

        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private String sharedPreferenceIdAl() {
        SharedPreferences sp = getActivity().getSharedPreferences("kullaniciverileri", Context.MODE_PRIVATE);
        String veritabani_id = sp.getString("veritabani_id", "default id");
        Log.i("tago", "Mesajlasma veritabani id = " + veritabani_id);
        return veritabani_id;
    }
    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 400);
            intent.putExtra("outputY", 600);
            intent.putExtra("aspectX", 1);
            //intent.putExtra("aspectY", 1);
            //intent.putExtra("scale", false);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getActivity().getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);

        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f, finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);


        return output;
    }
    public int uploadFile(String sourceFileUri) throws MalformedURLException {
        String userid = sharedPreferenceIdAl();
        String upLoadServerUri = "http://www.ceng.metu.edu.tr/~e1818871/shappy/photos/picture_activity.php?userID=" + userid;
        String fileName = sourceFileUri;
        int serverResponseCode = 0;
        ProgressDialog dialog = null;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        String name = sourceFile.getName();
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File Does not exist");
            return 0;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);
            conn.setRequestProperty("userID", userid.trim());
            Log.i("uploadfile userid", "  =========    " + userid);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            if (serverResponseCode == 200) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }
                });
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getActivity(), "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "Exception : " + e.getMessage(), e);
        }
        HttpURLConnection sconnection = null;
        try {
            sconnection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/paylas.php?userid=" +
                    userid + "&type=teklifoto&text=http://www.ceng.metu.edu.tr/~e1818871/shappy/photos/" + userid + "/" + name).openConnection();
            Log.i("tago", "Page Fragment1 yeni kanal kur bagı kuruldu");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sconnection.setDoOutput(true);
        sconnection.setDoInput(true);
        sconnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
        sconnection.setRequestProperty("Accept", "* /*");
        sconnection.setRequestProperty("Accept-Charset", "utf-8");
        sconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + "utf-8");

        try {
            OutputStream output = new BufferedOutputStream(sconnection.getOutputStream());
            //output.write(String.format("param1=%s", URLEncoder.encode("userid", "utf-8")).getBytes("utf-8"));
            output.close();
            int a = sconnection.getResponseCode();
            String b = sconnection.getResponseMessage();
            Log.i("tago", "rerere" + a + " " + b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] lines = selectedImagePath.split("/");
        return serverResponseCode;
    }

    public class ArkadanYaziGonder extends AsyncTask<String,Void,String> {

        protected String doInBackground(String... params) {
            String param1 = "id";
            String param2 = "type";
            String param3 = "text";
            charset = "UTF-8";
            try {
                query = String.format("param1=%s&param2=%s&param3=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset),
                        URLEncoder.encode(param3, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i("tago", "VeriTabani arkadan yazi gonderme başlatıldı");
            try {
                return yaziyigonder(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadı";
            }
        }

        private String yaziyigonder(String yazii) {
            URLConnection connection = null;

            try {
                Log.i("tago", yazii);
                Log.i("tago", veritabaniid);
                connection = new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/paylas.php?userid=" + veritabaniid
                        + "&type=text" + "&text=" + yazii).openConnection();
                Log.i("tago", "Arkadan Yazi Gonder bagı kurdum");
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            try {
                OutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(query.getBytes(charset));
                InputStream response = connection.getInputStream();
                Log.i("tago", "Arkadan Yazi Gonder yazdım");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Arkadan Yazi Gonder yazamadım");
            }
            return "alabama";
        }
    }
    public class PaylasilanlariCek extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String param1 = "id";
            charset = "UTF-8";
            try {
                query = String.format("param1=%s", URLEncoder.encode(param1, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i("tago", "PageFragment2 paylasilanlari alma baÅŸlatÄ±ldÄ±");
            try {
                return paylasilanlaricek(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "olmadÄ±";
            }
        }

        private String paylasilanlaricek(String veritabaniidd) {
            HttpURLConnection connection = null;

            try {
                Log.i("tago", veritabaniidd);
                connection = (HttpURLConnection) new URL("http://www.ceng.metu.edu.tr/~e1818871/shappy/paylasilanlari_al?id=" + veritabaniidd).openConnection();
                Log.i("tago", "Paylasinlari alma bagÄ± kurdum");
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

                    for (int i = 0; i < 3; i++) {
                        inputline = in.readLine();
                        Log.i("tago", "" + i + " for inputline= " + inputline);
                    }
                    PaylasilanlarListesi = new ArrayList();
                    JSONArray jsono = new JSONArray(inputline);
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        Paylasilanlar paylasilan = new Paylasilanlar();
                        paylasilan.setVeriid(object.optString("id"));
                        paylasilan.setGonderenid(object.optString("user_id"));
                        paylasilan.setCesit(object.optString("type"));
                        paylasilan.setYaziveyaurl(object.optString("text"));
                        paylasilan.setDate(object.optString("date"));
                        PaylasilanlarListesi.add(paylasilan);
                    }
                } else {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    Log.i("tago", "Error Stream");
                    String inputline;
                    while ((inputline = in.readLine()) != null) {
                        Log.i("tago", "camcamcam" + inputline);
                    }
                    PaylasilanlarListesi = new ArrayList();
                    JSONArray jsono = new JSONArray(inputline);
                    for (int i = 0; i < jsono.length(); i++) {
                        JSONObject object = jsono.getJSONObject(i);
                        Paylasilanlar paylasilan = new Paylasilanlar();
                        paylasilan.setVeriid(object.optString("id"));
                        paylasilan.setGonderenid(object.optString("user_id"));
                        paylasilan.setCesit(object.optString("type"));
                        paylasilan.setYaziveyaurl(object.optString("text"));
                        paylasilan.setDate(object.optString("date"));
                        PaylasilanlarListesi.add(paylasilan);
                    }
                }
                in.close();
                Log.i("tago", "Page Fragment cevredekileri gor inputline yazdï¿½m");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("tago", "Page Fragment cevredekileri gor yazamadï¿½m");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tago", "json Exception");
            }
            return "inputline";
        }

        protected void onPostExecute(String s) {
            PaylasilanlarAdapter adapter = new PaylasilanlarAdapter(getActivity() , PaylasilanlarListesi);
            liste1.setAdapter(adapter);
        }
    }
}