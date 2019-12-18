package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {

    private Spinner tripSpinner;
    private Spinner areaSpinner;
    private EditText msg;
    private Button finished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        msg= findViewById(R.id.msg);
        finished= findViewById(R.id.b_start);
        tripSpinner = findViewById(R.id.tripkindspinner);
        areaSpinner= findViewById(R.id.tripareaspinner);



        ArrayAdapter<String> tripAdapter = new ArrayAdapter<>(UserActivity.this, android.R.layout.simple_list_item_1,
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


        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(UserActivity.this, android.R.layout.simple_list_item_1,
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

                NewTrip trip= new NewTrip(tripSpinner.getSelectedItem().toString(), areaSpinner.getSelectedItem().toString());
                if(! msg.getText().toString().isEmpty()){
                    trip.addMsg(msg.toString());
                }
                FirebaseDatabase.getInstance().getReference("NewTrips")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(trip);
                startActivity(new Intent(UserActivity.this, HomeActivity.class));
            }
        });

    }
}