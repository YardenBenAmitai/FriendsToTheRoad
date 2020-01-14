package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SearchTripActivity extends AppCompatActivity {

    private Spinner tripSpinner;
    private Spinner areaSpinner;
    private Button search;
    private ImageButton back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trip);

        search = findViewById(R.id.b_search);
        back_home= findViewById(R.id.back_b);
        tripSpinner = findViewById(R.id.tripkindspinner);
        areaSpinner = findViewById(R.id.tripareaspinner);

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchTripActivity.this, HomeActivity.class));
            }
        });

        ArrayAdapter<String> tripAdapter = new ArrayAdapter<>(SearchTripActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.tripkinds));
        tripAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripSpinner.setAdapter(tripAdapter);

        tripSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tripSpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(SearchTripActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.areakinds));
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaSpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SearchTripActivity.this, SearchListActivity.class);
                intent.putExtra("KIND", tripSpinner.getSelectedItem().toString());
                intent.putExtra("AREA", areaSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }
}
