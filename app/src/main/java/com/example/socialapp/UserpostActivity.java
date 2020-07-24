package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserpostActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DatabaseReference dref;
    private String list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpost);
        rv = findViewById(R.id.recyclerView2);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Post_model> list = getList();
        MyAdapter adp = new MyAdapter(list);

        rv.setAdapter(adp);

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
        private List<Post_model> list;

        MyAdapter(List<Post_model> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v=getLayoutInflater().inflate(R.layout.post_item,parent,false);
            MyViewHolder myViewHolder=new MyViewHolder(v);
            return myViewHolder;
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


    private  List<Post_model> getList(){
        final List<Post_model> list=new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userid = auth.getCurrentUser().getEmail().split("@")[0];
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_post").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post_model p = snapshot.getValue(Post_model.class);
                list.add(p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserpostActivity.this, "List is not updated due to: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        return list;
    }



}
