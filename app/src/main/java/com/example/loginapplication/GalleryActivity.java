package com.example.loginapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapplication.JavaObjects.Cell;
import com.example.loginapplication.JavaObjects.MyRecycleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class GalleryActivity extends AppCompatActivity {

    List<Cell> image_list;
    FloatingActionButton take_picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        take_picture= findViewById(R.id.take_pic);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else{
            showImages();
        }

        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakerAction();
                showImages();
            }
        });
    }

    private void showImages(){
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Images/";
        image_list= new ArrayList<>();
        listFiles(path);

        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager= new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Cell> cells= prepareData();
        MyRecycleAdapter adapter= new MyRecycleAdapter(getApplicationContext(), cells);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Cell> prepareData(){
        ArrayList<Cell> all_images= new ArrayList<>();
        for(Cell c: image_list){
            Cell cell= new Cell();
            cell.setPath(c.getPath());
            cell.setTitle(c.getTitle());
            all_images.add(cell);
        }
        return all_images;
    }

    private void listFiles(String path){
        image_list= new ArrayList<>();
        File file= new File(path);
        File[] files= file.listFiles();
        if(files !=null){
            for (File f: files){
                Cell cell= new Cell();
                cell.setPath(f.getAbsolutePath());
                cell.setTitle(f.getName());
                image_list.add(cell);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results){
        if(request_code==1000){
            if(grant_results[0]==PackageManager.PERMISSION_GRANTED){
                showImages();
            } else{
                finish();
            }
        }
    }

    private void dispatchPictureTakerAction(){
        Intent take_pic= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(take_pic.resolveActivity(getPackageManager()) != null){
            File photo_file= null;
            photo_file= createPhotoFile();
            if(photo_file != null){
                Uri photo_uri= FileProvider.getUriForFile(GalleryActivity.this, "com.example.loginapplication.fileprovider", photo_file);
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
