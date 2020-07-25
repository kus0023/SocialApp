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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewFriendActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference reference;
    private List<UserModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        rv=findViewById(R.id.add_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userid = auth.getCurrentUser().getEmail().split("@")[0];
         reference = FirebaseDatabase.getInstance().getReference("user_info").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=getlist();
                MyAdapter adp=new MyAdapter(list);
                rv.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    static class MyviewHolder extends RecyclerView.ViewHolder{

        ImageView photo;
        TextView name;
        Button btn_add;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
             photo=itemView.findViewById(R.id.add_iv_profile);
             name=itemView.findViewById(R.id.add_tv_name);
             btn_add=itemView.findViewById(R.id.add_btn_add);
        }

    }

    class MyAdapter extends RecyclerView.Adapter<MyviewHolder>{
        private List<UserModel> list;
        MyAdapter(List<UserModel>list)
        {
            this.list=list;
        }

        @NonNull
        @Override
        public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v=getLayoutInflater().inflate(R.layout.item_add_friend,parent,false);
            return new MyviewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
            final UserModel userModel=list.get(position);

            holder.name.setText(userModel.getFname()+" "+userModel.getLname());
            Glide.with(getApplicationContext()).load(userModel.getProfile()).into(holder.photo);

            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(AddNewFriendActivity.this,UserProfilePageAcitvity.class);
                    intent.putExtra("Email",userModel.getEmail());
                    startActivity(intent);

                }
            });

            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String userid_auth = auth.getCurrentUser().getEmail().split("@")[0];
                    String userid=userModel.getEmail().split("@")[0];
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("friend_request").child(userid);
                    reference2.child(userid_auth).setValue(auth.getCurrentUser().getEmail());
                    Toast.makeText(AddNewFriendActivity.this, "friend request send...", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

   private List<UserModel> getlist(){
        final List<UserModel> list=new ArrayList<>();
       final FirebaseAuth auth = FirebaseAuth.getInstance();
       final String userid = auth.getCurrentUser().getEmail().split("@")[0];
       DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("user_info");
       reference1.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot s : snapshot.getChildren()) {
                   UserModel userModel = s.getValue(UserModel.class);
                   if(!userModel.getEmail().equals(auth.getCurrentUser().getEmail())) {
                       list.add(userModel);
                   }
               }
               MyAdapter myAdapter=new MyAdapter(list);
               rv.setAdapter(myAdapter);
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {
           }
       });
         return list;
    }
}