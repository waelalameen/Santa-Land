package com.wael.alameen.santaland;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class EnterPhoneActivity extends AppCompatActivity {

    private String mPhone;
    private static final String hostURL = "http://mallnet.me/SantaLand/update_phone.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "comic.ttf");
        TextView hint = (TextView) findViewById(R.id.just_hint);
        final AutoCompleteTextView phone = (AutoCompleteTextView) findViewById(R.id.phone_number);
        mPhone = phone.getText().toString().trim();
        Button submit = (Button) findViewById(R.id.submit);
        hint.setTypeface(typeface);
        submit.setTypeface(typeface);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mPhone)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterPhoneActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle(getResources().getString(R.string.app_name));
                    builder.setMessage("Please Enter Phone Number");
                    Dialog dialog = builder.create();
                    dialog.show();
                } else {

                    new AsyncTask<String, Void, Void>() {

                        @Override
                        protected Void doInBackground(String... params) {
                            String phone = params[0];
                            try {
                                URL url = new URL(hostURL);
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setRequestMethod("POST");
                                httpURLConnection.setDoOutput(true);
                                OutputStream outputStream = httpURLConnection.getOutputStream();
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                                String data = URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(phone, "UTF-8");
                                bufferedWriter.write(data);
                                bufferedWriter.flush();
                                bufferedWriter.close();
                                outputStreamWriter.close();
                                outputStream.close();
                                httpURLConnection.disconnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            startActivity(new Intent(EnterPhoneActivity.this, MainActivity.class));
                        }
                    }.execute(mPhone);
                }
            }
        });
    }
}
