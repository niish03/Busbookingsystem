package com.shreebrahmanitravels.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class dashboard_booking extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference refnew = firebaseDatabase.getReference("users");
    TextView date, day_month, busroutetext, recentbooktext, dateonticket, busrouteonticket, seatnumberonticket;
    String currentday, currentmonth;
    String day_Monthstr, currentyear, busnofromdatabase;
    int date_, month_, year_;
    Button serchbusbtn , trackbus;
    Boolean bookingsecondtime = false;
    ImageView calenderimage;
    TextInputLayout outbox;
    RelativeLayout ticket;
    String selecteddate, selectedday, selectedmonth;
    ProgressBar progressBar;

    public dashboard_booking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dashboard_booking newInstance(String param1, String param2) {
        dashboard_booking fragment = new dashboard_booking();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String[] currentdate = {new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date())};
        final String username = new sessionmanager(requireContext()).getuserdetail();


        final AlertDialog[] dialog = {null};
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final LayoutInflater inflaterdialoge = requireActivity().getLayoutInflater();

        alert.setView(inflaterdialoge.inflate(R.layout.relaxseatalreadybooked, null));
        dialog[0] = alert.create();

        dialog[0].setCanceledOnTouchOutside(true);
        dialog[0].getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog[0].getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog[0].getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

        final ValueEventListener listener, listenerforticket;
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                final Date datetommorow = calendar.getTime();
                final String tommorowdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(datetommorow);

                try {
                    if (!Objects.requireNonNull(snapshot.child(username).child("seat").child(tommorowdate).child("seat ID").getValue()).toString().equals("null")) {
                        bookingsecondtime = false;
                        if (refnew != null && this != null) {
                            refnew.removeEventListener(this);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        dialog[0].show();

                    } else {
                        if (refnew != null && this != null) {
                            refnew.removeEventListener(this);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        intenttoconfirmticket(true);

                    }


                } catch (Exception e) {
                    alert.setView(inflaterdialoge.inflate(R.layout.bookingnotopenbysystem, null));
                    dialog[0] = alert.create();

                    dialog[0].setCanceledOnTouchOutside(true);
                    dialog[0].getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    dialog[0].getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog[0].getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                    dialog[0].show();
                    progressBar.setVisibility(View.INVISIBLE);
                    if (refnew != null && this != null) {
                        refnew.removeEventListener(this);
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        listenerforticket = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {

                final String lastbookeddate =  snapshot.child(username).child("lastbooked_date").getValue().toString();


                    if (!Objects.requireNonNull(snapshot.child(username).child("seat").child(lastbookeddate).child("seat ID").getValue()).toString().equals("null")) {

                        if (refnew != null && this != null) {
                            refnew.removeEventListener(this);
                        }
                        //snapshot.child(username).child("seat").child().getValue().toString()
                        trackbus.setVisibility(View.VISIBLE);
                        int date= Integer.parseInt(lastbookeddate.substring(0,2));
                        int month= Integer.parseInt(lastbookeddate.substring(3,5))-1;
                        int year= Integer.parseInt(lastbookeddate.substring(6,10));
                        Date lastdate= new Date(year,month,date);

                        String strmonth= (String) DateFormat.format("MMMM",lastdate);
                        String recentbookeddate= date+" " + strmonth +", " +  year;
                        dateonticket.setText(recentbookeddate);
                        String seatiddata= ": "+ snapshot.child(username).child("seat").child(lastbookeddate).child("seat ID").getValue().toString();
                        String busroutedata = ": "+snapshot.child(username).child("seat").child(lastbookeddate).child("busroute").getValue().toString();
                        busrouteonticket.setText(busroutedata);
                        seatnumberonticket.setText(seatiddata);

//dateonticket.setText();

                        recentbooktext.setVisibility(View.VISIBLE);
                        ticket.setVisibility(View.VISIBLE);
                        ticket.setAlpha(0.0f);
                        recentbooktext.setAlpha(0.0f);
                        //ticket.setAlpha(0.0f);

// Start the animation
                     /*   ticket.animate()
                                .translationY(0,0,ticket.getHeight(),0)
                                .alpha(1.0f)
                                .setListener(null);
*/
                        TranslateAnimation animate = new TranslateAnimation(
                                0,                 // fromXDelta
                                0,                 // toXDelta
                                100,  // fromYDelta
                                0);                // toYDelta
                        animate.setDuration(450);
                        animate.setFillAfter(true);


                        ticket.startAnimation(animate);
                        recentbooktext.startAnimation(animate);
                        ticket.animate().alpha(1.0f);
                        recentbooktext.animate().alpha(1.0f);

                    } else {
                        if (refnew != null && this != null) {
                            refnew.removeEventListener(this);
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard_booking, container, false);
        date = v.findViewById(R.id.setdatedate);
        day_month = v.findViewById(R.id.day_year);
        busroutetext = v.findViewById(R.id.editTextonseatbooking);
        calenderimage = v.findViewById(R.id.calendaricon);
        serchbusbtn = v.findViewById(R.id.serchbustn);
        outbox = v.findViewById(R.id.outboxofbusrouteonbooking);
        ticket = (RelativeLayout) v.findViewById(R.id.relativeticketview);
        recentbooktext = v.findViewById(R.id.recentbookedtext);
        dateonticket = v.findViewById(R.id.dateonticket);
        busrouteonticket = v.findViewById(R.id.busroutetextonticketedit);
        seatnumberonticket = v.findViewById(R.id.seatonticketedit);
        trackbus=v.findViewById(R.id.trackbusbtn);
        recentbooktext.setVisibility(View.INVISIBLE);
        ticket.setVisibility(View.GONE);
        progressBar = v.findViewById(R.id.progressbarseatbooking);

        progressBar.setVisibility(View.INVISIBLE);
        trackbus.setVisibility(View.INVISIBLE);


        refnew.addValueEventListener(listenerforticket);

        Calendar calendartommorw = Calendar.getInstance();
        calendartommorw.add(Calendar.DAY_OF_YEAR, 1);
        final int date_tommorow = calendartommorw.get(Calendar.DATE);

        String TOMMOROW_DAY = (String) DateFormat.format("EEE", calendartommorw);
        String tommorow_month = (String) DateFormat.format("MMM", calendartommorw);
        String cmbinemonth = TOMMOROW_DAY + ",\n" + tommorow_month;

        Calendar calendar = Calendar.getInstance();
        date_ = calendar.get(Calendar.DATE);
        month_ = calendar.get(Calendar.MONTH);
        year_ = calendar.get(Calendar.YEAR);
        currentmonth = (String) DateFormat.format("MMM", new Date());

        currentday = (String) DateFormat.format("EEE", new Date());
        day_Monthstr = currentday + ",\n" + currentmonth;
        date.setText(String.valueOf(date_tommorow));
        day_month.setText(cmbinemonth);


        final PopupMenu popupMenu = new PopupMenu(getContext(), busroutetext);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.dropdownbusselection, popupMenu.getMenu());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference ref = firebaseDatabase.getReference("users");
        final sessionmanager sessionmanagerobj = new sessionmanager(requireContext());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
try
                //System.out.println(dataSnapshot);

{
                String loginusername = sessionmanagerobj.getuserdetail();

                busnofromdatabase = dataSnapshot.child(loginusername).child("bus_no").getValue().toString();
                busroutetext.setText(busnofromdatabase);
                if (ref != null && this != null) {
                    ref.removeEventListener(this);
                }
}catch (Exception e)
{
    System.out.println(e);
}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });



        serchbusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                progressBar.setVisibility(View.VISIBLE);

                final AlertDialog[] dialog = {null};
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final LayoutInflater inflaterdialoge = requireActivity().getLayoutInflater();

                alert.setView(inflaterdialoge.inflate(R.layout.relaxseatalreadybooked, null));
                dialog[0] = alert.create();

                dialog[0].setCanceledOnTouchOutside(true);
                dialog[0].getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                dialog[0].getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog[0].getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                connectionmanager connectionmanagerobj = new connectionmanager();
                connectionmanagerobj.checkConnection(requireContext(), getActivity());


                if (connectionmanagerobj.flag == 0) {

                    //     Query checkalreadyexist = refnew.orderByChild(currentdate+"/SB1/"+)

                    if (busroutetext.getText().toString().trim().length() != 3) {
                        outbox.setError("Please select bus route.");
                        progressBar.setVisibility(View.INVISIBLE);
                    } else if (!date.getText().toString().equals(String.valueOf(date_tommorow))) {
                        busroutetext.setError(null);
                        busroutetext.setFocusable(false);
                        if (Integer.parseInt(date.getText().toString()) > date_tommorow) {
                            alert.setView(inflaterdialoge.inflate(R.layout.notacceptingbooking, null));
                            dialog[0] = alert.create();

                            dialog[0].setCanceledOnTouchOutside(true);
                            dialog[0].getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                            dialog[0].getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog[0].getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                            dialog[0].show();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else if (Integer.parseInt(date.getText().toString()) <= date_) {
                            alert.setView(inflaterdialoge.inflate(R.layout.bookingclosed, null));
                            dialog[0] = alert.create();

                            dialog[0].setCanceledOnTouchOutside(true);
                            dialog[0].getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                            dialog[0].getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog[0].getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                            dialog[0].show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if(new sessionmanager(getContext()).getuserdetail().equals("Guest user"))
                            {
                                Intent intent = new Intent(getActivity(),guestbooking.class);
                                intent.putExtra("busroute",busroutetext.getText().toString());
                                startActivity(intent);

                            progressBar.setVisibility(View.INVISIBLE);}
                        else
                        refnew.addValueEventListener(listener);
                    }

                } else
                    progressBar.setVisibility(View.INVISIBLE);
/*
                    refnew.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());


                            alert.setView(inflaterdialoge.inflate(R.layout.relaxseatalreadybooked, null));
                            dialog[0] = alert.create();

                            dialog[0].setCanceledOnTouchOutside(true);
                            dialog[0].getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                            dialog[0].getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                            dialog[0].getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

                            for (int i = 1; i <= 6; i++) {
                                for (int j = 1; j <= 2; j++) {

                                    if (Objects.requireNonNull(snapshot.child(currentdate).child("SB1").child("L" + i + j).child("username").getValue()).toString().equals(new sessionmanager(requireContext()).getuserdetail())) {
                                        bookingsecondtime = true;


                                    }


                                }
                            }

                            for (int i = 1; i <= 6; i++) {
                                for (int j = 1; j <= 3; j++) {


                                    if (Objects.requireNonNull(snapshot.child(currentdate).child("SB1").child("R" + i + j).child("username").getValue()).toString().equals(new sessionmanager(getContext()).getuserdetail())) {
                                        bookingsecondtime = true;

                                    }


                                }
                            }


                            if (!bookingsecondtime) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
*/


            }


        });

        trackbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),trackbus.class);
                startActivity(intent);
            }
        });

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


        day_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                        Date dateobj = new Date(i, i1, i2 - 1);


                        selecteddate = String.valueOf(i2);
                        year_ = i;
                        month_ = i1;
                        date_ = i2;

                        selectedmonth = (String) DateFormat.format("MMM", dateobj);

                        selectedday = (String) DateFormat.format("EEE", dateobj);
                        day_Monthstr = selectedday + ",\n" + selectedmonth;
                        date.setText(String.valueOf(i2));
                        day_month.setText(day_Monthstr);
                    }
                }, year_, month_, date_);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        calenderimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                        Date dateobj = new Date(i, i1, i2 - 1);


                        currentdate[0] = String.valueOf(i2);
                        year_ = i;
                        month_ = i1;
                        date_ = i2;

                        currentmonth = (String) DateFormat.format("MMM", dateobj);

                        currentday = (String) DateFormat.format("EEE", dateobj);
                        day_Monthstr = currentday + ",\n" + currentmonth;
                        date.setText(currentdate[0]);
                        day_month.setText(day_Monthstr);
                    }
                }, year_, month_, date_);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                        Date dateobj = new Date(i, i1, i2 - 1);


                        currentdate[0] = String.valueOf(i2);
                        year_ = i;
                        month_ = i1;
                        date_ = i2;

                        currentmonth = (String) DateFormat.format("MMM", dateobj);

                        currentday = (String) DateFormat.format("EEE", dateobj);
                        day_Monthstr = currentday + ",\n" + currentmonth;
                        date.setText(currentdate[0]);
                        day_month.setText(day_Monthstr);
                    }
                }, year_, month_, date_);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("Are you sure you want to Exit ?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requireActivity().finish();
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
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return v;


    }

    void intenttoconfirmticket(Boolean bookingsecondtime) {
        if (bookingsecondtime) {
            Intent intent = new Intent(getContext(), Confirmticket.class);

            outbox.setError(null);
            outbox.setErrorEnabled(false);
           /* if (refnew != null && listener != null) {
                refnew.removeEventListener(listener);*/
            intent.putExtra("busnumber", busroutetext.getText().toString());
            startActivity(intent);
        }
    }

    /*private void checkalreadybooked() {

        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference("Booking");

        final LayoutInflater inflaterdialoge = requireActivity().getLayoutInflater();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                AlertDialog dialog = alert.create();

                alert.setView(inflaterdialoge.inflate(R.layout.relaxseatalreadybooked, null));


                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

                for (int i = 1; i <= 6; i++) {
                    for (int j = 1; j <= 2; j++) {

                        if (Objects.requireNonNull(snapshot.child(currentdate).child("SB1").child("L" + i + j).child("username").getValue()).toString().equals(new sessionmanager(requireContext()).getuserdetail())) {
                            bookingsecondtime = true;


                        }


                    }
                }

                for (int i = 1; i <= 6; i++) {
                    for (int j = 1; j <= 3; j++) {


                        if (Objects.requireNonNull(snapshot.child(currentdate).child("SB1").child("R" + i + j).child("username").getValue()).toString().equals(new sessionmanager(getContext()).getuserdetail())) {
                            bookingsecondtime = true;

                        }


                    }
                }


                if (!bookingsecondtime) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

}



