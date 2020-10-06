package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class seatManagement extends AppCompatActivity {
    Button setvalue;
    String selecteddate;
    TextInputEditText updateusername, fullname, branch, stand, year, pendingfees, busroute;
    TextView fetchdata;
    ImageView backbtn;
    int year_, date_, month_;
    Boolean isexist = false;
    TextView date_output,combineday_year,emptyallseat;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_management);
        //setvalue = findViewById(R.id.setvalues);

        updateusername = findViewById(R.id.updateusername);
        fullname = findViewById(R.id.nameupdate);
        branch = findViewById(R.id.updatebranch);
        stand = findViewById(R.id.updatestand);
        year = findViewById(R.id.updateyear);
        pendingfees = findViewById(R.id.updatependingfees);
        busroute = findViewById(R.id.updateroute);
        fetchdata = findViewById(R.id.fetchdata);
        setvalue = findViewById(R.id.submitonupdate);
        backbtn = findViewById(R.id.backarrowonupdatedata);
        date_output=findViewById(R.id.setdatedateupdate);
        combineday_year=findViewById(R.id.day_yearupdate);
emptyallseat=findViewById(R.id.emptyallseat);
progressBar=findViewById(R.id.progressonseatmanager);
        Calendar calendar = Calendar.getInstance();
        year_=calendar.get(Calendar.YEAR);
        date_=calendar.get(Calendar.DATE);
        month_=calendar.get(Calendar.MONTH);

        progressBar.setVisibility(View.INVISIBLE);
        String currentday = (String) DateFormat.format("EEE", new Date());
        String currentmonth = (String) DateFormat.format("MMM", new Date());
        String day_Monthstr = currentday + ",\n" + currentmonth;
        date_output.setText(String.valueOf(date_));
        combineday_year.setText(day_Monthstr);



        //final String username = new sessionmanager(getBaseContext()).getuserdetail();
        fetchdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setvalue.setText("Submit");
                setvalue.setBackgroundResource(R.drawable.curvegradiantblue);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference setdata = firebaseDatabase.getReference("users");
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                setdata.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (updateusername.getText().toString().length() > 0 & snapshot.child(updateusername.getText().toString()).exists()) {
                            String dataname = snapshot.child(updateusername.getText().toString()).child("name").getValue().toString();
                            String databranch = snapshot.child(updateusername.getText().toString()).child("branch").getValue().toString();
                            String datastand = snapshot.child(updateusername.getText().toString()).child("stand").getValue().toString();
                            String datayear = snapshot.child(updateusername.getText().toString()).child("currentyear").getValue().toString();
                            String datapendingfees = snapshot.child(updateusername.getText().toString()).child("pendingfees").getValue().toString();
                            String databusroute = snapshot.child(updateusername.getText().toString()).child("bus_no").getValue().toString();
                            isexist = true;
                            fullname.setText(dataname);
                            branch.setText(databranch);
                            stand.setText(datastand);
                            year.setText(datayear);
                            pendingfees.setText(datapendingfees);
                            busroute.setText(databusroute);

                            setdata.removeEventListener(this);

                        } else
                            updateusername.setError("User not exist!");
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH, 1);


        emptyallseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                final Date selecteddatedata = new Date(year_-1900,month_,date_);
                System.out.println(year_);
                selecteddate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selecteddatedata);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference ref = firebaseDatabase.getReference("Booking");
                final DatabaseReference ref1 = firebaseDatabase.getReference("users");


                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            ref1.child(Objects.requireNonNull(dsp.getKey())).child("seat").child(selecteddate).child("seat ID").setValue("null");
                            if (ref1 != null && this != null) {
                                ref1.removeEventListener(this);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                for (int busno = 1; busno <= 5; busno++) {
                    for (int i = 1; i <= 6; i++) {
                        for (int j = 1; j <= 2; j++) {
                            ref.child(selecteddate).child("SB" + busno).child("L" + i + j).child("isbooked").setValue("null");

                            ref.child(selecteddate).child("SB" + busno).child("L" + i + j).child("username").setValue("null");

                        }


                    }
                }

                for (int busno = 1; busno <= 5; busno++) {
                    for (int i = 1; i <= 6; i++) {
                        for (int j = 1; j <= 3; j++) {
                            ref.child(selecteddate).child("SB" + busno).child("R" + i + j).child("isbooked").setValue("null");

                            ref.child(selecteddate).child("SB" + busno).child("R" + i + j).child("username").setValue("null");
                        }


                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(seatManagement.this, "Empty seat successfully !", Toast.LENGTH_LONG).show();
            }
        });

        setvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference updatedata = firebaseDatabase.getReference("users");
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (isexist) {

                    updatedata.child(updateusername.getText().toString()).child("name").setValue(fullname.getText().toString());
                    updatedata.child(updateusername.getText().toString()).child("branch").setValue(branch.getText().toString());
                    updatedata.child(updateusername.getText().toString()).child("stand").setValue(stand.getText().toString());
                    updatedata.child(updateusername.getText().toString()).child("currentyear").setValue(year.getText().toString());
                    updatedata.child(updateusername.getText().toString()).child("bus_no").setValue(busroute.getText().toString());
                    updatedata.child(updateusername.getText().toString()).child("pendingfees").setValue(pendingfees.getText().toString());

                    setvalue.setText("Updated");
                    setvalue.setBackgroundResource(R.drawable.gradiantyellowgreen);
                    setvalue.setTextColor(Color.BLACK);
                    isexist = false;

                } else
                    updateusername.setError("Please fetch the data from below button");
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
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

    public void selecdate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                Date dateobj = new Date(i, i1, i2 - 1);


                String selecteddate = String.valueOf(i2);
                year_ = i;
                month_ = i1;
                date_ = i2;

                String selectedmonth = (String) DateFormat.format("MMM", dateobj);

                String selectedday = (String) DateFormat.format("EEE", dateobj);
                String day_Monthstr = selectedday + ",\n" + selectedmonth;
                date_output.setText(String.valueOf(i2));
                combineday_year.setText(day_Monthstr);
            }
        }, year_, month_, date_);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
datePickerDialog.show();
    }
}