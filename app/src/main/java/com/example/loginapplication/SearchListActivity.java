package com.example.loginapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchListActivity extends AppCompatActivity {

    private ArrayList<String> desc= new ArrayList<>();
    private ArrayList<String> tripID= new ArrayList<>();
    private ArrayList<String> users= new ArrayList<>();
    private ArrayList<String> msg= new ArrayList<>();
    private ListView listView;
    private ImageButton back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        ListsGenerator();
        back_home= findViewById(R.id.back_b);


        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchListActivity.this, HomeActivity.class));
            }
        });

        listView= findViewById(R.id.listView_result);
        listView.setAdapter(new CustomListView(this));

    }

    public void ListsGenerator(){
        final String kind= getIntent().getStringExtra("KIND");
        final String area= getIntent().getStringExtra("AREA");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Trips");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if ((area.compareTo(dsp.child("area").getValue().toString())==0) &&
                            (kind.compareTo(dsp.child("kind").getValue().toString())==0)){

                        tripID.add(dsp.getKey());
                        desc.add(dsp.child("kind").getValue().toString()+" at "+dsp.child("area").getValue().toString());
                        users.add(dsp.child("participants").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


    private class CustomListView extends BaseAdapter {

        Context context;

        private CustomListView(Activity context) {
            this.context=context;
        }

        @Override
        public int getCount() {
            return desc.size();
        }

        @Override
        public String[] getItem(int position) {
            String[] ans={desc.get(position), tripID.get(position), users.get(position), msg.get(position)};
            return ans;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder viewHolder=null;
            if (convertView==null){
                LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView= layoutInflater.inflate(R.layout.listview_layout, parent, false);
                viewHolder= new ViewHolder(convertView);
                convertView.setTag(viewHolder);


                viewHolder.b_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //saves the details of the trip to profile

                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FirebaseDatabase.getInstance().getReference("Trips")
                                        .child(tripID.get(position))
                                        .child("participants").child(dataSnapshot.child("username").getValue().toString()).setValue("");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(getApplicationContext(), "the trip has been added to your map", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SearchListActivity.this, MyMapActivity.class));
                    }
                });


            } else{
                viewHolder= (ViewHolder) convertView.getTag();

            }
            viewHolder.desc_tv.setText(desc.get(position));
            viewHolder.tripid_tv.setText(tripID.get(position));
            viewHolder.users_tv.setText(users.get(position));
            viewHolder.msg_tv.setText(msg.get(position));
            return convertView;
        }

    }

    public class ViewHolder {

        TextView desc_tv;
        TextView tripid_tv;
        TextView users_tv;
        TextView msg_tv;
        ImageButton b_join;

        public ViewHolder(View view){

            desc_tv= view.findViewById(R.id.trip_desc);
            tripid_tv= view.findViewById(R.id.trip_id);
            users_tv= view.findViewById(R.id.users);
            msg_tv= view.findViewById(R.id.msg);
            b_join= view.findViewById(R.id.b_join);
        }
    }


}
