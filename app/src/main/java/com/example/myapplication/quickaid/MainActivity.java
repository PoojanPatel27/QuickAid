package com.example.myapplication.quickaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button org,user;
    CardView carduser,organization;
    FirebaseAuth authUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        org = findViewById(R.id.orgBtn);
        user = findViewById(R.id.userBtn);
        carduser = findViewById(R.id.cardUser);
        organization = findViewById(R.id.cradOrganization);


        organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginOrg.class);
                startActivity(intent);
            }
        });

        carduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginUser.class);
                startActivity(intent);
            }
        });

    }
}