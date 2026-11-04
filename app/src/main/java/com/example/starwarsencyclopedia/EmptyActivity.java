package com.example.starwarsencyclopedia;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        if (savedInstanceState == null) {
            Bundle details = getIntent().getExtras();
            DetailsFragment fragment = DetailsFragment.newInstance(details);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container, fragment)
                    .commit();
        }
    }
}
