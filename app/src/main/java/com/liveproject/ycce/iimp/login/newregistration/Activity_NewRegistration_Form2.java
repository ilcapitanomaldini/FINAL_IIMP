package com.liveproject.ycce.iimp.login.newregistration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.liveproject.ycce.iimp.JSONService;
import com.liveproject.ycce.iimp.Member;
import com.liveproject.ycce.iimp.MemberAddress;
import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.adapters.Adapter_SingleSelect_Handler;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.messaging.Activity_Home_Messaging;
import com.liveproject.ycce.iimp.networkservice.GetService;
import com.liveproject.ycce.iimp.networkservice.PostService;
import com.liveproject.ycce.iimp.adapters.headers.Header_SingleSelect_Handler;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tiger on 11-03-2017.
 */

public class Activity_NewRegistration_Form2 extends AppCompatActivity {


    EditText et_addr1, et_street, et_locality, et_city, et_pincode, et_state, et_country;
    Toast toast;
    ProgressBar progressBar;
    private Adapter_SingleSelect_Handler adapter_singleSelect_handler;

    String s_id, s_division_id, s_handler_id, s_handler_firstname, s_handler_lastname, s_status;
    String s_addr1, s_street, s_locality, s_city, s_pincode, s_state, s_country;
    String URL;
    List<MemberPersonalInfo> memberPersonalInfoList = new ArrayList<>();

    MemberPersonalInfo memberPersonalInfo;
    MemberAddress memberaddress;

    BroadcastReceiver membercreated, pincodeloader, handlerlist;

    int formLoaded = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        memberPersonalInfo = getIntent().getParcelableExtra("MemberPersonalInfo");
        s_division_id = getIntent().getStringExtra("DivisionID");

