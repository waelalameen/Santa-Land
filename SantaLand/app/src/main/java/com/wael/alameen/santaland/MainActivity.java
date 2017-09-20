package com.wael.alameen.santaland;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private RecyclerView.Adapter navAdapter;
    private List<NavItems> items = new ArrayList<>();
    private static final String phoneNumber = "07728462033";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
        TextView appTitle = (TextView) findViewById(R.id.app_title);
        final TextView textView = (TextView) findViewById(R.id.hint);
        TextView coverText = (TextView) findViewById(R.id.cover_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "comic.ttf");
        appTitle.setTypeface(typeface);
        appTitle.setText(getString(R.string.app_name));
        textView.setTypeface(typeface);
        coverText.setTypeface(typeface);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager.setOffscreenPageLimit(3);
        FragmentPager fragmentPager = new FragmentPager(getSupportFragmentManager());
        fragmentPager.add(new HomeFragment(), "");
        fragmentPager.add(new GiftsFragment(), "");
        fragmentPager.add(new StoryFragment(), "");
        viewPager.setAdapter(fragmentPager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.gift);
        tabLayout.getTabAt(2).setIcon(R.drawable.story);

        final ImageView fadAdd = (ImageView) findViewById(R.id.fab_add);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 2) {
                    AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                    fadeIn.setDuration(100);
                    fadeIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            fadAdd.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    fadAdd.startAnimation(fadeIn);
                    textView.startAnimation(fadeIn);

                } else {
                    AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            fadAdd.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    fadAdd.startAnimation(fadeOut);
                    textView.startAnimation(fadeOut);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //do nothing
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //do nothing
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.view_tab);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //do nothing
            }
        });

        fadAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WriteStoryActivity.class));
            }
        });

        int count = 0;
        NavItems navItems;
        String[] names = getResources().getStringArray(R.array.item_names);
        for (String name : names) {
            if (count == 0) {
                navItems = new NavItems(name, R.drawable.ic_share_black_24dp);
                items.add(navItems);
                count++;
            } else if (count == 1) {
                navItems = new NavItems(name, R.drawable.ic_call_black_24dp);
                items.add(navItems);
                count++;
            } else {
                navItems = new NavItems(name, R.drawable.ic_error_outline_black_24dp);
                items.add(navItems);
                count++;
            }
        }

        RecyclerView recyclerViewNavMenu = (RecyclerView) findViewById(R.id.nav_menu);
        recyclerViewNavMenu.setHasFixedSize(true);
        recyclerViewNavMenu.setLayoutManager(new LinearLayoutManager(this));
        navAdapter = new NavMenuAdapter(this, items, this);
        recyclerViewNavMenu.setAdapter(navAdapter);
        navAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_call) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

            startActivity(intent);
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                String message = "\nDon't miss the fun with Santa Land, Download it now on\n\n";
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Santa Land");
                String googlePlayLink = message + "https://play.google.com/store/apps/details?id=com.wael.alameen.santaland&hl=en\n\n";
                i.putExtra(Intent.EXTRA_TEXT, googlePlayLink);
                startActivity(Intent.createChooser(i, "choose one"));
                break;
            case 1:
                contactUs();
                break;
            case 2:
                showAbout();
                break;
            default:
                break;
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void contactUs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        LayoutInflater layoutInflater = getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.contact_us, null);
        builder.setView(v);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setContentView(v);
        TextView msg = (TextView) dialog.findViewById(R.id.hint);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Jannal.ttf");
        msg.setTypeface(typeface);

        ImageView call = (ImageView) dialog.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });

        ImageView face = (ImageView) dialog.findViewById(R.id.facebook);
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String faceLink = "https://www.facebook.com/SantalandGifts/?fref=ts";
                Uri uri = Uri.parse(faceLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.about_layout, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setContentView(view);
        TextView simpleHint = (TextView) dialog.findViewById(R.id.first_hint);
        TextView about = (TextView) dialog.findViewById(R.id.second_hint);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "comic.ttf");
        simpleHint.setTypeface(typeface);
        about.setTypeface(typeface);
        dialog.show();
    }
}
