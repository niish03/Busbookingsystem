package com.shreebrahmanitravels.app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class otp_verify extends AppCompatActivity {
    FirebaseDatabase Rootnote;
    DatabaseReference reference;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    String codebysystem;
    Button next_btn;
    TextView OTP_entered_byuser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        progressBar=findViewById(R.id.pgbar);
        progressBar.setVisibility(View.INVISIBLE);
        next_btn=(Button)findViewById(R.id.nxt_btn);
        OTP_entered_byuser=findViewById(R.id.number_otp_txt);
        String phoneNo = getIntent().getStringExtra("phone_no");
        sendverificationCodetouser(phoneNo);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = OTP_entered_byuser.getText().toString().trim();
                if (code.isEmpty()) {
                    OTP_entered_byuser.setError("Wrong OTP...");
                    OTP_entered_byuser.requestFocus();
                    return;
                }
                verifycode(code);

                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }



    private void sendverificationCodetouser(String phoneNO) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNO,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }



    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codebysystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code= phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                progressBar.setVisibility(View.VISIBLE);
                OTP_entered_byuser.setText(code);
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(otp_verify.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    };

    private void verifycode(String code) {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(codebysystem,code);
        signInTheUserByCredentials(credential);

    }


    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(otp_verify.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(otp_verify.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();

                            //Perform Your required action here to either let the user sign In or do something required
                            registeruser();
                            Intent intent = new Intent(otp_verify.this, login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            OTP_entered_byuser.setError("Wrong OTP");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void registeruser() {
        Rootnote = FirebaseDatabase.getInstance();
        reference= Rootnote.getReference("users");


        String phoneNo = getIntent().getStringExtra("phone_no");
        String name = getIntent().getStringExtra("name");
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        String bus_no = getIntent().getStringExtra("bus_no");

        user_helper USER_HELPER= new user_helper(name,username,phoneNo,password,bus_no);
        String currentdate =new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        reference.child(username).setValue(USER_HELPER);
        reference.child(username).child("seat").child(currentdate).child("seat ID").setValue("null");
        reference.child(username).child("branch").setValue("Not Registered");
        reference.child(username).child("stand").setValue("Not Registered");
        reference.child(username).child("pendingfees").setValue("NILL");
        reference.child(username).child("currentyear").setValue("Jan-Jun 2020");
    }
    @Override
    public void onBackPressed(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to cancel Verification?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

}