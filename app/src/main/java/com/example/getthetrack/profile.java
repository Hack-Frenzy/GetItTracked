package com.example.getthetrack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class profile extends AppCompatActivity {

    TextView name, age, email, type, aadhar,phoneno, bldgrp;
    Thread thread;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        phoneno = findViewById(R.id.phone2);

        bldgrp = findViewById(R.id.bloodgrp2);
        name =findViewById(R.id.name2);
        age =findViewById(R.id.age2);

        email =findViewById(R.id.email2);
        type =findViewById(R.id.type2);
        aadhar =findViewById(R.id.aadhar2);

        Bundle bundle = getIntent().getExtras();
        String[] message;
        message = bundle.getStringArray("message");


        name.setText(message[0]);
        age.setText(message[1]);
        phoneno.setText(message[2]);
        aadhar.setText(message[3]);
        bldgrp.setText(message[4]);
        type.setText(message[5]);
        email.setText(user.getEmail());
    }
}
