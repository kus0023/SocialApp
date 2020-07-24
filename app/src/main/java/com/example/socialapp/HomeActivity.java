package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FloatingActionButton addPost;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        addPost = findViewById(R.id.addpost);
        rv=findViewById(R.id.rview);



        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, AddUserPostActivity.class);
                startActivity(intent);
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userid = auth.getCurrentUser().getEmail().split("@")[0];
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_post").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post_model> list = getList();

                MyAdapter adp = new MyAdapter(list);

                rv.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit").setMessage("Are you sure you want to exit?");
        builder.setNeutralButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("Exit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView fname;
        TextView date;
        TextView time;
        ImageView photo;
        TextView caption;
        TextView like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fname=itemView.findViewById(R.id.firstname);
            date=itemView.findViewById(R.id.textView3);
            time=itemView.findViewById(R.id.textView8);
            photo=itemView.findViewById(R.id.postimage);
            caption=itemView.findViewById(R.id.postcaption);
            like=itemView.findViewById(R.id.like_tv);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        List<Post_model> list;

        public MyAdapter(List<Post_model> list) {
            this.list=list;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v=getLayoutInflater().inflate(R.layout.post_item,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post_model post_model=list.get(position);

            holder.date.setText(post_model.getDate());
            holder.time.setText(post_model.getTime());
            holder.caption.setText(post_model.getCaption());

            holder.fname.setText(post_model.getUserid());

            Glide.with(getApplicationContext())
                    .load(post_model.getImage())
                    .into(holder.photo);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    List<Post_model> getList(){
        final List<Post_model> list=new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userid = auth.getCurrentUser().getEmail().split("@")[0];
        Log.d("snapsot", "userid: "+userid);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_post").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapsot", "started");
                for(DataSnapshot s : snapshot.getChildren()){
                    Post_model p = s.getValue(Post_model.class);
                    Log.d("snapsot", " "+p);
                    list.add(p);
                }
                MyAdapter adp = new MyAdapter(list);

                rv.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "List is not updated due to: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }



}