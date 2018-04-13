package com.javatechnocrat.jtadmin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.javatechnocrat.jtadmin.Fragments.BatchAddFragment;
import com.javatechnocrat.jtadmin.Fragments.BatchRemoveFragment;
import com.javatechnocrat.jtadmin.Fragments.EnquiriesFragment;
import com.javatechnocrat.jtadmin.Fragments.GalleryFragment;
import com.javatechnocrat.jtadmin.Fragments.NoticeAddFragment;
import com.javatechnocrat.jtadmin.Fragments.NoticeRemoveFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,new EnquiriesFragment(),"home");
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_enquiries);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getSupportFragmentManager().findFragmentById(R.id.main_container).getTag().equals("home"))
                new AlertDialog.Builder(this)
                        .setTitle("Exit")
                        .setMessage("Do You really want to Exit ?")
                        .setPositiveButton("Yes, Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.super.onBackPressed();
                            }
                        }).setNegativeButton("No, Don't",null).show();

            else{
                Fragment f=new EnquiriesFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, f,"home");
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_enquiries) {
            Toast.makeText(this, "Go to Enquiries", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment f = null;
        String tag = null;
        if (id == R.id.nav_enquiries) {
            f = new EnquiriesFragment();
            tag="home";
        } else if (id == R.id.nav_add_batch) {
            f = new BatchAddFragment();
            tag="other";
        } else if (id == R.id.nav_remove_batch) {
            f = new BatchRemoveFragment();
            tag="other";
        } else if (id == R.id.nav_add_notice) {
            f = new NoticeAddFragment();
            tag="other";
        } else if (id == R.id.nav_remove_notice) {
            f = new NoticeRemoveFragment();
            tag="other";
        } else if (id == R.id.nav_gallery) {
            f = new GalleryFragment();
            tag="other";
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, f,tag);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
