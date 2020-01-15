package com.example.loginapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyMapActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ImageButton back_home;
    List<String> listGroup;
    HashMap<String, List<String[]>> listItem;
    ExpAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);

        expandableListView= findViewById(R.id.lvExp);
        back_home= findViewById(R.id.back_b);

        listGroup= new ArrayList<>();
        listItem= new HashMap<>();
        initListData();

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyMapActivity.this, HomeActivity.class));
            }
        });

        adapter= new ExpAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);

    }

    private void initListData(){
        listGroup.add("Trips Guided By Me:");
        listGroup.add("Trips I Joined:");
        listGroup.add("History:");

        listItem.put(listGroup.get(0), new ArrayList<String[]>());
        listItem.put(listGroup.get(1), new ArrayList<String[]>());
        listItem.put(listGroup.get(2), new ArrayList<String[]>());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String username=dataSnapshot.child("username").getValue().toString();
                for (DataSnapshot dsp1 : dataSnapshot.child("TripsCreatedList").getChildren()) {
                    String key=dsp1.getKey();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Trips").child(key);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dsp2) {
                            String[] trip_info=new String[4];
                            trip_info[0]= dsp2.child("kind").getValue().toString()+" at "+dsp2.child("area").getValue().toString();
                            trip_info[1]= "guiding: "+username;
                            trip_info[2]= "participating: "+ dsp2.child("participants").getValue(). toString();
                            trip_info[3]= "additional text: "+dsp2.child("msg").getValue().toString();

                            int index=2;
                            if (dsp2.child("open").getValue().toString().compareTo("true")==0){
                                index=0;
                            }
                            listItem.get(listGroup.get(index)).add(trip_info);
                            adapter= new ExpAdapter(MyMapActivity.this, listGroup, listItem);
                            expandableListView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                    adapter= new ExpAdapter(MyMapActivity.this, listGroup, listItem);
                    expandableListView.setAdapter(adapter);
                }

                for (DataSnapshot dsp1 : dataSnapshot.child("TripsJoinedList").getChildren()) {
                    String key=dsp1.getKey();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Trips").child(key);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dsp2) {
                            String[] trip_info=new String[4];
                            trip_info[0]= dsp2.child("kind").getValue().toString()+" at "+dsp2.child("area").getValue().toString();
                            trip_info[1]=username;
                            trip_info[2]= dsp2.child("participants").getValue().toString();
                            trip_info[3]=dsp2.child("msg").getValue().toString();

                            int index=2;
                            if (dsp2.child("open").getValue().toString().compareTo("true")==0){
                                index=1;
                            }
                            listItem.get(listGroup.get(index)).add(trip_info);
                            adapter= new ExpAdapter(MyMapActivity.this, listGroup, listItem);
                            expandableListView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                    adapter= new ExpAdapter(MyMapActivity.this, listGroup, listItem);
                    expandableListView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


    private class ExpAdapter extends BaseExpandableListAdapter{
        List<String> groups;
        HashMap<String, List<String[]>> items;
        Context context;

        public ExpAdapter(Context context, List<String> g, HashMap<String, List<String[]>> i){
            this.context=context;
            this.groups=g;
            this.items=i;
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return items.get(groups.get(0)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public String[] getChild(int groupPosition, int childPosition) {
            if(!items.get(groups.get(groupPosition)).isEmpty()){
                return items.get(groups.get(groupPosition)).get(childPosition);
            }
            String[] err={"-1"};
            return err;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            String group= (String) getGroup(groupPosition);
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.list_group, null);
            }

            TextView group_title= convertView.findViewById(R.id.list_title);
            group_title.setText(group);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.list_group_item, null);
            }

            String[] key= getChild(groupPosition, childPosition);
            if(key[0].compareTo("-1") != 0){
                TextView trip_title= convertView.findViewById(R.id.trip_title);
                TextView trip_creator= convertView.findViewById(R.id.trip_creator);
                TextView trip_participants= convertView.findViewById(R.id.users);
                TextView trip_msg= convertView.findViewById(R.id.msg);
                trip_title.setText(key[0]);
                trip_creator.setText(key[1]);
                trip_participants.setText(key[2]);
                trip_msg.setText(key[3]);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
