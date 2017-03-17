package com.liveproject.ycce.iimp.userprofile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liveproject.ycce.iimp.DesignationService;
import com.liveproject.ycce.iimp.Division;
import com.liveproject.ycce.iimp.DivisionService;
import com.liveproject.ycce.iimp.JSONService;
import com.liveproject.ycce.iimp.Member;
import com.liveproject.ycce.iimp.MemberAddress;
import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Roles;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.adapters.Adapter_MultiCheck_Roles;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.constants.Key;
import com.liveproject.ycce.iimp.adapters.headers.Header_MultiCheck_Roles;
import com.liveproject.ycce.iimp.volleyservice.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tiger on 27-08-2016.
 */
public class Activity_EditProfile extends AppCompatActivity {

    EditText et_firstname, et_lastname, et_emailid, et_mobileno, et_dob, et_doj;
    String s_id, s_firstname, s_lastname, s_emailid, s_gender, s_dob, s_doj, s_designation, s_division, s_division_id;
    String URL;

    Spinner spinner_designation, spinner_division;
    RadioGroup radioGenderGroup;
    RadioButton radioGenderButton;
    LinearLayout l1, l2, l3;
    Toast toast;
    ProgressBar progressBar;

    Calendar calendar;
    int year, month, day;

    EditText et_addr1, et_street, et_locality, et_city, et_pincode, et_state, et_country;
    String s_addr1, s_street, s_locality, s_city, s_pincode, s_state, s_country;

    ArrayList<String> designationArray = new ArrayList<>();
    ArrayList<String> divisionArray = new ArrayList<>();
    List<Division> divisionList = new ArrayList<>();
    List<Roles> rolesList = new ArrayList<>();
    List<String> roles = new ArrayList<>();

    private Adapter_MultiCheck_Roles adapter_multiCheck_roles;

    Member member;
    MemberPersonalInfo memberPersonalInfo;
    MemberAddress memberAddress;

    int formLoaded = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_title.setText("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        member = getIntent().getParcelableExtra("Member");
        memberPersonalInfo = new MemberPersonalInfo(member.getMemberPersonalInfo());
        memberAddress = new MemberAddress(member.getMemberAddress());

        progressBar = (ProgressBar) findViewById(R.id.pi_pb);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        et_firstname = (EditText) findViewById(R.id.pi_et_firstname);
        et_lastname = (EditText) findViewById(R.id.pi_et_lastname);
        et_emailid = (EditText) findViewById(R.id.pi_et_emailid);
        et_mobileno = (EditText) findViewById(R.id.pi_et_mobileno);
        et_dob = (EditText) findViewById(R.id.pi_et_dob);
        et_doj = (EditText) findViewById(R.id.pi_et_doj);
        l1 = (LinearLayout) findViewById(R.id.pi_app_bar);

        l2 = (LinearLayout) findViewById(R.id.ad_app_bar);
        l3 = (LinearLayout) findViewById(R.id.ad_ll_handler);
        et_addr1 = (EditText) findViewById(R.id.ad_et_addr1);
        et_street = (EditText) findViewById(R.id.ad_et_street);
        et_locality = (EditText) findViewById(R.id.ad_et_locality);
        et_city = (EditText) findViewById(R.id.ad_et_city);
        et_pincode = (EditText) findViewById(R.id.ad_et_pincode);
        et_state = (EditText) findViewById(R.id.ad_et_state);
        et_country = (EditText) findViewById(R.id.ad_et_country);

        // HINT : REMOVE MOBILE EDIT TEXT AND BUTTON.
        {
            et_mobileno.setVisibility(View.GONE);
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);
        }

        // HINT : RENAME EVERYTHING.
        {
            et_firstname.setText(memberPersonalInfo.getFirstname());
            et_lastname.setText(memberPersonalInfo.getLastname());
            et_emailid.setText(memberPersonalInfo.getEmailid());
            et_dob.setText(memberPersonalInfo.getDob());
            et_doj.setText(memberPersonalInfo.getDoj());

            et_addr1.setText(memberAddress.getAddrline());
            et_street.setText(memberAddress.getStreet());
            et_locality.setText(memberAddress.getLocality());
            et_city.setText(memberAddress.getCity());
            et_pincode.setText(memberAddress.getPincode());
            et_state.setText(memberAddress.getState());
            et_country.setText(memberAddress.getCountry());
        }
        {
            designationArray.add(0, memberPersonalInfo.getDesignation());
            spinner_designation = (Spinner) findViewById(R.id.pi_dd_designation);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, designationArray);
            spinner_designation.setAdapter(adapter);

