package com.example.getthetrack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class contact extends AppCompatActivity {
    EditText name, phno, email, aadhar, age, bldgrp, symptoms, pass;
    Spinner spinner;
    Button apply;
    DatabaseReference myRefpathit;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    String emailid,passget,phoneno,nameget,aadharget,ageget,bldgrpget,sympget;
    FirebaseUser user;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        final Thread thread;
        name = findViewById(R.id.namecontact);
        phno = findViewById(R.id.phnocontact);
        email = findViewById(R.id.emailcontact);
        aadhar = findViewById(R.id.aadharcontact);
        age = findViewById(R.id.agecontact);
        bldgrp = findViewById(R.id.bloodgroupcontact);
        symptoms = findViewById(R.id.symptomscontact);
        apply = findViewById(R.id.applycontact);
        spinner = findViewById(R.id.spinnercontact);
        pass = findViewById(R.id.passwordcontact);
        mAuth = FirebaseAuth.getInstance();
         emailid = email.getText().toString();
         passget = pass.getText().toString();
         phoneno = phno.getText().toString();
         nameget = name.getText().toString();
         aadharget = aadhar.getText().toString();
         ageget = age.getText().toString();
         bldgrpget = bldgrp.getText().toString();
         sympget = symptoms.getText().toString();
        apply.setEnabled(false);
        email.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                apply.setEnabled(true);
                return false;
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!phoneno.equals(""))
                {DatabaseReference myRef = database.getReference().child("contact").child(user.getUid());
                    myRef.child("name").setValue(nameget);
                    myRef.child("phno").setValue(phoneno);
                    myRef.child("aadharno").setValue(aadharget);
                    myRef.child("age").setValue(ageget);
                    myRef.child("bloodGroup").setValue(bldgrpget);
                    myRef.child("symptoms").setValue(sympget);
                }
            }
        });
        database = FirebaseDatabase.getInstance();
        if (emailid.equals("") || passget.equals("")) {
            mAuth.createUserWithEmailAndPassword(emailid, passget)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                 user = mAuth.getCurrentUser();
                                 thread.run();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(contact.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(contact.this, "Email and Password are mandatory to be filled.",
                    Toast.LENGTH_SHORT).show();
        }


    }
    }

