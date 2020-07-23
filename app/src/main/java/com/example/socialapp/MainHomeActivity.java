package com.example.socialapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;


import com.google.android.material.navigation.NavigationView;

public class MainHomeActivity extends AppCompatActivity {
    private NavigationView nav_view;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle adt;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        drawer=findViewById(R.id.drawer);
//        toolbar=findViewById(R.id.tv);

        adt=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.Open,R.string.Close);
        adt.syncState();
    }
}