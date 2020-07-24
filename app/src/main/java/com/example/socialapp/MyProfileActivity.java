package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        bshowpost=findViewById(R.id.btn_myprofile_viewpost);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String uid=auth.getCurrentUser().getEmail().split("@")[0];

        DatabaseReference dref=FirebaseDatabase.getInstance().getReference("user_info").child(uid);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel um=snapshot.getValue(UserModel.class);
                name.setText(um.getFname());
                email.setText(um.getEmail());
                phone.setText(um.getPhone());
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}