package com.liveproject.ycce.iimp.constants;

/*COPYRIGHT NOTICE

Copyright By "YCCE TEAM" on 15-03-2017.

Members of "YCCE TEAM" are stated in the postscript.

We, the creators of this software (i.e. developers) referenced as "YCCE TEAM" or "we" from here on,
allow the person who gets this software and/or code for the software to present this software/code
in a presentation dated 16-03-2017 only. Any further usage would be deemed to be breach of contract.
 Accepting this software/code is legally binding and would mean that the terms stated here have been
  accepted. The person does not have the right to copy/modify/distribute or in any form make the
  software or code available to anyone without the explicit permission of all the members of
   "YCCE TEAM". It is the responsibility of the aforementioned person that this software/code
   does not get illegally distributed till the time the person is in possession of the software/code.

P.S. :
Members of "YCCE TEAM" :
1. Aakash Wankhede
2. Akash Kahalkar
3. Mayur Dongare
4. Ved Mehta*/

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
    public static final String[] DATE = {"Null","Before", "On", "After"};
    public static final int MIN_AGE_LIMIT = 18;
    public static final String SEARCH_MEMBERS_FOR_CUSTOMIZED_GROUP_URL = "searchcontactbyconditions";
    public static final String[] USERSTATUS ={"New","Active","Blocked"} ;
    public static final String GETUSERSTATUS_URL = "getuserstatus";
    public static final String[] GROUPROLES ={"Admin","Management","Normal"};
    public static final String CREATEGROUP_URL = "creategroup";
    public static final String PROCESSPENDINGREQUEST_URL = "processpendingrequest";
}