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
import java.util.Arrays;


public class MainFragment extends Fragment{
    String tumisim;
    String firstname;
    String middlename;
    String lastname;
    String email;
    String cinsiyet;
    Profile profile;
    String urll;
    AccessToken accessToken;
    Bitmap bitmap=null;
    ProfileTracker protracker;
    AccessTokenTracker tracker;
    NettenIndir a = new NettenIndir();
    Button buton1;
    private CallbackManager mCallbackManager;

    public void onCreate(Bundle savedInstanceState){
        Log.i("tago" , "MainFragment onCreate");
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
        protracker.startTracking();
    }
    public void onStart() {
        Log.i("tago", "MainFragment onStart");
        super.onStart();
    }
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            tumisim = profile.getName();
            firstname = profile.getFirstName();
            middlename = profile.getMiddleName();
            lastname = profile.getLastName();
        }
    }
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            Log.i("tago" ,"MainFragment Login Result on Success okudum");
            displayCase(profile);
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                email = object.getString("email");
                                Log.i("tago" , "Main Fragment user email :" + email);
                                cinsiyet = object.getString("gender");
                                Log.i("tago" , "MainFragment user gender: "  + cinsiyet);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields" , "id,name,email,gender");
            request.setParameters(parameters);
            request.executeAsync();
        }

        public void onCancel() {

        }

        public void onError(FacebookException e) {

        }
    };
    private void displayCase(Profile profile) {
        if(profile!=null){
            a.execute(profile.getId());
            Log.i("tago" , "Kullanıcı Main Fragment .getId " + profile.getId());
            Log.i("tago" , "MainFragment fotoyu çektim");
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginb = (LoginButton) view.findViewById(R.id.login_button);
        loginb.setReadPermissions(Arrays.asList("user_friends" , "public_profile" ,"email"));
        loginb.setFragment(this);
        loginb.registerCallback(mCallbackManager, mCallBack);
        buton1 = (Button) view.findViewById(R.id.button);
        registerForContextMenu(buton1);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void onStop(){
        super.onStop();
        tracker.stopTracking();
        protracker.stopTracking();
        getActivity().finish();
    }

    public class NettenIndir extends AsyncTask<String , Void , Bitmap>{

        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL("https://graph.facebook.com/" + params[0]+ "/picture?type=large");
                urll = "https://graph.facebook.com/" + params[0]+ "/picture?type=large";
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.i("tago" , "MainFragment connection sağladm");
                connection.setDoInput(true);
                Log.i("tago" , "MainFragment doinput");
                connection.connect();
                Log.i("tago" , "MainFragment connect sağladım");
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                Log.i("tago" , "Main Fragment bitmap yaptım");
                if(bitmap == null){
                    Log.i("tago" , "Main Fragment bitmap yok");
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
            Log.i("tago" , "Main Fragment bitmap yerleştirdim");
            if(tumisim!=null) {
                Intent i = new Intent(getActivity(), TakipServisi.class);
                i.putExtra("isim", tumisim);
                i.putExtra("resim", bitmap);
                i.putExtra("resimurl" , urll);
                i.putExtra("firstname" , firstname);
                i.putExtra("lastname" , lastname);
                i.putExtra("email" , email);
                i.putExtra("gender" , cinsiyet);
                getActivity().startService(i);

            }
        }
    }
}

