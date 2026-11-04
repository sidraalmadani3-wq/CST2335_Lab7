package com.example.starwarsencyclopedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {
    public static DetailsFragment newInstance(Bundle details) {
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(details);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle details = getArguments();

        if (details != null) {
            TextView name = view.findViewById(R.id.name_value);
            TextView height = view.findViewById(R.id.height_value);
            TextView mass = view.findViewById(R.id.mass_value);

            name.setText(details.getString("name", ""));
            height.setText(details.getString("height", ""));
            mass.setText(details.getString("mass", ""));
        }

        return view;
    }
}
