package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.IpSecManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView logo1;
    private ImageView logo2;
    private ImageView logoname;
    Animation topAnim,bottomAnim;

    Handler handler;
    int counter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo1=findViewById(R.id.imageView7);
        logo2=findViewById(R.id.imageView8);
        logoname=findViewById(R.id.imageView9);

        //set animation
        logo1.setAnimation(topAnim);
        logo2.setAnimation(topAnim);
        logoname.setAnimation(bottomAnim);




        counter = 2; // number of seconds to hold

        handler = new Handler();
        handler.postDelayed(runnable, 0);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(counter<0){
                Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                counter--;
                handler.postDelayed(runnable, 1000);
            }
        }
    };
}