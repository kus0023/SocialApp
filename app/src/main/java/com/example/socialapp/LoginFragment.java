package com.example.socialapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginFragment extends Fragment {



    public LoginFragment() {
        // Required empty public constructor
    }



    private EditText et1, et2;
    private Button login;
    private FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        et1 = view.findViewById(R.id.et_username);
        et2 = view.findViewById(R.id.et_password);
        login = view.findViewById(R.id.btn_login_frg);
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(getContext());

        pd.setTitle("Loading");
        pd.setCancelable(false);

        sharedPreferences = getActivity().getSharedPreferences("UserInformation", Context.MODE_PRIVATE);


        String userIdInfo = sharedPreferences.getString("userId", "Guest");
        String userPassInfo = sharedPreferences.getString("password", "Guest");

        if(!(userIdInfo.equals("Guest") && userPassInfo.equals("Guest"))){
            pd.show();
            signIn(userIdInfo, userPassInfo);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = et1.getText().toString();
                String pass = et2.getText().toString();

                if(userId.equals("") || pass.equals("")){
                    Toast.makeText(getContext(), "Empty fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    pd.show();
                    signIn(userId,pass);
                }
            }
        });
        return view;
    }



    private void signIn(final String userId, final String pass){
        auth.signInWithEmailAndPassword(userId, pass)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        editor = sharedPreferences.edit();
                        editor.putString("userId", userId);
                        editor.putString("password", pass);
                        editor.apply();

                        Toast.makeText(getContext(), "Welcome", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), DashboardActivity.class);
                        startActivity(intent);
                        pd.dismiss();
                        Objects.requireNonNull(getActivity()).finish();
                    }
                });
    }
}