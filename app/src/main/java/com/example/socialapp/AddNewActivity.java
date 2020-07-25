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

public class AddNewActivity extends AppCompatActivity {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        rv=findViewById(R.id.add_rv);

        rv.setLayoutManager(new LinearLayoutManager(this));

       // MyAdapter adp=new MyAdapter(list);
       // rv.setAdapter(adp);
    }

    static class MyviewHolder extends RecyclerView.ViewHolder{

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            ImageView photo=itemView.findViewById(R.id.add_iv_profile);
            TextView name=itemView.findViewById(R.id.add_tv_name);
            Button btn_add=itemView.findViewById(R.id.confirm_btn);
            Button btn_view=itemView.findViewById(R.id.remove_btn);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyviewHolder>{
        private List<UserModel> list;
        MyAdapter(List<UserModel>list){
            this.list=list;
        }

        @NonNull
        @Override
        public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

//    private List<UserModel> getlist(){
//       // return list;
//    }
}