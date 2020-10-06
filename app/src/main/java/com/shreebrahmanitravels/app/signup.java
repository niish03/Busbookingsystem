package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class signup extends AppCompatActivity {

    FirebaseDatabase Rootnote;
    DatabaseReference reference;
    Button Alreadyhaveacc, Gobuttononsignup;
    TextInputLayout iduser, idpass;
    ImageView busimage;
    TextView welcome, signuptocontitxt;
    TextInputLayout fullnameintextinput, phonenumber;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBar;
    TextView busroutetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        busimage = findViewById(R.id.bus_image);
        welcome = findViewById(R.id.welcomeonsignup);
        signuptocontitxt = findViewById(R.id.signuptocontitxt);
        iduser = findViewById(R.id.UsernameSignup);
        idpass = findViewById(R.id.Passwordsignup);
        Gobuttononsignup = findViewById(R.id.gobuttomonsignup);
        Alreadyhaveacc = findViewById(R.id.ALreadyacc);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        busroutetext = findViewById(R.id.editText);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fullnameintextinput = findViewById(R.id.name);
        phonenumber = findViewById(R.id.phonenumber);

        final PopupMenu popupMenu = new PopupMenu(signup.this, busroutetext);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.dropdownbusselection, popupMenu.getMenu());

        busroutetext.setText("Select your bus Route");
        busroutetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenu.show();
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.SB1:
                        busroutetext.setText(menuItem.getTitle());
                        return true;
                    case R.id.SB2:
                        busroutetext.setText(menuItem.getTitle());
                        return true;
                    case R.id.SB3:
                        busroutetext.setText(menuItem.getTitle());
                        return true;
                    case R.id.SB4:
                        busroutetext.setText(menuItem.getTitle());
                        return true;
                    case R.id.SB5:
                        busroutetext.setText(menuItem.getTitle());
                        return true;
                    default:
                        return false;
                }
            }
        });


        Alreadyhaveacc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, login.class);


                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(busimage, "bus_image");
                pairs[1] = new Pair<View, String>(welcome, "Text");
                pairs[2] = new Pair<View, String>(signuptocontitxt, "signin_tran");
                pairs[3] = new Pair<View, String>(Alreadyhaveacc, "SIgnup_transi");
                pairs[4] = new Pair<View, String>(iduser, "username_transi");
                pairs[5] = new Pair<View, String>(idpass, "password_transi");
                pairs[6] = new Pair<View, String>(Gobuttononsignup, "button_tran");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(signup.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });
        Gobuttononsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                connectionmanager connectionmanagerOBJ = new connectionmanager();
                connectionmanagerOBJ.checkConnection(getBaseContext(), signup.this);
                if (connectionmanagerOBJ.flag == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    isexistusername();

                    if (!validateName() | !validateUsername() | !validphonenumber() | !validpassword() | !validbusroute()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                }


            }
        });


    }

    private void isexistusername() {
        final String new_username = iduser.getEditText().getText().toString();
        final String phonenum = phonenumber.getEditText().getText().toString();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkuser = reference.orderByChild("username").equalTo(new_username);
        final Query checkphone = reference.orderByChild("phoneNo").equalTo(phonenum);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    iduser.setError("Username already exist");
                    progressBar.setVisibility(View.INVISIBLE);

                } else if (true) {
                    checkphone.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                phonenumber.setError("Phone no. already exists");
                                progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                if (!validateName() | !validateUsername() | !validphonenumber() | !validpassword() | !validbusroute()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    return;
                                }
                                String regphonenumber = phonenumber.getEditText().getText().toString().trim();
                                String name = fullnameintextinput.getEditText().getText().toString().trim();
                                String username = iduser.getEditText().getText().toString().trim();
                                String password = idpass.getEditText().getText().toString().trim();
                                String bus_no = busroutetext.getText().toString();
                                Intent intent = new Intent(signup.this, otp_verify.class);
                                intent.putExtra("phone_no", regphonenumber);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                intent.putExtra("password", password);
                                intent.putExtra("bus_no", bus_no);

                                startActivity(intent);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private Boolean validateName() {
        String val = fullnameintextinput.getEditText().getText().toString();

        if (val.isEmpty()) {
            fullnameintextinput.setError("Field can not be empty");
            return false;
        } else {
            fullnameintextinput.setError(null);
            fullnameintextinput.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateUsername() {
        String val = iduser.getEditText().getText().toString();
        String noWhiteSpace = val.replaceAll("\\s", "");
        boolean contains2number = false, containbetnumber = false, containlastnumber = false;
        if (val.isEmpty()) {
            iduser.setError("Field can not be empty");
            return false;
        } else if (val.length() > 9) {
            iduser.setError("Username must be your Roll.no [19IT105]");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            iduser.setError("Whitespace are not allowed");
            return false;
        }


        /*else  {
            iduser.setError(null);
            iduser.setErrorEnabled(false);
            return true;
        }*/
        //19IT087      19CE087   19EC087    19ME087
//19BCA087  19BBA087


        else if (val.length() == 7) {

            for (int i = 0; i < val.length(); i++) {

                if ((val.charAt(0) == '1' | val.charAt(0) == '2') & ((val.charAt(1) >= '0') & val.charAt(1) <= '9')) {
                    contains2number = true;
                }
                if ((val.charAt(2) >= 'A' & val.charAt(2) <= 'Z') & (val.charAt(3) >= 'A' & val.charAt(3) <= 'Z')) {
                    containbetnumber = true;
                }
                if ((val.charAt(2) >= 'a' & val.charAt(2) <= 'z') & (val.charAt(3) >= 'a' & val.charAt(3) <= 'z')) {
                    containbetnumber = true;
                }
                if ((val.charAt(4) >= '0' & val.charAt(4) <= '9') & (val.charAt(5) >= '0' & val.charAt(5) <= '9') & (val.charAt(6) >= '0' & val.charAt(6) <= '9')) {
                    containlastnumber = true;
                }
            }
        }

        //19DIT087      19DCE087   19DEC087    19DME087
        //19BCA087      19BBA087 19BSC087 19BPH
        else if ((val.length() == 8) & (val.charAt(2) == 'b' | val.charAt(2) == 'B' | val.charAt(2) == 'd' | val.charAt(2) == 'D')) {

            if (val.charAt(1) >= '0' & val.charAt(1) <= '9') {
                for (int i = 0; i < val.length(); i++) {

                    if ((val.charAt(0) == '1' | val.charAt(0) == '2') & ((val.charAt(1) >= '0') & val.charAt(1) <= '9')) {
                        contains2number = true;
                    }
                    if ((val.charAt(2) >= 'A' & val.charAt(2) <= 'Z') & (val.charAt(3) >= 'A' & val.charAt(3) <= 'Z') & (val.charAt(4) >= 'A' & val.charAt(4) <= 'Z')) {
                        containbetnumber = true;
                    }
                    if ((val.charAt(2) >= 'a' & val.charAt(2) <= 'z') & (val.charAt(3) >= 'a' & val.charAt(3) <= 'z') & (val.charAt(4) >= 'a' & val.charAt(4) <= 'z')) {
                        containbetnumber = true;
                    }
                    if ((val.charAt(7) >= '0' & val.charAt(7) <= '9') & (val.charAt(5) >= '0' & val.charAt(5) <= '9') & (val.charAt(6) >= '0' & val.charAt(6) <= '9')) {
                        containlastnumber = true;
                    }
                }
            }
            //D19IT087
        } else if (val.length() == 8 & (val.charAt(0) == 'd' | val.charAt(0) == 'D')) {
            for (int i = 0; i < val.length(); i++) {

                if ((val.charAt(1) == '1' | val.charAt(1) == '2') & ((val.charAt(2) >= '0') & val.charAt(2) <= '9')) {
                    contains2number = true;
                }
                if ((val.charAt(4) >= 'A' & val.charAt(4) <= 'Z') & (val.charAt(3) >= 'A' & val.charAt(3) <= 'Z')) {
                    containbetnumber = true;
                }
                if ((val.charAt(4) >= 'a' & val.charAt(4) <= 'z') & (val.charAt(3) >= 'a' & val.charAt(3) <= 'z')) {
                    containbetnumber = true;
                }
                if ((val.charAt(7) >= '0' & val.charAt(7) <= '9') & (val.charAt(5) >= '0' & val.charAt(5) <= '9') & (val.charAt(6) >= '0' & val.charAt(6) <= '9')) {
                    containlastnumber = true;
                }
            }
        }
        if (contains2number & containbetnumber & containlastnumber) {
            iduser.setError(null);
            iduser.setErrorEnabled(false);
            return true;
        } else {
            iduser.setError("Not an valid Rollnumber");
            return false;
        }


    }

    private Boolean validphonenumber() {
        String val = phonenumber.getEditText().getText().toString();

        if (val.isEmpty()) {
            phonenumber.setError("Enter a valid number");
            return false;
        } else if (val.length() != 10) {
            phonenumber.setError("Enter a valid number");
            return false;

        } else {
            phonenumber.setError(null);
            phonenumber.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validpassword() {
        String val = idpass.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=!])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (val.isEmpty()) {
            idpass.setError("Password can not be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            idpass.setError("Password must contain: Letter,specialchar,Atleast 4 char");
            return false;

        } else {
            idpass.setError(null);
            idpass.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validbusroute() {
        String val = busroutetext.getText().toString();

        if (val.equals("Select your bus Route")) {
            busroutetext.setError("Please select Bus route");
            return false;
        } else {
            busroutetext.setError(null);
            return true;
        }

    }

}