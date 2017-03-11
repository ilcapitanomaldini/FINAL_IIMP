package com.liveproject.ycce.iimp.login.newregistration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.networkservice.GetService;

/**
 * Created by Tiger on 07-09-2016.
 */
public class Activity_OTP_Verification_NewRegistration extends AppCompatActivity {

    EditText et_otp;
    String s_mobileno, s_otp;
    Toast toast;

    BroadcastReceiver otp_receiver;

    String URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_title.setText("OTP Verification");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        otp_receiver = new OTPReceiver();
        this.registerReceiver(otp_receiver, new IntentFilter("android.intent.action.OTPReceiver"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(otp_receiver);
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
            toast.makeText(getBaseContext(), "Enter OTP", Toast.LENGTH_SHORT).show();
        else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            //HINT : DATABASE FETCHING FROM LOGIN TABLE.
            s_mobileno = DatabaseService.fetchMobileNo();

            URL = Constants.SITE_URL + Constants.OTP_URL + "?phone=" + s_mobileno + "&otp=" + s_otp;
            Intent intent = new Intent(getBaseContext(), GetService.class);
            intent.putExtra("URL", URL);
            intent.putExtra("NAME", "OTPReceiver");
            getBaseContext().startService(intent);
        }
    }

    private class OTPReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                if (response.equalsIgnoreCase("\"valid\"")) {
                    // HINT : DATABASE ENTRY OF STATUS VERIFIED.
                    DatabaseService.statusUpdate(Constants.STATUS[3]);

                    Intent i = new Intent(getBaseContext(), Activity_NewRegistration_Form1.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else {
                    toast.makeText(getBaseContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}