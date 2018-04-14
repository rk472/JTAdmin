package com.javatechnocrat.jtadmin.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javatechnocrat.jtadmin.EnquiryActivity;
import com.javatechnocrat.jtadmin.R;


public class EnquiriesFragment extends Fragment {


    private View root;
    private RecyclerView enquiryList;
    private DatabaseReference enquiryRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_inquiries, container, false);
        enquiryList=root.findViewById(R.id.enquiry_list);
        AppCompatActivity main=(AppCompatActivity)getActivity();
        main.getSupportActionBar().setTitle("Enquires");
        final ProgressDialog p=new ProgressDialog(getActivity());
        p.setTitle("Please Wait");
        p.setMessage("Loading Enquiries");
        p.setCanceledOnTouchOutside(false);
        p.setCancelable(false);
        p.show();
        enquiryList.setHasFixedSize(true);
        enquiryRef= FirebaseDatabase.getInstance().getReference().child("enquiry");
        FirebaseRecyclerAdapter<Enquiry,EnquiryViewHolder> f=new FirebaseRecyclerAdapter<Enquiry, EnquiryViewHolder>(
                Enquiry.class,
                R.layout.enquiry_row,
                EnquiryViewHolder.class,
                enquiryRef
        ) {
            @Override
            protected void populateViewHolder(EnquiryViewHolder viewHolder, Enquiry model, final int position) {
                p.dismiss();
                viewHolder.setName(model.getName());
                viewHolder.setCollege(model.getCollege());
                viewHolder.setPhone(model.getContact_no());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getActivity(), EnquiryActivity.class);
                        i.putExtra("id",getRef(position).getKey());
                        startActivity(i);
                    }
                });
            }
        };
        enquiryList.setAdapter(f);

        return root;
    }
    public static class EnquiryViewHolder extends RecyclerView.ViewHolder{
        TextView name,college,phone;
        View mView;
        public EnquiryViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.enquiry_name_row);
            college=itemView.findViewById(R.id.enquiry_college_row);
            phone=itemView.findViewById(R.id.enquiry_phone_row);
            mView=itemView;
        }
        public void setName(String s){
            name.setText(s);
        }
        public void setCollege(String s){college.setText(s);}
        public void setPhone(String s){phone.setText(s);}
    }


}
class Enquiry{
    String name,contact_no,college;

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Enquiry(String name, String contact_no, String college) {
        this.name = name;
        this.contact_no = contact_no;
        this.college = college;
    }

    public Enquiry() {

    }
}
