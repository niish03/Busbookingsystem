package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;


public class feedback extends AppCompatActivity {

    SmileyRating smileRating;
    TextInputLayout message_feedback;
    ImageView backbutton;
    int rating;
    Button submitfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
        message_feedback = findViewById(R.id.msgfeedback);
        submitfeedback = findViewById(R.id.submitfeedback);
        backbutton = findViewById(R.id.backarrowonfeedback);
        smileRating = (SmileyRating) findViewById(R.id.smile_ratingid);



        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        smileRating.setRating(SmileyRating.Type.TERRIBLE,true);
        smileRating.setRating(1, true);




        submitfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Feedback");


                    sessionmanager sessionmanagerobj = new sessionmanager(feedback.this);
                    String username = sessionmanagerobj.getuserdetail();

                    String message = message_feedback.getEditText().getText().toString();
                    databaseReference.child(username).setValue("Rating : " + smileRating.getSelectedSmiley().getRating() + "    Message : " + message);
                    message_feedback.getEditText().getText().clear();
                    message_feedback.clearFocus();

                    Toast.makeText(feedback.this, "Feedback send !", Toast.LENGTH_LONG).show();



            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); }
}