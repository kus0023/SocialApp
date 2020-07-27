package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference reqReference;
    private DatabaseReference userInfoReference;
    private DatabaseReference friendReference;
    private List<RequestModel> list;
    private String currenntUserIdName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        setTitle("Friend Requests");

        rv=findViewById(R.id.recyclerView3);
        rv.setLayoutManager(new LinearLayoutManager(this));
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String userid=auth.getCurrentUser().getEmail().split("@")[0];
        currenntUserIdName = userid;

        reqReference = FirebaseDatabase.getInstance().getReference("friend_request").child(userid);
        userInfoReference = FirebaseDatabase.getInstance().getReference("user_info");
        friendReference = FirebaseDatabase.getInstance().getReference("friends");

        reqReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=new ArrayList<>();
                for(DataSnapshot s : snapshot.getChildren()){
                    userInfoReference.child(s.getValue().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           UserModel user=snapshot.getValue(UserModel.class);
                           String name=user.getFname()+" "+user.getLname();
                           String profile=user.getProfile();
                           list.add(new RequestModel(name, profile, user.getEmail()));
                           rv.setAdapter(new Myadapter(list));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }



    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView fname;
        ImageView fimage;
        Button confirm_btn,remove_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fname=itemView.findViewById(R.id.add_tv_name);
            fimage=itemView.findViewById(R.id.add_iv_profile);
            confirm_btn=itemView.findViewById(R.id.add_btn_confirm);
            remove_btn=itemView.findViewById(R.id.add_btn_remove);

        }
    }

    private class Myadapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<RequestModel> list;

        public Myadapter(List<RequestModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v=getLayoutInflater().inflate(R.layout.item_all_request_friend_list,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final RequestModel model=list.get(position);
            holder.fname.setText(model.getFname());
            Glide.with(getApplicationContext()).load(model.getFimage()).into(holder.fimage);

            holder.remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqReference.child(model.getEmail().split("@")[0]).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            list.remove(model);
                            rv.setAdapter(new Myadapter(list));
                            if(list.isEmpty()){
                                Toast.makeText(FriendRequestActivity.this, "No Friend Request Found", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            });

            holder.confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqReference.child(model.getEmail().split("@")[0]).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //adding friends
                                    friendReference.child(currenntUserIdName).child(model.getEmail().split("@")[0]).setValue(model.getEmail().split("@")[0]);
                                    friendReference.child(model.getEmail().split("@")[0]).child(currenntUserIdName).setValue(currenntUserIdName);
                                    Toast.makeText(FriendRequestActivity.this, "You are become friends now", Toast.LENGTH_SHORT).show();

                                    list.remove(model);
                                    rv.setAdapter(new Myadapter(list));
                                    if(list.isEmpty()){
                                        Toast.makeText(FriendRequestActivity.this, "No Friend Request Found", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class RequestModel{

        private String fname;
        private String fimage;
        private String email;

        public RequestModel(String fname, String fimage, String email) {
            this.email = email;
            this.fname = fname;
            this.fimage = fimage;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getFimage() {
            return fimage;
        }

        public void setFimage(String fimage) {
            this.fimage = fimage;
        }
    }
}