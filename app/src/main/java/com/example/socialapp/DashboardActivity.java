package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView nav_view;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton addpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawerLayout=findViewById(R.id.drower_layout);
        toolbar=findViewById(R.id.toolbar);
        nav_view=findViewById(R.id.nav_view);
        addpost=findViewById(R.id.addpost);


        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerToggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashboardActivity.this, AddUserPostActivity.class);
                startActivity(intent);
            }
        });


    }

    private void signOut(){
        SharedPreferences sharedPreferences=getSharedPreferences("UserInformation",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor = sharedPreferences.edit();
        editor.putString("userId", "Guest");
        editor.putString("password", "Guest");
        editor.apply();

        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(DashboardActivity.this,MyProfileActivity.class);
                startActivity(i);
                break;
            case R.id.posts_list:
                Toast.makeText(this, "post_list", Toast.LENGTH_SHORT).show();
                Intent j=new Intent(DashboardActivity.this,UserpostActivity.class);
                startActivity(j);
                break;
            case R.id.setting:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
               // Intent k=new Intent(DashboardActivity.this,MyProfileActivity.class);
               // startActivity(k);
                break;
            case R.id.logout:
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                signOut();
                break;
        }
        drawerLayout.closeDrawers();
        return false;
    }
}