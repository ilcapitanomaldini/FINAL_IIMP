package com.liveproject.ycce.iimp;

import com.liveproject.ycce.iimp.constants.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiger on 28-08-2016.
 */
public class JSONService {
    // HINTS : For Member parsing.
    public static String[] ids;
    public static String[] names;
    public static String[] emails;
    public static final String JSON_ARRAY = "attributes";
    public static final String KEY_ID = "Id";
    public static final String KEY_FIRSTNAME = "FirstName";

    public static final String KEY_EMAIL = "Email";

    private JSONArray users = null;

    private String json;

    public JSONService() {
    }

    public JSONService(String json) {
        this.json = json;
    }

    protected void parseJSON() {
        JSONObject jsonObject = null;
        JSONArray jarray = null;
        try {
            //  jsonObject = new JSONObject(json);
            //  users = jsonObject.getJSONArray(JSON_ARRAY);

            jarray = new JSONArray(json);

            ids = new String[jarray.length()];
            names = new String[jarray.length()];
            emails = new String[jarray.length()];

            for (int j = 0; j < jarray.length(); j++) {

                JSONObject jo = jarray.getJSONObject(j);
                ids[j] = jo.getString(KEY_ID);
                names[j] = jo.getString(KEY_FIRSTNAME);
                emails[j] = jo.getString(KEY_EMAIL);
            }

            /*for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                ids[i] = jo.getString(KEY_ID);
                names[i] = jo.getString(KEY_FIRSTNAME);
                emails[i] = jo.getString(KEY_EMAIL);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // HINTS : FOR DESIGNATION PARSING.
    private ArrayList<String> designationArray = new ArrayList<>();

    public ArrayList<String> parseDesignation() {
        designationArray.add(0, "Choose Your Designation");
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 1, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);
                designationArray.add(i, jo.getString(Key.NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return designationArray;
    }

    // HINTS : FOR CITY PARSING.
    private ArrayList<String> cityArray = new ArrayList<>();

    public ArrayList<String> parseCity() {
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);
                cityArray.add(i, jo.getString(Key.NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityArray;
    }

    // HINTS : FOR DIVISIONS PARSING.
    private ArrayList<Division> divisionArray = new ArrayList<>();

    public ArrayList<Division> parseDivision() {
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);
                divisionArray.add(i, new Division(jo.getString(Key.ID), jo.getString(Key.NAME)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return divisionArray;
    }

    // HINTS : FOR ROLES PARSING.
    private ArrayList<String> roleArray = new ArrayList<>();

    public ArrayList<String> parseRoles() {
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);
                roleArray.add(i, jo.getString(Key.NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return roleArray;
    }

    // HINTS : FOR PARSING NAMES IN DL.
    private ArrayList<String> listArray = new ArrayList<>();

    public ArrayList<String> parseList() {
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);
                listArray.add(i, jo.getString(Key.NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listArray;
    }

    public Member parseSearchByID() {
        Member member;
        MemberPersonalInfo memberPersonalInfo = new MemberPersonalInfo();
        MemberAddress memberAddress = new MemberAddress();

        try {
            JSONObject jo = new JSONObject(json);
            memberPersonalInfo.setId(jo.getString(Key.ID));
            memberPersonalInfo.setFirstname(jo.getString(Key.FIRST_NAME));
            memberPersonalInfo.setLastname(jo.getString(Key.LAST_NAME));
            memberPersonalInfo.setEmailid(jo.getString(Key.EMAIL));
            memberPersonalInfo.setDesignation(jo.getString(Key.DESIGNATION));
            memberPersonalInfo.setDivision(jo.getString(Key.DIVISION));
            memberPersonalInfo.setDob(jo.getString(Key.DOB));
            memberPersonalInfo.setDoj(jo.getString(Key.DOJ));
            memberPersonalInfo.setGender(jo.getString(Key.GENDER));
            memberPersonalInfo.setMobileno(jo.getString(Key.MOBILE));
            memberPersonalInfo.setHandler_id(jo.getString(Key.HANDLER_ID));
            memberPersonalInfo.setHandler_firstname(jo.getString(Key.HANDLER_FIRST_NAME));
            memberPersonalInfo.setHandler_lastname(jo.getString(Key.HANDLER_LAST_NAME));
            memberPersonalInfo.setStatus(jo.getString(Key.STATUS));

            memberAddress.setAddrline(jo.getString(Key.ADDRESS_LINE_1));
            memberAddress.setStreet(jo.getString(Key.STREET));
            memberAddress.setCity(jo.getString(Key.CITY));
            memberAddress.setLocality(jo.getString(Key.LOCALITY));
            memberAddress.setState(jo.getString(Key.STATE));
            memberAddress.setCountry(jo.getString(Key.COUNTRY));
            memberAddress.setPincode(jo.getString(Key.PINCODE));

            JSONArray roles = jo.getJSONArray(Key.ROLES);
            List<String> rolesList = new ArrayList<>();
            for (int i = 0; i < roles.length(); i++) {
                JSONObject roleChild = roles.getJSONObject(i);
                rolesList.add(i, roleChild.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return member = new Member(memberPersonalInfo, memberAddress);
    }

    public List<Roles> parseRolesForMultiSelect() {
        List<Roles> rolesList = new ArrayList<>();
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);
                rolesList.add(i, new Roles(jo.getString(Key.NAME)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rolesList;
    }


    public List<MemberPersonalInfo> parseListOfMembers() {
        List<MemberPersonalInfo> memberPersonalInfoList = new ArrayList<>();
        List<String> rolesList = new ArrayList<>();
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);

                if (jo.has(Key.ROLES)) {
                    JSONArray roles = jo.getJSONArray(Key.ROLES);
                    for (int k = 0; k < roles.length(); k++) {
                        JSONObject roleChild = roles.getJSONObject(k);
                        rolesList.add(k, roleChild.toString());
                    }
                }
                memberPersonalInfoList.add(i,
                        new MemberPersonalInfo(jo.getString(Key.ID),
                                jo.getString(Key.FIRST_NAME),
                                jo.getString(Key.LAST_NAME),
                                jo.getString(Key.EMAIL),
                                jo.getString(Key.MOBILE),
                                jo.getString(Key.GENDER),
                                jo.getString(Key.DOB),
                                jo.getString(Key.DOJ),
                                jo.getString(Key.DESIGNATION),
                                jo.getString(Key.DIVISION),
                                jo.getString(Key.HANDLER_ID),
                                jo.getString(Key.HANDLER_FIRST_NAME),
                                jo.getString(Key.HANDLER_LAST_NAME),
                                rolesList,
                                jo.getString(Key.STATUS)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return memberPersonalInfoList;
    }

    public List<MemberPersonalInfo> parseListOfGroupMembers() {
        List<MemberPersonalInfo> memberPersonalInfoList = new ArrayList<>();
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);

                JSONObject member = jo.getJSONObject("Contact__Id__r");
                memberPersonalInfoList.add(i,
                        new MemberPersonalInfo(member.getString(Key.ID),
                                member.getString(Key.FIRST_NAME),
                                member.getString(Key.LAST_NAME),
                                jo.getString("Role__c")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return memberPersonalInfoList;
    }

    public List<GroupCondition> parseListOfGroupCondition() {
        List<GroupCondition> groupConditionList = new ArrayList<>();
        String designation = null, role = null, division = null, doj = null, dob = null, city = null, gender = null, doj_timing = null, dob_timing = null;
        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0, j = 0; j < jarray.length() + 1; i++, j++) {
                JSONObject jo = jarray.getJSONObject(j);

                if (jo.has("Designation__r")) {
                    JSONObject jsonObject = jo.getJSONObject("Designation__r");
                    designation = jsonObject.getString("Name");
                }
                if (jo.has("Role__r")) {
                    JSONObject jsonObject = jo.getJSONObject("Role__r");
                    division = jsonObject.getString("Name");
                }
                if (jo.has("Division__r")) {
                    JSONObject jsonObject = jo.getJSONObject("Division__r");
                    division = jsonObject.getString("Name");
                }
                if (jo.has("City__r")) {
                    JSONObject jsonObject = jo.getJSONObject("City__r");
                    city = jsonObject.getString("Name");
                }
                if (jo.has("Gender__c")) {
                    gender = jo.getString("Gender__c");
                }
                if (jo.has("Birthdate__c")) {
                    dob = jo.getString("Birthdate__c");
                }
                if (jo.has("BirthdateCriteria__c")) {
                    dob_timing = jo.getString("BirthdateCriteria__c");
                }
                if (jo.has("JoiningDate__c")) {
                    doj = jo.getString("JoiningDate__c");
                }
                if (jo.has("JoiningDateCriteria__c")) {
                    doj_timing = jo.getString("JoinigDateCriteria__c");
                }
                groupConditionList.add(i,
                        new GroupCondition(designation, role, division, city, gender, doj, dob, doj_timing, dob_timing));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groupConditionList;
    }
}