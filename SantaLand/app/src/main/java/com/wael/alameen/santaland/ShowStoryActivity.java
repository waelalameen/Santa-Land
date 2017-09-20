package com.wael.alameen.santaland;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowStoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView title = (TextView) findViewById(R.id.welcome_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Jannal.ttf");
        ImageView giftImage = (ImageView) findViewById(R.id.img_gift);
        TextView giftName = (TextView) findViewById(R.id.gift_name);
        TextView giftDesc = (TextView) findViewById(R.id.gift_desc);
        String name = getIntent().getExtras().getString("name");
        String desc = getIntent().getExtras().getString("desc");
        String image = getIntent().getExtras().getString("img");
        title.setTypeface(typeface);
        title.setText(name);
        giftName.setTypeface(typeface);
        giftDesc.setTypeface(typeface);
        giftName.setText(name);
        giftDesc.setText(desc);
        Picasso.with(this).load(image).resize(600, 300).onlyScaleDown().into(giftImage);
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
}
