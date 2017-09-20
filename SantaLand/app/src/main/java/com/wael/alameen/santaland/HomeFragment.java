package com.wael.alameen.santaland;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Slider adapterSlider;
    private List<String> images = new ArrayList<>();
    private String image1, image2, image3, image4;
    private ImageView one, two, three, four;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new HomeTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        one = (ImageView) view.findViewById(R.id.image_one);
        two = (ImageView) view.findViewById(R.id.image_two);
        three = (ImageView) view.findViewById(R.id.image_three);
        four = (ImageView) view.findViewById(R.id.image_four);
        adapterSlider = new Slider(getContext());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.ads_view_pager);
        viewPager.setOffscreenPageLimit(6);
        viewPager.setAdapter(adapterSlider);
        adapterSlider.notifyDataSetChanged();
    }

    private class Slider extends PagerAdapter {

        Context context;

        Slider(Context context) {
            LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slide, container, false);
            ImageView imagesSlide = (ImageView) view.findViewById(R.id.images_slide);
            Picasso.with(getContext()).load(images.get(position)).resize(600, 300).onlyScaleDown().into(imagesSlide);
            //imagesSlide.setImageResource(R.drawable.logo_5tech);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView((RelativeLayout) object);
        }
    }

    class HomeTask extends AsyncTask<Void, Void, Void> {

        private static final String ADS_URL = "http://mallnet.me/SantaLand/read_ads.php";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(ADS_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = buffer.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                String json_string = stringBuilder.toString().trim();
                JSONObject object = new JSONObject(json_string);
                JSONArray jsonArray = object.getJSONArray("server_response");
                int i = 0;
                while (i < jsonArray.length()) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    i++;
                    image1 = jsonObj.getString("img7");
                    image2 = jsonObj.getString("img8");
                    image3 = jsonObj.getString("img9");
                    image4 = jsonObj.getString("img10");
                    images.add(0, jsonObj.getString("img1"));
                    images.add(1, jsonObj.getString("img2"));
                    images.add(2, jsonObj.getString("img3"));
                    images.add(3, jsonObj.getString("img4"));
                    images.add(4, jsonObj.getString("img5"));
                    images.add(5, jsonObj.getString("img6"));

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            try {
                adapterSlider.notifyDataSetChanged();
            } catch (Exception e) {}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Picasso.with(getContext()).load(image1).resize(600, 300).onlyScaleDown().into(one);
            Picasso.with(getContext()).load(image2).resize(600, 300).onlyScaleDown().into(two);
            Picasso.with(getContext()).load(image3).resize(600, 300).onlyScaleDown().into(three);
            Picasso.with(getContext()).load(image4).resize(600, 300).onlyScaleDown().into(four);
            adapterSlider.notifyDataSetChanged();
        }
    }
}
