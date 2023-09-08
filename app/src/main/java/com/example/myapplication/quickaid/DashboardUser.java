package com.example.myapplication.quickaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardUser extends AppCompatActivity {
    
    CardView card1,card5,card4;
    TextView nametv;
    FirebaseAuth userAuth;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);
        
        card1 = findViewById(R.id.card1);
        card5 = findViewById(R.id.card5);
        card4 = findViewById(R.id.card4);
        nametv = findViewById(R.id.userNameTv);
        reference = FirebaseDatabase.getInstance().getReference("User");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    OrgDetailsUploadModel model = snapshot.getValue(OrgDetailsUploadModel.class);

                    if (model != null){
                        String name = model.getName();
                        nametv.setText(name);
                    }
                } else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userAuth = FirebaseAuth.getInstance();

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NgoList.class);
                startActivity(intent);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userAuth.signOut();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        nametv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = userAuth.getUid();
                Toast.makeText(DashboardUser.this, uid, Toast.LENGTH_SHORT).show();
            }
        });
    }
}