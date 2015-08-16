package com.example.bahadir.myapplicationn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainFragment extends Fragment implements View.OnClickListener {
    TextView tv1;
    Button buton1;
    Profile profile;
    ImageView image1;
    AccessToken accessToken;
    Bitmap bitmap=null;
    ProfileTracker protracker;
    AccessTokenTracker tracker;
    NettenIndir a = new NettenIndir();
    private CallbackManager mCallbackManager;

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            Log.i("tago" ,"okudum");
            displayCase(profile);
            GraphRequest request = GraphRequest.newMeRequest( AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,GraphResponse response) {
                            try {
                                String email=object.getString("user");
                                Log.d("tago" , "user email :" + email);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    });

            request.executeAsync();

        }

        public void onCancel() {

        }

        public void onError(FacebookException e) {

        }
    };
    private void displayCase(Profile profile) {
        if(profile!=null){
            tv1.setText("Welcome " + profile.getName());
            Log.i("tago", "adı yazdım");
            a.execute(profile.getId());
            Log.i("tago" , "fotoyu çektim");
            image1.setImageBitmap(bitmap);
            Log.i("tago", "yazdım");
        }
    }
    public MainFragment() {
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken nev) {

            }
        };
        protracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldpro, Profile newpro) {

            }
        };
        tracker.startTracking();
        protracker.stopTracking();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginb = (LoginButton) view.findViewById(R.id.login_button);
        loginb.setReadPermissions("user_friends" , "public_profile" ,"email");
        loginb.setFragment(this);
        loginb.registerCallback(mCallbackManager, mCallBack);
        tv1 = (TextView) view.findViewById(R.id.textView);
        image1 = (ImageView) view.findViewById(R.id.imageView);
        buton1 = (Button) view.findViewById(R.id.button);
        buton1.setOnClickListener(this);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            tv1.setText("Welcome " + profile.getName());
        }
    }
    public void onStop(){
        super.onStop();
        tracker.stopTracking();
        protracker.stopTracking();
    }
    public void onClick(View v) {
        Intent i = new Intent(getActivity() ,GoogleMeps.class);
        startActivity(i);
    }
    public class NettenIndir extends AsyncTask<String , Void , Bitmap>{

        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL("https://graph.facebook.com/" + params[0]+ "/picture?type=large");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.i("tago" , "connection sağladm");
                connection.setDoInput(true);
                Log.i("tago" , "doinput");
                connection.connect();
                Log.i("tago" , "connection sağladım");
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                Log.i("tago" , "bitmap yaptım");
                if(bitmap == null){
                    Log.i("tago" , "bitmap yok");
                }
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            image1.setImageBitmap(bitmap);
            Log.i("tago" , "bitmap yerleştirdim");
        }
    }
}
