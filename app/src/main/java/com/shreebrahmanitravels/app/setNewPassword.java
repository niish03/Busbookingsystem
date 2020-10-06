package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class setNewPassword extends AppCompatActivity {

    FirebaseDatabase Rootnote;
    DatabaseReference reference;
    TextInputLayout new_password,newConfirmpassword;
    Button nextbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);


        new_password=findViewById(R.id.Newpsswrd);
        newConfirmpassword=findViewById(R.id.newconfirmpsswrd);
        nextbtn=findViewById(R.id.nextbuttononforgetpasswordscreen);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                connectionmanager connectionmanagerOBJ = new connectionmanager();
                connectionmanagerOBJ.checkConnection(getBaseContext(),setNewPassword.this);
                if(connectionmanagerOBJ.flag==0)
                {
                    if(!validnewpassword() | !validnewconfirmpassword())
                        return;
                    changepassword();
                }


            }
        });

    }

    private void changepassword() {
        Rootnote = FirebaseDatabase.getInstance();
        reference= Rootnote.getReference("users");
        final String password= newConfirmpassword.getEditText().getText().toString();
        String PhoneNo = getIntent().getStringExtra("phone__No");



        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkpass = reference.orderByChild("phoneNo").equalTo(PhoneNo);





        checkpass.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                reference.child(snapshot.getKey().toString()).child("password").setValue(password);
                Intent intent=new Intent(setNewPassword.this,forgetpassword_sucessmsg.class);
                startActivity(intent);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private Boolean validnewpassword(){
        String val= new_password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +  "$";             //at least 4 characters

        if(val.isEmpty()) {
            new_password.setError("Password can not be empty");
            return false;
        }

        else if(!val.matches(passwordVal)){
            new_password.setError("Password must contain: Letter,Specialchar,Atleast 4 char");
            return false;

        }


        else {
            new_password.setError(null);

            new_password.setErrorEnabled(false);

            return true;
        }

    }

    private Boolean validnewconfirmpassword(){
        String val= new_password.getEditText().getText().toString();
        String val1= newConfirmpassword.getEditText().getText().toString();

        if(!val.matches(val1)){
            newConfirmpassword.setError("Password didn't match");
            return false;
        }
        else{
            newConfirmpassword.setError(null);

            newConfirmpassword.setErrorEnabled(false);

            return true;
        }

    }

    @Override
    public void onBackPressed() {

    }
}