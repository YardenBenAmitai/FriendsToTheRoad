package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity {

    private String role="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button profile= findViewById(R.id.b_profile);
        Button newTrip= findViewById(R.id.b_newTrip);
        Button map= findViewById(R.id.b_search);
        Button gallery= findViewById(R.id.b_gallery);
        ImageButton signout= findViewById(R.id.signout_b);

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("signin_role")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                role= dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        newTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.compareTo("Junior Traveler")==0) {
                    startActivity(new Intent(HomeActivity.this, SearchTripActivity.class));
                } else{
                    startActivity(new Intent(HomeActivity.this, NewTripActivity.class));
                }
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MyMapActivity.class));
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GalleryActivity.class));
            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, StartActivity.class));
            }
        });
    }
}
