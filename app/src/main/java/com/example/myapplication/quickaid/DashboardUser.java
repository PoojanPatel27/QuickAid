package com.example.myapplication.quickaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardUser extends AppCompatActivity {
    
    CardView card1,card5;
    FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);
        
        card1 = findViewById(R.id.card1);
        card5 = findViewById(R.id.card5);

        userAuth = FirebaseAuth.getInstance();
        
//        card1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DashboardUser.this, "Card 1", Toast.LENGTH_SHORT).show();
//            }
//        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userAuth.signOut();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}