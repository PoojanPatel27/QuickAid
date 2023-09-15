package com.example.myapplication.quickaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserComplain extends AppCompatActivity {

    private EditText name,contact,problem, locationEt;
    private TextView autoLocation;
    private Spinner experties;
    private Button submit;
    DatabaseReference databaseReference,reference2,msgRef;
    FirebaseUser currentUser,currentuser2;


    FusedLocationProviderClient fusedLocationProviderClient;
    private  final static int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_complain);

        name = findViewById(R.id.nameComp);
        contact = findViewById(R.id.contactComp);
        problem = findViewById(R.id.ProblemDescComp);
        locationEt = findViewById(R.id.locationComp);
        experties = findViewById(R.id.spinnerComp);
        autoLocation = findViewById(R.id.autoLocationReg);
        submit = findViewById(R.id.submitBtnComp);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Organization");
        msgRef = FirebaseDatabase.getInstance().getReference();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentuser2 = FirebaseAuth.getInstance().getCurrentUser();




        if (currentUser!=null){
            String uid = currentUser.getUid();
            reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String nameEt = snapshot.child("name").getValue(String.class);
                        String contactEt = snapshot.child("number").getValue(String.class);

                        if (nameEt!=null){
                            name.setText(nameEt);
                        }
                        if (contactEt!=null){
                            contact.setText(contactEt);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<OrgDetailsModel> spinnerCategories = new ArrayList<>();

                for (DataSnapshot userSnapshot: snapshot.getChildren()){
                    OrgDetailsModel orgModel = userSnapshot.getValue(OrgDetailsModel.class);

                    if (orgModel!=null){
                        spinnerCategories.add(orgModel);
                    }
                }

                List<String> spinnerCategory = new ArrayList<>();
                for (OrgDetailsModel model : spinnerCategories){
                    String spinnerCat = model.getCategory();
                    if (spinnerCat!=null){
                        spinnerCategory.add(spinnerCat);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,spinnerCategory);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                experties.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textName = name.getText().toString();
                String textContact = contact.getText().toString();
                String textProblem = problem.getText().toString();
                String textLocation = locationEt.getText().toString();
                String textExperties = experties.getSelectedItem().toString();
                String textuid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                if (TextUtils.isEmpty(textName)){
                    Toast.makeText(UserComplain.this, "Enter name!!", Toast.LENGTH_SHORT).show();
                    name.setError("Name cannot be empty");
                    name.requestFocus();
                } else if (TextUtils.isEmpty(textContact)){
                    Toast.makeText(UserComplain.this, "Enter Contact!!", Toast.LENGTH_SHORT).show();
                    contact.requestFocus();
                    contact.setError("Contact cannot be empty");
                } else if (TextUtils.isEmpty(textProblem)){
                    Toast.makeText(UserComplain.this, "Enter Problem!!", Toast.LENGTH_SHORT).show();
                    problem.requestFocus();
                    problem.setError("Problem cannot be empty");
                } else if (TextUtils.isEmpty(textLocation)){
                    Toast.makeText(UserComplain.this, "Enter location!!", Toast.LENGTH_SHORT).show();
                    locationEt.requestFocus();
                    locationEt.setError("location cannot be empty");
                } else  if (TextUtils.isEmpty(textExperties)){
                    Toast.makeText(UserComplain.this, "Enter experties!!", Toast.LENGTH_SHORT).show();
                    experties.requestFocus();
                } else {
//                    registerUserComplain(textName,textContact,textProblem,textLocation,textExperties,uid);

                    sendMessageToOrganization(textuid,textExperties,textName,textContact,textLocation,textProblem);
                }

            }
        });

        autoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
    }

    private void sendMessageToOrganization(String textuid, String textExperties, String textName, String textContact, String textLocation, String textProblem) {
        Query orgRef =  msgRef.child("Organization").orderByChild("category").equalTo(textExperties);

        orgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orgSnapshot : snapshot.getChildren()){
                    String orguid = orgSnapshot.getKey();

                    DatabaseReference messageRef = msgRef.child("Organization").child(orguid).child("messages").push();
                    Map<String, Object> messageData = new HashMap<>();
                    messageData.put("userUid",textuid);
                    messageData.put("name",textName);
                    messageData.put("contact",textContact);
                    messageData.put("location",textLocation);
                    messageData.put("problem",textProblem);

                    messageRef.setValue(messageData);
                    name.setText("");
                    contact.setText("");
                    problem.setText("");
                    locationEt.setText("");


                    Toast.makeText(UserComplain.this, "Message sent to "+textExperties, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null){
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            if (addresses!=null){
                                Address address = addresses.get(0);
                                String addr = address.getAddressLine(0);
                                UserComplain.this.locationEt.setText(addr);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else
        {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(UserComplain.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Required Permission!!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void registerUserComplain(String textName, String textContact, String textProblem, String textLocation, String textExperties, String uid ) {
        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();

        UserComplainModel model = new UserComplainModel(textName,textContact,textProblem,textLocation,textExperties,uid);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints");

        reference.child(firebaseUser.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UserComplain.this, "Success!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(UserComplain.this, "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}