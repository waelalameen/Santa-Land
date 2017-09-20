package com.wael.alameen.santaland;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ShowGiftActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView facebook, insta, call, active;
    private SharedPreferences preferences, preferences2;
    private int pos;
    private String faceLink, instaLink, phone;
    private static int numberOfPresses = 0;
    private String name;
    private static final String hostURL = "http://mallnet.me/SantaLand/update_gift.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView title = (TextView) findViewById(R.id.welcome_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Jannal.ttf");
        ImageView giftImage = (ImageView) findViewById(R.id.img_gift);
        TextView giftName = (TextView) findViewById(R.id.gift_name);
        TextView giftDesc = (TextView) findViewById(R.id.gift_desc);
        name = getIntent().getExtras().getString("name");
        String desc = getIntent().getExtras().getString("desc");
        String image = getIntent().getExtras().getString("img");
        faceLink = getIntent().getExtras().getString("facebook");
        instaLink = getIntent().getExtras().getString("instagram");
        phone = getIntent().getExtras().getString("phone");
        pos = getIntent().getExtras().getInt("position");
        title.setTypeface(typeface);
        title.setText(name);
        giftName.setTypeface(typeface);
        giftDesc.setTypeface(typeface);
        giftName.setText(name);
        giftDesc.setText(desc);
        Picasso.with(this).load(image).resize(600, 300).onlyScaleDown().into(giftImage);

        facebook = (ImageView) findViewById(R.id.facebook);
        insta = (ImageView) findViewById(R.id.instagram);
        call = (ImageView) findViewById(R.id.call);
        active = (ImageView) findViewById(R.id.active);

        facebook.setOnClickListener(this);
        insta.setOnClickListener(this);
        call.setOnClickListener(this);
        active.setOnClickListener(this);

        preferences = getSharedPreferences("dayTime", MODE_PRIVATE);
        long pressTime = preferences.getLong("time", 0);
        preferences2 = getSharedPreferences("pos", MODE_PRIVATE);
        int currentPos = preferences2.getInt("position", 0);

        if ((System.currentTimeMillis() - pressTime) < 24 * 60 * 60 * 1000 && currentPos == pos) {
            active.setImageResource(R.drawable.on);
        } else {
            active.setImageResource(R.drawable.off);
            numberOfPresses = 0;
            SharedPreferences.Editor editor2 = preferences2.edit();
            editor2.putInt("numOfPresses", numberOfPresses);
            editor2.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook:
                setFaceLink();
                break;
            case R.id.call:
                setCall();
                break;
            case R.id.active:
                setActiveButton();
                break;
            case R.id.instagram:
                setInstaLink();
                break;
            default:
                break;
        }
    }

    private void setInstaLink() {
        insta.setImageResource(R.drawable.insta2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                insta.setImageResource(R.drawable.insta1);
            }
        }, 200);

        Uri uri = Uri.parse(instaLink);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setActiveButton() {
        active.setImageResource(R.drawable.on);
        preferences = getSharedPreferences("dayTime", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("time", System.currentTimeMillis());
        editor.commit();
        preferences2 = getSharedPreferences("pos", MODE_PRIVATE);
        numberOfPresses = preferences2.getInt("numOfPresses", 0);
        numberOfPresses++;
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.putInt("position", pos);
        editor2.putInt("numOfPresses", numberOfPresses);
        editor2.commit();
        SharedPreferences pref = getSharedPreferences("userName", MODE_PRIVATE);
        String user = pref.getString("name", "");
        new Task().execute(name, Integer.toString(numberOfPresses), user);
    }

    private void setCall() {
        call.setImageResource(R.drawable.phone2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                call.setImageResource(R.drawable.phone1);
            }
        }, 200);

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    private void setFaceLink() {
        facebook.setImageResource(R.drawable.face2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                facebook.setImageResource(R.drawable.face1);
            }
        }, 200);

        Uri uri = Uri.parse(faceLink);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    class Task extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String name = params[0];
            String presses = params[1];
            String user = params[2];

            try {
                URL url = new URL(hostURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"
                        +URLEncoder.encode("presses", "UTF-8")+"="+URLEncoder.encode(presses, "UTF-8")+"&"
                        +URLEncoder.encode("user", "UTF-8")+"="+URLEncoder.encode(user, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowGiftActivity.this);
            builder.setCancelable(false);
            LayoutInflater layoutInflater = getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.active_button_dialog, null);
            builder.setView(v);
            final AlertDialog dialog = builder.create();
            dialog.setContentView(v);
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
    }
}
