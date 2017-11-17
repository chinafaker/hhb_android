package utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;



import java.util.Locale;

import base.App;


public class SystemUtils {
    public static final String companyPhone = "4008881500";

    /**
     * 拨打公司电话
     * @param context
     */
    public static void toCallCompany(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + companyPhone));
        context.startActivity(intent);
    }

    /**
     * 拨打公司电话
     * @param context
     */
    public static void toCallCompany(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInputFromWindow(Context context, View edi) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edi.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     * 警告：对于刚跳到一个新的界面就要弹出软键盘的情况上述代码可能由于界面为加载完全而无法弹出软键盘。
     */
    public static void showSoftInput(Context context, View edi) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edi, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 用于是否要设置systembar（系统状态栏）
     *
     * @return 平台版本是否高于android 4.4
     */
    public static boolean isPlatformVersionOver44() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @return 系统状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * @return App版本号
     */
    public static String getAppVersionName() {
        PackageManager pm = App.getApp().getPackageManager();
        String versionName = "";
        try {
            versionName = pm.getPackageInfo(App.getApp().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return versionName;
    }

    /**
     * @return 设备品牌
     */
    public static String getDeviceInfo() {
        return Build.BRAND + " " + Build.MODEL;
    }

    /**
     * @return 获取系统版本
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE + " " + Build.VERSION.SDK_INT;
    }

    /**
     * @return 获取语言信息
     */
    public static String getLanguageInfo() {
        Locale locale = App.getApp().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language;
    }
}
