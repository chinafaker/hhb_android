package utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import java.net.HttpURLConnection;
import java.net.URL;


public class StringUtils {

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            return true;
        }
        return TextUtils.isEmpty(str);
    }


    /**
     * 返回非“null”
     *
     * @param str
     * @return
     */
    public static String noNull(String str) {
        if (str == null || str.equals("null")) {
            return "";
        } else {
            return str;
        }
    }

    public static String noNull(String str, String s) {
        if (str == null || str.equals("null")) {
            return s;
        } else {
            return str;
        }
    }

    /**
     * 判断url是否有效
     *
     * @param strLink
     * @return
     */
    public static boolean isUrlValid(String strLink) {
        URL url;
        try {
            url = new URL(strLink);
            HttpURLConnection connt = (HttpURLConnection) url.openConnection();
            connt.setRequestMethod("HEAD");
            String strMessage = connt.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            connt.disconnect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

}
