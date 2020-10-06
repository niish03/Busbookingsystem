package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class forgetpassword_sucessmsg extends AppCompatActivity {

        Button loginbtn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forgetpassword_sucessmsg);
            loginbtn=findViewById(R.id.Loginbuttononsucessmsg);
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(forgetpassword_sucessmsg.this,login.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onBackPressed() {

        }
    }