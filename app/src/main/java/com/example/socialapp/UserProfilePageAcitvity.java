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

import java.util.List;

public class UserProfilePageAcitvity extends AppCompatActivity {

    private ImageView profile,back_profile;
    private TextView name,date,phone,email,gender;
    private Button add;
    private DatabaseReference dref;
    private List<UserModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page_acitvity);
        setTitle("Information");

        name=findViewById(R.id.uname);
        date=findViewById(R.id.udob);
        phone=findViewById(R.id.umobnumber);
        email=findViewById(R.id.uemail);
        gender=findViewById(R.id.ugender);
        profile=findViewById(R.id.img_info_profile);
        back_profile=findViewById(R.id.img_info_back);
        add=findViewById(R.id.badd);


        FirebaseAuth auth=FirebaseAuth.getInstance();
        Intent intent=getIntent();
         final String userid=intent.getStringExtra("Email").split("@")[0];
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("user_info").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel um = snapshot.getValue(UserModel.class);
                name.setText(um.getFname()+" "+um.getLname());
                date.setText(um.getDob());
                phone.setText(um.getPhone());
                email.setText(um.getEmail());
                gender.setText(um.getGender());
                Glide.with(getApplicationContext()).load(um.getProfile()).into(profile);
                Glide.with(getApplicationContext()).load(um.getBackground()).into(back_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userid_auth = auth.getCurrentUser().getEmail().split("@")[0];
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("friend_request").child(userid);
                reference1.child(userid_auth).setValue(userid_auth);
                Toast.makeText(UserProfilePageAcitvity.this, "friend Request send...", Toast.LENGTH_SHORT).show();
                add.setText("added");

            }
        });
    }
}