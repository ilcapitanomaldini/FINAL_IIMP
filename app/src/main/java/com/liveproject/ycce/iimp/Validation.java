package com.liveproject.ycce.iimp;

import com.liveproject.ycce.iimp.constants.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tiger on 19-01-2017.
 */

public class Validation {

    public static boolean isEmpty(String string){
        if (string == null || string.isEmpty())
            return true;
        else
            return false;
    }

    public static boolean isValidName(String name) {
        if (name.matches("[A-Z][a-zA-Z]*"))
            return true;
        else
            return false;
    }

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidMobile(String phone) {
        if(phone.length() == 10 && !(phone.contains(" ")))
            return true;
        else
            return false;
    }

    public static boolean isValidDOB(String dateStr) {
        try {
            Calendar calMax = Calendar.getInstance();
            calMax.add(Calendar.YEAR, -Constants.MIN_AGE_LIMIT); //18
            Date dateLimit = calMax.getTime();

            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date = format.parse(dateStr);

            if (date != null && dateLimit != null) {
                if (date.before(dateLimit)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidDesignation(String designation){
        if(designation.contains("Choose your designation"))
            return false;
        else
            return true;
    }

    public static boolean isValidDivision(String division) {
        if(division.contains("Choose your division"))
            return false;
        else
            return true;
    }
}
