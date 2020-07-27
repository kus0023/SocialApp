package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewFriendActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference databaseReference;
    private List<UserModel> list;

    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        setTitle("Add Friends");


        rv=findViewById(R.id.add_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userid = auth.getCurrentUser().getEmail().split("@")[0];
        currentUser =  userid;
        databaseReference = FirebaseDatabase.getInstance().getReference("user_info").child(currentUser);

        //this code is not working...=========================================================
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list = new ArrayList<>();
//                List<String> users = new ArrayList<>();
//                users.add(currentUser);
//
//                for(DataSnapshot table: snapshot.getChildren()) {
//                    if (table.getKey() != null && table.getKey().equals("user_info"))
//                        for (DataSnapshot userinfo : table.getChildren())
//                            if (userinfo.getKey() != null && !users.contains(userinfo.getKey())) {
//                                Log.d("values", "key: " + userinfo.getKey() + " values: " + userinfo.getValue());
//                                list.add(userinfo.getValue(UserModel.class));
//                            }
//                }
//
//                for(DataSnapshot table : snapshot.getChildren()){
//                    if(table.getKey() != null && table.getKey().equals("friends"))
//                        for(DataSnapshot value: table.getChildren())
//                            if(value.getKey().equals(currentUser))
//                                for(DataSnapshot f : value.getChildren())
//                                    list.remove(f.getValue());
//                }
//
//                for(DataSnapshot table : snapshot.getChildren()){
//                    if(table.getKey() != null && table.getKey().equals("friend_request"))
//                        for(DataSnapshot value: table.getChildren())
//                            if(!value.getKey().equals(currentUser))
//                                for(DataSnapshot f : value.getChildren())
//                                    list.remove(f.getValue());
//                }
//
////                    if(table.getKey()!= null && table.getKey().equals("friends")) //friends table
////                        for(DataSnapshot friend: table.getChildren()){
////                            if(friend.getKey()!= null && friend.getKey().equals( currentUser ))
////                                for(DataSnapshot value : friend.getChildren()) {
////                                    users.add(value.getValue().toString());
////                        Log.d("values", "friends :  key: "+value.getKey()+" values: "+value.getValue());
////                                }
////                        }
////
////
////
////                    if(table.getKey()!= null && table.getKey().equals("friend_request"))
////                        for(DataSnapshot req : table.getChildren())
////                            if(req.getKey()!=null && req.getKey().equals(currentUser))
////                                for(DataSnapshot value: req.getChildren()) {
////                                    users.add(value.getValue().toString());
////                                    Log.d("values", ""+value.getValue());
////                                }
////
////
////
////                }
//
//
//                rv.setAdapter(new MyAdapter(list));
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        //===================================================================================
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = getlist();
                rv.setAdapter(new MyAdapter(list));
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
             btn_add=itemView.findViewById(R.id.add_btn_confirm);
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
        public void onBindViewHolder(@NonNull final MyviewHolder holder, final int position) {
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
                    reference2.child(userid_auth).setValue(userid_auth);
                    Toast.makeText(AddNewFriendActivity.this, "friend request sent...", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

   private List<UserModel> getlist(){
        final List<UserModel> list1=new ArrayList<>();
       final FirebaseAuth auth = FirebaseAuth.getInstance();
       final String userid = auth.getCurrentUser().getEmail().split("@")[0];
       final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("user_info");
       final DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("friend_request");
       final DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("friends");


       reference1.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot s : snapshot.getChildren()) {
                   final UserModel userModel = s.getValue(UserModel.class);
                   if(userModel!=null) {
                       if (!userModel.getEmail().equals(auth.getCurrentUser().getEmail())) {
                           list1.add(userModel);
                           reference2.child(userid).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   for (DataSnapshot s1 : snapshot.getChildren()) {
                                       Log.d("shweta", "equels: " + s1.getValue().toString().equals(userModel.getEmail().split("@")[0]));
                                       if (s1.getValue().toString().equals(userModel.getEmail().split("@")[0])) {
                                           Log.d("shweta", "onDataChange: " + s1.getValue());
                                           list1.remove(userModel);
                                           rv.setAdapter(new MyAdapter(list1));
                                       }
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {
                               }
                           });

                           reference3.child(userid).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   for (DataSnapshot s1 : snapshot.getChildren()) {
                                       if (s1.getValue().toString().equals(userModel.getEmail().split("@")[0])) {
                                           list1.remove(userModel);
                                           rv.setAdapter(new MyAdapter(list1));
                                       }
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });
                       }
                   }
               }
             MyAdapter myAdapter=new MyAdapter(list1);
               rv.setAdapter(myAdapter);
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {
           }
       });
         return list1;
    }
}