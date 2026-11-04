package com.example.starwarsencyclopedia;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView characterList;
    private FrameLayoutHolder frameLayoutHolder;
    private ArrayList<JSONObject> characters = new ArrayList<>();
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        characterList = findViewById(R.id.character_list);
        View detailsContainer = findViewById(R.id.details_container);
        frameLayoutHolder = new FrameLayoutHolder(detailsContainer != null);
        adapter = new CharacterAdapter();
        characterList.setAdapter(adapter);

        characterList.setOnItemClickListener((parent, view, position, id) -> {
            JSONObject selectedCharacter = characters.get(position);
            Bundle details = new Bundle();
            details.putString("name", selectedCharacter.optString("name"));
            details.putString("height", selectedCharacter.optString("height"));
            details.putString("mass", selectedCharacter.optString("mass"));

            if (!frameLayoutHolder.hasContainer) {
                Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                intent.putExtras(details);
                startActivity(intent);
            } else {
                DetailsFragment fragment = DetailsFragment.newInstance(details);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.details_container, fragment)
                        .commit();
            }
        });

        new StarWarsTask().execute();
    }

    private class StarWarsTask extends AsyncTask<Void, Void, ArrayList<JSONObject>> {
        private boolean failed;

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... values) {
            ArrayList<JSONObject> result = new ArrayList<>();
            HttpURLConnection connection = null;
            try {
                URL url = new URL("https://swapi.dev/api/people/?format=json");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray people = new JSONObject(response.toString()).getJSONArray("results");
                for (int i = 0; i < people.length(); i++) {
                    result.add(people.getJSONObject(i));
                }
            } catch (Exception e) {
                failed = true;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            characters.clear();
            characters.addAll(result);
            adapter.notifyDataSetChanged();
            if (failed) {
                Toast.makeText(MainActivity.this, R.string.loading_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CharacterAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return characters.size();
        }

        @Override
        public Object getItem(int position) {
            return characters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                row = LayoutInflater.from(MainActivity.this).inflate(R.layout.row_character, parent, false);
            }
            TextView name = row.findViewById(R.id.character_name);
            name.setText(characters.get(position).optString("name"));
            return row;
        }
    }

    private static class FrameLayoutHolder {
        private final boolean hasContainer;

        private FrameLayoutHolder(boolean hasContainer) {
            this.hasContainer = hasContainer;
        }
    }
}
