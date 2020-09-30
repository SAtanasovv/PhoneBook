package com.satanasov.phonebook.globalData;

public class Utils {
    public enum ChangeOptions {
        ADD_CONTACT ,
        EDIT_CONTACT,
        VIEW_CONTACT
    }

     public static String INTENT_EXTRA_OPTION  = "intentExtraOption";
     public static String INTENT_USER_DETAILS  = "intentUserDetails";

     public static Long    WORK_PHONE_NUMBER    = Long.valueOf(1);
     public static Long    MAIN_PHONE_NUMBER    = Long.valueOf(2);
     public static Long    HOME_PHONE_NUMBER    = Long.valueOf(3);
     public static Long    MOBILE_PHONE_NUMBER  = Long.valueOf(4);


}
