package com.example.myapplication.quickaid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class OrgDetailActivity extends AppCompatActivity {

    private TextView name,experties,contact,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_detail);

        name = findViewById(R.id.orgNameDtl);
        experties = findViewById(R.id.orgContactTv);
        contact = findViewById(R.id.orgContactTv);
        address = findViewById(R.id.orgAddressTV);


    }
}