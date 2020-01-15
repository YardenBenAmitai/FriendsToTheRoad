package com.example.loginapplication;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class ProfileActivity extends AppCompatActivity {

    TextView email;
    TextView signin_role;
    EditText name;
    EditText about_me;
    ImageView capture_image;
    String file_path;

    Button update;
   ImageView back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email= findViewById(R.id.email);
        signin_role= findViewById(R.id.role);
        name= findViewById(R.id.name);
        about_me= findViewById(R.id.about_me);
        capture_image= findViewById(R.id.profile_picture);
        update= findViewById(R.id.update);
        back_home= findViewById(R.id.back_b);

        if(Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });

        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakerAction();
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

        ref.child("avatar_file_path").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                file_path= dataSnapshot.getValue(String.class);
                if(file_path.compareTo("")!=0){
                    Bitmap bitmap= BitmapFactory.decodeFile(file_path);
                    capture_image.setImageBitmap(bitmap);
                }
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
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data) {

        super.onActivityResult(request_code, result_code, data);
        if(result_code == RESULT_OK){
            if(request_code == 1){
                Bitmap bitmap= BitmapFactory.decodeFile(file_path);
                capture_image.setImageBitmap(bitmap);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("avatar_file_path").setValue(file_path);
            }
        }
    }

    private void dispatchPictureTakerAction(){
        Intent take_pic= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(take_pic.resolveActivity(getPackageManager()) != null){
            File photo_file= null;
            photo_file= createPhotoFile();
            if(photo_file != null){
                file_path= photo_file.getAbsolutePath();
                Uri photo_uri= FileProvider.getUriForFile(ProfileActivity.this, "com.example.loginapplication.fileprovider", photo_file);
                take_pic.putExtra(MediaStore.EXTRA_OUTPUT, photo_uri);
                startActivityForResult(take_pic, 1);
            }
        }
    }
    private File createPhotoFile(){
        String name= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storage= getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image= null;
        try {
            image= File.createTempFile(name, ".jpg", storage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }
}
