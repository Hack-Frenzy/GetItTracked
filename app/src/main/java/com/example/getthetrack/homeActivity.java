package com.example.getthetrack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Spinner spinner;
    private static final String[] paths = {"","Profile Settings", "Sign Out"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            Intent myIntent = new Intent(homeActivity.this, MainActivity.class);
            homeActivity.this.startActivity(myIntent);
            finish();
        }
        else {
            paths[0]=currentUser.getEmail();
        }
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(homeActivity.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setBackgroundColor(Color.rgb(255,255,255));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        // Whatever you want to happen when the first item gets selected
                        Intent myIntentprofile = new Intent(homeActivity.this, profile.class);
                        homeActivity.this.startActivity(myIntentprofile);

                        break;
                    case 2:
                        // Whatever you want to happen when the second item gets selected
                        mAuth.signOut();
                        Intent myIntenthome = new Intent(homeActivity.this, MainActivity.class);
                        homeActivity.this.startActivity(myIntenthome);
                        finish();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
