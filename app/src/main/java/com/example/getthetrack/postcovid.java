package com.example.getthetrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class postcovid extends AppCompatActivity {
    EditText name, email, phNo, aadharNo, age, blood, pass;
    Spinner spinner;
    ImageView img;
    Button apply, uploadPic;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference refPathIt;
    Thread thread;
    public String key;
    String url2;
    private String uid;
    FirebaseUser user;
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
        emailId = email.getText().toString();
        password = email.getText().toString();
        phNoget = phNo.getText().toString();
        nameget = name.getText().toString();
        aadharNoget = aadharNo.getText().toString();
        ageget = age.getText().toString();
        bloodget = blood.getText().toString();
        apply.setEnabled(false);
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                apply.setEnabled(true);
                return false;
            }
        });

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRefKey = database.getReference().child("postcovid").child(user.getUid());
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
                }
            }
        });

        if (emailId.equals("") || password.equals("")) {
            mAuth.createUserWithEmailAndPassword(emailId, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                user = mAuth.getCurrentUser();
                                thread.start();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(postcovid.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(postcovid.this, "Email and password are mandatory.",
                    Toast.LENGTH_SHORT).show();
        }
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
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
                                                                                      refPathIt = database.getReference().child("postcovid").child(user.getUid());
                                                                                      refPathIt.child("url").setValue(url2);
                                                                                      url2 = String.valueOf(uri);
                                                                                      Log.i("Tag", url2);
                                                                                  }
                                                                              }
                                    );
                                    // Dismiss dialog
                                    uid = user.getUid();

                                    Toast
                                            .makeText(getApplicationContext(),
                                                    "Image Selected!! NOW CLICK UPLOAD",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                    apply.setEnabled(true);
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
                                        UploadTask.TaskSnapshot taskSnapshot) {}
                            });
        }
    }
}
