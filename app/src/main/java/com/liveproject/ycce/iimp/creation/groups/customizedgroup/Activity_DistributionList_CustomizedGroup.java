package com.liveproject.ycce.iimp.creation.groups.customizedgroup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.liveproject.ycce.iimp.viewholder.Header_Members;
import com.liveproject.ycce.iimp.viewholder.Header_MultiCheck_Members;
import com.liveproject.ycce.iimp.volleyservice.VolleySingleton;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

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
 * Created by Tiger on 15-02-2017.
 */

public class Activity_DistributionList_CustomizedGroup extends AppCompatActivity {
    Spinner spinner_designation, spinner_role, spinner_division, spinner_doj, spinner_dob, spinner_city, spinner_gender;
    EditText et_doj, et_dob;
    Button btn_add_members, btn_search_member;
    RecyclerView rv_selectedmembers, rv_addmembers;
    ProgressBar progressBar;
    Toast toast;

    String s_designation, s_role, s_division, s_division_id, s_doj, s_dob, s_city, s_gender, s_doj_timing, s_dob_timing;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_list_personal);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("Select Members");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }

        rv_addmembers = (RecyclerView) findViewById(R.id.dlp_rv_addmembers);
        rv_selectedmembers = (RecyclerView) findViewById(R.id.dlp_rv_selectedmember);
        progressBar = (ProgressBar) findViewById(R.id.dlc_pb);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
                            Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            VolleySingleton.getRequestQueue().add(designationRequest);
        }
        { // HINTS : DISPLAY CITIES.
            cityArray.add(0, "Choose City");
            spinner_city = (Spinner) findViewById(R.id.dlp_dd_city);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArray);
            spinner_designation.setAdapter(adapter);
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
                            Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            VolleySingleton.getRequestQueue().add(cityRequest);
        }

        { // HINTS : DISPLAY DIVISIONS.
            divisionArray.add(0, "Choose Division");
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
                            Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
            roleArray.add(0, "Choose Role");
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
                            Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        btn_search_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                s_doj = et_doj.getText().toString();
                s_dob = et_dob.getText().toString();

                LinearLayout ll_search_members = (LinearLayout) findViewById(R.id.dlp_ll_search_member);
                ll_search_members.setVisibility(View.VISIBLE);

                final JSONObject params = new JSONObject();
                try {
                    params.put("gender", s_gender);
                    params.put("dob", s_dob);
                    params.put("doj", s_doj);
                    params.put("designation", s_designation);
                    params.put("division", s_division_id);
                    params.put("role", s_role);
                    params.put("city", s_city);
                    params.put("dobcriteria", s_dob_timing);
                    params.put("dojcriteria", s_doj_timing);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Search member url
                URL = Constants.SITE_URL + Constants.SEARCH_MEMBERS_FOR_CUSTOMIZED_GROUP_URL;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL
                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (Validation.isEmpty(response)) {
                        } else {
                            JSONService jsonService = new JSONService(response);
                            memberPersonalInfoList = jsonService.parseListOfMembers();
                            Header_MultiCheck_Members headerMembers1 = new Header_MultiCheck_Members("Select Members", memberPersonalInfoList);
                            List<Header_MultiCheck_Members> headerMembers = Arrays.asList(headerMembers1);

                            adapter_multiCheck_members = new Adapter_MultiCheck_Members(headerMembers);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                            rv_addmembers.setLayoutManager(layoutManager);
                            rv_addmembers.setAdapter(adapter_multiCheck_members);
                            activityloaded++;
                            if (activityloaded >= 1) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                activityloaded = 0;
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Please check your internet connection!!!", Toast.LENGTH_SHORT).show();
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
                VolleySingleton.getRequestQueue().add(stringRequest);

                btn_add_members = (Button) findViewById(R.id.dlp_btn_add_members);
                getSelectedMembers();
                if (selectedMemberList.size() > 0) {
                    btn_add_members.setVisibility(View.VISIBLE);
                    btn_add_members.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LinearLayout ll_group_member = (LinearLayout) findViewById(R.id.dlp_ll_group_member);
                            ll_group_member.setVisibility(View.VISIBLE);
                            Header_Members header_members = new Header_Members("Selected Members", selectedMemberList);
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
                s_division_id = divisionList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // HINTS : DISPLAY CITIES IN SPINNER.
    private void showCity() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArray);
        spinner_role.setAdapter(adapter);
        spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
}