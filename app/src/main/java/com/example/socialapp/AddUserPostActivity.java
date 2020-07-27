package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddUserPostActivity extends AppCompatActivity {

    private Button post, choose;
    private TextView tv;
    private ImageView iv;
    Uri uri;
    FirebaseAuth auth ;
    String image = "";
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_post);

        post = findViewById(R.id.btn_post);
        choose = findViewById(R.id.btn_choose);
        tv = findViewById(R.id.et_multiline);
        iv = findViewById(R.id.img_post);
        iv.setVisibility(View.INVISIBLE);
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2000);
            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String caption = tv.getText().toString();

                if(!caption.equals("") && uri != null) {
                    pd.show();
                    pd.setCancelable(false);

                    final String userid = auth.getCurrentUser().getEmail().split("@")[0];

                    int like = 0;
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    String time = new SimpleDateFormat(("hh:mm")).format(new Date());

                    final String generatedName = new SimpleDateFormat("ddmmyyyyHHmmss").format(new Date());

                    if (uri != null) {
                        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("post/" + userid + "/" + generatedName + ".jpg");
                        storageReference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (task.isSuccessful()) {
                                    return storageReference.getDownloadUrl();
                                } else {
                                    throw task.getException();
                                }
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull final Task<Uri> task) {

                                image = task.getResult().toString();

                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_post").child(userid).child(generatedName);
                                reference.child("image").setValue(image);
                                pd.dismiss();
                                finish();
                            }
                        });
                    } else {
                        image = "";
                        pd.dismiss();
                    }

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_post").child(userid).child(generatedName);
                    Post_model model = new Post_model(userid, image, like, caption, date, time);
                    databaseReference.setValue(model);
                }
                else{
                    Toast.makeText(AddUserPostActivity.this, "No Caption or Image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2000 && resultCode == RESULT_OK){
            iv.setVisibility(View.VISIBLE);
            iv.setImageURI(data.getData());
            uri = data.getData();
        }
    }
}