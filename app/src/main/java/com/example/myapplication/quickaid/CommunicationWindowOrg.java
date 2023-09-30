package com.example.myapplication.quickaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommunicationWindowOrg extends AppCompatActivity {

    TextView nameTv;
    ImageView back;
    ImageButton send;
    EditText msgEt;
    String uid;

    RecyclerView recyclerView;

    Query reference;
    DatabaseReference ref2;
    FirebaseAuth userAuth;
    FirebaseUser currentUser;

    ChatAdapter chatAdapter;
    List<ChatModel> chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_window_org);

        nameTv = findViewById(R.id.orgNameTitleComOrg);
        back = findViewById(R.id.backImgBtnComOrg);
        send = findViewById(R.id.btnSendCollabOrg);
        msgEt = findViewById(R.id.messageEtCollabOrg);

        recyclerView = findViewById(R.id.chatRecViewOrg);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");


        ref2 = FirebaseDatabase.getInstance().getReference("Users");
        userAuth = FirebaseAuth.getInstance();
        currentUser = userAuth.getCurrentUser();
        String sender = currentUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Organization");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readMessage(currentUser.getUid(),uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ref2.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()){
                    uid = userSnapshot.getKey();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (name!=null){
            nameTv.setText(name);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgEt.getText().toString();
                if (TextUtils.isEmpty(msg)){
                    Toast.makeText(CommunicationWindowOrg.this, "cannot send empty message!!!", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(sender,uid,msg);
                    msgEt.setText("");
                }
            }
        });



    }

    private void sendMessage(String sender, String uid, String msg) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",uid);
        hashMap.put("message",msg);

        reference.child("collaboration chats").push().setValue(hashMap);

    }
    private void readMessage(String myid, String userid){
        DatabaseReference reference;


        chatModel = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("collaboration chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                chatModel.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()){

                    ChatModel chat = snapshot.getValue(ChatModel.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        chatModel.add(chat);

                    }

                   chatAdapter = new ChatAdapter(getApplicationContext(),chatModel);
                    recyclerView.setAdapter(chatAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}