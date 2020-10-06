package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Forgetpassword extends AppCompatActivity {
    ImageView backbtn;
    TextInputLayout phonenumber;
    Button next_Button_fp_screen;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        phonenumber= findViewById(R.id.phonenumberonforgetpassword);
        next_Button_fp_screen = findViewById(R.id.nextbuttononforgetpasswordscreen);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        backbtn=findViewById(R.id.backarrow);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forgetpassword.this,login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        next_Button_fp_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                connectionmanager connectionmanagerOBJ = new connectionmanager();
                connectionmanagerOBJ.checkConnection(getBaseContext(),Forgetpassword.this);
                if(connectionmanagerOBJ.flag==0)
                {
                    progressBar.setVisibility(View.VISIBLE);

                    if(!validphonenumber()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    isphoneexist();
                }

               /* connectionmanager connectionmanagerOBJ = new connectionmanager();
                connectionmanagerOBJ.checkConnection(getBaseContext(),signup.this);
                if(connectionmanagerOBJ.flag==0)
                {

                }*/



            }
        });
    }

    private void isphoneexist() {
        final String new_phonenumber= phonenumber.getEditText().getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        final Query checkuser = reference.orderByChild("phoneNo").equalTo(new_phonenumber);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    if( !validphonenumber() ) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    Intent intent = new Intent(Forgetpassword.this,otp_verify_forgotpassword.class);
                    String phnnmber= phonenumber.getEditText().getText().toString();
                    intent.putExtra("phone_no",phnnmber);
                    if (checkuser != null && this != null) {
                        checkuser.removeEventListener(this);
                    }
                    startActivity(intent);
                    progressBar.setVisibility(View.INVISIBLE);

                }
                else
                {
                    phonenumber.setError("Phone No. is not registered");

                    progressBar.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Forgetpassword.this,login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private Boolean validphonenumber(){
        String val= phonenumber.getEditText().getText().toString();

        if(val.isEmpty()) {
            phonenumber.setError("Enter a valid number");
            return false;
        }
        else if(val.length()!=10){
            phonenumber.setError("Enter a valid number");
            return false;

        }
        else {
            phonenumber.setError(null);
            phonenumber.setErrorEnabled(false);
            return true;
        }

    }
}