            DesignationService designationRequest = new DesignationService(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONService jsonService = new JSONService(response);
                            designationArray = jsonService.parseDesignation();
                            showDesignation();
                            formLoaded++;
                            if (formLoaded >= 3) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                formLoaded = 0;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            VolleySingleton.getRequestQueue().add(designationRequest);
        }
        {
            divisionArray.add(0, memberPersonalInfo.getDivision());
            spinner_division = (Spinner) findViewById(R.id.pi_dd_division);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, divisionArray);
            spinner_division.setAdapter(adapter);
            final DivisionService divisonRequest = new DivisionService(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONService jsonService = new JSONService(response);
                            divisionList = jsonService.parseDivision();
                            for (int i = 0; i < divisionList.size(); i++) {
                                divisionArray.add(i, divisionList.get(i).getName());
                            }
                            showDivision();
                            formLoaded++;
                            if (formLoaded >= 3) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                formLoaded = 0;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            toast.makeText(getBaseContext(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    });
            divisonRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 4, 1));
            VolleySingleton.getRequestQueue().add(divisonRequest);
        }
        {         // HINT : DISPLAY ROLES.
            URL = Constants.SITE_URL + Constants.ROLE_URL;
            Log.d("Role", "URL");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONService jsonService = new JSONService(response);
                    rolesList = jsonService.parseRolesForMultiSelect();
                    Header_MultiCheck_Roles header1 = new Header_MultiCheck_Roles("Roles", rolesList);
                    List<Header_MultiCheck_Roles> header = Arrays.asList(header1);

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pi_rv_roles);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());

                    adapter_multiCheck_roles = new Adapter_MultiCheck_Roles(header);
                    try {
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter_multiCheck_roles);
                        formLoaded++;
                        if (formLoaded >= 3) {
                            progressBar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            formLoaded = 0;
                        }
                    } catch (NullPointerException e) {
                        Log.e("RecylcerView", "onResponse: " + e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    toast.makeText(getBaseContext(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 4, 1));
            VolleySingleton.getRequestQueue().add(stringRequest);
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                showDialog(999);
            }
        });

        et_doj.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                showDialog(998);
            }
        });

        //HINT : FOR DYNAMIC LOADING OF STATE AND COUNTRY.
        et_pincode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    s_pincode = et_pincode.getText().toString();

                    URL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + s_pincode + "&sensor=true";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            toast.makeText(getBaseContext(), "Please check your internet connection!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    VolleySingleton.getRequestQueue().add(stringRequest);
                    return true;
                }
                return false;
            }
        });
    }

    private void getFields() {
        s_firstname = et_firstname.getText().toString();
        s_lastname = et_lastname.getText().toString();
        s_emailid = et_emailid.getText().toString();
        s_doj = et_doj.getText().toString();
        s_dob = et_dob.getText().toString();

        getGender();

        s_addr1 = et_addr1.getText().toString();
        s_street = et_street.getText().toString();
        s_locality = et_locality.getText().toString();
        s_city = et_city.getText().toString();
        s_pincode = et_pincode.getText().toString();
        s_state = et_state.getText().toString();
        s_country = et_country.getText().toString();
    }

    // HINTS : COLLECT GENDER FROM RADIO GROUP.
    private void getGender() {
        try {
            radioGenderGroup = (RadioGroup) findViewById(R.id.pi_rg_gender);
            radioGenderButton = (RadioButton) findViewById(radioGenderGroup.getCheckedRadioButtonId());
            s_gender = radioGenderButton.getText().toString();
        } catch (Exception e) {
            toast.makeText(getBaseContext(), "Select the Gender!!!", Toast.LENGTH_SHORT).show();
        }
    }

    // HINTS : DISPLAY DESIGNATION IN SPINNER.
    private void showDesignation() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, designationArray);
        spinner_designation.setAdapter(adapter);
        spinner_designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView designation = (TextView) view;
                s_designation = designation.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDOBListener, year, month, day);
        } else if (id == 998) {
            return new DatePickerDialog(this, myDOJListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDOBListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            et_dob.setText((arg2 + 1) + "/" + arg3 + "/" + arg1);
        }
    };
    private DatePickerDialog.OnDateSetListener myDOJListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            et_doj.setText((arg2 + 1) + "/" + arg3 + "/" + arg1);
        }
    };


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

            // HINTS : COLLECT DATA FROM ALL FIELDS.
            getFields();

            if (!(Validation.isEmpty(s_firstname) || Validation.isEmpty(s_lastname) || Validation.isEmpty(s_emailid) || Validation.isEmpty(s_dob) || Validation.isEmpty(s_doj) ||
                    Validation.isEmpty(s_gender) || Validation.isEmpty(s_addr1) || Validation.isEmpty(s_street) || Validation.isEmpty(s_locality) || Validation.isEmpty(s_city) || Validation.isEmpty(s_pincode)
                    || Validation.isEmpty(s_state) || Validation.isEmpty(s_country))) {

                if (!Validation.isValidName(s_firstname))
                    toast.makeText(getBaseContext(), "Enter valid First Name.", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidName(s_lastname))
                    toast.makeText(getBaseContext(), "Enter valid Last Name.", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidEmail(s_emailid))
                    toast.makeText(getBaseContext(), "Enter valid Email ID.", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidDOB(s_dob))
                    toast.makeText(getBaseContext(), "Enter valid Date of Birth.", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidDesignation(s_designation))
                    toast.makeText(getBaseContext(), "Choose Designation.", Toast.LENGTH_SHORT).show();
                else {
                    if (memberPersonalInfo.getFirstname().equalsIgnoreCase(s_firstname) && memberPersonalInfo.getLastname().equalsIgnoreCase(s_lastname) &&
                            memberPersonalInfo.getEmailid().equalsIgnoreCase(s_emailid) && memberPersonalInfo.getDob().equalsIgnoreCase(s_dob) &&
                            memberPersonalInfo.getDoj().equalsIgnoreCase(s_doj) && memberAddress.getAddrline().equalsIgnoreCase(s_addr1) &&
                            memberAddress.getStreet().equalsIgnoreCase(s_street) && memberAddress.getLocality().equalsIgnoreCase(s_locality) &&
                            memberAddress.getCity().equalsIgnoreCase(s_city) && memberAddress.getPincode().equalsIgnoreCase(s_pincode) &&
                            memberAddress.getState().equalsIgnoreCase(s_state) && memberAddress.getCountry().equalsIgnoreCase(s_country)) {
                        toast.makeText(getBaseContext(), "No changes made.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent("com.liveproject.persi.ycce.iimp.USER_PROFILE");
                        startActivity(i);
                    } else {
                        final JSONObject params = new JSONObject();
                        try {
                            params.put(Key.FIRST_NAME, s_firstname);
                            params.put(Key.LAST_NAME, s_lastname);
                            params.put(Key.EMAIL, s_emailid);
                            params.put(Key.MOBILE, memberPersonalInfo.getMobileno());
                            params.put(Key.GENDER, s_gender);
                            params.put(Key.DOB, s_dob);
                            params.put(Key.DOJ, s_doj);
                            params.put(Key.DESIGNATION, s_designation);
                            params.put(Key.ADDRESS_LINE_1, s_addr1);
                            params.put(Key.STREET, s_street);
                            params.put(Key.LOCALITY, s_locality);
                            params.put(Key.CITY, s_city);
                            params.put(Key.PINCODE, s_pincode);
                            params.put(Key.STATE, s_state);
                            params.put(Key.COUNTRY, s_country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        StringRequest post = new StringRequest(Request.Method.POST, Constants.SITE_URL + Constants.REGISTER_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        toast.makeText(getBaseContext(), "Changes made. Sending for Approval.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent("com.liveproject.persi.ycce.iimp.USER_PROFILE");
                                        startActivity(i);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        toast.makeText(getBaseContext(), "Please check your internet connection!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return params.toString().getBytes("UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json";
                            }
                        };
                        VolleySingleton.getRequestQueue().add(post);
                    }
                }
            } else {
                toast.makeText(getBaseContext(), "Missing Field(s) are detected. Fill all blanks!!!", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    // HINTS : DISPLAY DIVISION IN SPINNER.
    private void showDivision() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, divisionArray);
        spinner_division.setAdapter(adapter);
        spinner_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView division = (TextView) view;
                s_division = division.getText().toString();
                if (s_division.equalsIgnoreCase(divisionArray.get(0))) {
                    s_division_id = divisionList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}