package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
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

public class EditUserInfoActivity extends AppCompatActivity {

    private Button edit_details, del_acc;
    private EditText et_fname;
    private EditText et_lname;
    private EditText et_mob;
    private EditText et_date;
    private ImageView iv_profile, iv_back;
    private Uri path_profile, path_back;
    private final int PROFILE_PICK_CODE = 100;
    private final int BACKGROUND_PICK_CODE = 200;
    private List<String> list;
    private DatabaseReference dref;
    String fname, lname, date, phone_no;

    private String currentUser;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        et_date = findViewById(R.id.et_info_date);
        et_mob = findViewById(R.id.et_info_mobile);
        et_fname = findViewById(R.id.et_info_fname);
        et_lname = findViewById(R.id.et_info_lname);
        edit_details = findViewById(R.id.btn_info_edit);
        del_acc = findViewById(R.id.btn_info_del);
        iv_profile = findViewById(R.id.img_info_profile);
        iv_back = findViewById(R.id.img_info_back);
        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser().getEmail();
        String uid = auth.getCurrentUser().getEmail().split("@")[0];

        dref = FirebaseDatabase.getInstance().getReference("user_info").child(uid);


        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel um = snapshot.getValue(UserModel.class);

                fname = um.getFname();
                lname = um.getLname();
                date = um.getDob();
                phone_no = um.getPhone();
                Toast.makeText(EditUserInfoActivity.this, "detail" + fname + lname + date, Toast.LENGTH_LONG).show();
                et_fname.setText(fname);
                et_lname.setText(lname);
                et_date.setText(date);
                et_mob.setText(phone_no);
                Glide.with(getApplicationContext()).load(um.getProfile()).into(iv_profile);
                Glide.with(getApplicationContext()).load(um.getBackground()).into(iv_back);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        iv_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, BACKGROUND_PICK_CODE);
//            }
//        });
//
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, BACKGROUND_PICK_CODE);
//            }
//        });

        edit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isfnamechange() || islnamechange() || isdatechange() || ismobchange() )
                {
                    Toast.makeText(EditUserInfoActivity.this, "you clicked"+"update: "+et_mob.getText().toString()+" "+phone_no+"/n"+et_date.getText().toString()+" "+date+
                            "/n"+et_fname.getText().toString()+fname, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditUserInfoActivity.this, "no update found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        del_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInfoActivity.this);
                builder.setTitle("Delete Account").setMessage("Are You sure you want to delete?")
                        .setCancelable(false)
                        .setNeutralButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("yes! delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            deleteUser();
                                            SharedPreferences sp = getSharedPreferences("UserInformation", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("userId", "Guest");
                                            editor.putString("password", "Guest");
                                            editor.apply();
                                            Intent i = new Intent(EditUserInfoActivity.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditUserInfoActivity.this, "failed : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).show();
            }
        });

        

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void deleteUser(){
        String id = currentUser.split("@")[0];
        FirebaseDatabase.getInstance().getReference("user_info").child(id).removeValue();
        FirebaseDatabase.getInstance().getReference("user_post").child(id).removeValue();

        FirebaseStorage.getInstance().getReference("post/"+id).delete();
        FirebaseStorage.getInstance().getReference("profilePhoto/"+id).delete()
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    deleteFriends();

            }
        });


    }

    private void deleteFriends(){
        final String id = currentUser.split("@")[0];
        final DatabaseReference friendReference = FirebaseDatabase.getInstance().getReference("friends");
        friendReference.child(id).removeValue();
        friendReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    for(DataSnapshot s1: s.getChildren()){
                        if(s1.getValue().toString().equals(id)){
                            s1.getRef().removeValue();
//                            Log.d("values", "onDataChange: "+s1.getRef());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private boolean islnamechange() {
        if(!(lname.equals(et_lname.getText().toString()))){
            dref.child("lname").setValue(et_lname.getText().toString());
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isfnamechange() {
        if(!(fname.equals(et_fname.getText().toString()))){
           dref.child("fname").setValue(et_fname.getText().toString());
           return true;
         }
         else{
             return false;
         }
    }

    private boolean ismobchange() {
        if(!(phone_no.equals(et_mob.getText().toString() ))){
            dref.child("phone").setValue(et_mob.getText().toString());
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isdatechange() {
        if(!date.equals(et_date.getText().toString())){
            dref.child("dob").setValue(et_date.getText().toString());
            return true;
        }
        else{
            return false;
        }
    }

}

