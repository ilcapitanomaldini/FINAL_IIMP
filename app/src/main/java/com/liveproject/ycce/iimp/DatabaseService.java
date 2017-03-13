package com.liveproject.ycce.iimp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.events.Event;
import com.liveproject.ycce.iimp.messaging.groupmessaging.GroupClass;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Message;
import com.liveproject.ycce.iimp.messaging.personalmessaging.PersonalMessage;
import com.liveproject.ycce.iimp.news.News;
import com.liveproject.ycce.iimp.pendingrequests.PendingRequest;
import com.liveproject.ycce.iimp.polling.Poll;
import com.liveproject.ycce.iimp.polling.PollMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tiger on 05-10-2016.
 */
public class DatabaseService {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "IIMP_DB";

    private static final String TABLE_LOGIN = "LOGIN";
    private static final String TABLE_USERPROFILE = "USERPROFILE";
    private static final String TABLE_ADDR = "ADDRESS";
    private static final String TABLE_AREACODE = "AREACODE";
    private static final String TABLE_DESIGNATION = "DESIGNATION";
    private static final String TABLE_GROUP = "GROUPS";
    private static final String TABLE_GROUPMAP = "GROUPMAP";
    private static final String TABLE_CONDGROUPMAP = "CONDGROUPMAP";
    private static final String TABLE_GROUPMESSAGING = "GROUPMESSAGING";
    private static final String TABLE_PERSONALMESSAGING = "PERSONALMESSAGING";
    private static final String TABLE_PERSONALMESSAGEMAPPING = "PERSONALMESSAGEMAPPING";
    private static final String TABLE_NEWS = "NEWS";
    private static final String TABLE_PR = "PENDINGREQUESTS";
    private static final String TABLE_EVENTS = "EVENTS";
    private static final String TABLE_POLL = "POLL";
    private static final String TABLE_POLLMAPPING = "POLLMAPPING";
    private static final String TABLE_ROLES = "ROLES";

    //all columnsof ROLES Table :
    private static final String ROLES_ID = "ID";
    private static final String ROLES_CONTACT_ID = "CONTACT_ID";
    private static final String ROLES_NAME = "NAME";

    //all columns of PERSONALMESSAGEMAPPING Table :
    private static final String PERSONALMESSAGEMAPPING_PMID = "PMID";
    private static final String PERSONALMESSAGEMAPPING_UID = "UID";

    //all columns of POLL Table :
    private static final String POLL_PID = "PID";
    private static final String POLL_TITLE = "TITLE";
    private static final String POLL_NUM_ANS = "NUMANS";
    private static final String POLL_CREATOR_ID = "CREATOR";
    private static final String POLL_DURATION = "DURATION";
    private static final String POLL_DATETIME = "DATETIME";
    private static final String POLL_GID = "GID";
    private static final String POLL_STATUS = "STATUS";
    private static final String POLL_LOCAL_ID = "LOCAL_ID";

    //all columns of POLLMAPPING Table :
    private static final String POLL_MAPPING_AID = "AID";
    private static final String POLL_MAPPING_PID = "PID";
    private static final String POLL_MAPPING_ANSWER_TITLE = "ANSWERTITLE";
    private static final String POLL_MAPPING_NUM_VOTES = "NUMVOTES";

    //all columns of EVENT Table:
    private static final String EVENTS_ID = "EID";
    private static final String EVENTS_GID = "GID";
    private static final String EVENTS_POSTED_BY = "POSTEDBY";
    private static final String EVENTS_MESSAGE = "MESSAGE";
    private static final String EVENTS_STATUS = "STATUS";
    private static final String EVENTS_DATETIME = "EVENTDATETIME";
    private static final String EVENTS_DURATION = "DURATION";


    //all columns of PendingRequests table:
    private static final String PR_ID = "PRID";
    private static final String PR_HANDLER = "HANDLER";
    private static final String PR_POSTER = "POSTER";
    private static final String PR_TYPE = "TYPE";
    private static final String PR_ADDITIONALINFO = "ADDITIONALINFO";


    //All columns of News table:
    private static final String NEWS_ID = "NID";
    private static final String NEWS_TITLE = "TITLE";
    private static final String NEWS_MESSAGE = "MESSAGE";
    private static final String NEWS_IMAGE_PATH = "IMAGE";
    private static final String NEWS_POSTER = "POSTER";
    private static final String NEWS_LOCAL_ID = "LOCAL_ID";


    //All columns of personalmessaging table:
    private static final String PM_ID = "PMID";
    private static final String PM_TXT = "MESSAGE";
    private static final String PM_FROM = "FROM_SENDER";
    private static final String PM_DATE = "DATE";
    private static final String PM_SUBJECT = "SUBJECT";
    private static final String PM_LOCALID = "LOCALID";


    //All columns of groupmessaging table:
    private static final String MSG_ID = "MID";
    private static final String MSG_TXT = "TEXT";
    private static final String MSG_FROM = "FROM_SENDER";
    private static final String MSG_GID = "GID";
    private static final String MSG_TYPE = "TYPE";
    private static final String MSG_EVENT_ID = "EVENT_ID";
    private static final String MSG_POLL_ID = "POLL_ID";
    private static final String MSG_LOCAL_ID = "LOCAL_ID";


    //All Columns of LOGIN TABLE:
    private static final String LOGIN_MOBILE_NO = "MOBILE_NO";
    private static final String LOGIN_STATUS = "STATUS";
    private static final String LOGIN_LAST_LOGIN = "LAST_LOGIN";

    //All Columns of USER_PROFILE TABLE:
    private static final String USERPROFILE_UPID = "UPID";
    private static final String USERPROFILE_FIRST_NAME = "FIRST_NAME";
    private static final String USERPROFILE_LAST_NAME = "LAST_NAME";
    private static final String USERPROFILE_MOBILE_NO = "MOBILE_NO";
    private static final String USERPROFILE_EMAIL_ID = "EMAIL_ID";
    private static final String USERPROFILE_DIVISION = "DIVISION";
    private static final String USERPROFILE_DESIGNATION = "DESIGNATION";
    private static final String USERPROFILE_HANDLER_ID = "HANDLER_ID";
    private static final String USERPROFILE_HANDLER_FIRST_NAME = "HANDLER_FIRST_NAME";
    private static final String USERPROFILE_HANDLER_LAST_NAME = "HANDLER_LAST_NAME";
    private static final String USERPROFILE_STATUS = "STATUS";
    private static final String USERPROFILE_ADDRLINE = "ADDRLINE";
    private static final String USERPROFILE_STREET = "STREET";
    private static final String USERPROFILE_LOCALITY = "LOCALITY";
    private static final String USERPROFILE_CITY = "CITY";
    private static final String USERPROFILE_PINCODE = "PINCODE";
    private static final String USERPROFILE_STATE = "STATE";
    private static final String USERPROFILE_COUNTRY = "COUNTRY";
    private static final String USERPROFILE_DOB = "DOB";
    private static final String USERPROFILE_DOJ = "DOJ";
    private static final String USERPROFILE_GENDER = "GENDER";


