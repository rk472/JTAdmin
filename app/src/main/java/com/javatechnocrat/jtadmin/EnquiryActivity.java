package com.javatechnocrat.jtadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EnquiryActivity extends AppCompatActivity {
    private DatabaseReference enquiryRef;
    private TextView place,name,proff,father,fatherProf,mother,motherProf,college,sem,trade,email,contact_no,alt_no,ref_friend,ref_marketing,sub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        place=findViewById(R.id.enquiry_place);
        name=findViewById(R.id.enquiry_name);
        proff=findViewById(R.id.enquiry_prof);
        father=findViewById(R.id.enquiry_father);
        fatherProf=findViewById(R.id.enquiry_father_prof);
        mother=findViewById(R.id.enquiry_mother);
        motherProf=findViewById(R.id.enquiry_mother_prof);
        college=findViewById(R.id.enquiry_college);
        sem=findViewById(R.id.enquiry_sem);
        trade=findViewById(R.id.enquiry_trade);
        email=findViewById(R.id.enquiry_email);
        contact_no=findViewById(R.id.enquiry_phone);
        alt_no=findViewById(R.id.enquiry_alt_no);
        ref_friend=findViewById(R.id.enquiry_friend_ref);
        ref_marketing=findViewById(R.id.enquiry_ref_marketing);
        sub=findViewById(R.id.enquiry_subjects);
        String key=getIntent().getExtras().getString("id");
        enquiryRef= FirebaseDatabase.getInstance().getReference().child("enquiry").child(key);
        enquiryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot d) {
                place.setText("Place : "+d.child("place").getValue().toString());
                name.setText("Name : "+d.child("name").getValue().toString());
                proff.setText("Proffession : "+d.child("profession").getValue().toString());
                father.setText("Father : "+d.child("father").getValue().toString());
                fatherProf.setText("Father Proffesion : "+d.child("father_prof").getValue().toString());
                mother.setText("Mother : "+d.child("mother").getValue().toString());
                motherProf.setText("Mother Proffession : "+d.child("mother_prof").getValue().toString());
                college.setText("College : "+d.child("college").getValue().toString());
                sem.setText("Semester : "+d.child("sem").getValue().toString());
                trade.setText("Trade : "+d.child("trade").getValue().toString());
                email.setText("Email : "+d.child("email").getValue().toString());
                contact_no.setText("Phone No : "+d.child("contact_no").getValue().toString());
                alt_no.setText("Alternative NO : "+d.child("alt_no").getValue().toString());
                ref_friend.setText("Friend Rerference : "+d.child("ref_friend").getValue().toString());
                ref_marketing.setText("Marketing Reference : "+d.child("ref_marketing").getValue().toString());
                sub.setText(getSubList(d));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String getSubList(DataSnapshot d) {
        String s="";
        if(d.child("c").getValue(Boolean.class))s+="C , ";
        if(d.child("c++").getValue(Boolean.class))s+="C ++ , ";
        if(d.child("core java").getValue(Boolean.class))s+="Core Java , ";
        if(d.child("adv java").getValue(Boolean.class))s+="Advance Java , ";
        if(d.child("oracle").getValue(Boolean.class))s+="Oracle , ";
        if(d.child("iot").getValue(Boolean.class))s+="IOT , ";
        if(d.child("php").getValue(Boolean.class))s+="PHP , ";
        if(d.child("python").getValue(Boolean.class))s+="Python , ";
        if(d.child("web technology").getValue(Boolean.class))s+="Web Technology , ";
        if(d.child("spring & hibernate").getValue(Boolean.class))s+="Spring & Hibernate , ";
        if(d.child("data structure").getValue(Boolean.class))s+="Data Structure , ";
        if(d.child("android").getValue(Boolean.class))s+="Android , ";
        if(d.child("dot_net").getValue(Boolean.class))s+=".NET , ";
        if(d.child("big datahadoop").getValue(Boolean.class))s+="Big Data Hardoop , ";
        if(d.child("spoken english").getValue(Boolean.class))s+="Spoken English , ";
        s=s.substring(0,s.length()-2);
        return s;
    }
}
