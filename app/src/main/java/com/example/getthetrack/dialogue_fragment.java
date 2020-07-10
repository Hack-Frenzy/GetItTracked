package com.example.getthetrack;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dialogue_fragment extends AppCompatActivity {
    FirebaseDatabase database ;
    DatabaseReference myRef;
    ListView listView;
    String[] tionData = new  String[6];
    String[] ansData = new  String[6];
    Thread machax;
    int i=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue_fragment);
        Bundle bundle = getIntent().getExtras();
        String message;
        //rr ya spo2 ys ..
        message = bundle.getString("message");
        database = FirebaseDatabase.getInstance();
        listView = findViewById(R.id.list);
        myRef = database.getReference().child("Data").child(message);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    anstion dataget = snapshot.getValue(anstion.class);
                    tionData[i]=dataget.qq;
                    ansData[i] = dataget.ans;
                    i++;
                }
                machax.run();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
       machax = new Thread(new Runnable() {
           @Override
           public void run() {
            adapter ouradapter = new adapter(getApplicationContext(),tionData,ansData);
            listView.setAdapter(ouradapter);
           }
       });
    }
}
class anstion{
    String ans, qq;

    public anstion() {
    }

    public anstion(String ans, String qq) {
        this.ans = ans;
        this.qq = qq;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
