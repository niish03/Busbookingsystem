package com.shreebrahmanitravels.app;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.awt.font.TextAttribute;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dashboard_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button aboutus,whyonlinebooking;
    ImageView menubtn;
    TextView headerrollnumber;


    DrawerLayout drawerLayout;
    private FragmentActivity myContext;

    public Dashboard_home() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard_home.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard_home newInstance(String param1, String param2) {
        Dashboard_home fragment = new Dashboard_home();
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

       /* final LayoutInflater factory = getLayoutInflater();

        final View textEntryView = factory.inflate(R.layout.menuheader, null);
        TextView usernamemenubtn;
        usernamemenubtn=(TextView) textEntryView.findViewById(R.id.usernameonmenubtn);
        usernamemenubtn.setText("Nooppppp");*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_dashboard_home, container, false);


        menubtn = v.findViewById(R.id.menubtn);
        whyonlinebooking= v.findViewById(R.id.WhyOnlineBookingbtn);
        aboutus = v.findViewById(R.id.aboutus);
        drawerLayout = v.findViewById(R.id.drawerlayout);



        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });







        NavigationView navigationView;
        navigationView = v.findViewById(R.id.sidenavview);
        navigationView.bringToFront();
        Menu menu= navigationView.getMenu();
        menu.findItem(R.id.seatmagaer).setVisible(false);

        sessionmanager sessionmanagerobj = new sessionmanager(getContext());
        try
{
    if(sessionmanagerobj.getuserdetail().equals("19IT105"))
{
    menu.findItem(R.id.seatmagaer).setVisible(true);
}
    if(sessionmanagerobj.getuserdetail().equals("Guest user"))
        menu.findItem(R.id.updatepassword).setVisible(false);

}catch (Exception e)
        {
            System.out.println(e);
        }
        View headerview = navigationView.getHeaderView(0);
        headerrollnumber= headerview.findViewById(R.id.usernameonmenubtn);
                headerrollnumber.setText(sessionmanagerobj.getuserdetail());
                navigationView.


                setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    Fragment fragment = null;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.booking:
                                fragment = new dashboard_booking();
                                break;
                            case R.id.profile:
                                fragment = new dashboard_profile();
                                break;
                            case R.id.updatepassword:
                                Intent intentnew = new Intent(getActivity(), updatepassword.class);
                                startActivity(intentnew);
                                break;
                            case R.id.cotactus:
                                Intent intent = new Intent(getActivity(), Aboutus.class);
                                startActivity(intent);
                                break;
                            case R.id.developer:
                                Intent intent1 = new Intent(getActivity(), contact_developer.class);
                                startActivity(intent1);
                                break;
                            case R.id.feedbaack:
                                Intent intent2 = new Intent(getActivity(), feedback.class);
                                startActivity(intent2);
                                break;
                            case R.id.logout:
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setMessage("Are you sure you want to Log out ?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        sessionmanager sessionmanager_obj = new sessionmanager(getContext());
                                        sessionmanager_obj.logoutuserfromsession();

                                        Intent intent3 = new Intent(getContext(), login.class);
                                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent3);
                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                                break;
                            case R.id.seatmagaer:
                               Intent intent4 = new Intent(getContext(),seatManagement.class);
                               startActivity(intent4);
                                break;
                        }

                        drawerLayout.closeDrawers();

                        //welcomePage welcomePageobj = new welcomePage();
                        /*
welcomePageobj.chipBooingselected();
*/

                        if (fragment != null) {
                            drawerLayout.closeDrawers();

                            //View inflatedview = getLayoutInflater().inflate(R.layout.activity_welcome_page,null);
                            //ChipNavigationBar chipNavigationBar = inflatedview.findViewById(R.id.bottom_navigation_menu);
                            // chipNavigationBar.setItemSelected(R.id.profiletab,true);
                            //  chipNavigationBar.collapse();
//welcomePageobj.chipBooingselected();
                            myContext.getSupportFragmentManager().beginTransaction().replace(R.id.fragmant_container, fragment).commit();


                            //welcomePageobj.chipNavigationBar.setItemSelected(R.id.BookingTab,true);
                        }
                        return true;


                    }

                });


        whyonlinebooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), com.shreebrahmanitravels.app.whyonlinebooking.class);
                startActivity(intent);
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Aboutus.class);
                startActivity(intent);
            }
        });



        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.closeDrawers();
                }
                else {
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
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);




        //requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()

        return v;

    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }




}