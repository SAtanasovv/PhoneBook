package com.satanasov.phonebook.globalData;

public class Utils {
    public enum ChangeOptions {
        ADD_CONTACT ,
        EDIT_CONTACT,
        VIEW_CONTACT
    }

     public static String INTENT_EXTRA_OPTION  = "intentExtraOption";
     public static String INTENT_USER_DETAILS  = "intentUserDetails";

     public static Long    HOME_PHONE_NUMBER   = 0L;
     public static Long    MOBILE_PHONE_NUMBER = 1L;
     public static Long    WORK_PHONE_NUMBER   = 2L;
     public static Long    MAIN_PHONE_NUMBER   = 3L;

     public static float ROTATE_TO_180_DEGREES = 180f;
     public static float ROTATE_TO_0_DEGREES   = 0f;

     public static Long    HOME_EMAIL    = 1L;
     public static Long    WORK_EMAIL    = 2L;

     public static int     INSERT   = 1;
     public static int     UPDATE   = 2;
     public static int     DELETE   = 3;


}
