package com.javatechnocrat.jtadmin.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.javatechnocrat.jtadmin.AddImageActivity;
import com.javatechnocrat.jtadmin.MainActivity;
import com.javatechnocrat.jtadmin.R;
import com.squareup.picasso.Picasso;

public class GalleryFragment extends Fragment {

    private View root;
    private RecyclerView galleryList;
    private DatabaseReference galleryRef;
    private FloatingActionButton add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryList=root.findViewById(R.id.gallery_list);
        galleryRef= FirebaseDatabase.getInstance().getReference().child("gallery");
        add=root.findViewById(R.id.add_image);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),AddImageActivity.class);
                startActivity(i);
            }
        });
        FirebaseRecyclerAdapter<Gallery,GalleryViewHolder> f=new FirebaseRecyclerAdapter<Gallery, GalleryViewHolder>(
                Gallery.class,
                R.layout.gallery_row,
                GalleryViewHolder.class,
                galleryRef
        ) {
            @Override
            protected void populateViewHolder(final GalleryViewHolder viewHolder, final Gallery model, final int position) {
                viewHolder.setImage(model.getUrl(),getActivity());
                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Delete Picture")
                                .setMessage("Do You really want to Delete ?")
                                .setPositiveButton("Yes, Sure", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseStorage.getInstance().getReferenceFromUrl(model.getUrl()).delete();
                                        getRef(position).removeValue();
                                    }
                                }).setNegativeButton("No, Don't",null).show();
                    }
                });
            }
        };
        galleryList.setAdapter(f);
        galleryList.setHasFixedSize(true);
        galleryList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return root;
    }
    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        Button delete;
        public GalleryViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.gallery_image);
            delete=itemView.findViewById(R.id.gallery_delete);

        }
        public void setImage(String url,Context c){
            Picasso.with(c).load(url).placeholder(R.drawable.ic_file_download_black_24dp).into(image);
        }

    }
}
class Gallery{
    String url;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Gallery() {
    }
    public Gallery(String url) {
        this.url = url;
    }
}
