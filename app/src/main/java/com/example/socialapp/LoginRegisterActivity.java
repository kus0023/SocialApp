package com.example.socialapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginRegisterActivity extends AppCompatActivity {

    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        setTitle("ChatterBox");

        login = findViewById(R.id.btn_login_frg);
        register = findViewById(R.id.btn_reg_frg);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    login.setBackgroundColor(getColor(R.color.colorAccent));
                    login.setTextColor(getColor(R.color.white));
                    register.setBackgroundColor(getColor(R.color.white));
                    register.setTextColor(getColor(R.color.colorAccent));
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new LoginFragment())
                        .commit();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    register.setBackgroundColor(getColor(R.color.colorAccent));
                    register.setTextColor(getColor(R.color.white));
                    login.setBackgroundColor(getColor(R.color.white));
                    login.setTextColor(getColor(R.color.colorAccent));
                }


                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new RegisterFragment())
                        .commit();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


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
}

/*
HomeActivity -->
all post of other user who are friends

navigation--> left side
    add friend-->avantika
    account(profile)-->shweta
    settings--app*
    Logout
Floating button right bottom
    add new Post
Action bar
    search
    message
*/