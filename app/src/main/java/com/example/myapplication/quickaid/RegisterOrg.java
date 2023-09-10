package com.example.myapplication.quickaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterOrg extends AppCompatActivity {

    private EditText name,number,category,email,passsword,address,pincode;
    private Button register;
//    FirebaseAuth authUser;
    private ProgressBar progressBar;
    String uid;
    FirebaseAuth auth;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_org);

        name = findViewById(R.id.ngoNameEtReg);
        number = findViewById(R.id.ngomobileNoEtReg);
        category = findViewById(R.id.ngocategoryEtReg);
        email = findViewById(R.id.ngoemailEtReg);
        passsword = findViewById(R.id.ngopasswordEtReg);
        address = findViewById(R.id.ngoaddressEtReg);
        pincode = findViewById(R.id.orgAreaPinReg);
        register = findViewById(R.id.orgRegBtn);
        progressBar = findViewById(R.id.orgProgbarReg);

        auth = FirebaseAuth.getInstance();
        String fUser = auth.getUid();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textName = name.getText().toString();
                String textnumber = number.getText().toString();
                String textcategory = category.getText().toString();
                String textemail = email.getText().toString();
                String textpassword = passsword.getText().toString();
                String textAddress = address.getText().toString();
                String textPincode = pincode.getText().toString();
                String textuid = fUser;
                
                if (TextUtils.isEmpty(textName)){
                    Toast.makeText(RegisterOrg.this, "Enter name of organization!!", Toast.LENGTH_SHORT).show();
                    name.setError("Name cannot be empty");
                    name.requestFocus();
                } else if(TextUtils.isEmpty(textnumber)){
                    Toast.makeText(RegisterOrg.this, "Enter Contact Number!!", Toast.LENGTH_SHORT).show();
                    number.setError("mobile number must required!!");
                    number.requestFocus();
                } else if (textnumber.length() != 10){
                    Toast.makeText(RegisterOrg.this, "Enter 10 digit mobile number!!", Toast.LENGTH_SHORT).show();
                    number.setError("Enter valid mobile number");
                    number.requestFocus();
                } else if (TextUtils.isEmpty(textcategory)){
                    Toast.makeText(RegisterOrg.this, "Enter category of organization!!", Toast.LENGTH_SHORT).show();
                    category.setError("Category must required!!");
                    category.requestFocus();
                } else  if (TextUtils.isEmpty(textemail)){
                    Toast.makeText(RegisterOrg.this, "Enter email!!", Toast.LENGTH_SHORT).show();
                    email.setError("Email must required!!");
                    email.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()){
                    Toast.makeText(RegisterOrg.this, "Re-enter email!!", Toast.LENGTH_SHORT).show();
                    email.setError("Enter valid email!!");
                    email.requestFocus();
                }
                else if(TextUtils.isEmpty(textpassword)){
                    Toast.makeText(RegisterOrg.this, "Re-Enter Password!!", Toast.LENGTH_SHORT).show();
                    passsword.setError("Password cannot be empty!!");
                    passsword.requestFocus();
                }
                else if (textpassword.length() < 6){
                    Toast.makeText(RegisterOrg.this, "Re-enter password!", Toast.LENGTH_SHORT).show();
                    passsword.setError("Password must caontain 6 character!!");
                    passsword.requestFocus();
                }
                else if (TextUtils.isEmpty(textAddress)){
                    Toast.makeText(RegisterOrg.this, "Enter Address!!", Toast.LENGTH_SHORT).show();
                    address.setError("address cannot be empty!!");
                    address.requestFocus();
                } else  if (TextUtils.isEmpty(textPincode)){
                    Toast.makeText(RegisterOrg.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                    pincode.setError("Pincode required!!");
                    pincode.requestFocus();
                }
                else {
                        progressBar.setVisibility(View.VISIBLE);
                        registerOrg(textName,textnumber,textcategory,textemail,textpassword,textAddress,textPincode);
                }
            }
        });

    }

    private void registerOrg(String textName, String textnumber, String textcategory, String textemail, String textpassword, String textAddress, String textPincode) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(textemail,textpassword).addOnCompleteListener(RegisterOrg.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String orgUid = auth.getUid();
                    progressBar.setVisibility(View.GONE);
                    FirebaseUser user = auth.getCurrentUser();


                OrgDetailsModel orgdetails = new OrgDetailsModel(textName,textnumber,textcategory,textemail,textAddress,textPincode,orgUid);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Organization");

                    reference.child(user.getUid()).setValue(orgdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterOrg.this, "Registered Successfully!!!", Toast.LENGTH_SHORT).show();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterOrg.this, "Error occured!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            }
        });
    }
}