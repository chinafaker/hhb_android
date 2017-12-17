package base;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import utils.SystemUtils;

public class App extends MultiDexApplication {
    /**
     * 程序是否在前台
     */
    public static boolean isActive = true;
    public static String userAgent;
    private static Context context;
    public static App app;

    public static Context getContext() {
        return context;
    }

    private static SharedPreferences sharedPref;

    public static void setContext(Context context) {
        App.context = context;
    }

    public static final String COM_FLAG = "miaomiaoke";
    private static final String PREFIX_USER_TOKEN = "user_token";
    private static final String USER_PHONE = "user_phone";
    private static final String USER_NORMAL_TOKEN = "user_normal_token";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        app = this;

        sharedPref = getSharedPreferences(COM_FLAG, Context.MODE_PRIVATE);
        // 设置请求体相关数据
        userAgent = getUserAgent();

/*        HttpConfig httpConfig = new HttpConfig();
        //  httpConfig.setAgent(true);//有代理的情况能不能访问
        httpConfig.setDebug(true);//是否debug模式 如果是debug模式打印log
        httpConfig.setTagName("QRM");//打印log的tagname

        //可以添加一些公共字段 每个接口都会带上
        httpConfig.addCommonField("pf", "android");
        httpConfig.addCommonField("version_code", "" + Utils.getVersionCode(getApplicationContext()));
        //初始化HTTPCaller类
        HTTPCaller.getInstance().setHttpConfig(httpConfig);*/
    }


    public static String getUserAgent() {
        return "MiaoMiaoKe/" + SystemUtils.getAppVersionName() + "("
                + SystemUtils.getDeviceInfo() + ";"
                + SystemUtils.getSystemVersion() + ";"
                + SystemUtils.getLanguageInfo()
                + ")";
    }

    public static String token() {
        return sharedPref.getString(PREFIX_USER_TOKEN + getUserphone(), "");
    }

    public static String getUserphone() {
        return sharedPref.getString(USER_PHONE, "");
    }


    public static String normalToken() {
        return sharedPref.getString(USER_NORMAL_TOKEN, "");
    }


    public static App getApp() {
        return app;
    }

}
