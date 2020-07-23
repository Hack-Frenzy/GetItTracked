package com.example.getthetrack;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.ArrayList;

public class positive extends AppCompatActivity {
    EditText name,email,phno,aadharno,age,blood,pass;
    Spinner spinner;
    ImageView img;
    FirebaseDatabase database;
    Thread thread,machaxspin;
    Button apply,uploadpic;
    DatabaseReference refpathit,hospi;
    private FirebaseAuth mAuth;
    String bloodgrp;
    String ageget;
    String aadharget;
    String nameget;
    String phnoget;
    String passget;
    String emailid;
    String url2;
    String reuest, reuestname;
    String[] hospiname = new  String[100];
    ArrayList<String> str=new ArrayList<String>();
    public String key;
    private String uid;
    StorageReference storageReference;
    FirebaseUser user;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.positive);
        name = findViewById(R.id.namepositive);
        phno = findViewById(R.id.phnopositive);
        email = findViewById(R.id.emailpositive);
        aadharno = findViewById(R.id.aadharpositive);
        age = findViewById(R.id.agepositive);
        blood = findViewById(R.id.bloodgrouppositive);
        spinner = findViewById(R.id.spinnerpositive);
        img = findViewById(R.id.reportpositive);
        apply = findViewById(R.id.applypositive);
        pass =findViewById(R.id.passpositive);
        uploadpic = findViewById(R.id.uploadimagepositive);
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

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRefkey = database.getReference().child("positive");
        key=myRefkey.push().getKey();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!phnoget.equals(""))
                {
                    DatabaseReference myRef = database.getReference().child("positive").child(user.getUid());
                    myRef.child("name").setValue(nameget);
                    myRef.child("phno").setValue(phnoget);
                    myRef.child("aadharno").setValue(aadharget);
                    myRef.child("age").setValue(ageget);
                    myRef.child("bloodGroup").setValue(bloodgrp);
                    myRef.child("request").setValue(reuestname);
                    myRef.child("rr").setValue(0);
                    myRef.child("spo2").setValue(0);
                    myRef.child("avpu").setValue(0);
                    myRef.child("bp").setValue(0);
                    myRef.child("uid").setValue(user.getUid().toString());
                    myRef.child("mews").setValue(0);
                    myRef.child("heartrate").setValue(0);
                    DatabaseReference myRefAccess = database.getReference().child("Access").child(user.getUid());
                    myRefAccess.child("hospital").setValue("positive");
                    myRefAccess.child("phno").setValue(Integer.valueOf(phnoget));
                    uploadImage();
                }
            }
        });

        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        // ****************************************//

        hospi = database.getReference().child("HospitalInfo");
        hospi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int i=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hospiinfois hos = snapshot.getValue(hospiinfois.class);
                    hospiname[i]=hos.name +": (" + hos.address+" )";
                    str.add(hos.name +" (" + hos.address+" )");
                    i++;
                }
                machaxspin.run();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        machaxspin = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(positive.this,
                        android.R.layout.simple_spinner_item,str);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setBackgroundColor(Color.rgb(255,255,255));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        reuest = hospiname[position];
                        reuestname = ((hospiname[position]).split(":"))[0].trim();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!(email.getText()==null || pass.getText()==null || phno.getText()==null || name.getText()==null || aadharno.getText()==null || age.getText()==null || blood.getText()==null))
                {
                    emailid = email.getText().toString();
                passget = pass.getText().toString();
                phnoget = phno.getText().toString();
                nameget = name.getText().toString();
                aadharget = aadharno.getText().toString();
                ageget = age.getText().toString();
                bloodgrp = blood.getText().toString();
                if(!(emailid.equals("") || passget.equals(""))){
                    apply.setEnabled(false);
                    mAuth.createUserWithEmailAndPassword(emailid, passget)
                            .addOnCompleteListener(positive.this, new OnCompleteListener<AuthResult>() {
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
                                        Toast.makeText(positive.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    // ...
                                }
                            });
                }
                else{
                    Toast.makeText(positive.this, "Email and password are mandatory",
                            Toast.LENGTH_SHORT).show();
                }}
            }
        });
    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();


            // Setting image on image view using Bitmap
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                this.getContentResolver(),
                                filePath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            apply.setEnabled(false);
            user = mAuth.getCurrentUser();
            uid = user.getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://covid-monitoring-system.appspot.com");
            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(uid).child(key);
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                  @Override
                                                                                  public void onSuccess(Uri uri) {
                                                                                      //Do what you want with the url
                                                                                      url2 = String.valueOf(uri);
                                                                                      refpathit = database.getReference().child("positive").child(user.getUid());
                                                                                      refpathit.child("url").setValue(url2);
                                                                                      Log.i("Tag", url2);
                                                                                  }
                                                                              }
                                    );
                                    // Dismiss dialog
                                    uid = user.getUid();
                                    Toast
                                            .makeText(getApplicationContext(),
                                                    "Sign Up Completed Successfully",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                    apply.setEnabled(true);

                                    email.setText("");
                                    pass.setText("");
                                    phno.setText("");
                                    name.setText("");
                                    aadharno.setText("");
                                    age.setText("");
                                    blood.setText("");
                                    Intent intent = new Intent(positive.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            Toast
                                    .makeText(getApplicationContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                            apply.setEnabled(true);
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                }
                            });
        }

    }
}
class hospiinfois {
   public String name,email,address;
    long  regno;
    public hospiinfois(String name, String email, long  regno, String address) {
        this.name = name;
        this.email = email;
        this.regno = regno;
        this.address = address;
    }

    public hospiinfois() {
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