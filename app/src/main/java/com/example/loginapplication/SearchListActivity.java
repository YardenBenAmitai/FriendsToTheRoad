package com.example.loginapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    ArrayList<String[]> info_list;
    ListView listView;
    ImageButton back_home;
    //CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        info_list=new ArrayList<>();
        InfoListsGenerator();

        back_home= findViewById(R.id.back_b);
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchListActivity.this, HomeActivity.class));
            }
        });

        listView= findViewById(R.id.listView);
        CustomAdapter adapter= new CustomAdapter(SearchListActivity.this, R.layout.listview_layout);
        listView.setAdapter(adapter);

    }


    public void InfoListsGenerator(){

        final String kind= getIntent().getStringExtra("KIND");
        final String area= getIntent().getStringExtra("AREA");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Trips");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] items=new String[4];
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if ((area.compareTo(dsp.child("area").getValue().toString())==0) &&
                            (kind.compareTo(dsp.child("kind").getValue().toString())==0)){

                        items[0]= dsp.getKey();
                        items[1]=dsp.child("kind").getValue().toString()+" at "+dsp.child("area").getValue().toString();
                        items[2]=dsp.child("participants").getValue().toString();
                        items[3]=dsp.child("msg").getValue().toString();
                        info_list.add(items);
                        Toast.makeText(getApplicationContext(), "found match", Toast.LENGTH_SHORT).show();

                    }
                }



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        ref.child("TripsJoinedList").child(info_list.get(position)[0]).setValue("");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FirebaseDatabase.getInstance().getReference("Trips")
                                        .child(info_list.get(position)[0])
                                        .child("participants").child(dataSnapshot.child("username").getValue().toString()).setValue("");
                                Toast.makeText(getApplicationContext(), "the trip has been added to your map", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SearchListActivity.this, MyMapActivity.class));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


    class CustomAdapter extends ArrayAdapter<String> {
        //ArrayList<String[]> items=new ArrayList<>();
        Context context;

        CustomAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            //this.items.addAll(arr);
            this.context=context;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater= (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View item_trip= inflater.inflate(R.layout.listview_layout, parent, false);

            TextView desc_tv= convertView.findViewById(R.id.trip_desc);
            TextView tripid_tv= convertView.findViewById(R.id.trip_id);
            TextView users_tv= convertView.findViewById(R.id.users);
            TextView msg_tv= convertView.findViewById(R.id.msg);

            String[] item=info_list.get(position);
            desc_tv.setText(item[0]);
            tripid_tv.setText(item[1]);
            users_tv.setText(item[2]);
            msg_tv.setText(item[3]);

            return item_trip;
        }

    }
}
