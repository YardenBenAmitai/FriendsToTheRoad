package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView email;
    private TextView signin_role;
    private EditText name;
    private EditText about_me;
    private ImageView profile_picture;
    private Button update;
    private ImageView back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email= findViewById(R.id.email);
        signin_role= findViewById(R.id.role);
        name= findViewById(R.id.name);
        about_me= findViewById(R.id.about_me);
        profile_picture= findViewById(R.id.profile_picture);
        update= findViewById(R.id.update);
        back_home= findViewById(R.id.back_b);
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });
        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        ref.child("signin_role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                signin_role.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("about_me").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                about_me.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //profile_picture show

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("username").setValue(name.getText().toString());
                ref.child("about_me").setValue(about_me.getText().toString());
                //profile_picture
                validate();
            }
        });



    }

    protected void validate(){
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
    }
}
