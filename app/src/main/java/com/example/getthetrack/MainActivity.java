package com.example.getthetrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button login;
    Button signup;
    EditText email,pass;
    TextView additional;
    private FirebaseAuth mAuth;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.loginButton);
        login.setEnabled(false);
        signup = findViewById(R.id.signupbut);
        login.setBackgroundColor(Color.rgb(255,255,255));
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        additional = findViewById(R.id.additionalInfo);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent myIntent = new Intent(MainActivity.this, homeActivity.class);
            MainActivity.this.startActivity(myIntent);
            finish();
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signup.class);
                Log.i("iiop","iio");
                startActivity(intent);
                Log.i("iio","iio");
            }
        });
        email.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                login.setEnabled(true);
                login.setBackgroundColor(Color.rgb(28,130,211));
                return false;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("") ){
                    email.setError("This Field is required");
                }
                else if(pass.getText().toString().equals("")){
                    pass.setError("This Field is required");
                }
                else{
                Signin();
                login.setEnabled(false);
                    login.setBackgroundColor(Color.rgb(255,255,255));
                }
            }
        });

    }
    void Signin(){
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent myIntent = new Intent(MainActivity.this, homeActivity.class);
                            MainActivity.this.startActivity(myIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            additional.setText("NOTE : If you are logging in for the first time use your phone number as password. \nIf still not able to login Check if You Are Registered.");
                            login.setEnabled(true);
                            login.setBackgroundColor(Color.rgb(28,130,211));
                            pass.setText("");
                        }
                        // ...
                    }
                });
    }
}