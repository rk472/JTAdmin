package com.javatechnocrat.jtadmin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
                viewHolder.setName(model.getName());
                viewHolder.name.setOnClickListener(new View.OnClickListener() {
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
        TextView name;
        public EnquiryViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.enquiry_name_row);
        }
        public void setName(String s){
            name.setText(s);
        }
    }


}
class Enquiry{
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Enquiry(String name) {

        this.name = name;
    }

    public Enquiry() {

    }
}
