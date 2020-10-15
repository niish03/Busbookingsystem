package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityOptions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class login extends AppCompatActivity {
    ImageView busimage;
    TextView welcometext, signintocontitxt;
    TextInputLayout username, password;
    Button Go_onLOGIN, new_userbtn;
    ProgressBar progressBar;
    TextView forgetPasswordtxt;
    TextView skipsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

//hookups
        busimage = findViewById(R.id.bus_image);
        welcometext = findViewById(R.id.welcomebacktext);
        signintocontitxt = findViewById(R.id.signintoconttext);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        Go_onLOGIN = findViewById(R.id.Go_button_on_login);
        new_userbtn = findViewById(R.id.signupbuttononlogin);
        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.INVISIBLE);
        skipsignup=findViewById(R.id.skipsignup);

        forgetPasswordtxt = findViewById(R.id.ForgetText);

        forgetPasswordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(login.this, Forgetpassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        new_userbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(busimage, "bus_image");
                pairs[1] = new Pair<View, String>(welcometext, "Text");
                pairs[2] = new Pair<View, String>(signintocontitxt, "signin_tran");
                pairs[3] = new Pair<View, String>(new_userbtn, "SIgnup_transi");
                pairs[4] = new Pair<View, String>(username, "username_transi");
                pairs[5] = new Pair<View, String>(password, "password_transi");
                pairs[6] = new Pair<View, String>(Go_onLOGIN, "button_tran");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
                Intent intent = new Intent(login.this, signup.class);


                startActivity(intent, options.toBundle());
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Go_onLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                connectionmanager connectionmanagerOBJ = new connectionmanager();
                connectionmanagerOBJ.checkConnection(getBaseContext(), login.this);
                if (connectionmanagerOBJ.flag == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    loginuser();
                }

                //Intent intent = new Intent(login.this,welcomePage.class);


            }
        });
skipsignup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(login.this,guestwarning.class));
    }
});

    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to Exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
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


    private void loginuser() {
        if (!validateUsername() | !validpassword()) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        } else {

            check_isUser();
        }
    }

    private void check_isUser() {
        final String userstored_lower_username = username.getEditText().getText().toString().trim();
        final String userStored_data_password = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkuser = reference.orderByChild("username").equalTo(userstored_lower_username);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);


                    String password_from_DB = snapshot.child(userstored_lower_username).child("password").getValue().toString();

                    if (password_from_DB.equals(userStored_data_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);
                        sessionmanager sessionmanager_obj = new sessionmanager(login.this);
                        String username_ = username.getEditText().getText().toString();
                        String password_ = password.getEditText().getText().toString();

                        sessionmanager_obj.createloginsession(username_, password_);
                        //sessionmanager_obj.setloginusername(username_);
                        Intent intent = new Intent(login.this, welcomePage.class);

                        startActivity(intent);
                    } else {
                        password.setError("Wrong password");
                        password.requestFocus();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    username.setError("User not exist");
                    username.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean validpassword() {
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("Password can't be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Username can't be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        sessionmanager sessionmanager_obj = new sessionmanager(login.this);
        String username = sessionmanager_obj.getuserdetail();
        if (username != null) {
            movetowelcomepage();

        }

    }

    private void movetowelcomepage() {
        Intent intent = new Intent(login.this, welcomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}