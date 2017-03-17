package com.liveproject.ycce.iimp.creation.personalmessage;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.liveproject.ycce.iimp.CityService;
import com.liveproject.ycce.iimp.DesignationService;
import com.liveproject.ycce.iimp.Division;
import com.liveproject.ycce.iimp.DivisionService;
import com.liveproject.ycce.iimp.JSONService;
import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.RoleService;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.adapters.Adapter_MemberWithClose;
import com.liveproject.ycce.iimp.adapters.Adapter_MultiCheck_Members;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.messaging.Activity_Home_Messaging;
import com.liveproject.ycce.iimp.networkservice.PostService;
import com.liveproject.ycce.iimp.adapters.headers.Header_Members;
import com.liveproject.ycce.iimp.adapters.headers.Header_MultiCheck_Members;
import com.liveproject.ycce.iimp.volleyservice.VolleySingleton;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Tiger on 04-03-2017.
 */

public class Activity_DistributionList_PersonalMessage extends AppCompatActivity {

    Spinner spinner_designation, spinner_role, spinner_division, spinner_doj, spinner_dob, spinner_city, spinner_gender;
    EditText et_firstname, et_lastname, et_mobileno, et_doj, et_dob;
    Button btn_add_members, btn_search_member;
    RecyclerView rv_selectedmembers, rv_addmembers;
    ProgressBar progressBar;
    Toast toast;

    String s_firstname, s_lastname, s_mobileno, s_designation, s_role, s_division, s_division_id, s_doj, s_dob, s_city, s_gender, s_doj_timing, s_dob_timing;
    String s_prev_firstname = null, s_prev_last_name = null, s_prev_mobileno = null, s_prev_designation = null, s_prev_role = null, s_prev_division_id = null, s_prev_doj = null, s_prev_dob = null, s_prev_city = null, s_prev_gender = null, s_prev_doj_timing = null, s_prev_dob_timing = null;
    String s_pmid;
    String URL;
    int activityloaded = 0;

    Calendar calendar;
    int year, month, day;

    ArrayList<String> designationArray = new ArrayList<>();
    ArrayList<String> roleArray = new ArrayList<>();
    ArrayList<String> divisionArray = new ArrayList<>();
    ArrayList<String> cityArray = new ArrayList<>();
    List<Division> divisionList = new ArrayList<>();
    List<MemberPersonalInfo> memberPersonalInfoList = new ArrayList<>();
    List<MemberPersonalInfo> selectedMemberList = new ArrayList<>();

    private Adapter_MemberWithClose adapter_memberWithClose;
    private Adapter_MultiCheck_Members adapter_multiCheck_members;