        setContentView(R.layout.activity_address_detail);
        progressBar = (ProgressBar) findViewById(R.id.ad_pb);

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("Registration");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onClick: After Layout Change ", e.fillInStackTrace());
        }

        et_addr1 = (EditText) findViewById(R.id.ad_et_addr1);
        et_street = (EditText) findViewById(R.id.ad_et_street);
        et_locality = (EditText) findViewById(R.id.ad_et_locality);
        et_city = (EditText) findViewById(R.id.ad_et_city);
        et_pincode = (EditText) findViewById(R.id.ad_et_pincode);
        et_state = (EditText) findViewById(R.id.ad_et_state);
        et_country = (EditText) findViewById(R.id.ad_et_country);

        URL = Constants.SITE_URL + Constants.SEARCHHANDLER_URL + "?divisionid=" + s_division_id + "&designation=" + memberPersonalInfo.getDesignation();
        Intent intent = new Intent(getBaseContext(), GetService.class);
        intent.putExtra("URL", URL);
        intent.putExtra("NAME", "HandlerList");
        getBaseContext().startService(intent);

        //HINT : FOR DYNAMIC LOADING OF STATE AND COUNTRY.
        et_pincode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    s_pincode = et_pincode.getText().toString();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    URL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + s_pincode + "&sensor=true";
                    Intent intent = new Intent(getBaseContext(), GetService.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("NAME", "PincodeLoader");
                    getBaseContext().startService(intent);
                    return true;
                }
                return false;
            }
        });

    }

    private class HandlerList extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                JSONService jsonService = new JSONService(response);
                memberPersonalInfoList = jsonService.parseListOfMembers();

                Header_SingleSelect_Handler header1 = new Header_SingleSelect_Handler("Handler", memberPersonalInfoList);
                List<Header_SingleSelect_Handler> header = Arrays.asList(header1);

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ad_rv_handler);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                adapter_singleSelect_handler = new Adapter_SingleSelect_Handler(header);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter_singleSelect_handler);
                formLoaded++;
                if (formLoaded >= 1) {
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    formLoaded = 0;
                }
            }
        }
    }

    private class PincodeLoader extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String Status = jsonObj.getString("status");
                    if (Status.equalsIgnoreCase("OK")) {
                        JSONArray Results = jsonObj.getJSONArray("results");
                        JSONObject zero = Results.getJSONObject(0);
                        JSONArray address_components = zero.getJSONArray("address_components");

                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject zero2 = address_components.getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            String Type = mtypes.getString(0);

                            if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                                if (Type.equalsIgnoreCase("locality")) {
                                    s_city = long_name;
                                    et_city.setText(s_city);
                                } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                    s_state = long_name;
                                    et_state.setText(s_state);
                                } else if (Type.equalsIgnoreCase("country")) {
                                    s_country = long_name;
                                    et_country.setText(s_country);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // HINT : COLLECT DATA FROM ALL FIELDS OF ADDRESS DETAILS.
    private void getAddressFields() {
        s_addr1 = et_addr1.getText().toString();
        s_street = et_street.getText().toString();
        s_locality = et_locality.getText().toString();
        s_city = et_city.getText().toString();
        s_pincode = et_pincode.getText().toString();
        s_state = et_state.getText().toString();
        s_country = et_country.getText().toString();
        getHandler();
    }

    private void getHandler() {
        List<CheckedExpandableGroup> checkedExpandableGroup = (List<CheckedExpandableGroup>) adapter_singleSelect_handler.getGroups();
        for (int i = 0; i < checkedExpandableGroup.get(0).getItemCount(); i++) {
            if (checkedExpandableGroup.get(0).selectedChildren[i] == true) {
                s_handler_id = memberPersonalInfoList.get(i).getId();
                s_handler_firstname = memberPersonalInfoList.get(i).getFirstname();
                s_handler_lastname = memberPersonalInfoList.get(i).getLastname();
                break;
            }
        }
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

        if(id == android.R.id.home){
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            getAddressFields();
            if (!(Validation.isEmpty(s_handler_id) || Validation.isEmpty(s_handler_firstname) || Validation.isEmpty(s_handler_lastname) || Validation.isEmpty(s_addr1) || Validation.isEmpty(s_street) || Validation.isEmpty(s_locality) || Validation.isEmpty(s_city) || Validation.isEmpty(s_pincode)
                    || Validation.isEmpty(s_state) || Validation.isEmpty(s_country))) {

                // HINT : SEND TO CLOUD.
                final JSONObject params = new JSONObject();
                final JSONArray jsonRoles = new JSONArray();

                try {
                    for (int i = 0; i < memberPersonalInfo.getRoles().size(); i++) {
                        jsonRoles.put(i, memberPersonalInfo.getRoles().get(i));
                    }

                    params.put("fname", memberPersonalInfo.getFirstname());
                    params.put("lname", memberPersonalInfo.getLastname());
                    params.put("email", memberPersonalInfo.getEmailid());
                    params.put("mobile", memberPersonalInfo.getMobileno());
                    params.put("gender", memberPersonalInfo.getGender());
                    params.put("dob", memberPersonalInfo.getDob());
                    params.put("doj", memberPersonalInfo.getDoj());
                    params.put("designation", memberPersonalInfo.getDesignation());
                    params.put("division", s_division_id);
                    params.put("roles", jsonRoles);
                    params.put("handler", s_handler_id);

                    params.put("addressline1", s_addr1);
                    params.put("street", s_street);
                    params.put("locality", s_locality);
                    params.put("city", s_city);
                    params.put("pin", s_pincode);
                    params.put("state", s_state);
                    params.put("country", s_country);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                URL = Constants.SITE_URL + Constants.REGISTER_URL;
                Intent intent = new Intent(getBaseContext(), PostService.class);
                intent.putExtra("URL", URL);
                intent.putExtra("NAME", "MemberCreated");
                Log.d("JSONOBJECT", "onOptionsItemSelected: " + params);
                intent.putExtra("JSONOBJECT", params.toString());
                getBaseContext().startService(intent);

            } else {
                toast.makeText(getBaseContext(), "Missing Field(s) are detected. Fill all blanks!!!", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class MemberCreated extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                if (response.equalsIgnoreCase("\"false\"")) {
                    toast.makeText(getBaseContext(), "Something went worng. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    s_id = response.substring(1, response.length() - 1);
                    s_status = "new";
                    memberPersonalInfo = new MemberPersonalInfo(s_id, memberPersonalInfo.getFirstname(), memberPersonalInfo.getLastname(), memberPersonalInfo.getEmailid(), memberPersonalInfo.getMobileno(), memberPersonalInfo.getGender(), memberPersonalInfo.getDob(), memberPersonalInfo.getDoj(), memberPersonalInfo.getDesignation(), memberPersonalInfo.getDivision(), s_handler_id, s_handler_firstname, s_handler_lastname, memberPersonalInfo.getRoles(), s_status);
                    memberaddress = new MemberAddress(s_addr1, s_street, s_locality, s_city, s_state, s_country, s_pincode);
                    Member m = new Member(memberPersonalInfo, memberaddress);

                    if (DatabaseService.insertUserProfile(m) && DatabaseService.insertRoles(memberPersonalInfo.getRoles(), memberPersonalInfo.getId())) {
                        // HINT : DATABASE ENTRY OF STATUS VERIFIED.
                        DatabaseService.statusUpdate(Constants.STATUS[4]);
                        Intent i = new Intent(getBaseContext(), Activity_Home_Messaging.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pincodeloader = new PincodeLoader();
        this.registerReceiver(pincodeloader, new IntentFilter("android.intent.action.PincodeLoader"));

        membercreated = new MemberCreated();
        this.registerReceiver(membercreated, new IntentFilter("android.intent.action.MemberCreated"));

        handlerlist = new HandlerList();
        this.registerReceiver(handlerlist, new IntentFilter("android.intent.action.HandlerList"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(pincodeloader);
        this.unregisterReceiver(membercreated);
        this.unregisterReceiver(handlerlist);
    }

}
