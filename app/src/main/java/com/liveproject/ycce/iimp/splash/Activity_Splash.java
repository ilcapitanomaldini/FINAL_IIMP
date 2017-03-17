package com.liveproject.ycce.iimp.splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.login.Activity_FrontLogin;
import com.liveproject.ycce.iimp.login.newregistration.Activity_NewRegistration_Form1;
import com.liveproject.ycce.iimp.messaging.Activity_Home_Messaging;
import com.liveproject.ycce.iimp.networkservice.GetService;

/**
 * Created by Tiger on 10-03-2017.
 */

public class Activity_Splash extends AppCompatActivity {
    private String s_id, s_status;
    private String URL;
    Toast toast;

    BroadcastReceiver user_status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            TextView tv_title = (TextView) findViewById(R.id.splash_tv_iimp);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/colonna_mt.ttf");
            tv_title.setTypeface(custom_font);
        } catch (Exception e) {
            Log.d("TAG", "onCreate: " + e.toString());
        }

        //HINT : INITIALIZE THE DATABASE.
        DatabaseService.init(this);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (Constants.STATUS[4].equals(DatabaseService.fetchStatus())) {
                        s_id = DatabaseService.fetchID();
                        s_status = DatabaseService.fetchUserStatus(s_id);

                        if ((Constants.USERSTATUS[0].equalsIgnoreCase(s_status)
                                || Constants.USERSTATUS[2].equalsIgnoreCase(s_status))) {
                            URL = Constants.SITE_URL + Constants.GETUSERSTATUS_URL + "?cid=" + s_id;
                            Intent intent = new Intent(getBaseContext(), GetService.class);
                            intent.putExtra("URL", URL);
                            intent.putExtra("NAME", "UserStatus");
                            getBaseContext().startService(intent);
                        } else if (Constants.USERSTATUS[1].equalsIgnoreCase(s_status)) {
                            Intent intent = new Intent(getBaseContext(), Activity_Home_Messaging.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    }else if (Constants.STATUS[3].equals(DatabaseService.fetchStatus())) {
                        Intent intent = new Intent(getBaseContext(), Activity_NewRegistration_Form1.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getBaseContext(), Activity_FrontLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timer.start();

    }


    private class UserStatus extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
                finish();
            } else if (response != null) {
                s_status = response.substring(1, response.length() - 1);
                if (DatabaseService.updateUserStatus(s_id, s_status)) {
                    Intent intent1 = new Intent(getBaseContext(), Activity_Home_Messaging.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  DatabaseService.insertDateTimeinLogin();
                    startActivity(intent1);
                    finish();
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        user_status = new UserStatus();
        this.registerReceiver(user_status, new IntentFilter("android.intent.action.UserStatus"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(user_status);
    }
}
