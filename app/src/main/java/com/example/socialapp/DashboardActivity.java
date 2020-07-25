package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView nav_view;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView rv;
    private FloatingActionButton addPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawerLayout=findViewById(R.id.drower_layout);
        toolbar=findViewById(R.id.toolbar);
        nav_view=findViewById(R.id.nav_view);
        rv= findViewById(R.id.recyclerView);
        addPost = findViewById(R.id.addPost);


        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerToggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);

        View headerView = nav_view.getHeaderView(0);
        final TextView headerusername =  headerView.findViewById(R.id.username);
        final ImageView headerImgae = headerView.findViewById(R.id.userpic);

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashboardActivity.this, AddUserPostActivity.class);
                startActivity(intent);
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userid = auth.getCurrentUser().getEmail().split("@")[0];
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_info").child(userid);
        DatabaseReference postDBReference = FirebaseDatabase.getInstance().getReference("user_post").child(userid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel p = snapshot.getValue(UserModel.class);
                Log.d("valueHeader", "name = "+p.getFname()+" "+p.getLname());
                Log.d("valueHeader", "profile = "+p.getProfile());
                headerusername.setText(p.getFname()+" "+p.getLname());
                Glide.with(DashboardActivity.this).load(p.getProfile()).into(headerImgae);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postDBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post_model> posts = new ArrayList<>();
                for(DataSnapshot s : snapshot.getChildren()){
                    Post_model p = s.getValue(Post_model.class);
                    posts.add(p);
                }

                rv.setAdapter(new MyAdapter(posts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DashboardActivity.this, "something wrong", Toast.LENGTH_LONG).show();
            }
        });



    }

    private void signOut(){
        SharedPreferences sharedPreferences=getSharedPreferences("UserInformation",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor = sharedPreferences.edit();
        editor.putString("userId", "Guest");
        editor.putString("password", "Guest");
        editor.apply();

        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_profile:
                Intent i=new Intent(DashboardActivity.this,MyProfileActivity.class);
                startActivity(i);
                break;
            case R.id.posts_list:
                Intent j=new Intent(DashboardActivity.this,UserpostActivity.class);
                startActivity(j);
                break;
            case R.id.setting:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
               // Intent k=new Intent(DashboardActivity.this,MyProfileActivity.class);
               // startActivity(k);
                break;
            case R.id.logout:
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                signOut();
                break;
        }
        drawerLayout.closeDrawers();
        return false;
    }




    static class MyViewHolder extends RecyclerView.ViewHolder{
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



}