    //All Columns of ADDRESS TABLE:
    private static final String ADDR_AID = "AID";
    private static final String ADDR_ADDRLINE = "ADDRLINE";
    private static final String ADDR_STREET = "STREET";
    private static final String ADDR_LOCALITY = "LOCALITY";
    private static final String ADDR_PINCODE = "PINCODE";

    //All Columns of AREACODE TABLE:
    private static final String AREACODE_PINCODE = "PINCODE";
    private static final String AREACODE_CITY = "CITY";
    private static final String AREACODE_STATE = "STATE";
    private static final String AREACODE_COUNTRY = "COUNTRY";

    //All Columns of DESIGNATION TABLE:
    private static final String DESIGNATION_DID = "DID";
    private static final String DESIGNATION_DESIGNATION = "DESIGNATION";


    //All Columns of GROUP TABLE:
    private static final String GROUP_ID = "GID";
    private static final String GROUP_NAME = "GNAME";
    private static final String GROUP_TYPE = "GTYPE";
    private static final String GROUP_STATUS = "STATUS";
    private static final String GROUP_ROLE = "GROUPROLE";

    //All Columns of GROUPMAP TABLE
    private static final String GROUPMAP_GID = "GID";
    private static final String GROUPMAP_UPID = "UPID";

    //All columns of CONDGROUPMAP TABLE
    private static final String CONDGROUPMAP_GID = "GID";
    private static final String CONDGROUPMAP_CITY = "CITY";
    private static final String CONDGROUPMAP_DESIGNATION = "DESIGNATION";

    /*********
     * Used to open database in synchronized way
     *********/
    private static DataBaseHelper DBHelper = null;

    protected DatabaseService() {
    }

    /**********
     * Initialize database
     *********/
    public static void init(Context context) {
        if (DBHelper == null)
            DBHelper = new DataBaseHelper(context);
    }

    public static boolean check_validity() {
        if (DBHelper == null)
            return false;
        else
            return true;
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN +
                    " (" + LOGIN_MOBILE_NO + " text , " +
                    LOGIN_STATUS + " text , " +
                    LOGIN_LAST_LOGIN + " text ); "
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERPROFILE +
                    " (" + USERPROFILE_UPID + " TEXT, " +
                    USERPROFILE_DESIGNATION + " TEXT , " +
                    USERPROFILE_FIRST_NAME + " TEXT , " +
                    USERPROFILE_LAST_NAME + " TEXT , " +
                    USERPROFILE_MOBILE_NO + " TEXT , " +
                    USERPROFILE_EMAIL_ID + " TEXT , " +
                    USERPROFILE_DOB + " TEXT , " +
                    USERPROFILE_GENDER + " TEXT , " +
                    USERPROFILE_DIVISION + " TEXT , " +
                    USERPROFILE_HANDLER_ID + " TEXT , " +
                    USERPROFILE_HANDLER_FIRST_NAME + " TEXT , " +
                    USERPROFILE_HANDLER_LAST_NAME + " TEXT , " +
                    USERPROFILE_DOJ + " TEXT , " +
                    USERPROFILE_STATUS + " TEXT , " +
                    USERPROFILE_ADDRLINE + " TEXT , " +
                    USERPROFILE_LOCALITY + " TEXT , " +
                    USERPROFILE_STREET + " TEXT , " +
                    USERPROFILE_STATE + " TEXT , " +
                    USERPROFILE_CITY + " TEXT , " +
                    USERPROFILE_COUNTRY + " TEXT , " +
                    USERPROFILE_PINCODE + " TEXT ); "
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ADDR +
                    " (" + ADDR_AID + " TEXT, " +       //Remember to truncate the table not just delete the rows.
                    ADDR_ADDRLINE + " TEXT, " +
                    ADDR_LOCALITY + " TEXT, " +
                    ADDR_STREET + " TEXT, " +
                    ADDR_PINCODE + " TEXT); "
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_AREACODE +
                    " (" + AREACODE_PINCODE + " TEXT, " +
                    AREACODE_CITY + " TEXT, " +
                    AREACODE_STATE + " TEXT, " +
                    AREACODE_COUNTRY + " TEXT); "
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DESIGNATION +
                    " (" + DESIGNATION_DID + " TEXT, " +
                    DESIGNATION_DESIGNATION + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GROUP +
                    " (" + GROUP_ID + " TEXT, " +
                    GROUP_NAME + " TEXT, " +
                    GROUP_STATUS + " TEXT, " +
                    GROUP_ROLE + " TEXT, " +
                    GROUP_TYPE + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GROUPMAP +
                    " (" + GROUPMAP_GID + " TEXT, " +
                    GROUPMAP_UPID + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CONDGROUPMAP +
                    " (" + CONDGROUPMAP_GID + " TEXT , " +
                    CONDGROUPMAP_CITY + " TEXT, " +
                    CONDGROUPMAP_DESIGNATION + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GROUPMESSAGING +
                    " (" + MSG_ID + " TEXT , " +
                    MSG_TXT + " TEXT, " +
                    MSG_FROM + " TEXT, " +
                    MSG_TYPE + " TEXT, " +
                    MSG_EVENT_ID + " TEXT, " +
                    MSG_POLL_ID + " TEXT, " +
                    MSG_LOCAL_ID + " TEXT, " +
                    MSG_GID + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PERSONALMESSAGING +
                    " (" + PM_ID + " TEXT , " +
                    PM_TXT + " TEXT, " +
                    PM_FROM + " TEXT, " +
                    PM_SUBJECT + " TEXT, " +
                    PM_LOCALID + " TEXT, " +
                    PM_DATE + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PERSONALMESSAGEMAPPING +
                    " (" + PERSONALMESSAGEMAPPING_PMID + " TEXT, " +
                    PERSONALMESSAGEMAPPING_UID + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NEWS +
                    " (" + NEWS_ID + " TEXT , " +
                    NEWS_TITLE + " TEXT, " +
                    NEWS_MESSAGE + " TEXT, " +
                    NEWS_IMAGE_PATH + " TEXT, " +
                    NEWS_LOCAL_ID + " TEXT, " +
                    NEWS_POSTER + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PR +
                    " (" + PR_ID + " TEXT , " +
                    PR_HANDLER + " TEXT, " +
                    PR_POSTER + " TEXT, " +
                    PR_TYPE + " TEXT, " +
                    PR_ADDITIONALINFO + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS +
                    " (" + EVENTS_ID + " TEXT , " +
                    EVENTS_GID + " TEXT, " +
                    EVENTS_MESSAGE + " TEXT, " +
                    EVENTS_STATUS + " TEXT, " +
                    EVENTS_POSTED_BY + " TEXT, " +
                    EVENTS_DATETIME + " TEXT, " +
                    EVENTS_DURATION + " TEXT );"
            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_POLL +
                    " (" + POLL_PID + " TEXT , " +
                    POLL_TITLE + " TEXT, " +
                    POLL_NUM_ANS + " TEXT, " +
                    POLL_CREATOR_ID + " TEXT, " +
                    POLL_DURATION + " TEXT, " +
                    POLL_STATUS + " TEXT, " +
                    POLL_DATETIME + " TEXT, " +
                    POLL_LOCAL_ID + " TEXT, " +
                    POLL_GID + " TEXT );"

            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_POLLMAPPING +
                    " (" + POLL_MAPPING_AID + " TEXT , " +
                    POLL_MAPPING_PID + " TEXT, " +
                    POLL_MAPPING_ANSWER_TITLE + " TEXT, " +
                    POLL_MAPPING_NUM_VOTES + " TEXT );"

            );
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ROLES +
                    " (" + ROLES_ID + " TEXT , " +
                    ROLES_CONTACT_ID + " TEXT , " +
                    ROLES_NAME + " TEXT ); "
            );

        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Add the following code :
            /*
             for (String table : ALL_TABLES) {     // Here, the alltables is a string that is declared above and contains names of all tables in a string array.
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
             */
        }
    }


    // HINT : OPEN DATABASE FOR INSERT,UPDATE,DELETE IN SYNCRONIZED MANNER
    private static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }


    // HINT : INSERT OPERATION ON USER_PROFILE TABLE.
    // Check for null Roles, if valid.
    // Check for null mpi and ma.
    public static boolean insertUserProfile(Member m) {
        final SQLiteDatabase db = open();

        MemberPersonalInfo mpi = m.getMpi();
        MemberAddress ma = m.getMemaddr();

        ContentValues cval = new ContentValues();
        cval.put(USERPROFILE_UPID, mpi.getId());
        cval.put(USERPROFILE_FIRST_NAME, mpi.getFirstname());
        cval.put(USERPROFILE_LAST_NAME, mpi.getLastname());
        cval.put(USERPROFILE_MOBILE_NO, mpi.getMobileno());
        cval.put(USERPROFILE_EMAIL_ID, mpi.getEmailid());
        cval.put(USERPROFILE_DOB, mpi.getDob());
        cval.put(USERPROFILE_GENDER, mpi.getGender());
        cval.put(USERPROFILE_DOJ, mpi.getDoj());
        cval.put(USERPROFILE_HANDLER_ID, mpi.getHandler_id());
        cval.put(USERPROFILE_HANDLER_FIRST_NAME, mpi.getHandler_firstname());
        cval.put(USERPROFILE_HANDLER_LAST_NAME, mpi.getHandler_lastname());
        cval.put(USERPROFILE_DIVISION, mpi.getDivision());
        cval.put(USERPROFILE_DESIGNATION,mpi.getDesignation());
        cval.put(USERPROFILE_STATUS, mpi.getStatus());

        cval.put(USERPROFILE_ADDRLINE, ma.getAddrline());
        cval.put(USERPROFILE_STREET, ma.getStreet());
        cval.put(USERPROFILE_LOCALITY, ma.getLocality());
        cval.put(USERPROFILE_CITY, ma.getCity());
        cval.put(USERPROFILE_STATE, ma.getState());
        cval.put(USERPROFILE_COUNTRY, ma.getCountry());
        cval.put(USERPROFILE_PINCODE, ma.getPincode());

        db.insert(TABLE_USERPROFILE, null, cval);

        db.close();
        return true;
    }

    //ROLES : insertion.
    public static boolean insertRoles(List<String> roleList, String UPID) {
        final SQLiteDatabase db = open();
        for (String s :
                roleList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ROLES_ID, new LocalIdGen().nextLocalId());
            //The UPID is of the current User.
            contentValues.put(ROLES_CONTACT_ID, UPID);
            contentValues.put(ROLES_NAME, s);
            db.insert(TABLE_ROLES, null, contentValues);
        }
        db.close();
        return true;
    }

    //ROLES : fetch the roles as a List<String> Object based on value of UPID passed.
