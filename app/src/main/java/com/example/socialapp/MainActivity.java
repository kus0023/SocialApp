package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.IpSecManager;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    int counter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 4; // number of seconds to hold

        handler = new Handler();
        handler.postDelayed(runnable, 1000);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(counter >=4){
                Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                handler.postDelayed(runnable, 2000);
                counter++;
            }
        }
    };
}