package com.javatechnocrat.jtadmin.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javatechnocrat.jtadmin.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BatchAddFragment extends Fragment {


    private View root;
    private EditText dateText,nameText,timingText;
    private Button addButton;
    private String cnt;
    private DatabaseReference batchRef;
    private ProgressDialog loadingBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_batch_add, container, false);
        dateText=root.findViewById(R.id.batch_date);
        nameText=root.findViewById(R.id.batch_name);
        timingText=root.findViewById(R.id.batch_timing);
        addButton=root.findViewById(R.id.batch_add_button);
        batchRef= FirebaseDatabase.getInstance().getReference().child("upcoming_batches");
        loadingBar =new ProgressDialog(getActivity());
        loadingBar.setCancelable(true);
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.setTitle("Please Wait");
        loadingBar.setMessage("please wait while we are creating the batch..");
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c=Calendar.getInstance();
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDate(c);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.show();
                final String date = dateText.getText().toString();
                String name = nameText.getText().toString();
                String timing = timingText.getText().toString();
                if (date.length() == 0 || name.length() == 0 || timing.length() == 0) {
                    Toast.makeText(getActivity(), "You Must fill All the fields..", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                } else {
                    final Map m = new HashMap();
                    m.put("date", date);
                    m.put("name", name);
                    m.put("timing", timing);
                    batchRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            batchRef.removeEventListener(this);
                            cnt = "0";

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                cnt = d.getKey();
                            }
                            cnt = Integer.toString(Integer.parseInt(cnt) + 1);
                            batchRef.child(cnt).updateChildren(m).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "batch added successfully...", Toast.LENGTH_SHORT).show();
                                        dateText.setText("");
                                        nameText.setText("");
                                        timingText.setText("");
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

    private void updateDate(Calendar c) {
        String month=getMonthForInt(c.get(Calendar.MONTH));
        int date=c.get(Calendar.DAY_OF_MONTH);
        dateText.setText(month+" "+date);
    }
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

}
