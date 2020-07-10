package com.example.getthetrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {
    Button positive, contact, postcovid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        positive = findViewById(R.id.positive);
        contact = findViewById(R.id.contact);
        postcovid = findViewById(R.id.postcovid);
        postcovid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, postcovid.class);
                startActivity(intent);
                finish();
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, contact.class);
                startActivity(intent);
                finish();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, positive.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
