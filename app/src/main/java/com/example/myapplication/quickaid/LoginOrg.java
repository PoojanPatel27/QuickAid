package com.example.myapplication.quickaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginOrg extends AppCompatActivity {

    TextView clickhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_org);

        clickhere = findViewById(R.id.clickHeretv);

        clickhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterOrg.class);
                startActivity(intent);
            }
        });
    }
}