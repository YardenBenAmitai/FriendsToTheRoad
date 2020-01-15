package com.example.loginapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchListActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<String[]> info_list;
    ListView listView;
    ImageButton back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        final String kind= getIntent().getStringExtra("KIND");
        final String area= getIntent().getStringExtra("AREA");

        databaseReference = FirebaseDatabase.getInstance().getReference("Trips");
        info_list=new ArrayList<>();
        info_list.add(new String[]{"test1", "test2","test3","test4"});
        back_home= findViewById(R.id.back_b);
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchListActivity.this, HomeActivity.class));
            }
        });

        listView= findViewById(R.id.listView);
        //adapter= new CustomAdapter(SearchListActivity.this, info_list);
        //listView.setAdapter(adapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if ((area.compareTo(dsp.child("area").getValue().toString())==0) &&
                            (kind.compareTo(dsp.child("kind").getValue().toString())==0)){
                        String[] items=new String[4];
                        items[1]= dsp.getKey();
                        items[0]=dsp.child("kind").getValue().toString()+" at "+dsp.child("area").getValue().toString();
                        items[2]=dsp.child("participants").getValue().toString();
                        items[3]=dsp.child("msg").getValue().toString();
                        info_list.add(items);
                        //System.out.println(info_list.get(info_list.size()-1)[0]);
                    }
                }
                CustomAdapter adapter= new CustomAdapter(SearchListActivity.this, info_list);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder= new AlertDialog.Builder(SearchListActivity.this);
                builder.setTitle("Information")
                        .setMessage("Are you sure you want to join this trip?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){

                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        ref.child("TripsJoinedList").child(info_list.get(position)[1]).setValue("");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FirebaseDatabase.getInstance().getReference("Trips")
                                        .child(info_list.get(position)[1])
                                        .child("participants").child(dataSnapshot.child("username").getValue().toString()).setValue("");
                                Toast.makeText(getApplicationContext(), "the trip has been added to your map", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SearchListActivity.this, MyMapActivity.class));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

    }



    class CustomAdapter extends BaseAdapter {
        ArrayList<String[]> items;
        Context context;

        CustomAdapter(@NonNull Context context, ArrayList<String[]> info) {
            this.items=info;
            this.context=context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.listview_layout, parent, false);
            }

            TextView desc_tv= convertView.findViewById(R.id.trip_desc);
            TextView tripid_tv= convertView.findViewById(R.id.trip_id);
            TextView users_tv= convertView.findViewById(R.id.users);
            TextView msg_tv= convertView.findViewById(R.id.msg);

            String[] item=items.get(position);
            desc_tv.setText(item[0]);
            tripid_tv.setText(item[1]);
            users_tv.setText(item[2]);
            msg_tv.setText(item[3]);

            return convertView;
        }

    }
}
