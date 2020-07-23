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

public class postcovid extends AppCompatActivity {
    EditText name, email, phNo, aadharNo, age, blood, pass;
    Spinner spinner;
    ImageView img;
    Button apply, uploadPic;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference refPathIt, hospi;
    Thread thread, machaxSpins;
    public String key;
    String url2;
    String request;
    private String uid;
    FirebaseUser user;
    String[] hospiname = new String[100];
    ArrayList<String> str = new ArrayList<String>();
    String emailId, password, phNoget, nameget, aadharNoget, ageget, bloodget;
    public static final int PICK_IMAGE_REQUEST = 1;
    StorageReference storageReference;
    private Uri filePath;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postcovid);
        name = findViewById(R.id.namepostcovid);
        email = findViewById(R.id.emailpostcovid);
        phNo = findViewById(R.id.phnopostcovid);
        aadharNo = findViewById(R.id.aadharpostcovid);
        age = findViewById(R.id.agepostcovid);
        blood = findViewById(R.id.bloodgrouppostcovid);
        spinner = findViewById(R.id.spinnerpostcovid);
        img = findViewById(R.id.reportpostcovid);
        apply = findViewById(R.id.applypostcovid);
        uploadPic = findViewById(R.id.uploadreportpost);
        pass = findViewById(R.id.passpost);
        mAuth = FirebaseAuth.getInstance();
        apply.setEnabled(false);
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                apply.setEnabled(true);
                return false;
            }
        });

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRefKey = database.getReference().child("postcovid");
        key = myRefKey.push().getKey();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!phNoget.equals("")) {
                    uploadImage();
                    DatabaseReference myRef = database.getReference().child("postcovid").child(user.getUid());
                    myRef.child("name").setValue(nameget);
                    myRef.child("phno").setValue(phNoget);
                    myRef.child("aadharno").setValue(aadharNoget);
                    myRef.child("age").setValue(ageget);
                    myRef.child("bloodGroup").setValue(bloodget);
                    String str = ((request).split(":"))[0].trim();
                    myRef.child("request").setValue(str);
                    myRef.child("rr").setValue(0);
                    myRef.child("spo2").setValue(0);
                    myRef.child("avpu").setValue(0);
                    myRef.child("bp").setValue(0);
                    myRef.child("mews").setValue(0);
                    myRef.child("uid").setValue(user.getUid().toString());
                    myRef.child("heartrate").setValue(0);
                    DatabaseReference myRefAccess = database.getReference().child("Access").child(user.getUid());
                    myRefAccess.child("hospital").setValue("postcovid");
                    myRefAccess.child("phno").setValue(Integer.valueOf(phNoget));
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(email.getText() == null || pass.getText() == null || phNo.getText() == null || name.getText() == null || aadharNo.getText() == null || age.getText() == null || blood.getText() == null))
                {
                    emailId = email.getText().toString();
                    password = email.getText().toString();
                    phNoget = phNo.getText().toString();
                    nameget = name.getText().toString();
                    aadharNoget = aadharNo.getText().toString();
                    ageget = age.getText().toString();
                    bloodget = blood.getText().toString();
                    if(!(emailId.equals("") || password.equals(""))){
                        apply.setEnabled(false);
                        mAuth.createUserWithEmailAndPassword(emailId, password)
                                .addOnCompleteListener(postcovid.this, new OnCompleteListener<AuthResult>() {
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
                                            Toast.makeText(postcovid.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        // ...
                                    }
                                });
                    }
                    else{
                        Toast.makeText(postcovid.this, "Email and password are mandatory",
                                Toast.LENGTH_SHORT).show();
                    }}
            }
        });
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        hospi = database.getReference().child("HospitalInfo");
        hospi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hospiinfoispos hos = snapshot.getValue(hospiinfoispos.class);
                    hospiname[i] = hos.name + ": (" + hos.address + ")";
                    str.add(hos.name +" (" + hos.address+" )");
                    i++;
                }
                machaxSpins.run();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        machaxSpins = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(postcovid.this,
                        android.R.layout.simple_spinner_item, str);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setBackgroundColor(Color.rgb(255,255,255));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        request = hospiname[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    void SelectImage() {

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

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
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


    @SuppressLint("ClickableViewAccessibility")
    private void uploadImage() {
        if (filePath != null) {
            apply.setEnabled(false);
            user = mAuth.getCurrentUser();
            uid = user.getUid();
            // Defining the child of storageReference
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://covid-monitoring-system.appspot.com");
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
                                                                                      refPathIt = database.getReference().child("postcovid").child(user.getUid());
                                                                                      refPathIt.child("url").setValue(url2);
                                                                                      Log.i("Tag", url2);
                                                                                  }
                                                                              }
                                    );
                                    // Dismiss dialog
                                    uid = user.getUid();

                                    Toast
                                            .makeText(getApplicationContext(),
                                                    "Sign Up Completed",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                    apply.setEnabled(true);

                                    email.setText("");
                                    pass.setText("");
                                    phNo.setText("");
                                    name.setText("");
                                    aadharNo.setText("");
                                    age.setText("");
                                    blood.setText("");
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
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                }
                            });
        }
    }
}
    class hospiinfoispos {
        public String name,email,address;
        long regno;
        public hospiinfoispos(String name, String email, long regno, String address) {
            this.name = name;
            this.email = email;
            this.regno = regno;
            this.address = address;
        }
        public hospiinfoispos() {
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
