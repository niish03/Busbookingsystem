package com.shreebrahmanitravels.app;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class connectionmanager {

    public int flag;
    AlertDialog dialog;


    public void checkConnection(final Context context, final Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        alert.setView(inflater.inflate(R.layout.interneterror,null));
        dialog = alert.create();


        check_innerconnection(context,activity);

    }

    private void check_innerconnection(final Context context, final Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {


            final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations =android.R.style.Animation_Dialog;

            if(info== null || !info.isConnected() || !info.isAvailable() )
            {
                flag=1;



                dialog.show();
                Button tryagainbtn = dialog.findViewById(R.id.retrybtn);
                ImageView canclebtn = dialog.findViewById(R.id.canclebtnoninterneterror);
                tryagainbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(info== null || !info.isConnected() || !info.isAvailable() )
                        {
                            dialog.cancel();
                            check_innerconnection(context,activity);
                        }
                        else
                            dialog.cancel();
                    }
                });

                canclebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

            }
            else{


                flag=0;
            }
        }

    }




}
