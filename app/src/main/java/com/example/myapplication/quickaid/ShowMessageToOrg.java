package com.example.myapplication.quickaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ShowMessageToOrg extends AppCompatActivity {
        RecyclerView msgrecView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message_to_org);

        msgrecView = findViewById(R.id.msgRecView);



    }
}