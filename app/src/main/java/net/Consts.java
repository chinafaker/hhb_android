package net;

import com.daikin.rqm.BuildConfig;

public class Consts {
    //public static String BASE_ADDRESS_RQM = "http://121.40.150.64:8080/rqmweb";
    //public static String RQM_CONTAINER_LIST_URL = "http://121.40.150.64:8080/rqmweb/page/container_list.html";
    //public static String RQM_TOP_NOTICE_URL = "http://121.40.150.64:8080/rqmweb/page/top_notice.html";

    public static String BASE_ADDRESS_RQM = BuildConfig.SERVER_URL + "/rqmweb";
    public static String RQM_CONTAINER_LIST_URL = BuildConfig.SERVER_URL + "/rqmweb/page/container_list.html";
    public static String RQM_TOP_NOTICE_URL = BuildConfig.SERVER_URL + "/rqmweb/page/top_notice.html";


    public static final String CHANNEL = "android";
    public static final String NET_VERSION = "1.0";
    public static final String SHAREDPREFENCERQM = "sharedPrefenceRQM";
    public static final String USETID = "userID";
    public static final String ISREGISTERUSERIDTIOUC = "isRegisterUserIDTouchID";
    public static final String NOTDISPLAYTODAY = "notdisplayagaintoday";
    public static final String LASTTIMESTR = "lastdatestr";
    public static String BASE_ADDRESS = "";
    public static String BASE_ADDRESS_HTTP = "";

    public static String urllinkend = "urllinkend";
    public static int PAGE_SIZE = 10;

    public static class PwdStrength {
        public static final int WEAK = 1;
        public static final int GENERAL = 2;
        public static final int STRONG = 3;
    }
}
