package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class guestwarning extends AppCompatActivity {
ImageView backbtn;
Button getstarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestwarning);

getstarted=findViewById(R.id.getstarted);
        backbtn= findViewById(R.id.backarrow);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionmanager sessionmanagerobj= new sessionmanager(getBaseContext());
                sessionmanagerobj.createloginsession("Guest user","Guest");
                startActivity(new Intent(guestwarning.this,welcomePage.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}