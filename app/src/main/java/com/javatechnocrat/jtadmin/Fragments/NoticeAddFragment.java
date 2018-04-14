package com.javatechnocrat.jtadmin.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javatechnocrat.jtadmin.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NoticeAddFragment extends Fragment {

    private View root;
    private EditText titleText,bodyText;
    private Button addNotice;
    private DatabaseReference noticeRef;
    private String cnt;
    private ProgressDialog loadingBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root=inflater.inflate(R.layout.fragment_notice_add, container, false);
        noticeRef= FirebaseDatabase.getInstance().getReference().child("notice");
        titleText=root.findViewById(R.id.notice_title);
        bodyText=root.findViewById(R.id.notice_body);
        addNotice=root.findViewById(R.id.notice_add_button);
        loadingBar =new ProgressDialog(getActivity());
        loadingBar.setCancelable(true);
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.setTitle("Please Wait");
        loadingBar.setMessage("please wait while we are creating the notice..");
        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.show();
                String title=titleText.getText().toString();
                String body=bodyText.getText().toString();
                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(body)){
                    Toast.makeText(getActivity(), "You Must enter all the fields..", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }else{
                    Calendar c=Calendar.getInstance();
                    final Map m = new HashMap();
                    m.put("title", title);
                    m.put("description", body);
                    m.put("date", c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
                    noticeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            noticeRef.removeEventListener(this);
                            cnt = "0";

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                cnt = d.getKey();
                            }
                            cnt = Integer.toString(Integer.parseInt(cnt) + 1);
                            noticeRef.child(cnt).updateChildren(m).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "batch added successfully...", Toast.LENGTH_SHORT).show();
                                        titleText.setText("");
                                        bodyText.setText("");
                                    }
                                    else
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
        return root;
    }
}
