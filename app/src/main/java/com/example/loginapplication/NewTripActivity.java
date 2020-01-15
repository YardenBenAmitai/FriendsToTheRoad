package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapplication.JavaObjects.NewTrip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewTripActivity extends AppCompatActivity {

    private Spinner tripSpinner;
    private Spinner areaSpinner;
    private EditText msg;
    private Button finished;
    private ImageButton back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtrip);

        msg= findViewById(R.id.msg);
        finished= findViewById(R.id.b_search);
        back_home= findViewById(R.id.back_b);
        tripSpinner = findViewById(R.id.tripkindspinner);
        areaSpinner= findViewById(R.id.tripareaspinner);

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewTripActivity.this, HomeActivity.class));
            }
        });

        ArrayAdapter<String> tripAdapter = new ArrayAdapter<>(NewTripActivity.this, android.R.layout.simple_list_item_1,
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


        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(NewTripActivity.this, android.R.layout.simple_list_item_1,
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

        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewTrip trip= new NewTrip(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        tripSpinner.getSelectedItem().toString(),
                        areaSpinner.getSelectedItem().toString(),
                        msg.getText().toString());


                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Trips").push();
                String key= ref.getKey();
                ref.setValue(trip);


                FirebaseDatabase.getInstance().getReference("Users").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("TripsCreatedList").child(key).setValue("true");
                Toast.makeText(getApplicationContext(), "trip uploaded.\ngo to your map to check it out!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(NewTripActivity.this, HomeActivity.class));
            }
        });

    }
}