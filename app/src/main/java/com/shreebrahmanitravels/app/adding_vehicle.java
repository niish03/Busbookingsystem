package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.google.firebase.firestore.core.OrderBy;

public class adding_vehicle extends AppCompatActivity {
    
    TextInputLayout entered_name , from , to , vehicle_model , vehicle_number , cost_per_passanger , entered_phone_no ;
    TextView vehicle_type;
    Button add_vehicle;
    ProgressBar progressBar;
    String data_name , data_phone_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_vehicle);

        entered_name = findViewById(R.id.name_ridesharetext);
        from = findViewById(R.id.from_ridesharetext);
        to = findViewById(R.id.to_ridesharetext);
        vehicle_model = findViewById(R.id.vehicle_model_rideshare);
        vehicle_number = findViewById(R.id.vehiclenumber_rideshare);
        cost_per_passanger = findViewById(R.id.cost_ridesharetext);
        entered_phone_no = findViewById(R.id.phonenumber_rideshare);
        vehicle_type = findViewById(R.id.vehicletypedropdown);
        add_vehicle = findViewById(R.id.addvehicle_rideshare);
        progressBar = findViewById(R.id.progressbar_rideshare);
        
        final PopupMenu popupMenu = new PopupMenu(adding_vehicle.this, vehicle_type);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.dropdown_vehicletype, popupMenu.getMenu());

        vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenu.show();
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.car:
                        vehicle_type.setText(menuItem.getTitle());
                        return true;
                    case R.id.bike:
                        vehicle_type.setText(menuItem.getTitle());
                        return true;
                    case R.id.bus:
                        vehicle_type.setText(menuItem.getTitle());
                        return true;
                    case R.id.other:
                        vehicle_type.setText(menuItem.getTitle());
                        return true;
                    default:
                        return false;
                }
            }
        });

        fetch_user_data();

        add_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                connectionmanager connectionmanagerOBJ = new connectionmanager();
                connectionmanagerOBJ.checkConnection(getBaseContext(), adding_vehicle.this);
                if (connectionmanagerOBJ.flag == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    push_vehicledata();

                    if (!validateName() | !validphonenumber() | !valid_from() | !valid_to() | !valid_vehiclemodel() | !valid_vehiclenumber() | !validcost()) {
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                }


            }
        });

    }

    private void fetch_user_data() {
        final String username = new sessionmanager(getBaseContext()).getuserdetail();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data_name = snapshot.child(username).child("name").getValue().toString();
                data_phone_no = snapshot.child(username).child("phoneNo").getValue().toString();
                entered_name.getEditText().setText(data_name);
                entered_phone_no.getEditText().setText(data_phone_no);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void push_vehicledata() {
        String entered_name_string = entered_name.getEditText().getText().toString();
        String from_string = entered_name.getEditText().getText().toString();
        String to_string = entered_name.getEditText().getText().toString();
        String vehicle_model_string = entered_name.getEditText().getText().toString();
        String vehicle_type_string = entered_name.getEditText().getText().toString();
        String vehicle_number_string = entered_name.getEditText().getText().toString();
        String cost_pp_string = entered_name.getEditText().getText().toString();
        String entered_phone_number = entered_name.getEditText().getText().toString();

        if(!data_name.equals(entered_phone_number))
        {

        }
    }

    private Boolean validateName() {
        String val = entered_name.getEditText().getText().toString();

        if (val.isEmpty()) {
            entered_name.setError("Field can not be empty");
            return false;
        } else {
            entered_name.setError(null);
            entered_name.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean valid_from() {
        String val = from.getEditText().getText().toString();

        if (val.isEmpty()) {
            from.setError("Field can not be empty");
            return false;
        } else {
            from.setError(null);
            from.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean valid_to() {
        String val = to.getEditText().getText().toString();

        if (val.isEmpty()) {
            to.setError("Field can not be empty");
            return false;
        } else {
            to.setError(null);
            to.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean valid_vehiclemodel() {
        String val = vehicle_model.getEditText().getText().toString();

        if (val.isEmpty()) {
            vehicle_model.setError("Field can not be empty");
            return false;
        } else {
            vehicle_model.setError(null);
            vehicle_model.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean valid_vehiclenumber() {
        String val = vehicle_number.getEditText().getText().toString();

        if (val.isEmpty()) {
            vehicle_number.setError("Field can not be empty");
            return false;}
    else {
            vehicle_number.setError(null);
            vehicle_number.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validcost() {
        String val = cost_per_passanger.getEditText().getText().toString();

        if (val.isEmpty()) {
            cost_per_passanger.setError("Field can not be empty");
            return false;
        } else {
            cost_per_passanger.setError(null);
            cost_per_passanger.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validphonenumber() {
        String val = entered_phone_no.getEditText().getText().toString();

        if (val.isEmpty()) {
            entered_phone_no.setError("Enter a valid number");
            return false;
        } else if (val.length() != 10) {
            entered_phone_no.setError("Enter a valid number");
            return false;

        } else {
            entered_phone_no.setError(null);
            entered_phone_no.setErrorEnabled(false);
            return true;
        }

    }
    @Override
    public void onBackPressed() {

       super.onBackPressed();

    }
}