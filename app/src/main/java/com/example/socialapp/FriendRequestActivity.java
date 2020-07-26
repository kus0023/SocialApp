package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private DatabaseReference dbrf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        rv=findViewById(R.id.recyclerView3);
        rv.setLayoutManager(new LinearLayoutManager(this));
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String userid=auth.getCurrentUser().getEmail().split("@")[0];

        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("friend_requests");
        final DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("user_info").child(userid);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<UserModel> list=new ArrayList<>();
                for(DataSnapshot s:snapshot.getChildren()){
                    //reference.child(s)

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView fname;
        ImageView fimage;
        Button confirm_btn,remove_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fname=itemView.findViewById(R.id.add_tv_name);
            fimage=itemView.findViewById(R.id.add_iv_profile);
            confirm_btn=itemView.findViewById(R.id.add_btn_add);
            remove_btn=itemView.findViewById(R.id.add_btn_view);

        }
    }

    class Myadapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<UserModel> list;

        public Myadapter(List<UserModel> list) {

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
            UserModel um=list.get(position);
            holder.fname.setText(um.getFname() + " "+ um.getLname());
            Glide.with(getApplicationContext()).load(um.getProfile()).into(holder.fimage);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


}