//Fail Return : null sent on not found.
    public static List<String> fetchRoles(String UPID) {
        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_ROLES,
                new String[]{ROLES_NAME},
                ROLES_CONTACT_ID + "=?",
                new String[]{UPID},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {
                stringList.add(cursor.getString(i));
            }
            cursor.close();
            db.close();
            return stringList;
        } else {
            db.close();
            return null;
        }
    }

    // Fetch Member from user profile table.
    // Fail Return : null.
    //NOTE : If the User has no role, then null is added to the role in Member Object.
    //Check for it when using this function.
    public static Member getMember(String id) {
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(TABLE_USERPROFILE,
                new String[]{USERPROFILE_UPID, USERPROFILE_FIRST_NAME, USERPROFILE_LAST_NAME,
                        USERPROFILE_EMAIL_ID, USERPROFILE_MOBILE_NO, USERPROFILE_GENDER,
                        USERPROFILE_DOB, USERPROFILE_DOJ, USERPROFILE_DESIGNATION,
                        USERPROFILE_DIVISION, USERPROFILE_HANDLER_ID, USERPROFILE_HANDLER_FIRST_NAME,
                        USERPROFILE_HANDLER_LAST_NAME, USERPROFILE_STATUS, USERPROFILE_ADDRLINE,
                        USERPROFILE_STREET, USERPROFILE_LOCALITY, USERPROFILE_CITY,
                        USERPROFILE_STATE, USERPROFILE_COUNTRY, USERPROFILE_PINCODE},
                USERPROFILE_UPID + "=?",
                new String[]{id},
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            List<String> roleList = new ArrayList<>();
            Cursor cursor1 = db.query(TABLE_ROLES,
                    new String[]{ROLES_NAME},
                    ROLES_CONTACT_ID + "=?",
                    new String[]{id},
                    null, null, null, null);
            if (cursor1 != null && cursor1.getCount() > 0) {
                for (int i = 0; i < cursor1.getCount(); i++) {
                    roleList.add(cursor1.getString(i));
                }
                cursor1.close();
            }
            MemberPersonalInfo mpi = new MemberPersonalInfo(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), roleList, cursor.getString(13));
            MemberAddress ma = new MemberAddress(cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getString(19), cursor.getString(20));

            cursor1.close();
            db.close();

            return new Member(mpi, ma);
        } else
            return null;
    }


    // HINTS :  Fetch status from user profile table.
    //Fail Return : String false.
    public static String fetchUserStatus(String id) {
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(TABLE_USERPROFILE,
                new String[]{USERPROFILE_STATUS},
                USERPROFILE_UPID + "=?",
                new String[]{id},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String s = cursor.getString(0);
            cursor.close();
            db.close();
            return s;
        } else
            return "false";
    }

    public static boolean updateUserStatus(String id, String status) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(USERPROFILE_STATUS, status);

        db.update(TABLE_USERPROFILE, cval, USERPROFILE_UPID + "=?", new String[]{id});
        return true;
    }


    // HINT : MOBILE INSERT OPERATION ON LOGIN TABLE:
    public static boolean insertLogin(String str) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(LOGIN_MOBILE_NO, str);
        cval.put(LOGIN_STATUS, Constants.STATUS[1]);

        db.delete(TABLE_LOGIN, null, null);
        db.insert(TABLE_LOGIN, null, cval);

        db.close();
        return true;
    }

    // HINT : STATUS UPDATE AFTER OTP VERIFIED IN LOGIN TABLE.
    public static boolean statusUpdate(String status) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(LOGIN_STATUS, status);

        db.update(TABLE_LOGIN, cval, LOGIN_STATUS + "= ?", new String[]{Constants.STATUS[1]});
        db.update(TABLE_LOGIN, cval, LOGIN_STATUS + "= ?", new String[]{Constants.STATUS[3]});
        db.close();
        return true;
    }


    //NOTE :: THE FUNCTIONS WERE CHECKED FROM HERE.
    //--------------------------------------------
    //============================================
    //____________________________________________

    // HINT : FETCH MOBILE NO FROM LOGIN TABLE.
    //Fail Return : Check for a return value of STRING false.
    public static String fetchMobileNo() {

        // Open database for Read / Write
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(TABLE_LOGIN,
                new String[]{LOGIN_MOBILE_NO},
                null,
                null,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String result = cursor.getString(0);
            cursor.close();
            db.close();
            // HINT : RETURN MOBILE NO.
            return result;
        } else return "false";
    }

    // HINT : FETCH STATUS FROM LOGIN TABLE.
    //Fail Return : Check for a return value of STRING false.
    public static String fetchStatus() {

        // Open database for Read / Write
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(TABLE_LOGIN,
                new String[]{LOGIN_STATUS},
                null,
                null,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String result = cursor.getString(0);
            cursor.close();
            db.close();
            // HINT : RETURN STATUS.
            return result;
        }
        return "false";
    }

    //Fetch my Groups using only a mobile no passed to it.
    //TODO : Get all the required information correctly.
    //Fail Return : null
    public static ArrayList<GroupClass> getMyGroups(String Mob_no){
        String ids,names;
        ArrayList<GroupClass> result;
        final SQLiteDatabase db =open();
        Cursor cursor=db.rawQuery("select gid,gname,grouprole from groups;",new String[]{});
        //Cursor cursor =db.rawQuery("select " + GROUP_ID + " , " + GROUP_NAME + " from "+ TABLE_GROUP + " where " + GROUP_ID +
        //       " = ( select " + GROUPMAP_GID  + " from " + TABLE_GROUPMAP + " where " + GROUPMAP_UPID +
        //       " = ( select " + USERPROFILE_ID +  " from " + TABLE_USERPROFILE + " where " + USERPROFILE_MOBILE_NO + " = ?));",
        //       new String[]{Mob_no});
        int length = cursor.getCount();


        if(cursor!=null && length>0) {
            result = new ArrayList<>();
            cursor.moveToFirst();

            for (int i = 0; i < length; i++) {
                GroupClass group = new GroupClass();
                group.setGid(cursor.getString(0));
                group.setGName(cursor.getString(1));
                group.setGRole(cursor.getString(2));
                result.add(group);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return result;
        }
        else
            return null;
    }

    //GROUP : Fetch gid using the Group Name.
    //Fail Return : String false.
    public static String fetchgidbygname(String gname) {
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(TABLE_GROUP,
                new String[]{GROUPMAP_GID},
                GROUP_NAME + "=?",
                new String[]{gname},
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String result = cursor.getString(0);
            cursor.close();
            db.close();
            return result;
        } else
            return "false";
    }

    //GROUPMESSAGING : fetch the messages in a group identified by GID.
    //Fail Return : null.
    public static ArrayList<Message> fetchMessages(String gid) {
        ArrayList<Message> ml = new ArrayList<Message>();

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_GROUPMESSAGING,
                new String[]{MSG_ID, MSG_TXT, MSG_FROM, MSG_GID, MSG_TYPE, MSG_EVENT_ID, MSG_POLL_ID, MSG_LOCAL_ID},
                MSG_GID + "=?",
                new String[]{gid},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Message m = new Message();
                m.setMid(cursor.getString(0));
                m.setMessage(cursor.getString(1));
                m.setSender(cursor.getString(2));
                m.setGid(cursor.getString(3));
                m.setType(cursor.getString(4));
                m.setEventID(cursor.getString(5));
                m.setPollID(cursor.getString(6));
                m.setLocalID(cursor.getString(7));
                ml.add(i, m);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return ml;
        } else
            return null;
    }

    //USERPROFILE : Fetch my ID from the UserProfile.
    //FailReturn : String false.
    public static String fetchID() {

        // Open database for Read / Write
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(TABLE_LOGIN,
                new String[]{LOGIN_MOBILE_NO},
                null,
                null,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Cursor cursor_new = db.query(TABLE_USERPROFILE,
                    new String[]{USERPROFILE_UPID},
                    USERPROFILE_MOBILE_NO + "=?",
                    new String[]{cursor.getString(0)},
                    null, null, null, null);

            if (cursor_new != null && cursor.getCount() > 0) {
                cursor_new.moveToFirst();

                String result = cursor_new.getString(0);
                cursor.close();
                cursor_new.close();
                //db.close();
                // HINT : RETURN CURRENT USER ID
                return result;
            } else
                return "false";
        } else
            return "false";
    }

    //PERSONALMESSAGING : Fetch all.
    //Fail Return : null.
    public static ArrayList<PersonalMessage> fetchPersonalMessages() {
        ArrayList<PersonalMessage> ml = new ArrayList<PersonalMessage>();

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_PERSONALMESSAGING,
                new String[]{PM_ID, PM_TXT, PM_FROM, PM_DATE, PM_SUBJECT},
                null,
                null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                PersonalMessage m = new PersonalMessage();
                m.setPmid(cursor.getString(0));
                m.setMessage(cursor.getString(1));
                m.setSender(cursor.getString(2));
                m.setDate(cursor.getString(3));
                m.setSubject(cursor.getString(4));
                ml.add(i, m);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return ml;
        } else
            return null;
    }

    //NEWS : Fetch all.
    //Fail Return : null.
    public static ArrayList<News> fetchNews() {
        ArrayList<News> ml = new ArrayList<News>();

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_NEWS,
                new String[]{NEWS_ID, NEWS_TITLE, NEWS_MESSAGE, NEWS_IMAGE_PATH, NEWS_POSTER},
                null,
                null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                News m = new News();
                m.setNid(cursor.getString(0));
                m.setTitle(cursor.getString(1));
                m.setMessage(cursor.getString(2));
                m.setImage_loc(cursor.getString(3));
                m.setPoster(cursor.getString(4));
                ml.add(i, m);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return ml;
        } else
            return null;
    }

    //NEWS : Static Table Creation Function.
    public static void createnewstable() {
        final SQLiteDatabase db = open();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NEWS +
                " (" + NEWS_ID + " TEXT , " +
                NEWS_TITLE + " TEXT, " +
                NEWS_MESSAGE + " TEXT, " +
                NEWS_IMAGE_PATH + " TEXT, " +
                NEWS_POSTER + " TEXT );"
        );
        db.close();
    }

    //Pending Requests : Static Table Creation Function.
    public static void createprtable() {
        final SQLiteDatabase db = open();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PR +
                " (" + PR_ID + " TEXT , " +
                PR_HANDLER + " TEXT, " +
                PR_POSTER + " TEXT, " +
                PR_TYPE + " TEXT, " +
                PR_ADDITIONALINFO + " TEXT );"
        );
        db.close();
    }

    //PENDING REQUESTS : Fetch all.
    //Fail Return : null.
    public static ArrayList<PendingRequest> fetchPendingRequests() {
        ArrayList<PendingRequest> ml = new ArrayList<PendingRequest>();

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_PR,
                new String[]{PR_ID, PR_HANDLER, PR_POSTER, PR_TYPE, PR_ADDITIONALINFO},
                null,
                null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                PendingRequest m = new PendingRequest();
                m.setPrid(cursor.getString(0));
                m.setUid_handler(cursor.getString(1));
                m.setUid_poster(cursor.getString(2));
                m.setType(cursor.getString(3));
                m.setAdditional_info(cursor.getString(4));
                ml.add(i, m);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return ml;
        } else
            return null;
    }

    //EVENTS : Static Table creation function.
    public static void createeventstable() {
        final SQLiteDatabase db = open();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS +
                " (" + EVENTS_ID + " TEXT , " +
                EVENTS_GID + " TEXT, " +
                EVENTS_MESSAGE + " TEXT, " +
                EVENTS_STATUS + " TEXT, " +
                EVENTS_POSTED_BY + " TEXT, " +
                EVENTS_DATETIME + " TEXT, " +
                EVENTS_DURATION + " TEXT );"
        );
        db.close();
    }

    //EVENTS : Fetch all.
    //Fail Return : null.
    public static ArrayList<Event> fetchEvents() {
        ArrayList<Event> eventList = new ArrayList<Event>();

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_EVENTS,
                new String[]{EVENTS_ID, EVENTS_GID, EVENTS_MESSAGE, EVENTS_STATUS, EVENTS_POSTED_BY, EVENTS_DATETIME, EVENTS_DURATION},
                EVENTS_STATUS + "=?",
                new String[]{"new"},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Event m = new Event();
                m.setEID(cursor.getString(0));
                m.setGID(cursor.getString(1));
                m.setEventMessage(cursor.getString(2));
                m.setStatus(cursor.getString(3));
                m.setPostedBy(cursor.getString(4));
                m.setDateTime(cursor.getString(5));
                m.setDuration(cursor.getString(6));
                eventList.add(i, m);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return eventList;
        } else
            return null;
    }

    //EVENTS : Fetch Event by using its ID.
    //Fail Return : null.
    public static Event fetchEvent(String eventid) {
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(TABLE_EVENTS,
                new String[]{EVENTS_MESSAGE, EVENTS_DATETIME, EVENTS_DURATION, EVENTS_ID, EVENTS_STATUS},
                EVENTS_ID + "=?",
                new String[]{eventid},
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Event m = new Event();
            m.setEventMessage(cursor.getString(0));
            m.setDateTime(cursor.getString(1));
            m.setDuration(cursor.getString(2));
            m.setEID(cursor.getString(3));
            m.setStatus(cursor.getString(4));
            cursor.close();
            db.close();
            return m;
        } else
            return null;
    }

    //POLL : Fetch Poll by using Poll ID.
    //Poll also contains the POLLMAPPING Object.
    //Fail Return : null, if either fails.
    public static Poll fetchPoll(String pollid) {
        final SQLiteDatabase db = open();
        ArrayList<PollMapping> pollMappings = new ArrayList<>();

        //Get the poll mapping in one place.
        Cursor cursor1 = db.query(TABLE_POLLMAPPING,
                new String[]{POLL_MAPPING_ANSWER_TITLE, POLL_MAPPING_NUM_VOTES},
                POLL_PID + "=?",
                new String[]{pollid},
                null, null, null, null);

        if (cursor1 != null && cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            for (int i = 0; i < cursor1.getCount(); i++) {
                PollMapping m = new PollMapping();
                m.setAnswerTitle(cursor1.getString(0));
                m.setNumberOfVotes(cursor1.getString(1));
                pollMappings.add(m);
                cursor1.moveToNext();
            }
            cursor1.close();


            Cursor cursor = db.query(TABLE_POLL,
                    new String[]{POLL_TITLE, POLL_DURATION, POLL_NUM_ANS},
                    POLL_PID + "=?",
                    new String[]{pollid},
                    null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Poll m = new Poll();
                m.setTitle(cursor.getString(0));
                m.setDuration(cursor.getString(1));
                m.setNumber_answers(Integer.valueOf(cursor.getString(2)));
                m.setPid(pollid);
                m.setPm(pollMappings);
                cursor.close();
                db.close();
                return m;

            } else
                return null;
        } else
            return null;
    }

    //POLL Table creation function.
    //NOTE : The columns could be old. Check if using.
    public static void createpolltable() {
        final SQLiteDatabase db = open();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_POLL +
                " (" + POLL_PID + " TEXT , " +
                POLL_TITLE + " TEXT, " +
                POLL_NUM_ANS + " TEXT, " +
                POLL_CREATOR_ID + " TEXT, " +
                POLL_DURATION + " TEXT, " +
                POLL_STATUS + " TEXT, " +
                POLL_DATETIME + " TEXT, " +
                POLL_GID + " TEXT );"

        );
        db.close();
    }

    //POLLMAPPING Table creation function.
    public static void createpollmappingtable() {
        final SQLiteDatabase db = open();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_POLLMAPPING +
                " (" + POLL_MAPPING_AID + " TEXT , " +
                POLL_MAPPING_PID + " TEXT, " +
                POLL_MAPPING_ANSWER_TITLE + " TEXT, " +
                POLL_MAPPING_NUM_VOTES + " TEXT );"

        );
        db.close();
    }

    //PERSONALMESSAGING : Fetch the messages that have id as "null".
    //The PMs should have a localID set.
    //NOTE : IF Receivers are not set, then the Receiver Object would be null. CHECK FOR NULL!!
    //Fail Return : null.
    public static ArrayList<PersonalMessage> fetchNewPersonalMessages() {
        ArrayList<PersonalMessage> pmList = new ArrayList<PersonalMessage>();

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_PERSONALMESSAGING,
                new String[]{PM_ID, PM_FROM, PM_SUBJECT, PM_TXT, PM_LOCALID},
                PM_ID + "=?",
                new String[]{"null"},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                PersonalMessage m = new PersonalMessage();
                m.setPmid(cursor.getString(0));
                m.setSender(cursor.getString(1));
                m.setSubject(cursor.getString(2));
                m.setMessage(cursor.getString(3));
                m.setLocalID(cursor.getString(4));

                //Find associated receiver list
                Cursor cursor1 = db.query(TABLE_PERSONALMESSAGEMAPPING,
                        new String[]{PERSONALMESSAGEMAPPING_PMID, PERSONALMESSAGEMAPPING_UID},
                        PERSONALMESSAGEMAPPING_PMID + "=?",
                        new String[]{cursor.getString(4)},
                        null, null, null, null);
                if (cursor1 != null && cursor1.getCount() > 0) {
                    //Log.d("DB", "fetchNewPersonalMessages: cursor is not null");
                    cursor1.moveToFirst();
                    ArrayList<String> pmReceiver = new ArrayList<>();
                    for (int j = 0; j < cursor1.getCount(); j++) {
                        String receiver;
                        receiver = cursor1.getString(1);
                        pmReceiver.add(j, receiver);
                        cursor1.moveToNext();
                    }
                    cursor1.close();
                    m.setReceivers(pmReceiver);
                } else m.setReceivers(null);

                pmList.add(i, m);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return pmList;
        } else {
            db.close();
            return null;
        }
    }

    //PM : Updates the PMID after finding it via a  localID field that is randomly generated.
    public static void updatePMID(String localid, String newID) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(PM_ID, newID);
        db.update(TABLE_PERSONALMESSAGING, cval, PM_LOCALID + "= ?", new String[]{localid});
        db.close();

    }

    //PM : Get the complete PM Object after Specifying an ID.
    //Fail Return : null.
    //NOTE : The Receiver may be null inside the PM Object. Check!
    public static PersonalMessage getPMbyID(String ID) {
        final SQLiteDatabase db = open();


        Cursor cursor = db.query(TABLE_PERSONALMESSAGING,
                new String[]{PM_TXT, PM_FROM, PM_SUBJECT, PM_DATE, PM_LOCALID},
                PM_ID + "=?",
                new String[]{ID},
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            PersonalMessage m = new PersonalMessage();
            m.setMessage(cursor.getString(0));
            m.setSender(cursor.getString(1));
            m.setSubject(cursor.getString(2));
            m.setDate(cursor.getString(3));
            m.setLocalID(cursor.getString(4));

            Cursor cursor1 = db.query(TABLE_PERSONALMESSAGEMAPPING,
                    new String[]{PERSONALMESSAGEMAPPING_PMID, PERSONALMESSAGEMAPPING_UID},
                    PERSONALMESSAGEMAPPING_PMID + "=?",
                    new String[]{cursor.getString(4)},
                    null, null, null, null);
            if (cursor1 != null && cursor1.getCount() > 0) {
                Log.d("DB", "fetchNewPersonalMessages: cursor is not null");
                cursor1.moveToFirst();
                ArrayList<String> pmReceiver = new ArrayList<>();
                for (int j = 0; j < cursor1.getCount(); j++) {
                    String receiver;
                    receiver = cursor1.getString(1);
                    pmReceiver.add(j, receiver);
                    cursor1.moveToNext();
                }
                cursor1.close();
                m.setReceivers(pmReceiver);
            } else m.setReceivers(null);

            cursor.close();
            db.close();
            return m;
        } else
            return null;

    }

    //GM : Updates the GroupMessageID after finding a particular roe  with the use of localID.
    public static void updateGMByID(String localid, String newID) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(MSG_ID, newID);
        db.update(TABLE_GROUPMESSAGING, cval, MSG_LOCAL_ID + "= ?", new String[]{localid});
        db.close();

    }

    //EVENTS : update the event ID directly.
    public static void updateEventByID(String oldId, String newID) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(EVENTS_ID, newID);
        db.update(TABLE_EVENTS, cval, EVENTS_ID + "= ?", new String[]{oldId});
        db.close();

    }

    //MESSAGING : fetch all group messages by mID. Returns array of messages.
    //Fail Return : null.
    public static ArrayList<Message> fetchMessagesById(String id) {
        ArrayList<Message> ml = new ArrayList<Message>();

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_GROUPMESSAGING,
                new String[]{MSG_ID, MSG_TXT, MSG_FROM, MSG_GID, MSG_TYPE, MSG_EVENT_ID, MSG_POLL_ID, MSG_LOCAL_ID},
                MSG_ID + "=?",
                new String[]{id},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Message m = new Message();
                m.setMid(cursor.getString(0));
                m.setMessage(cursor.getString(1));
                m.setSender(cursor.getString(2));
                m.setGid(cursor.getString(3));
                m.setType(cursor.getString(4));
                m.setEventID(cursor.getString(5));
                m.setPollID(cursor.getString(6));
                m.setLocalID(cursor.getString(7));
                ml.add(i, m);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return ml;
        } else
            return null;
    }

    //NEWS : fetch all news by NID. Returns array.
    //Fail Return : null.
    public static ArrayList<News> fetchNewsById(String ID) {
        ArrayList<News> ml = new ArrayList<News>();

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_NEWS,
                new String[]{NEWS_ID, NEWS_TITLE, NEWS_MESSAGE, NEWS_IMAGE_PATH, NEWS_POSTER, NEWS_LOCAL_ID},
                NEWS_ID + "=?",
                new String[]{ID},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                News m = new News();
                m.setNid(cursor.getString(0));
                m.setTitle(cursor.getString(1));
                m.setMessage(cursor.getString(2));
                m.setImage_loc(cursor.getString(3));
                m.setPoster(cursor.getString(4));
                m.setLocalID(cursor.getString(5));
                ml.add(i, m);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return ml;
        } else
            return null;
    }

    //NEWS : update the nID using the localID.
    public static void updateNewsByID(String localid, String newID) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(NEWS_ID, newID);
        db.update(TABLE_NEWS, cval, NEWS_LOCAL_ID + "= ?", new String[]{localid});
        db.close();

    }

    //EVENTS : update the EventID in message.
    public static void updateEventIdinMessage(String eventId, String gmId) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(MSG_EVENT_ID, eventId);
        db.update(TABLE_GROUPMESSAGING, cval, MSG_ID + "= ?", new String[]{gmId});
        db.close();
    }

    //GROUPMESSAGING : insert message using Message Object.
    //NOTE : If FAIL detected, i.e., non-insertion, then check whether all the columns have been inserted into.
    public static boolean insertMessage(Message message) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(MSG_ID, message.getMid());
        cval.put(MSG_TYPE, message.getType());
        cval.put(MSG_GID, message.getGid());
        cval.put(MSG_TXT, message.getMessage());
        cval.put(MSG_EVENT_ID, message.getEventID());
        cval.put(MSG_POLL_ID, message.getPollID());
        cval.put(MSG_LOCAL_ID, new LocalIdGen().nextLocalId());
        cval.put(MSG_FROM, message.getSender());
        Log.d("Message : ", "insertMessage: " + message.getPollID());
        db.insert(TABLE_GROUPMESSAGING, null, cval);

        db.close();
        return true;
    }

    //NEWS : insert a news item into the database. LocalId is generated here.
    public static boolean insertNewNews(News news) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();

        cval.put(NEWS_ID, news.getNid());
        cval.put(NEWS_LOCAL_ID, new LocalIdGen().nextLocalId());
        cval.put(NEWS_IMAGE_PATH, news.getImage_loc());
        cval.put(NEWS_MESSAGE, news.getMessage());
        cval.put(NEWS_POSTER, news.getPoster());
        cval.put(NEWS_TITLE, news.getTitle());

        db.insert(TABLE_NEWS, null, cval);

        db.close();
        return true;
    }

    //EVENTS : insert new Event using a new Event Object.
    public static boolean insertnewEvent(Event event) {
        final SQLiteDatabase db = open();
        ContentValues contentValues = new ContentValues();

        contentValues.put(EVENTS_ID, event.getEID());
        contentValues.put(EVENTS_DURATION, event.getDuration());
        contentValues.put(EVENTS_DATETIME, event.getDateTime());
        contentValues.put(EVENTS_GID, event.getGID());
        contentValues.put(EVENTS_STATUS, "new");
        contentValues.put(EVENTS_POSTED_BY, event.getPostedBy());

        db.insert(TABLE_EVENTS, null, contentValues);
        db.close();
        return true;
    }

    //POLLMAPPING : update the number of votes for an answer by title.
    public static boolean updatePollMappingAnswerNumberByTitle(String title) {
        final SQLiteDatabase db = open();
        ContentValues cval = new ContentValues();
        cval.put(POLL_MAPPING_NUM_VOTES, DatabaseService.fetchPollMappingAnswerNumberByTitle(title) + 1);
        db.update(TABLE_POLLMAPPING, cval, POLL_MAPPING_ANSWER_TITLE + "= ?", new String[]{title});
        db.close();
        return true;
    }

    //POLLMAPPING : get the number of votes for a answer.
    //Fail Return : -1.
    public static int fetchPollMappingAnswerNumberByTitle(String title) {
        // Open database for Read / Write
        final SQLiteDatabase db = open();


        Cursor cursor_new = db.query(TABLE_POLLMAPPING,
                new String[]{POLL_MAPPING_NUM_VOTES},
                POLL_MAPPING_ANSWER_TITLE + "=?",
                new String[]{title},
                null, null, null, null);
        try {
            if (cursor_new != null && cursor_new.getCount() > 0) {
                cursor_new.moveToFirst();
                return Integer.valueOf(cursor_new.getString(0));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //db.close();
        // TODO : Check for -1 in calling finction.
        return -1;
    }

    //POLL : set status for the poll.
    public static void setPollStatus(String status, String pID) {
        final SQLiteDatabase db = open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POLL_STATUS, status);
        db.update(TABLE_POLL, contentValues, POLL_PID + "= ?", new String[]{pID});
        db.close();
    }

    //POLL : check poll status.
    //Fail Return : String false.
    public static String checkPollStatus(String pID) {
        final SQLiteDatabase db = open();


        Cursor cursor_new = db.query(TABLE_POLL,
                new String[]{POLL_STATUS},
                POLL_PID + "=?",
                new String[]{pID},
                null, null, null, null);
        try {
            if (cursor_new != null && cursor_new.getCount() > 0) {
                cursor_new.moveToFirst();
                String s = cursor_new.getString(0);
                cursor_new.close();
                db.close();
                return s;
            } else
                return "false";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    //POLLMAPPING : get answer mapping by poll id.
    //Fail Return : null.
    public static ArrayList<PollMapping> fetchPollMappingByPollID(String pID) {
        ArrayList<PollMapping> pollMappingArray = new ArrayList<PollMapping>();
        final SQLiteDatabase db = open();
        Cursor cursor_new = db.query(TABLE_POLLMAPPING,
                new String[]{POLL_MAPPING_ANSWER_TITLE, POLL_MAPPING_NUM_VOTES},
                POLL_MAPPING_PID + "=?",
                new String[]{pID},
                null, null, null, null);
        if (cursor_new != null && cursor_new.getCount() > 0) {
            cursor_new.moveToFirst();
            for (int i = 0; i < cursor_new.getCount(); i++) {
                PollMapping pollMapping = new PollMapping();
                pollMapping.setAnswerTitle(cursor_new.getString(0));
                pollMapping.setNumberOfVotes(cursor_new.getString(1));
                pollMappingArray.add(pollMapping);
                cursor_new.moveToNext();
            }
            cursor_new.close();
            db.close();
            return pollMappingArray;
        } else {
            db.close();
            return null;
        }
    }

    //LOGIN : Fetch last login time
    //Fail return : String false.
    public static String fetchLastLogin() {
        final SQLiteDatabase db = open();
        Cursor cursor = db.query(TABLE_LOGIN,
                new String[]{LOGIN_LAST_LOGIN},
                null,
                null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String s = cursor.getString(0);
            cursor.close();
            db.close();
            return s;
        } else
            return "false";
    }

    //PERSONALMESSAGE : insert an array of personal messages.
    //Dependency : checkIfPMExistsByID()
    public static void insertPersonalMessages(ArrayList<PersonalMessage> personalMessages) {
        final SQLiteDatabase db = open();
        for (PersonalMessage personalMessage :
                personalMessages) {

            // TODO : Add a checkifexists() here for same ID messages.
            if (DatabaseService.checkIfPMExistsByID(personalMessage.getPmid()))
                continue;

            ContentValues contentValues = new ContentValues();

            contentValues.put(PM_ID, personalMessage.getPmid());
            contentValues.put(PM_DATE, personalMessage.getDate());
            contentValues.put(PM_FROM, personalMessage.getSender());
            contentValues.put(PM_LOCALID, new LocalIdGen().nextLocalId());
            contentValues.put(PM_SUBJECT, personalMessage.getSubject());
            contentValues.put(PM_TXT, personalMessage.getMessage());

            db.insert(TABLE_PERSONALMESSAGING, null, contentValues);
        }
        db.close();
    }

    //PERSONALMESSAGING : check the existence of a personalmessage by ID.
    //Since there is a dependency, the db Object is not pre-maturely closed.
    //Fail Return : boolean false.
    //TODO : Possible ERROR here. Since the return is boolean, no way to differentiate whether the
    //function failed or the ID does not exist.
    public static boolean checkIfPMExistsByID(String pmID) {
        final SQLiteDatabase db = open();
        Cursor cursor_new = db.query(TABLE_PERSONALMESSAGING,
                new String[]{PM_ID},
                PM_ID + "=?",
                new String[]{pmID},
                null, null, null, null);
        if (cursor_new == null || cursor_new.getCount() <= 0)
            return false;
        else {
            cursor_new.moveToFirst();
            if (cursor_new.getString(0).equals(pmID)) {
                cursor_new.close();
                return true;
            } else
                return false;
        }
    }

    //POLL : fetch complete Poll Object (complete with PollMapping) by using its local ID.
    //Fail Return : null.
    public static Poll fetchPollByLocalID(String pollLocalId) {
        final SQLiteDatabase db = open();
        ArrayList<PollMapping> pollMappings = new ArrayList<>();

        //Get the poll mapping in one place.
        Cursor cursor1 = db.query(TABLE_POLLMAPPING,
                new String[]{POLL_MAPPING_ANSWER_TITLE, POLL_MAPPING_NUM_VOTES},
                POLL_MAPPING_PID + "=?",
                new String[]{pollLocalId},
                null, null, null, null);

        if (cursor1 != null && cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            for (int i = 0; i < cursor1.getCount(); i++) {
                PollMapping m = new PollMapping();
                m.setAnswerTitle(cursor1.getString(0));
                m.setNumberOfVotes(cursor1.getString(1));
                m.setPid(pollLocalId);
                pollMappings.add(m);
                cursor1.moveToNext();
            }
            cursor1.close();


            Cursor cursor = db.query(TABLE_POLL,
                    new String[]{POLL_TITLE, POLL_DURATION, POLL_NUM_ANS},
                    POLL_PID + "=?",
                    new String[]{pollLocalId},
                    null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Poll m = new Poll();
                m.setTitle(cursor.getString(0));
                m.setDuration(cursor.getString(1));
                m.setNumber_answers(Integer.valueOf(cursor.getString(2)));
                m.setPid(pollLocalId);
                m.setPm(pollMappings);
                cursor.close();
                db.close();
                return m;

            } else
                return null;
        } else
            return null;
    }

    //POLL : Insert into Poll Table as well as PollMappingTable.
    //returns the localID that was set for the poll.
    //TODO : Not Complete. ex: DateTime.
    public static String insertPoll(Poll poll) {
        final SQLiteDatabase db = open();
        String localID = new LocalIdGen().nextLocalId();
        //Insert into poll mapping table first.
        for (PollMapping pollMapping :
                poll.getPm()) {
            ContentValues cv = new ContentValues();
            cv.put(POLL_MAPPING_PID, poll.getPid());
            cv.put(POLL_MAPPING_AID, "null");
            cv.put(POLL_MAPPING_ANSWER_TITLE, pollMapping.getAnswerTitle());
            cv.put(POLL_MAPPING_NUM_VOTES, "0");
            db.insert(TABLE_POLLMAPPING, null, cv);
        }
        //Now the Poll table entry.
        ContentValues cv = new ContentValues();
        cv.put(POLL_CREATOR_ID, DatabaseService.fetchID());
        cv.put(POLL_DATETIME, "null");
        cv.put(POLL_DURATION, poll.getDuration());
        //cv.put(POLL_GID,poll.get);
        cv.put(POLL_NUM_ANS, poll.getNumber_answers());
        cv.put(POLL_PID, poll.getPid());
        Log.d("Poll : ", "onClick: " + poll.getPid());
        cv.put(POLL_TITLE, poll.getTitle());
        cv.put(POLL_LOCAL_ID, localID);
        db.insert(TABLE_POLL,null,cv);
        db.close();
        return localID;
    }

    //GROUPS : Insert into the table GROUPS.
    public static void insertGroups(ArrayList<GroupClass> groupClasses) {
        final SQLiteDatabase db = open();
        for (GroupClass groupClass :
                groupClasses) {
            ContentValues cv = new ContentValues();
            cv.put(GROUP_ID, groupClass.getGid());
            cv.put(GROUP_NAME, groupClass.getGName());
            cv.put(GROUP_ROLE, groupClass.getGRole());
            cv.put(GROUP_TYPE, groupClass.getGType());
            cv.put(GROUP_STATUS, groupClass.getStatus());
            db.insert(TABLE_GROUP, null, cv);
        }
        db.close();
    }

    //POLL : update the poll ID.
    public static void updatePollByID(String oldId, String newID) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(POLL_PID, newID);
        db.update(TABLE_POLL, cval, POLL_PID + "= ?", new String[]{oldId});
        db.close();

    }

    //POLLMAPPING : update the poll mapping ID.
    public static void updatePollMappingID(String oldId, String newID) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(POLL_MAPPING_PID, newID);
        db.update(TABLE_POLLMAPPING, cval, POLL_MAPPING_PID + "= ?", new String[]{oldId});
        db.close();

    }

    //GROUPMESSAGING : POLLID : update the PollID in messsage.
    public static void updatePollIdinMessage(String pollId, String gmId) {
        final SQLiteDatabase db = open();

        ContentValues cval = new ContentValues();
        cval.put(MSG_POLL_ID, pollId);
        db.update(TABLE_GROUPMESSAGING, cval, MSG_ID + "= ?", new String[]{gmId});
        db.close();
    }

    //LOGIN : insert the current date/time into the table :
    //NOTE : The month variable has been incremented by one inside.
    public static void updateLoginDateTime(){
        final SQLiteDatabase db = open();
        ContentValues contentValues = new ContentValues();
        String DateTime;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1);
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd\'T\'hh:mm:ss\'Z\'", Locale.getDefault());
        //calendar.setTime(simpleDateFormat.parse(event.getDateTime()));
        try {
            contentValues.put(LOGIN_LAST_LOGIN, simpleDateFormat.format(date));
        }catch (IllegalArgumentException iae){iae.printStackTrace();}
        db.update(TABLE_LOGIN,contentValues,null,null);
        db.close();
    }


}