package com.example.loginapplication.JavaObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapplication.R;

import java.io.File;
import java.util.ArrayList;

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder> {

    ArrayList<Cell> gallery_list;
    Context context;

    public MyRecycleAdapter(Context context, ArrayList<Cell> gallery_list){
        this.gallery_list= gallery_list;
        this.context=context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell, viewGroup, false);
        return new MyRecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i){
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageFromPath(gallery_list.get(i).getPath(), viewHolder.img);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enlarge
            }
        });
    }

    @Override
    public int getItemCount() {
        return gallery_list.size();
    }

    private void setImageFromPath(String path, ImageView image){
        File img_file= new File(path);
        if(img_file.exists()){
            Bitmap bitmap= BitmapFactory.decodeFile(img_file.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.image);
            title= itemView.findViewById(R.id.title);
        }
    }
}
