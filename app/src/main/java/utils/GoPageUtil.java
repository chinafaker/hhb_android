package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import preGuide.GuideActivity;
import web.WebviewHome;


/**
 * @ClassName: GoPageUtil
 * @Description: 跳转的工具类
 */

public class GoPageUtil {

    public static void goPage(Context context, Class clz) {
        Intent intent = null;
        intent = new Intent(context, clz);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param clz
     * @param requestCode
     */
    public static void goPage(Context context, Class clz, int requestCode) {
        Intent intent = null;
        intent = new Intent(context, clz);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * @param context
     * @param clz
     * @param bundle
     * @return void 返回类型
     * @Title: goPage
     * @Description: 跳转
     * @author vincent
     */
    public static void goPage(Context context, Class clz, Bundle bundle) {
        Intent intent = null;
        intent = new Intent(context, clz);
        if (bundle != null)
            intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param clz
     * @param bundle
     * @param requestCode
     */
    public static void goPage(Context context, Class clz, Bundle bundle, int requestCode) {
        Intent intent = null;
        intent = new Intent(context, clz);
        if (bundle != null)
            intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    // 跳转到其他页面
    public static void jumpToActivity(Context context, Class cls) {
        context.startActivity(new Intent(context, cls));
    }

    //跳转到webview或者指定页面
    public static void jumpTobyUrlLink(Context context, String linkUrl) {
        jumpTobyUrlLink(context, linkUrl, "", null);
    }

    public static void jumpTobyUrlLink(Context context, String linkUrl, String title) {
        jumpTobyUrlLink(context, linkUrl, title, null);
    }

    public static void jumpTobyUrlLink(Context context, String linkUrl, String title, Bundle bundlebundle) {
        if (StringUtils.isEmpty(linkUrl)) {
            return;
        }
        if (linkUrl.startsWith("###")) {
            if (linkUrl.contains("###toAppHome")) {
                Bundle bundle = new Bundle();
                GoPageUtil.jumpToActivity(context, GuideActivity.class, bundle);
            }
        } else {
            if (bundlebundle == null) {
                Intent intent = new Intent();
                Bundle bundle3 = new Bundle();
                bundle3.putString("url", linkUrl);
                if (!StringUtils.isEmpty(title)) {
                    bundle3.putString("title", title);
                }
                intent.setClass(context, WebviewHome.class);
                intent.putExtras(bundle3);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.setClass(context, WebviewHome.class);
                bundlebundle.putString("url", linkUrl);
                intent.putExtras(bundlebundle);
                context.startActivity(intent);
            }

        }
    }

    // 跳转到其他页面
    public static void jumpToActivity(Context context, Class cls, Bundle bundle) {
        try {
            Intent intent;
            intent = new Intent(context, cls);
            if (bundle != null)
                intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.show(context, "数据异常，请稍后再试");
        }
    }

    // 跳转到其他页面
    public static void jumpToActivity(Context context, Class cls, Bundle bundle, int requestCode) {
        Intent intent;
        intent = new Intent(context, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    // 跳转到其他页面
    public static void jumpToActivity(Context context, Class cls, int requestCode) {
        Intent intent;
        intent = new Intent(context, cls);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

}
