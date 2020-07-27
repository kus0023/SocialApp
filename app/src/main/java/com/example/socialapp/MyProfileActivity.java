package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {

    private ImageView profile;
    private ImageView backprofile;
    private TextView name;
    private TextView email;
    private TextView phone;
    private TextView Dob;
    private Button bedit,bshowpost;
    private DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        profile=findViewById(R.id.img_myprofile_main);
        backprofile=findViewById(R.id.img_myprofile_back);
        name=findViewById(R.id.tv_myprofile_name);
        email=findViewById(R.id.tv_myprofile_email);
        phone=findViewById(R.id.tv_myprofile_mobile);
        bedit=findViewById(R.id.btn_myprofile_edit);
        Dob=findViewById(R.id.tv_myprofile_dob);
        bshowpost=findViewById(R.id.btn_myprofile_viewpost);

        bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyProfileActivity.this,EditUserInfoActivity.class);
                startActivity(i);
                finish();
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String uid=auth.getCurrentUser().getEmail().split("@")[0];

        DatabaseReference dref=FirebaseDatabase.getInstance().getReference("user_info").child(uid);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel um=snapshot.getValue(UserModel.class);
                if(um != null) {
                    name.setText(um.getFname() + " " + um.getLname());
                    email.setText(um.getEmail());
                    phone.setText(um.getPhone());
                    Dob.setText(um.getDob());
                    Glide.with(getApplicationContext())
                            .load(um.getProfile()).into(profile);
                    Glide.with(getApplicationContext())
                            .load(um.getBackground()).into(backprofile);
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}