package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MainHomeActivity extends AppCompatActivity {
    private NavigationView nav_view;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle adt;
    private Toolbar toolbar;

    private RecyclerView rv;
    private DatabaseReference dref;
    private String list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        drawer=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);

        adt=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        adt.syncState();

        rv = findViewById(R.id.rview);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Post_model> list = getList();

        MyAdapter adp = new MyAdapter(list);

        rv.setAdapter(adp);
    }
     class MyViewHolder extends RecyclerView.ViewHolder{
         TextView fname;
         TextView lname;
         TextView date;
         TextView time;
         ImageView post_photo;
         TextView caption;
         ImageButton like;


         public MyViewHolder(@NonNull View itemView) {
             super(itemView);

                 fname=itemView.findViewById(R.id.firstname);
                 lname=itemView.findViewById(R.id.lastname);
                 date=itemView.findViewById(R.id.textView3);
                 time=itemView.findViewById(R.id.textView8);
                 post_photo=itemView.findViewById(R.id.postimage);
                 caption=itemView.findViewById(R.id.postcaption);
                 like=itemView.findViewById(R.id.likeib);
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
             MyViewHolder myViewHolder=new MyViewHolder(v);
             return myViewHolder;
         }

         @Override
         public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
             Post_model post_model=list.get(position);
             holder.date.setText(post_model.getDate());
             holder.time.setText(post_model.getTime());
//            holder.post_photo.setImageResource(post_model.getImage());
             holder.caption.setText(post_model.getCaption());
             holder.like.setBackground(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light));

         }

         @Override
         public int getItemCount() {
             return list.size();
         }
     }

     List<Post_model> getList(){
        List<Post_model> list=new ArrayList<>();
         list.add(new Post_model("Avantika","",100,"no caption","01/02/2020","1 min ago"));
         list.add(new Post_model("Avantika","",100,"no caption","01/02/2020","1 min ago"));
         list.add(new Post_model("Avantika","",100,"no caption","01/02/2020","1 min ago"));
         list.add(new Post_model("Avantika","",100,"no caption","01/02/2020","1 min ago"));
        return list;
     }

}