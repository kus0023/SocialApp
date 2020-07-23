package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserpostActivity extends AppCompatActivity {

    private ListView lv;
    private DatabaseReference dref;
    private String list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpost);
        lv=findViewById(R.id.postlistview);
        /*dref= FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })
*/
        final List<Post_model> list=getList();

       // CursorAdapter adp=new CustomAdapter(list);

       // lv.setAdapter(adp);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(UserpostActivity.this, "post selected", Toast.LENGTH_SHORT).show();
            }
        });
    }


        class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v=getLayoutInflater().inflate(R.layout.post_item,viewGroup,false);
            ImageView iv=v.findViewById(R.id.postimage);
            TextView tv=v.findViewById(R.id.postcaption);
            ImageButton ib=v.findViewById(R.id.likeib);
            return v;
        }
    }

    private List<Post_model> getList(){
        List<Post_model> list=new ArrayList<>();
        list.add(new Post_model("","caption"));
        return list;
    }
}