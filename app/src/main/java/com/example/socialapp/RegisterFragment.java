package com.example.socialapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment {
    private final String TAG = "testregistration";

    public RegisterFragment() {
        // Required empty public constructor
    }

    private Button register;
    private EditText et_username;
    private EditText et_fname;
    private EditText et_lname;
    private EditText et_pass1;
    private EditText et_pass2;
    private EditText et_mob;
    private RadioGroup rb_gender;
    private EditText et_date;
    private ImageView iv_profile, iv_back;
    private  Uri path_profile, path_back;
    private final int PROFILE_PICK_CODE =100;
    private final int BACKGROUND_PICK_CODE =200;
    private List<String> list;
    private ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        et_date = view.findViewById(R.id.et_info_date);
        rb_gender = view.findViewById(R.id.rb_reg_gender);
        et_mob = view.findViewById(R.id.et_info_mobile);
        et_pass2 = view.findViewById(R.id.et_reg_pass2);
        et_pass1 = view.findViewById(R.id.et_reg_pass);
        et_username = view.findViewById(R.id.et_reg_email);
        et_fname = view.findViewById(R.id.et_info_fname);
        et_lname = view.findViewById(R.id.et_info_lname);
        register = view.findViewById(R.id.btn_reg_frg);
        iv_profile = view.findViewById(R.id.img_info_profile);
        iv_back = view.findViewById(R.id.img_reg_back);
        list = new ArrayList<>();

        final Context context = getContext();


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, BACKGROUND_PICK_CODE);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PROFILE_PICK_CODE);
            }
        });




        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(year, month, dayOfMonth);
                                et_date.setText(SimpleDateFormat.getDateInstance().format(calendar1.getTime()));
                            }
                        },year, month, day);

                pickerDialog.show();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String fname = et_fname.getText().toString();
                String lname = et_lname.getText().toString();
                String emailid = et_username.getText().toString();
                String pass1 = et_pass1.getText().toString();
                String pass2 = et_pass2.getText().toString();
                String mobile = et_mob.getText().toString();
                String date = et_date.getText().toString();

                int select_id = rb_gender.getCheckedRadioButtonId();
                RadioButton g_btn = (RadioButton)view.findViewById(select_id);
                String gender = g_btn.getText().toString();


                if(!(fname.equals("") && lname.equals("") && fname.equals("") && emailid.equals("") && mobile.equals("") && pass1.equals("") && date.equals(""))){
                    if(isUnique(emailid))
                    {

                        if(pass1.equals(pass2) && pass1.length() >=6){

                            progressDialog = new ProgressDialog(getContext());
                            progressDialog.setTitle("Registering You...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            String[] temp = emailid.split("@");
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_info").child(temp[0]);
                            String path_of_profile = getPhotoPathFromDb(path_profile, "profile");
                            String path_of_back = getPhotoPathFromDb(path_back, "background");

                            UserModel userModel = new UserModel(fname, lname, emailid, mobile, gender, date, path_of_profile, path_of_back);
                            databaseReference.setValue(userModel);

                            auth.createUserWithEmailAndPassword(emailid, pass1)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(context, "Uploading Your Files", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else{
                            Toast.makeText(context, "Password is not same Or too short", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(context, "This userId is not available Please try different email", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(context, "One or more Field(s) Are Empty", Toast.LENGTH_SHORT).show();
                }




            }
        });


        return view;
    }

    private String getPhotoPathFromDb(Uri uri, final String lastname) {
        if(uri==null){
            if(lastname.equals("profile")){
                uri = Uri.parse("android.resource://"+getActivity().getApplicationContext().getPackageName()+"/drawable/avatar");
            }
            else{
                uri = Uri.parse("android.resource://"+getActivity().getApplicationContext().getPackageName()+"/drawable/background");
            }

        }

        final StringBuilder sb = new StringBuilder();

        final String[] temp = et_username.getText().toString().split("@");

        final String name = new SimpleDateFormat("ddmmyyyyHHmmss").format(new Date()).concat("_"+lastname+".jpg");


        final StorageReference reference = FirebaseStorage.getInstance().getReference("profilePhoto/"+temp[0]+"/"+name);
        reference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(task.isSuccessful()){
                    return reference.getDownloadUrl();
                }
                else {
                    throw task.getException();
                }
            }
        })
        .addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(final @NonNull Task<Uri> task) {

                if(task.getResult()!=null) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_info").child(temp[0]);//user_info
                    databaseReference.child(lastname).setValue(task.getResult().toString());
                    progressDialog.dismiss();
//                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Map<String, Object> update = new HashMap<>();
//                            for(DataSnapshot s : snapshot.getChildren()){
//                                update.put(s.getKey(), s.getValue());
//                            }
//                            update.put(lastname, task.getResult().toString());
//                            databaseReference.updateChildren(update);
//
//
//                            progressDialog.dismiss();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                            progressDialog.dismiss();
//                        }
//                    });


                    Toast.makeText(getContext(), "Congratulation! You can login Now", Toast.LENGTH_LONG).show();
                    //back to login
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, new LoginFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    Toast.makeText(getContext(), "File Not Uploaded", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return sb.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case PROFILE_PICK_CODE:
                if(resultCode == RESULT_OK){
                    path_profile = data.getData();
                    iv_profile.setBackground(null);

                    iv_profile.setImageURI(data.getData());
                    iv_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                else {
                    Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                }

                break;

            case BACKGROUND_PICK_CODE:
                if(resultCode == RESULT_OK) {
                    path_back = data.getData();
                    iv_back.setBackground(null);
                    iv_back.setImageURI(path_back);
                    iv_back.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                else{
                    Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private boolean isUnique(String id){

        final String temp[] = id.split("@");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    list.add(s.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return !list.contains(temp[0]);
    }
}