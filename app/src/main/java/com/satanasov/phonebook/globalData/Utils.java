package com.satanasov.phonebook.globalData;

public class Utils {
    public enum ChangeOptions {
        ADD_CONTACT ,
        EDIT_CONTACT,
        VIEW_CONTACT
    }

     public static String INTENT_EXTRA_OPTION  = "intentExtraOption";
     public static String INTENT_USER_DETAILS  = "intentUserDetails";

     public static Long    WORK_PHONE_NUMBER    = 1L;
     public static Long    MAIN_PHONE_NUMBER    = 2L;
     public static Long    HOME_PHONE_NUMBER    = 3L;
     public static Long    MOBILE_PHONE_NUMBER  = 4L;

     public static Long    HOME_EMAIL    = 1L;
     public static Long    WORK_EMAIL    = 2L;

     public static int     CONTACT_ID          = 0;
     public static int     CONTACT_FIRST_NAME  = 1;
     public static int     CONTACT_LAST_NAME   = 2;

     public static int     CONTACT_NUMBER_ID   = 0;
     public static int     CONTACT_NUMBER      = 1;
     public static int     CONTACT_NUMBER_TYPE = 2;

     public static int     CONTACT_EMAIL_ID    = 0;
     public static int     CONTACT_EMAIL       = 1;
     public static int     CONTACT_EMAIL_TYPE  = 2;


}
