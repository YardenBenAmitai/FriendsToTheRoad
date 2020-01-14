package com.example.loginapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class AvatarPictureActivity extends AppCompatActivity {

    ImageView capture_image;
    ImageView camera_view;
    String file_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_picture);

        capture_image= findViewById(R.id.capture_b);
        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakerAction();
            }
        });

        camera_view= findViewById(R.id.image);
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data) {

        super.onActivityResult(request_code, result_code, data);
        if(result_code == RESULT_OK){
            if(request_code == 1){
                Bitmap bitmap= BitmapFactory.decodeFile(file_path);
                camera_view.setImageBitmap(bitmap);
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
                Uri photo_uri= FileProvider.getUriForFile(AvatarPictureActivity.this, "fssdfs", photo_file);
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
