package com.wael.alameen.santaland;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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

public class GiftsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Menu> menuList = new ArrayList<>();

    public GiftsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gifts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GiftTask().execute();
        recyclerView = (RecyclerView) view.findViewById(R.id.gift_items);
        recyclerView.setHasFixedSize(true);
        adapter = new ItemsAdapter(getContext(), menuList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), ShowGiftActivity.class);
        intent.putExtra("name", menuList.get(position).getName());
        intent.putExtra("desc", menuList.get(position).getDesc());
        intent.putExtra("img", menuList.get(position).getImage());
        intent.putExtra("position", position);
        intent.putExtra("facebook", menuList.get(position).getFacebookLink());
        intent.putExtra("instagram", menuList.get(position).getInstaLink());
        intent.putExtra("phone", menuList.get(position).getPhone());
        startActivity(intent);
    }

    class GiftTask extends AsyncTask<Void, Menu, Void> {

        private static final String ADS_URL = "http://mallnet.me/SantaLand/read_gifts.php";

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
                    Menu menu = new Menu(jsonObj.getString("name"), jsonObj.getString("desc"), jsonObj.getString("img")
                        , jsonObj.getString("face"), jsonObj.getString("insta"), jsonObj.getString("phone"));
                    publishProgress(menu);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Menu... values) {
            super.onProgressUpdate(values);
            menuList.add(0, values[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //adapter.notifyDataSetChanged();
        }
    }
}
