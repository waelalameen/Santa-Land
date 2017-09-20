package com.wael.alameen.santaland;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Task extends AsyncTask {

    private String fullName, age, gender, phoneNumber, photo;
    private Context context;
    private static final String hostURL = "http://mallnet.me/SantaLand/upload_info.php";
    private static boolean isLoggedIn = false;

    Task(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        fullName = (String) params[0];
        age = (String) params[1];
        gender = (String) params[2];
        phoneNumber = (String) params[3];
//        SharedPreferences preferences = context.getSharedPreferences("FacebookLogin", Context.MODE_PRIVATE);
//        boolean isLoggedInWithFacebook = preferences.getBoolean("isLoggedInWithFacebook", false);

        try {
            URL url = new URL(hostURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(fullName, "UTF-8")+"&"
                    +URLEncoder.encode("age", "UTF-8")+"="+URLEncoder.encode(age, "UTF-8")+"&"
                    +URLEncoder.encode("gender", "UTF-8")+"="+URLEncoder.encode(gender, "UTF-8")+"&"
                    +URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(phoneNumber, "UTF-8");
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
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
//        SharedPreferences preferences = context.getSharedPreferences("FacebookLogin", Context.MODE_PRIVATE);
//        boolean isLoggedInWithFacebook = preferences.getBoolean("isLoggedInWithFacebook", false);

        context.startActivity(new Intent(context, MainActivity.class));
        isLoggedIn = true;
        SharedPreferences preferences = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit().putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }
}
