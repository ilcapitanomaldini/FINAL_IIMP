package com.liveproject.ycce.iimp.login.alreadyregistered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.JSONService;
import com.liveproject.ycce.iimp.Member;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.messaging.Activity_Home_Messaging;
import com.liveproject.ycce.iimp.networkservice.GetService;

/**
 * Created by Tiger on 07-09-2016.
 */
public class Activity_OTP_Verification extends AppCompatActivity {

    EditText et_otp;
    Toast toast;
    String s_mobileno, s_otp;
    String URL;

    BroadcastReceiver otp_validation, user_profile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("OTP Verification");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }
        et_otp = (EditText) findViewById(R.id.otp_et_otp);

        et_otp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateOTP();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        otp_validation = new OTPValidation();
        this.registerReceiver(otp_validation, new IntentFilter("android.intent.action.OTPValidation"));
        user_profile = new UserProfile();
        this.registerReceiver(user_profile, new IntentFilter("android.intent.action.UserProfile"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(otp_validation);
        this.unregisterReceiver(user_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            validateOTP();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateOTP() {
        s_otp = et_otp.getText().toString();
        //HINT : VALDIATE OTP.
        if (Validation.isEmpty(s_otp))
            Toast.makeText(getBaseContext(), "Enter OTP", Toast.LENGTH_LONG).show();
        else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            //HINT : DATABASE FETCHING FROM LOGIN TABLE.
            s_mobileno = DatabaseService.fetchMobileNo();
            URL = Constants.SITE_URL + Constants.OTP_URL + "?phone=" + s_mobileno + "&otp=" + s_otp + "&isAlready=true";
            Intent intent = new Intent(getBaseContext(), GetService.class);
            intent.putExtra("URL", URL);
            intent.putExtra("NAME", "OTPValidation");
            getBaseContext().startService(intent);
        }
    }

    private class OTPValidation extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (error != null) {
                toast.makeText(getBaseContext(), "Please Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else if (response != null) {
                if (response.equalsIgnoreCase("\"valid\"")) {
                    // HINT : DATABASE ENTRY OF STATUS VERIFIED.
                    DatabaseService.statusUpdate(Constants.STATUS[4]);

                    URL = Constants.SITE_URL + Constants.SEARCHCONTACT_URL + "?mobilephone=" + s_mobileno;
                    Intent intent1 = new Intent(getBaseContext(), GetService.class);
                    intent1.putExtra("URL", URL);
                    intent1.putExtra("NAME", "UserProfile");
                    getBaseContext().startService(intent1);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getBaseContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class UserProfile extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");
            if (error != null) {
                toast.makeText(getBaseContext(), "Please Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else if (response != null) {
                JSONService jsonService = new JSONService(response);
                Member member = jsonService.parseSearchByID();
                if (DatabaseService.insertUserProfile(member) &&
                        DatabaseService.insertRoles(member.getMpi().getRoles(), member.getMpi().getId())) {

                    if(member.getMemberPersonalInfo().getStatus().equalsIgnoreCase(Constants.USERSTATUS[1])){
                        Intent i = new Intent(getBaseContext(), Activity_Home_Messaging.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        toast.makeText(getBaseContext(), "Successful", Toast.LENGTH_SHORT).show();
                    }else if(member.getMemberPersonalInfo().getStatus().equalsIgnoreCase(Constants.USERSTATUS[0])){
                        toast.makeText(getBaseContext(),"Waiting for your handler to accept the request.",Toast.LENGTH_LONG).show();
                        finish();
                    }else if(member.getMemberPersonalInfo().getStatus().equalsIgnoreCase(Constants.USERSTATUS[2])){
                        toast.makeText(getBaseContext(),"Your status is blocked.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        }
    }
}