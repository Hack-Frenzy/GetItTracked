package com.example.getthetrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class contact extends AppCompatActivity {
    EditText name, phno, email, aadhar, age, bldgrp, symptoms, pass;
    Spinner spinner;
    Button apply;
    DatabaseReference myRefpathit;
    DatabaseReference hospi;
    FirebaseDatabase database;
    Thread spin;
    String request;

    private FirebaseAuth mAuth;
    String emailid,passget,phoneno,nameget,aadharget,ageget,bldgrpget,sympget;
    String[] hospitalname = new String[50];
    ArrayList<String> str=new ArrayList<String>();
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
                    myRef.child("rr").setValue(0);
                    myRef.child("spo2").setValue(0);
                    myRef.child("avpu").setValue(0);
                    myRef.child("bp").setValue(0);
                    myRef.child("mews").setValue(0);
                    myRef.child("uid").setValue(user.getUid().toString());
                    myRef.child("heartrate").setValue(0);
                    DatabaseReference myRefAccess = database.getReference().child("Access").child(user.getUid());
                    myRefAccess.child("hospital").setValue("contact");
                    myRefAccess.child("phno").setValue(Integer.valueOf(phoneno));
                    String str = ((request).split(":"))[0].trim();
                    myRef.child("request").setValue(str);
                    apply.setEnabled(true);
                    email.setText("");
                    pass.setText("");
                    phno.setText("");
                    name.setText("");
                    aadhar.setText("");
                    age.setText("");
                    bldgrp.setText("");
                    symptoms.setText("");
                }
            }
        });
        database = FirebaseDatabase.getInstance();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(email.getText()==null || pass.getText()==null || phno.getText()==null || name.getText()==null || aadhar.getText()==null || age.getText()==null || bldgrp.getText()==null))
                {
                    emailid = email.getText().toString();
                    passget = pass.getText().toString();
                    phoneno = phno.getText().toString();
                    nameget = name.getText().toString();
                    aadharget = aadhar.getText().toString();
                    ageget = age.getText().toString();
                    bldgrpget = bldgrp.getText().toString();
                    sympget = symptoms.getText().toString();
                    if(!(emailid.equals("") || passget.equals(""))){
                        apply.setEnabled(false);
                        mAuth.createUserWithEmailAndPassword(emailid, passget)
                                .addOnCompleteListener(contact.this, new OnCompleteListener<AuthResult>() {
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
                    }
                    else{
                        Toast.makeText(contact.this, "Email and password are mandatory",
                                Toast.LENGTH_SHORT).show();
                    }}
            }
        });
        hospi=database.getReference().child("HospitalInfo");
        hospi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hospiinfoiscon hos = snapshot.getValue(hospiinfoiscon.class);
                   hospitalname[i]=hos.name + ": (" + hos.address+ " )";
                    str.add(hos.name +" (" + hos.address+" )");
                   i++;
                } spin.run();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        spin = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(contact.this,
                        android.R.layout.simple_spinner_item,str);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setBackgroundColor(Color.rgb(255,255,255));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        request = hospitalname[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

    }
    }

class hospiinfoiscon {
    public String name,email,address;
    long  regno;
    public hospiinfoiscon(String name, String email, long  regno, String address) {
        this.name = name;
        this.email = email;
        this.regno = regno;
        this.address = address;
    }
    public hospiinfoiscon() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public long getRegno() {
        return regno;
    }
    public void setRegno(long regno) {
        this.regno = regno;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}