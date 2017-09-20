package com.wael.alameen.santaland;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WriteStoryActivity extends AppCompatActivity {

    private String mStory, photo, name;
    private String photoBitmap;
    private static final String hostURL = "http://mallnet.me/SantaLand/upload_story.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "comic.ttf");
        Typeface typefaceTwo = Typeface.createFromAsset(getAssets(), "Jannal.ttf");
        final AutoCompleteTextView story = (AutoCompleteTextView) findViewById(R.id.phone_number);
        Button upload = (Button) findViewById(R.id.upload);
        Button submit = (Button) findViewById(R.id.submit);
        TextView title = (TextView) findViewById(R.id.welcome_title);
        title.setTypeface(typefaceTwo);
        story.setTypeface(typefaceTwo);
        upload.setTypeface(typeface);
        submit.setTypeface(typeface);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 200);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStory = story.getText().toString().trim();
                SharedPreferences preferences = getSharedPreferences("userName", MODE_PRIVATE);
                String user = preferences.getString("name", "");
                Task task = new Task();
                task.execute(mStory, name, photoBitmap, user);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            String[] path = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(image, path, null, null, null);
            cursor.moveToFirst();
            int colIndex = cursor.getColumnIndex(path[0]);
            photo = cursor.getString(colIndex);
            photoBitmap = encode(photo);
            File file = new File(photo);
            name = file.getName();
            Log.d("photo", photo);
            cursor.close();
        }
    }

    private String encode(String imagePath) {
        File file = new File(imagePath);
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap decodeFile = BitmapFactory.decodeStream(fileStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decodeFile.compress(Bitmap.CompressFormat.JPEG, 50, out);
        byte[] bytes = out.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(WriteStoryActivity.this, MainActivity.class));
        }
        return true;
    }

    class Task extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String story = params[0];
            String name = params[1];
            String photo = params[2];
            String user = params[3];

            try {
                URL url = new URL(hostURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String data = URLEncoder.encode("story", "UTF-8")+"="+URLEncoder.encode(story, "UTF-8")+"&"
                        +URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"
                        +URLEncoder.encode("photo", "UTF-8")+"="+URLEncoder.encode(photo, "UTF-8")+"&"
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
            AlertDialog.Builder builder = new AlertDialog.Builder(WriteStoryActivity.this);
            builder.setCancelable(true);
            builder.setTitle(getResources().getString(R.string.app_name));
            builder.setMessage("You Story Have Been Sent");
            Dialog dialog = builder.create();
            dialog.show();
            //startActivity(new Intent(WriteStoryActivity.this, MainActivity.class));
        }
    }
}
