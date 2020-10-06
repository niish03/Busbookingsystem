package com.shreebrahmanitravels.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class sessionmanager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String shared_pref_name="session";
    public String key_username="username";
    String key_password="password";
    public String usernameonprofile;

    sessionmanager()
    {
    }
    public sessionmanager(Context context){
        sharedPreferences = context.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void createloginsession(String username, String password){

        editor.putBoolean(shared_pref_name,true);

        editor.putString(key_username,username);
        editor.putString(key_password,password);
        editor.commit();
    }

public void setlastbookeddate(String date)
{

    editor.putString("Last date",date);
    editor.commit();
}
public String getlastbookeddate()
{
    return sharedPreferences.getString("Last date",null);
}

    public String getuserdetail(){


        return sharedPreferences.getString(key_username, null);
    }


    /* public boolean checklogin()
     {
         if(sharedPreferences.getBoolean(shared_pref_name,false)){
             return true;
         }
         else
             return false;
     }
 */
    public void logoutuserfromsession(){
        editor.putString(key_username,null);
        editor.commit();
    }
}
