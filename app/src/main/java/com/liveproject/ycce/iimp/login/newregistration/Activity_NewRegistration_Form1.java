package com.liveproject.ycce.iimp.login.newregistration;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.DesignationService;
import com.liveproject.ycce.iimp.Division;
import com.liveproject.ycce.iimp.DivisionService;
import com.liveproject.ycce.iimp.JSONService;
import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Roles;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.adapters.Adapter_MultiCheck_Roles;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.viewholder.Header_MultiCheck_Roles;
import com.liveproject.ycce.iimp.volleyservice.VolleySingleton;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Tiger on 10-03-2017.
 */

public class Activity_NewRegistration_Form1 extends AppCompatActivity {

    EditText et_firstname, et_lastname, et_emailid, et_mobileno, et_dob, et_doj;
    Toast toast;
    Spinner spinner_designation, spinner_division;
    RadioGroup radioGenderGroup;
    RadioButton radioGenderButton;
    ProgressBar progressBar;
    Calendar calendar;
    int year, month, day;

    private Adapter_MultiCheck_Roles adapter_multiCheck_roles;

    String s_firstname, s_lastname, s_emailid, s_mobileno, s_gender, s_dob, s_doj, s_designation, s_division, s_division_id;
    String URL;
    ArrayList<String> designationArray = new ArrayList<>();
    ArrayList<String> divisionArray = new ArrayList<>();
    List<Division> divisionList = new ArrayList<>();
    List<Roles> rolesList = new ArrayList<>();
    List<String> roles = new ArrayList<>();
    int formLoaded = 0;

    MemberPersonalInfo memberPersonalInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("Registration");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }
        progressBar = (ProgressBar) findViewById(R.id.pi_pb);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        et_firstname = (EditText) findViewById(R.id.pi_et_firstname);
        et_lastname = (EditText) findViewById(R.id.pi_et_lastname);
        et_emailid = (EditText) findViewById(R.id.pi_et_emailid);
        et_mobileno = (EditText) findViewById(R.id.pi_et_mobileno);
        et_dob = (EditText) findViewById(R.id.pi_et_dob);
        et_doj = (EditText) findViewById(R.id.pi_et_doj);

        et_mobileno.setText(DatabaseService.fetchMobileNo());
        et_mobileno.setEnabled(false);

        { // HINT : Display Designation.
            designationArray.add(0, "Choose Your Designation");
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
                            toast.makeText(getBaseContext(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    });
            designationRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 4, 1));
            VolleySingleton.getRequestQueue().add(designationRequest);
        }
        { // HINTS : DISPLAY DIVISIONS.
            divisionArray.add(0, "Choose your division");
            spinner_division = (Spinner) findViewById(R.id.pi_dd_division);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, divisionArray);
            spinner_division.setAdapter(adapter);
            final DivisionService divisonRequest = new DivisionService(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONService jsonService = new JSONService(response);
                            divisionList = jsonService.parseDivision();
                            for (int i = 1; i < divisionList.size(); i++) {
                                divisionArray.add(i, divisionList.get(i - 1).getName());
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

    // HINTS : COLLECT DATA FROM ALL FIELDS OF PERSONAL DETAILS.
    private void getPersonalDetailsFields() {
        s_firstname = et_firstname.getText().toString();
        s_lastname = et_lastname.getText().toString();
        s_emailid = et_emailid.getText().toString();
        s_mobileno = et_mobileno.getText().toString();
        s_doj = et_doj.getText().toString();
        s_dob = et_dob.getText().toString();
        // HINTS : COLLECT GENDER.
        getGender();
        getRoles();
    }

    private void getRoles() {
        int already = 0;
        List<CheckedExpandableGroup> checkedExpandableGroup = (List<CheckedExpandableGroup>) adapter_multiCheck_roles.getGroups();
        for (int i = 0, j = 0; i < checkedExpandableGroup.get(0).getItemCount(); i++) {
            if (checkedExpandableGroup.get(0).selectedChildren[i] == true) {
                for (int k = 0; k < roles.size(); k++) {
                    already = 0;
                    if (roles.get(k).equalsIgnoreCase(rolesList.get(i).getRolesName())) {
                        already = 1;
                        break;
                    }
                }
                if (already == 0)
                    roles.add(j, rolesList.get(i).getRolesName());
            } else {
                for (int k = 0; k < roles.size(); k++) {
                    if (roles.get(k).equalsIgnoreCase(rolesList.get(i).getRolesName())) {
                        roles.remove(k);
                        break;
                    }
                }
            }
        }
    }

    // HINTS : COLLECT GENDER FROM RADIO GROUP.
    private void getGender() {
        try {
            radioGenderGroup = (RadioGroup) findViewById(R.id.pi_rg_gender);
            radioGenderButton = (RadioButton) findViewById(radioGenderGroup.getCheckedRadioButtonId());
            s_gender = radioGenderButton.getText().toString();
        } catch (Exception e) {
        }
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
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // TODO Auto-generated method stub
            et_dob.setText((month + 1) + "/" + day + "/" + year);
        }
    };
    private DatePickerDialog.OnDateSetListener myDOJListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // TODO Auto-generated method stub
            et_doj.setText((month + 1) + "/" + day + "/" + year);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next) {

            // HINTS : COLLECT DATA FROM ALL FIELDS.
            getPersonalDetailsFields();

            if (!(Validation.isEmpty(s_firstname) || Validation.isEmpty(s_lastname) || Validation.isEmpty(s_emailid) ||
                    Validation.isEmpty(s_mobileno) || Validation.isEmpty(s_dob) || Validation.isEmpty(s_doj) ||
                    Validation.isEmpty(s_gender))) {

                if (!Validation.isValidName(s_firstname))
                    toast.makeText(getBaseContext(), "Enter First Name in format \"Steve\".", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidName(s_lastname))
                    toast.makeText(getBaseContext(), "Enter Last Name in format \"Jobs\".", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidEmail(s_emailid))
                    toast.makeText(getBaseContext(), "Enter valid Email ID.", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidMobile(s_mobileno))
                    toast.makeText(getBaseContext(), "Enter valid Mobile Number.", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidDOB(s_dob))
                    toast.makeText(getBaseContext(), "Enter valid Date of Birth.", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidDesignation(s_designation))
                    toast.makeText(getBaseContext(), "Choose Designation.", Toast.LENGTH_SHORT).show();
                else if (!Validation.isValidDivision(s_division))
                    toast.makeText(getBaseContext(), "Choose Division.", Toast.LENGTH_SHORT).show();
                else {
                    memberPersonalInfo = new MemberPersonalInfo(s_firstname, s_lastname, s_emailid, s_mobileno, s_gender, s_dob, s_doj, s_designation, s_division, roles);
                    Intent intent = new Intent(getBaseContext(), Activity_NewRegistration_Form2.class);
                    intent.putExtra("MemberPersonalInfo", memberPersonalInfo);
                    intent.putExtra("DivisionID",s_division_id);
                    startActivity(intent);
                }
            } else {
                toast.makeText(getBaseContext(), "Missing Field(s) are detected. Fill all blanks!!!", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
