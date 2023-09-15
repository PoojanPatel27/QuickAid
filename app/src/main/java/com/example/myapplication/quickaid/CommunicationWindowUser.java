package com.example.myapplication.quickaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CommunicationWindowUser extends AppCompatActivity {


    ImageButton sendBtn;
    EditText messageEt;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_window);

        Intent intent = getIntent();
        String senderuid = intent.getStringExtra("uid");
        String orgId = "1";


        sendBtn = findViewById(R.id.btnSendCollab);
        messageEt = findViewById(R.id.messageEtCollab);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageEt.getText().toString();
                if (msg!=null){
                    sendMessage(senderuid,orgId,msg);
                } else {
                    Toast.makeText(CommunicationWindowUser.this, "Can't send empty message!", Toast.LENGTH_SHORT).show();
                }
                messageEt.setText("");
            }
        });


    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("collaboration chats").push().setValue(hashMap);

    }
}