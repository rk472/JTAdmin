package com.javatechnocrat.jtadmin.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javatechnocrat.jtadmin.R;

import java.util.ArrayList;
import java.util.List;

public class NoticeRemoveFragment extends Fragment {


    private View root;
    private Spinner s;
    private DatabaseReference noticeRef;
    private Button deleteButton;
    private List<String> key,title,body;
    private ValueEventListener v;
    private TextView descText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_notice_remove, container, false);
        s=root.findViewById(R.id.notice_list);
        deleteButton=root.findViewById(R.id.notice_delete_button);
        AppCompatActivity main=(AppCompatActivity)getActivity();
        main.getSupportActionBar().setTitle("Remove Notice");
        descText=root.findViewById(R.id.notice_description);
        noticeRef= FirebaseDatabase.getInstance().getReference().child("notice");
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                descText.setText(body.get(s.getSelectedItemPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        v=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deleteButton.setEnabled(false);
                key=new ArrayList<>();
                title=new ArrayList<>();
                body=new ArrayList<>();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    key.add(d.getKey());
                    title.add(d.child("title").getValue().toString());
                    body.add(d.child("description").getValue().toString());
                }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,title);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(arrayAdapter);
                deleteButton.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        noticeRef.addValueEventListener(v);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cnt=s.getSelectedItemPosition();
                noticeRef.child(key.get(cnt)).removeValue();
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        noticeRef.removeEventListener(v);
    }

}
