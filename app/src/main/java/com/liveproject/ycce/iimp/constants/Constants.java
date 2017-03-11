package com.liveproject.ycce.iimp.constants;

/**
 * Created by Tiger on 28-08-2016.
 */
public class Constants {

    public static final String SITE_URL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/";
    public static final String MEMBER_URL = "member" ;
    public static final String OTP_URL = "otp";
    public static final String REGISTER_URL = "register";
    public static final String DESIGNATION_URL = "designations";
    public static final String CITY_URL = "cities";
    public static final String DIVISION_URL = "divisions";
    public static final String ROLE_URL = "roles";
    public static final String SEARCHCONTACT_URL = "searchcontact";
    public static final String SEARCHHANDLER_URL = "searchhandler";
    public static final String CONDITIONALGROUPCONDITIONS = "addconditionalgroupconditions";

    public static final String[] STATUS = {"inactive", "otpgenerated", "otpverified","otpverifiednewuser", "active"};
    public static final String[] GENDER = {"Any", "Male", "Female"};
    public static final String[] DATE = {"Before", "On", "After"};
    public static final int MIN_AGE_LIMIT = 18;
    public static final String SEARCH_MEMBERS_FOR_CUSTOMIZED_GROUP_URL = "searchcontactbyconditions";
    public static final String[] USERSTATUS ={"New","Active","Blocked"} ;
    public static final String GETUSERSTATUS_URL = "getuserstatus";
}