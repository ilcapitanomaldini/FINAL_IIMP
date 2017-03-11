package com.liveproject.ycce.iimp.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.login.alreadyregistered.Activity_OTP_Verification;
import com.liveproject.ycce.iimp.login.newregistration.Activity_Validate_NewRegistration;
import com.liveproject.ycce.iimp.networkservice.GetService;
import com.liveproject.ycce.iimp.volleyservice.VolleySingleton;

/**
 * Created by user on 9/4/2016.
 */
public class Activity_FrontLogin extends AppCompatActivity {
    Button btn_login;
    TextView tv_register;
    String s_mobile;
    EditText et_mobile;
    ProgressBar progressBar;
    BroadcastReceiver otp_receiver;
    Toast toast;

    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //HINT : INITIALIZE THE DATABASE.
        DatabaseService.init(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_login);
        try {
            TextView tv_title = (TextView) findViewById(R.id.ffl_tv_title);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/colonna_mt.ttf");
            tv_title.setTypeface(custom_font);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }
        btn_login = (Button) findViewById(R.id.ffl_btn_login);
        tv_register = (TextView) findViewById(R.id.ffl_tv_register);
        et_mobile = (EditText) findViewById(R.id.ffl_et_mobile);
        progressBar = (ProgressBar) findViewById(R.id.ffl_pb);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openEnterProfile = new Intent(getBaseContext(), Activity_Validate_NewRegistration.class);
                startActivity(openEnterProfile);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                s_mobile = et_mobile.getText().toString();
                if (Validation.isValidMobile(s_mobile)) {
                    //HINT : DATABASE ENTRY IN LOGIN TABLE.
                    DatabaseService.insertLogin(s_mobile);

                    URL = Constants.SITE_URL + Constants.OTP_URL + "?phone=" + s_mobile + "&isAlready=true";
                    Intent intent = new Intent(getBaseContext(), GetService.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("NAME", "OTPReceiver");
                    getBaseContext().startService(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Enter Mobile Number!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        otp_receiver = new OTPReceiver();
        this.registerReceiver(otp_receiver, new IntentFilter("android.intent.action.OTPReceiver"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(otp_receiver);
    }

    private class OTPReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (error != null) {
                toast.makeText(getBaseContext(), "Please Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else if (response != null) {
                if (response.equalsIgnoreCase("\"Invalid User\""))
                    Toast.makeText(getBaseContext(), "Invalid User.", Toast.LENGTH_LONG).show();
                else {
                    Intent i = new Intent(getBaseContext(), Activity_OTP_Verification.class);
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        }
    }
}