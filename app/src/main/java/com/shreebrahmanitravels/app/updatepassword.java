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

public class updatepassword extends AppCompatActivity {
    TextInputLayout currentpassword, newpassword, conformnewpassword;
    Button nextbutton;
    String databasepassword;
    String usercurrentpassword;
    ImageView backarrow;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String username = new sessionmanager(getBaseContext()).getuserdetail();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepassword);
        currentpassword = findViewById(R.id.current_password);
        newpassword = findViewById(R.id.new_password);
        conformnewpassword = findViewById(R.id.re_entered_password);
        nextbutton = findViewById(R.id.nextbuttononchangepasswordscreen);
        backarrow = findViewById(R.id.backarrowonupdatepass);
        progressBar = findViewById(R.id.progressbaronupdatepssword);

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                progressBar.setVisibility(View.VISIBLE);
                connectionmanager connectionmanagerOBJ = new connectionmanager();
                connectionmanagerOBJ.checkConnection(getBaseContext(), updatepassword.this);
                if (connectionmanagerOBJ.flag == 0) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference Reference = firebaseDatabase.getReference("users");

                    // Query checkpassword= Reference.orderByChild("password").equalTo(usercurrentpassword);


                    Reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            usercurrentpassword = currentpassword.getEditText().getText().toString();
databasepassword= snapshot.child(username).child("password").getValue().toString();
                            if (databasepassword.equals(usercurrentpassword)) {
                                if(Reference!=null && this!=null)
                                    Reference.removeEventListener(this);
                                currentpassword.setError(null);
                                currentpassword.setErrorEnabled(false);

                                if (!validnewpassword() | !validnewconfirmpassword()) {
                                    if(Reference!=null && this!=null)
                                        Reference.removeEventListener(this);
                                    return;
                                }


                                updatepassword(newpassword.getEditText().getText().toString(), username);

                            } else {
                                currentpassword.setError("Password didn't Matched");
                                progressBar.setVisibility(View.INVISIBLE);
                                if(Reference!=null && this!=null)
                                    Reference.removeEventListener(this);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updatepassword(String s, String username) {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference Reference = firebaseDatabase.getReference("users");
        Reference.child(username).child("password").setValue(s);
        Intent intent = new Intent(this,updatedpasswordsucess.class);
        startActivity(intent);
        progressBar.setVisibility(View.INVISIBLE);

    }

    private Boolean validnewpassword() {
        String val = newpassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" + "$";             //at least 4 characters

        if (val.isEmpty()) {
            newpassword.setError("Password can not be empty");
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        } else if (!val.matches(passwordVal)) {
            newpassword.setError("Password must contain: Letter,Specialchar,Atleast 4 char");
            progressBar.setVisibility(View.INVISIBLE);
            return false;

        } else {
            newpassword.setError(null);

            newpassword.setErrorEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }

    }

    private Boolean validnewconfirmpassword() {
        String val = newpassword.getEditText().getText().toString();
        String val1 = conformnewpassword.getEditText().getText().toString();

        if (!val.matches(val1)) {
            conformnewpassword.setError("Password didn't match");
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        } else {
            conformnewpassword.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            conformnewpassword.setErrorEnabled(false);

            return true;
        }
    }


}