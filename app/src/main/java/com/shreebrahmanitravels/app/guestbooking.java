package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class guestbooking extends AppCompatActivity {
TextInputLayout fullname,username,phonenumber;
TextView busroute,vacantseat;
Button reqticket;
ImageView backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestbooking);

        fullname=findViewById(R.id.nameguest);
        username=findViewById(R.id.Usernameguest);
        phonenumber=findViewById(R.id.phonenumberguest);
        reqticket=findViewById(R.id.confirmticketguest);
        busroute=findViewById(R.id.busrouteeditt);
        vacantseat=findViewById(R.id.vacantedit);
        backbutton=findViewById(R.id.backarrrowonboard);

        final String concat= ": "+getIntent().getStringExtra("busroute");
        busroute.setText(concat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        final String tommorowdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(tomorrow);

        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
        final DatabaseReference reference =firebaseDatabase.getReference("Booking/"+tommorowdate+"/"+getIntent().getStringExtra("busroute"));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                int vacantseatint=0;
                for (int i = 1; i <= 6; i++) {
                    for (int j = 1; j <= 2; j++) {

                        if (!Objects.requireNonNull(snapshot.child("L" + i + j).child("isbooked").getValue()).toString().equals("true")) {
                            vacantseatint++;

                        }

                    }
                }

                for (int i = 1; i <= 6; i++) {
                    for (int j = 1; j <= 3; j++) {


                        if (!Objects.requireNonNull(snapshot.child("R" + i + j).child("isbooked").getValue()).toString().equals("true")) {

                          vacantseatint++;
                        }


                    }
                }
String concat= ": "+ vacantseatint;
                vacantseat.setText(concat);}catch (Exception e)
                {
                    vacantseat.setText(": Error fetching seats");
                }
reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      backbutton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              onBackPressed();
          }
      });
        reqticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateUsername() | ! validphonenumber() | !validateName())
                    return;
                Intent intent= new Intent(guestbooking.this, otp_verify.class);
                intent.putExtra("fromguest","true");
                intent.putExtra("fullname",fullname.getEditText().getText().toString());
                intent.putExtra("username",username.getEditText().getText().toString());
                intent.putExtra("phone_no",phonenumber.getEditText().getText().toString());
                intent.putExtra("busroute",busroute.getText().toString());
                startActivity(intent);
            }
        });
    }
    private Boolean validateName() {
        String val = fullname.getEditText().getText().toString();

        if (val.isEmpty()) {
            fullname.setError("Field can not be empty");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        String noWhiteSpace = val.replaceAll("\\s", "");
        boolean contains2number = false, containbetnumber = false, containlastnumber = false;
        if (val.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else if (val.length() > 9) {
            username.setError("Username must be your Roll.no [19IT105]");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            username.setError("Whitespace are not allowed");
            return false;
        }


        /*else  {
            username.setError(null);
            username.setErrorEnabled(false);
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
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        } else {
            username.setError("Not an valid Rollnumber");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}