    BroadcastReceiver personalmessagecreation, personalmessagerecipents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_list_personal);

        s_pmid = getIntent().getStringExtra("PMID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_title.setText("Select Recipents");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rv_addmembers = (RecyclerView) findViewById(R.id.dlp_rv_addmembers);
        rv_selectedmembers = (RecyclerView) findViewById(R.id.dlp_rv_selectedmember);
        progressBar = (ProgressBar) findViewById(R.id.dlp_pb);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        et_firstname = (EditText) findViewById(R.id.dlp_et_firstname);
        et_lastname = (EditText) findViewById(R.id.dlp_et_lastname);
        et_mobileno = (EditText) findViewById(R.id.dlp_et_mobileno);

        { // HINTS : DISPLAY DESIGNATION.
            designationArray.add(0, "All");
            spinner_designation = (Spinner) findViewById(R.id.dlp_dd_designation);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, designationArray);
            spinner_designation.setAdapter(adapter);
            DesignationService designationRequest = new DesignationService(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Mayur", response);
                            JSONService jsonService = new JSONService(response);
                            designationArray = jsonService.parseDesignation();
                            showDesignation();
                            activityloaded++;
                            if (activityloaded >= 4) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                activityloaded = 0;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            VolleySingleton.getRequestQueue().add(designationRequest);
        }
        { // HINTS : DISPLAY CITIES.
            cityArray.add(0, "All");
            spinner_city = (Spinner) findViewById(R.id.dlp_dd_city);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArray);
            spinner_city.setAdapter(adapter);
            CityService cityRequest = new CityService(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Mayur", response);
                            JSONService jsonService = new JSONService(response);
                            cityArray = jsonService.parseCity();
                            showCity();
                            activityloaded++;
                            if (activityloaded >= 4) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                activityloaded = 0;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            VolleySingleton.getRequestQueue().add(cityRequest);
        }

        { // HINTS : DISPLAY DIVISIONS.
            divisionArray.add(0, "All");
            spinner_division = (Spinner) findViewById(R.id.dlp_dd_division);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, divisionArray);
            spinner_division.setAdapter(adapter);
            DivisionService divisonRequest = new DivisionService(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Mayur", response);
                            JSONService jsonService = new JSONService(response);
                            divisionList = jsonService.parseDivision();
                            for (int i = 0; i < divisionList.size(); i++) {
                                divisionArray.add(i, divisionList.get(i).getName());
                            }
                            showDivision();
                            activityloaded++;
                            if (activityloaded >= 4) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                activityloaded = 0;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            VolleySingleton.getRequestQueue().add(divisonRequest);
        }
        { // HINTS : DISPLAY DOJ.
            spinner_doj = (Spinner) findViewById(R.id.dlp_dd_doj);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.DATE);
            spinner_doj.setAdapter(adapter);
            spinner_doj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView doj = (TextView) view;
                    s_doj_timing = doj.getText().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            et_doj = (EditText) findViewById(R.id.dlp_et_doj);

            et_doj.setOnClickListener(new View.OnClickListener() {
                @Override
                @SuppressWarnings("deprecation")
                public void onClick(View v) {
                    showDialog(998);
                }
            });
        }
        { // HINTS : DISPLAY DOB.
            spinner_dob = (Spinner) findViewById(R.id.dlp_dd_dob);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.DATE);
            spinner_dob.setAdapter(adapter);
            spinner_dob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView doj = (TextView) view;
                    s_dob_timing = doj.getText().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            et_dob = (EditText) findViewById(R.id.dlp_et_dob);

            et_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                @SuppressWarnings("deprecation")
                public void onClick(View v) {
                    showDialog(999);
                }
            });
        }
        { // HINTS : DISPLAY ROLES.
            roleArray.add(0, "All");
            spinner_role = (Spinner) findViewById(R.id.dlp_dd_roles);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, roleArray);
            spinner_role.setAdapter(adapter);
            RoleService roleRequest = new RoleService(
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Mayur", response);
                            JSONService jsonService = new JSONService(response);
                            roleArray = jsonService.parseRoles();
                            showRole();
                            activityloaded++;
                            if (activityloaded >= 4) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                activityloaded = 0;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            VolleySingleton.getRequestQueue().add(roleRequest);
        }
        { // HINTS : DISPLAY GENDER.
            spinner_gender = (Spinner) findViewById(R.id.dlp_dd_gender);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.GENDER);
            spinner_gender.setAdapter(adapter);
            spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView gender = (TextView) view;
                    s_gender = gender.getText().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        btn_search_member = (Button) findViewById(R.id.dlp_btn_search_members);
        btn_search_member.setText("Search Recipent");

        btn_search_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                s_firstname = et_firstname.getText().toString();
                s_lastname = et_lastname.getText().toString();
                s_mobileno = et_mobileno.getText().toString();
                s_doj = et_doj.getText().toString();
                s_dob = et_dob.getText().toString();

                if (!Validation.isEmpty(s_doj) && s_doj_timing.equalsIgnoreCase("Null")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    toast.makeText(getBaseContext(), "Select valid condition of Date of Joining.", Toast.LENGTH_SHORT).show();
                } else if (!Validation.isEmpty(s_dob) && s_dob_timing.equalsIgnoreCase("Null")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    toast.makeText(getBaseContext(), "Select valid condition of Date of Birth.", Toast.LENGTH_SHORT).show();
                } else if (!(s_firstname.equalsIgnoreCase(s_prev_firstname) &&
                        s_lastname.equalsIgnoreCase(s_prev_last_name) &&
                        s_mobileno.equalsIgnoreCase(s_prev_mobileno) &&
                        s_gender.equalsIgnoreCase(s_prev_gender) &&
                        s_role.equalsIgnoreCase(s_prev_role) &&
                        s_city.equalsIgnoreCase(s_prev_city) &&
                        s_designation.equalsIgnoreCase(s_prev_designation) &&
                        s_division_id.equalsIgnoreCase(s_prev_division_id) &&
                        s_dob.equalsIgnoreCase(s_prev_dob) &&
                        s_doj.equalsIgnoreCase(s_prev_doj) &&
                        s_dob_timing.equalsIgnoreCase(s_prev_dob_timing) &&
                        s_doj_timing.equalsIgnoreCase(s_prev_doj_timing))) {

                    if (Validation.isEmpty(s_dob)) {
                        s_dob_timing = "";
                    }
                    if (Validation.isEmpty(s_doj)) {
                        s_doj_timing = "";
                    }

                    final JSONObject params = new JSONObject();
                    try {
                        params.put("fname", s_firstname);
                        params.put("lname", s_lastname);
                        params.put("mobile", s_mobileno);
                        params.put("gender", s_gender);
                        params.put("dob", s_dob);
                        params.put("doj", s_doj);
                        params.put("designation", s_designation);
                        params.put("divisionid", s_division_id);
                        params.put("role", s_role);
                        params.put("city", s_city);
                        params.put("dobcriteria", s_dob_timing);
                        params.put("dojcriteria", s_doj_timing);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("params", "onClick: " + params);
                    URL = Constants.SITE_URL + Constants.SEARCH_MEMBERS_FOR_CUSTOMIZED_GROUP_URL;

                    Intent intent = new Intent(getBaseContext(), PostService.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("NAME", "PersonalMessageCreation");
                    intent.putExtra("JSONOBJECT", params.toString());
                    getBaseContext().startService(intent);

                } else {
                    activityloaded++;
                    if (activityloaded >= 1) {
                        progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        activityloaded = 0;
                    }
                    toast.makeText(getBaseContext(), "Same Conditions. No new members added.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class PersonalMessageCreation extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                if (Validation.isEmpty(response)) {
                    LinearLayout ll_search_members = (LinearLayout) findViewById(R.id.dlp_ll_search_member);
                    ll_search_members.setVisibility(View.GONE);
                    toast.makeText(getBaseContext(), "No Contact Found!!!", Toast.LENGTH_SHORT).show();
                } else {
                    LinearLayout ll_search_members = (LinearLayout) findViewById(R.id.dlp_ll_search_member);
                    ll_search_members.setVisibility(View.VISIBLE);
                    JSONService jsonService = new JSONService(response);
                    memberPersonalInfoList = jsonService.parseListOfMembers();
                    Header_MultiCheck_Members headerMembers1 = new Header_MultiCheck_Members("Select Recipents", memberPersonalInfoList);
                    List<Header_MultiCheck_Members> headerMembers = Arrays.asList(headerMembers1);

                    adapter_multiCheck_members = new Adapter_MultiCheck_Members(headerMembers);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                    rv_addmembers.setLayoutManager(layoutManager);
                    rv_addmembers.setAdapter(adapter_multiCheck_members);

                    s_prev_firstname = s_firstname;
                    s_prev_last_name = s_lastname;
                    s_prev_mobileno = s_mobileno;
                    s_prev_gender = s_gender;
                    s_prev_city = s_city;
                    s_prev_designation = s_designation;
                    s_prev_division_id = s_division_id;
                    s_prev_dob = s_dob;
                    s_prev_doj = s_doj;
                    s_prev_dob_timing = s_dob_timing;
                    s_prev_doj_timing = s_doj_timing;
                    s_prev_role = s_role;

                    btn_add_members = (Button) findViewById(R.id.dlp_btn_add_members);
                    btn_add_members.setText("Add Recipents");

                    btn_add_members.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSelectedMembers();
                            LinearLayout ll_group_member = (LinearLayout) findViewById(R.id.dlp_ll_group_member);
                            ll_group_member.setVisibility(View.VISIBLE);
                            Header_Members header_members = new Header_Members("Selected Recipents", selectedMemberList);
                            List<Header_Members> headerSelectedMembers = Arrays.asList(header_members);
                            adapter_memberWithClose = new Adapter_MemberWithClose(headerSelectedMembers);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                            rv_selectedmembers.setLayoutManager(layoutManager);
                            rv_selectedmembers.setAdapter(adapter_memberWithClose);
                            activityloaded++;
                            if (activityloaded >= 1) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                activityloaded = 0;
                            }
                        }
                    });
                }
            }
        }
    }


    // HINTS : DISPLAY DIVISION IN SPINNER.
    private void showDivision() {
        divisionArray.add(0, "All");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, divisionArray);
        spinner_division.setAdapter(adapter);
        spinner_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView division = (TextView) view;
                s_division = division.getText().toString();
                if (s_division == "All") {
                    s_division_id = "All";
                } else
                    s_division_id = divisionList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // HINTS : DISPLAY CITIES IN SPINNER.
    private void showCity() {
        cityArray.add(0, "All");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArray);
        spinner_city.setAdapter(adapter);
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView city = (TextView) view;
                s_city = city.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // HINTS : DISPLAY ROLES IN SPINNER.
    private void showRole() {
        roleArray.add(0, "All");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, roleArray);
        spinner_role.setAdapter(adapter);
        spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView role = (TextView) view;
                s_role = role.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // HINTS : DISPLAY DESIGNATION IN SPINNER.
    private void showDesignation() {
        designationArray.add(0, "All");
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

    private void getSelectedMembers() {
        int already = 0;
        List<CheckedExpandableGroup> checkedExpandableGroup = (List<CheckedExpandableGroup>) adapter_multiCheck_members.getGroups();
        for (int i = 0, j = 0; i < checkedExpandableGroup.get(0).getItemCount(); i++) {
            if (checkedExpandableGroup.get(0).selectedChildren[i] == true) {
                for (int k = 0; k < selectedMemberList.size(); k++) {
                    already = 0;
                    if (selectedMemberList.get(k).getId().equals(memberPersonalInfoList.get(i).getId())) {
                        already = 1;
                        break;
                    }
                }
                if (already == 0)
                    selectedMemberList.add(j, memberPersonalInfoList.get(i));
            }
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
    protected void onResume() {
        super.onResume();
        personalmessagecreation = new PersonalMessageCreation();
        this.registerReceiver(personalmessagecreation, new IntentFilter("android.intent.action.PersonalMessageCreation"));
        personalmessagerecipents = new PersonalMessageRecipents();
        this.registerReceiver(personalmessagerecipents, new IntentFilter("android.intent.action.PersonalMessageRecipents"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(personalmessagecreation);
        this.unregisterReceiver(personalmessagerecipents);
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
            URL = Constants.SITE_URL + Constants.PERSONALMESSAGERECEIVER_URL;
            JSONObject params = new JSONObject();
            JSONArray recipents = new JSONArray();

            try {
                params.put("pmid", s_pmid);

                for (int i = 0; i < selectedMemberList.size(); i++) {
                    recipents.put(i, selectedMemberList.get(i).getId());
                }
                params.put("tocontactids", recipents);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getBaseContext(), PostService.class);
            intent.putExtra("URL", URL);
            intent.putExtra("NAME", "PersonalMessageRecipents");
            intent.putExtra("JSONOBJECT", params.toString());
            getBaseContext().startService(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class PersonalMessageRecipents extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                if (response.equalsIgnoreCase("\"false\"")) {
                    toast.makeText(getBaseContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                } else if (response.equalsIgnoreCase("\"true\"")) {
                    Intent i = new Intent(getBaseContext(), Activity_Home_Messaging.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    toast.makeText(getBaseContext(), "Message sent.", Toast.LENGTH_LONG).show();
                    startActivity(i);
                    finish();
                }
            }
        }
    }
}
