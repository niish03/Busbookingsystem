package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Confirmticket extends AppCompatActivity {
    Button confirmticket;
    ImageView backbtn;
    ImageView L11, infoicon;
    String busroute;
    int recentviewID = 0;
    String reservedseatID;
    boolean isavailable = false;
    AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmticket);
        L11 = findViewById(R.id.L11);
        infoicon = findViewById(R.id.infoicon);
        backbtn = findViewById(R.id.backarrow);
        confirmticket = findViewById(R.id.confirmticket);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflaterdialoge = this.getLayoutInflater();
        alert.setView(inflaterdialoge.inflate(R.layout.seathintdialoge, null));
        dialog = alert.create();

        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;


        busroute = getIntent().getStringExtra("busnumber");

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference ref = firebaseDatabase.getReference("Booking");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        final String tommorowdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(tomorrow);
        try   { ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (int i = 1; i <= 6; i++) {
                    for (int j = 1; j <= 2; j++) {

                        if (Objects.requireNonNull(dataSnapshot.child(tommorowdate).child(busroute).child("L" + i + j).child("isbooked").getValue()).toString().equals("true")) {

                            int id = getResources().getIdentifier("L" + i + j, "id", getApplicationContext().getPackageName());
                            getResources().getResourceName(id);
                            // System.out.println(reservedseatID);
                            // System.out.println(id);
                            ImageView replace = findViewById(id);
                            replace.setTag("reserved");
                            replace.setImageResource(R.drawable.reservedseat);
                        }

                    }
                }

                for (int i = 1; i <= 6; i++) {
                    for (int j = 1; j <= 3; j++) {


                            if (Objects.requireNonNull(dataSnapshot.child(tommorowdate).child(busroute).child("R" + i + j).child("isbooked").getValue()).toString().equals("true")) {

                                int id = getResources().getIdentifier("R" + i + j, "id", getApplicationContext().getPackageName());
                                getResources().getResourceName(id);
                                // System.out.println(reservedseatID);
                                // System.out.println(id);
                                ImageView replace = findViewById(id);
                                replace.setTag("reserved");
                                replace.setImageResource(R.drawable.reservedseat);
                            }


                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });
} catch (Exception e)
        {
            System.out.println(e);
        }
        infoicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        confirmticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connectionmanager connectionmanagerobj = new connectionmanager();
                connectionmanagerobj.checkConnection(getApplicationContext(),Confirmticket.this);

              if ( connectionmanagerobj.flag==0){

                  System.out.println(selectedseatID);
                  if (selectedseatID != null) {
                      checkvalidseat(selectedseatID,busroute);

                  } else
                      Toast.makeText(Confirmticket.this, "Seat not selected", Toast.LENGTH_SHORT).show();
               }
            }
        });

    }

    private void checkvalidseat(final String selectedseatID, final String busroute) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        final String tommorowdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(tomorrow);
        String path = "Booking/" + tommorowdate + "/"+getIntent().getStringExtra("busnumber")+"/" + selectedseatID + "/isbooked";
        System.out.println(path);
        final DatabaseReference ref = firebaseDatabase.getReference(path);
        System.out.println(selectedseatID);

final String username =new sessionmanager(getBaseContext()).getuserdetail();
        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        final DatabaseReference ref1 = firebaseDatabase1.getReference("Booking");
        final DatabaseReference ref2 = firebaseDatabase1.getReference("users");
        ValueEventListener listener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue().toString());
                if (snapshot.getValue().toString().equals("null")) {



                    ref1.child(tommorowdate).child(busroute).child(selectedseatID).child("isbooked").setValue(true);
                    ref2.child(username).child("seat").child(tommorowdate).child("busroute").setValue(busroute);
                    ref2.child(username).child("seat").child(tommorowdate).child("seat ID").setValue(selectedseatID);
                    ref1.child(tommorowdate).child(busroute).child(selectedseatID).child("username").setValue(username);
                    isavailable=true;
                    if (ref != null && this != null) {
                        ref.removeEventListener(this);
                    }
                    submitseat(isavailable,selectedseatID,busroute);



                } else
                    Toast.makeText(Confirmticket.this, "not available", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addValueEventListener(listener);


    }

    public void submitseat(Boolean isavailable,String selectedseatID, String busroute) {
        if (isavailable) {
            Intent intent = new Intent(Confirmticket.this, bookedsuccesfully.class);
            intent.putExtra("id",selectedseatID);
            intent.putExtra("bus route",busroute);
            startActivity(intent);
        }
    }

     String selectedseatID = null;
    public void Onselectedseat(View view) {

        ImageView img = view.findViewById(view.getId());
        ImageView recentelectedImage = null;

        if (recentviewID != 0) {
            recentelectedImage = findViewById(recentviewID);
        }

        if (view.getTag().toString().equals("true")) {
            img.setImageResource(R.drawable.notselectedseat);
            view.setTag("false");
            selectedseatID = null;

        } else if (view.getTag().toString().equals("false")) {
            img.setImageResource(R.drawable.selectedseat);
            view.setTag("true");
            selectedseatID = getResources().getResourceEntryName(view.getId()).trim();
            System.out.println(selectedseatID);
            if (recentviewID != 0 & recentviewID != view.getId()) {
                recentelectedImage.setImageResource(R.drawable.notselectedseat);
                recentelectedImage.setTag("false");
            }
            recentviewID = view.getId();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}