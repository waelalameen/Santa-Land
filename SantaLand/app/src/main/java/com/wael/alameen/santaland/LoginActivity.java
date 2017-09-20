package com.wael.alameen.santaland;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private String name, gender, birthday;
    private static boolean isLoggedInWithFacebook = false;
    private static boolean isLoggedIn = false;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        TextView welcomeTitle = (TextView) findViewById(R.id.welcome_title);
        TextView someHint = (TextView) findViewById(R.id.some_hint);
        Button appInfo = (Button) findViewById(R.id.app_info);
        Button logIn = (Button) findViewById(R.id.log_in);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Jannal.ttf");
        welcomeTitle.setTypeface(typeface);
        someHint.setTypeface(typeface);
        appInfo.setTypeface(typeface);
        logIn.setTypeface(typeface);

//        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login);
//        callbackManager = CallbackManager.Factory.create();
//
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                isLoggedInWithFacebook = true;
//                preferences = getSharedPreferences("FacebookLogin", MODE_PRIVATE);
//                editor = preferences.edit().putBoolean("isLoggedInWithFacebook", isLoggedInWithFacebook);
//                editor.apply();
//                AccessToken accessToken = loginResult.getAccessToken();
//                Profile profile = Profile.getCurrentProfile();
//                name = profile.getName();
//                preferences = getSharedPreferences("userName", MODE_PRIVATE);
//                editor.putString("name", name).apply();
//
//                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback()
//                {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        try {
//                            gender = object.getString("gender");
//                            birthday = object.getString("birthday");
//                            Task task = new Task(LoginActivity.this);
//                            task.execute(name, birthday, gender);
//                            isLoggedIn = true;
//                            preferences = getSharedPreferences("LogIn", MODE_PRIVATE);
//                            editor = preferences.edit().putBoolean("isLoggedIn", isLoggedIn);
//                            editor.apply();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.d("JSONException", e.getMessage());
//                        }
//                    }
//                });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "email, gender, birthday");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                builder.setCancelable(true);
//                builder.setTitle(getResources().getString(R.string.app_name));
//                builder.setMessage("You cancelled Facebook Login");
//                Dialog dialog = builder.create();
//                dialog.show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                builder.setCancelable(true);
//                builder.setTitle(getResources().getString(R.string.app_name));
//                builder.setMessage(error.getMessage());
//                Dialog dialog = builder.create();
//                dialog.show();
//            }
//        });
//
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isLoggedInWithFacebook = false;
//                preferences = getSharedPreferences("FacebookLogin", MODE_PRIVATE);
//                editor = preferences.edit().putBoolean("isLoggedInWithFacebook", isLoggedInWithFacebook);
//                editor.apply();
                startActivity(new Intent(LoginActivity.this, NormalLoginActivity.class));
            }
        });

        appInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.app_info, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.setContentView(view);
                TextView msg = (TextView) dialog.findViewById(R.id.hint);
                Typeface typeface = Typeface.createFromAsset(getAssets(), "Jannal.ttf");
                msg.setTypeface(typeface);
                Button ok = (Button) dialog.findViewById(R.id.ok);
                ok.setTypeface(typeface);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

