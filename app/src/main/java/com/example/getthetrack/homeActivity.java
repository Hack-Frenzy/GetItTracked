package com.example.getthetrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeActivity extends AppCompatActivity {
    Button signout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        signout = findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            Intent myIntent = new Intent(homeActivity.this, MainActivity.class);
            homeActivity.this.startActivity(myIntent);
            finish();
        }
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mAuth.signOut();
            Intent myIntent = new Intent(homeActivity.this, MainActivity.class);
            homeActivity.this.startActivity(myIntent);
            finish();
            }
        });
    }
}
