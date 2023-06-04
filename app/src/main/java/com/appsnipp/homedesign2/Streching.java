package com.appsnipp.homedesign2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.namespace.R;

public class Streching extends Fragment {
    CardView neck, waist, shoulder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.streching, container, false);

        neck = v.findViewById(R.id.neck);
        waist = v.findViewById(R.id.waist);

        neck.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Neckstreching.class);
            startActivity(intent);
        });

        waist.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Waiststreching.class);
            startActivity(intent);

        });


        return v;
    }
}