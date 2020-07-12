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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fillDetail extends AppCompatActivity {
    TextView head;
    EditText data;
    private FirebaseAuth mAuth;
    DatabaseReference myRefpathit;
    Button submit;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_detail);
        head = findViewById(R.id.headingData);
        data = findViewById(R.id.fillData);
        submit=findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        String[] message;
            message = bundle.getStringArray("message");
            //message[2] = hospitalname
        //message[1] = phone no
        //message[0] = rr or spo2
        //message[3] = previous value
            Log.i("tag",message[0].toString()+message[1]+message[2]+message[3]);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         String[] value = new String[2];
        String pathit;
         if(message[2].equals("positive") || message[2].equals("contact") || message[2].equals("postcovid"))
        {
            pathit=message[2]+"/"+mAuth.getCurrentUser().getUid()+"/"+message[0];
        }
         else
         {
              pathit="Patients/"+message[2]+"/"+message[1]+"/"+message[0];
         }
             Log.i("tag",pathit);
        myRefpathit = database.getReference(pathit);
        // Read from the database
        final String[] valuepathit = new String[1];
        head.setText(message[0]);
        data.setHint(message[3]);
        submit.setEnabled(false);
        data.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                submit.setEnabled(true);
                return false;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pressed",Toast.LENGTH_LONG).show();
                try {
                    int sett = Integer.parseInt(data.getText().toString());
                    Log.i("tag",String.valueOf(sett));
                    myRefpathit.setValue(sett);
                }
                catch (Exception e){
                    data.setError("A no. required");
                }

            }
        });
    }
}
