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
import android.widget.Toast;

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

public class StoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Menu> storyList = new ArrayList<>();

    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new StoryTask().execute();
        recyclerView = (RecyclerView) view.findViewById(R.id.story_items);
        recyclerView.setHasFixedSize(true);
        adapter = new ItemsAdapter(getContext(), storyList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), ShowStoryActivity.class);
        intent.putExtra("name", storyList.get(position).getName());
        intent.putExtra("desc", storyList.get(position).getDesc());
        intent.putExtra("img", storyList.get(position).getImage());
        startActivity(intent);
    }

    class StoryTask extends AsyncTask<Void, Menu, Void> {

        private static final String ADS_URL = "http://mallnet.me/SantaLand/read_stories.php";

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
                    Menu menu = new Menu(jsonObj.getString("user"), jsonObj.getString("desc"), jsonObj.getString("img"), "", "", "");
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
            storyList.add(0, values[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //adapter.notifyDataSetChanged();
        }
    }
}
