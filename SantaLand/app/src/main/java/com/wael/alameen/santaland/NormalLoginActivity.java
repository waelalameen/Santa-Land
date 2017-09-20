package com.wael.alameen.santaland;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class NormalLoginActivity extends AppCompatActivity {

    private String mFullName, mAge, mGender, mPhoneNumber;
    private Spinner gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_login);
        final AutoCompleteTextView fullName = (AutoCompleteTextView) findViewById(R.id.full_name);
        final AutoCompleteTextView age = (AutoCompleteTextView) findViewById(R.id.age);
        final AutoCompleteTextView phoneNumber = (AutoCompleteTextView) findViewById(R.id.phone_number);
        Button submit = (Button) findViewById(R.id.submit);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "comic.ttf");
        TextView hint = (TextView) findViewById(R.id.just_hint);
        TextView text = (TextView) findViewById(R.id.text);
        hint.setTypeface(typeface);
        fullName.setTypeface(typeface);
        age.setTypeface(typeface);
        text.setTypeface(typeface);
        gender = (Spinner) findViewById(R.id.gender);
        phoneNumber.setTypeface(typeface);
        submit.setTypeface(typeface);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_gender, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(arrayAdapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFullName = fullName.getText().toString().trim();
                mAge = age.getText().toString().trim();
                mPhoneNumber = phoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(mFullName) || TextUtils.isEmpty(mAge) || TextUtils.isEmpty(mGender) || TextUtils.isEmpty(mPhoneNumber)
                        || ((TextUtils.isEmpty(mFullName) && TextUtils.isEmpty(mAge) && TextUtils.isEmpty(mGender) && TextUtils.isEmpty(mPhoneNumber)))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NormalLoginActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle(getResources().getString(R.string.app_name));
                    builder.setMessage("You Have To Fill All The Fields !!");
                    Dialog dialog = builder.create();
                    dialog.show();
                } else {
                    Task task = new Task(NormalLoginActivity.this);
                    task.execute(mFullName, mAge, mGender, mPhoneNumber);
                    SharedPreferences preferences = getSharedPreferences("userName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", mFullName).apply();
                }
            }
        });
    }
}
