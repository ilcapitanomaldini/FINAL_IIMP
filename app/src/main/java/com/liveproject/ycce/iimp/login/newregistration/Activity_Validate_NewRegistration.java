package com.liveproject.ycce.iimp.login.newregistration;

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
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.networkservice.GetService;

/**
 * Created by Tiger on 07-10-2016.
 */
public class Activity_Validate_NewRegistration extends AppCompatActivity {

    String s_mobile;
    ProgressBar progressBar;
    EditText et_mobile;
    Toast toast;
    BroadcastReceiver otp_receiver, validmobileno;

    String URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressBar = (ProgressBar) findViewById(R.id.reg_pb);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("New Registration");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            Log.e("Exception", "onCreate: " + e.toString());
        }

        et_mobile = (EditText) findViewById(R.id.reg_et_mobile);

        et_mobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateMobileno();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            validateMobileno();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateMobileno() {
        s_mobile = et_mobile.getText().toString();

        // HINT : VALIDATION OF MOBILE NO.
        if (Validation.isValidMobile(s_mobile)) {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            URL = Constants.SITE_URL + Constants.OTP_URL + "?phone=" + s_mobile + "&isAlready=true";
            Intent intent = new Intent(getBaseContext(), GetService.class);
            intent.putExtra("URL", URL);
            intent.putExtra("NAME", "ValidMobileNo");
            getBaseContext().startService(intent);
        } else {
            toast.makeText(getBaseContext(), "Enter Mobile Number!!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        otp_receiver = new OTPReceiver();
        this.registerReceiver(otp_receiver, new IntentFilter("android.intent.action.OTPReceiver"));
        validmobileno = new ValidMobileNo();
        this.registerReceiver(validmobileno, new IntentFilter("android.intent.action.ValidMobileNo"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(otp_receiver);
        this.unregisterReceiver(validmobileno);
    }

    private class ValidMobileNo extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                if (response.equalsIgnoreCase("\"Invalid User\"")) {
                    //HINT : DATABASE ENTRY IN LOGIN TABLE.
                    DatabaseService.insertLogin(s_mobile);

                    URL = Constants.SITE_URL + Constants.OTP_URL + "?phone=" + s_mobile;
                    Intent intent1 = new Intent(getBaseContext(), GetService.class);
                    intent1.putExtra("URL", URL);
                    intent1.putExtra("NAME", "OTPReceiver");
                    getBaseContext().startService(intent1);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    toast.makeText(getBaseContext(), "Already registered mobile number.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class OTPReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                Intent i = new Intent(getBaseContext(), Activity_OTP_Verification_NewRegistration.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                startActivity(i);
                finish();
            }
        }
    }
}