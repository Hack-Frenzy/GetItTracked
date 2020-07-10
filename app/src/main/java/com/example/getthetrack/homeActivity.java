package com.example.getthetrack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

public class homeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Spinner spinner;
    DatabaseReference myRefpathit;
    allData Alldata;
    FirebaseDatabase database;
    ImageButton brr,bspo2,bhr,bavpu,bbp;
    passData Data ;
    int setter=0;
    Thread thread;
    CountDownLatch done = new CountDownLatch(1);
    TextView rr,spo2,hr,bp,avpu;
    private static final String[] paths = {"","Profile Settings", "Sign Out"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Data = new passData();
        rr = findViewById(R.id.rrData);
        spo2 = findViewById(R.id.spo2Data);
        hr = findViewById(R.id.heartrateData);
        bp = findViewById(R.id.bpData);
        avpu = findViewById(R.id.avpuData);
        brr = findViewById(R.id.searchrr);
        bbp =findViewById(R.id.searchbp);
        bspo2 = findViewById(R.id.searchspo2);
        bhr =findViewById(R.id.searchhr);
        bavpu = findViewById(R.id.searchAVPU);

        //Click listeners

        brr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, dialogue_fragment.class);
                intent.putExtra("message","rr");
                startActivity(intent);
            }
        });
        bavpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, dialogue_fragment.class);
                intent.putExtra("message","avpu");
                startActivity(intent);
            }
        });
        bbp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, dialogue_fragment.class);
                intent.putExtra("message","bp");
                startActivity(intent);
            }
        });
        bhr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, dialogue_fragment.class);
                intent.putExtra("message","pulse");
                startActivity(intent);
            }
        });
        bspo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, dialogue_fragment.class);
                intent.putExtra("message","spo2");
                startActivity(intent);
            }
        });
        rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, fillDetail.class);
                Data.message="rr";
                String[] str=new  String[4];
                str[0]=Data.message;
                str[1]=Data.phno;
                str[2]=Data.hospital;
                str[3] = String.valueOf(Alldata.rr);
                intent.putExtra("message",str);
                startActivity(intent);
            }
        });
        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, fillDetail.class);
                Data.message="bp";
                String[] str=new  String[4];
                str[0]=Data.message;
                str[1]=Data.phno;
                str[2]=Data.hospital;
                str[3] = String.valueOf(Alldata.bp);
                intent.putExtra("message",str);                startActivity(intent);
            }
        });
        avpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, fillDetail.class);
                Data.message="avpu";
                String[] str=new  String[4];
                str[0]=Data.message;
                str[1]=Data.phno;
                str[2]=Data.hospital;
                str[3] = String.valueOf(Alldata.avpu);
                intent.putExtra("message",str);                startActivity(intent);
            }
        });
        hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, fillDetail.class);
                Data.message="heartrate";
                String[] str=new  String[4];
                str[0]=Data.message;
                str[1]=Data.phno;
                str[2]=Data.hospital;
                str[3] = String.valueOf(Alldata.heartrate);
                intent.putExtra("message",str);
                startActivity(intent);
            }
        });
        spo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, fillDetail.class);
                Data.message="spo2";
                String[] str=new  String[4];
                str[0]=Data.message;
                str[1]=Data.phno;
                str[2]=Data.hospital;
                str[3] = String.valueOf(Alldata.spo2);
                intent.putExtra("message",str);
                startActivity(intent);
            }
        });
        rr.setEnabled(false);
        spo2.setEnabled(false);
        hr.setEnabled(false);
        avpu.setEnabled(false);
        bp.setEnabled(false);
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


        String uid=mAuth.getCurrentUser().getUid();

         database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // Read from the database
        final String[] valuefine = new String[2];
         thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String pathit="Patients/"+valuefine[1]+"/"+valuefine[0];
                Data.hospital=valuefine[1];
                Data.phno=valuefine[0];
                Log.i("Yup",valuefine[1]+valuefine[0]);
                DatabaseReference myRefpathit = database.getReference();
                myRefpathit.child(pathit).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                         Alldata = dataSnapshot.getValue(allData.class);
                        rr.setText(String.valueOf(Alldata.rr));
                        bp.setText(String.valueOf(Alldata.bp));
                        spo2.setText(String.valueOf(Alldata.spo2));
                        hr.setText(String.valueOf(Alldata.heartrate));
                        avpu.setText(String.valueOf((Alldata.avpu)));
                        rr.setEnabled(true);
                        spo2.setEnabled(true);
                        hr.setEnabled(true);
                        avpu.setEnabled(true);
                        bp.setEnabled(true);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
            }
        });
        myRef.child("Access/").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                duo Duo = dataSnapshot.getValue(duo.class);
                Log.i("gol",dataSnapshot.getValue().toString());
                valuefine[0] = String.valueOf(Duo.phno);
                valuefine[1]= Duo.hospital;
                Log.i("gol",valuefine[0]+valuefine[1]);
                setter=1;
                thread.run();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

}
class duo{
    long phno;
    String hospital;

    public duo() {
    }

    public duo(long phno, String hospital) {
        this.phno = phno;
        this.hospital = hospital;
    }


    public long getPhno() {
        return phno;
    }

    public String getHospital() {
        return hospital;
    }

    public void setPhno(long phno) {
        this.phno = phno;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
}
class allData{
   public long rr,spo2,heartrate,avpu,bp,aadharno,age,phoneNo,mews;
    public String bloodgrp,email,firstname,gender,lastname;
    public allData() {
    }

    public allData(long rr, long spo2, long heartrate, long avpu, long bp, long aadharno, long age, long phoneNo, String bloodgrp, String email, String firstname, String gender, String lastname, long mews) {
        this.rr = rr;
        this.spo2 = spo2;
        this.heartrate = heartrate;
        this.avpu = avpu;
        this.bp = bp;
        this.aadharno = aadharno;
        this.age = age;
        this.phoneNo = phoneNo;
        this.bloodgrp = bloodgrp;
        this.email = email;
        this.firstname = firstname;
        this.gender = gender;
        this.lastname = lastname;
        this.mews = mews;
    }
}
class passData{
    public String phno,message,hospital;
    public int current;
}