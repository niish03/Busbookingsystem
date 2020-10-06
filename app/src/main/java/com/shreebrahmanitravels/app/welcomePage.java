package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.BreakIterator;

public class welcomePage extends AppCompatActivity {

    public ChipNavigationBar chipNavigationBar;
    protected Boolean tohome = false;
    String usernameonprofile;

       @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        chipNavigationBar=findViewById(R.id.bottom_navigation_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_navigation_home,true);




        getSupportFragmentManager().beginTransaction().replace(R.id.fragmant_container,new Dashboard_home()).commit();
        bottommenu();



    }

    /*public void chipBooingselected()
    {

        chipNavigationBar=findViewById(R.id.bottom_navigation_menu);
        chipNavigationBar.setItemSelected(R.id.BookingTab,true);

    }
*/

    private void bottommenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment=null;

                switch (i){
                    case R.id.bottom_navigation_home:
                        fragment= new Dashboard_home();
                        break;

                    case R.id.BookingTab:
                        fragment= new dashboard_booking();
                        break;

                    case R.id.profiletab:
                        fragment= new dashboard_profile();
                        break;
                }



                    dashboard_profile fragobj = new dashboard_profile();
                    Bundle bundle = new Bundle();
                    bundle.putString("edttext", "From Activity");

                    fragobj.setArguments(bundle);



                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_fade_enter,R.anim.fragment_fade_exit).replace(R.id.fragmant_container,fragment).commit();
            }
        });
    }


}



