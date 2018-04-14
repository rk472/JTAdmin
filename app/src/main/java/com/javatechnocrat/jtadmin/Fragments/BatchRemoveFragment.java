package com.javatechnocrat.jtadmin.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javatechnocrat.jtadmin.R;

import java.util.ArrayList;
import java.util.List;

public class BatchRemoveFragment extends Fragment {

    private View root;
    private Spinner s;
    private DatabaseReference batchRef;
    private Button deleteButton;
    private List<String> key,data;
    private ValueEventListener v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_batch_remove, container, false);
        s=root.findViewById(R.id.batch_list);
        deleteButton=root.findViewById(R.id.batch_delete_button);
        batchRef= FirebaseDatabase.getInstance().getReference().child("upcoming_batches");
        v=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deleteButton.setEnabled(false);
                key=new ArrayList<>();
                data=new ArrayList<>();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    key.add(d.getKey());
                    String dat=d.child("date").getValue().toString()+d.child("name").getValue().toString()+"\n"+d.child("timing").getValue().toString();
                    data.add(dat);
                }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,data);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(arrayAdapter);
                deleteButton.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        batchRef.addValueEventListener(v);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cnt=s.getSelectedItemPosition();
                batchRef.child(key.get(cnt)).removeValue();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        batchRef.removeEventListener(v);
    }
}
