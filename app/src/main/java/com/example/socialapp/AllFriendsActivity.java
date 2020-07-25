package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllFriendsActivity extends AppCompatActivity {

    private DatabaseReference friendReference;
    private DatabaseReference userInfoReference;
    private FirebaseAuth auth;
    private String currentUserId;

    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_friends);

        rv= findViewById(R.id.rv_allFriend);
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getEmail();
        String path = currentUserId.split("@")[0];

        rv.setLayoutManager(new LinearLayoutManager(this));

        userInfoReference = FirebaseDatabase.getInstance().getReference("user_info");

        friendReference = FirebaseDatabase.getInstance().getReference("friends").child(path);

        final List<FriendModel> list = new ArrayList<>();



        friendReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
//                Log.d("friend", "onDataChange: "+snapshot.getValue());
                for(DataSnapshot snapshot1: snapshot.getChildren()){
//                    Log.d("friend", "onDataChange: "+snapshot1.getValue());
                    userInfoReference.child((String) snapshot1.getValue())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserModel user = snapshot.getValue(UserModel.class);
                                    String name = user.getFname()+" "+user.getLname();
                                    list.add(new FriendModel(user.getProfile(), name,user.getEmail()));
                                    rv.setAdapter(new MyAdapter(list));
//                                    Log.d("friends", "user info "+user.getFname());
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AllFriendsActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.d("friends", "onCancelled: "+error.getMessage());
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllFriendsActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("friends", "onCancelled: "+error.getMessage());
            }
        });




    }


    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView iv;
        Button button;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.add_iv_profile);
            tv = itemView.findViewById(R.id.add_tv_name);
            button = itemView.findViewById(R.id.btn_remove);

        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        List<FriendModel> list;

        public MyAdapter(List<FriendModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_all_users_friend, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final FriendModel friendModel = list.get(position);
            holder.tv.setText(friendModel.getName());
            Glide.with(getBaseContext()).load(friendModel.getProfilePhoto()).into(holder.iv);

            Log.d("friend", "onBindViewHolder: "+friendModel.getProfilePhoto());
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String id = friendModel.getUserId().split("@")[0];
                    friendReference.child(id).removeValue();

                    Log.d("friend", "key: "+friendReference.child(id).getKey()+" child"+friendReference.child(id)+" child 2: "+friendReference.child(id));
                    Log.d("friend", "onClick: "+friendModel.getUserId()+" id: "+id);

                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class FriendModel{
        private String profilePhoto;
        private String name;
        private String userId;

        public FriendModel(String profilePhoto, String name, String userId) {
            this.profilePhoto = profilePhoto;
            this.name = name;
            this.userId = userId;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}