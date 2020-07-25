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

import java.util.List;

public class FriendRequestActivity extends AppCompatActivity {

    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        rv=findViewById(R.id.recyclerView3);
        rv.setLayoutManager(new LinearLayoutManager(this));


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
        private List<Request_model> list;

        public Myadapter(List<Request_model> list) {

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

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class Request_model{
        private String f_img;
        private String f_name;

        public Request_model(String f_img, String f_name) {
            this.f_img = f_img;
            this.f_name = f_name;
        }

        public String getF_img() {
            return f_img;
        }

        public void setF_img(String f_img) {
            this.f_img = f_img;
        }

        public String getF_name() {
            return f_name;
        }

        public void setF_name(String f_name) {
            this.f_name = f_name;
        }
    }
}