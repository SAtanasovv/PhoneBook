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
}
