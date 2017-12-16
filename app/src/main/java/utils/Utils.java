package utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;

import net.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import base.App;

public class Utils {
    /**
     * @param context
     * @param progressNum
     * @param remain      底图
     * @param bought      购买进度
     * @param top         顶图
     * @return
     */
    public static BitmapDrawable createProgressBitmap(Context context,
                                                      int progressNum, int remain, int bought, int top) {
        BitmapDrawable circleRemaining = (BitmapDrawable) context
                .getResources().getDrawable(remain);
        BitmapDrawable circleBought = (BitmapDrawable) context.getResources()
                .getDrawable(bought);
        BitmapDrawable circleTop = (BitmapDrawable) context.getResources()
                .getDrawable(top);
        int width = circleRemaining.getMinimumWidth();
        int height = circleRemaining.getMinimumHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(circleRemaining.getBitmap(), 0, 0, new Paint(1));
        Bitmap bitmapBought = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        Canvas canvasBought = new Canvas(bitmapBought);
        creatCircle(canvasBought, circleBought.getBitmap(), 0, progressNum,
                width, height);

        Bitmap bitmapGuarantee = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Bitmap bitmapRemaining = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvasRemaining = new Canvas(bitmapRemaining);
        creatCircle(canvasRemaining, circleRemaining.getBitmap(), progressNum,
                100 - progressNum, width, height);
        canvas.drawBitmap(bitmapBought, 0, 0, new Paint(1));
        canvas.drawBitmap(bitmapGuarantee, 0, 0, new Paint(1));
        canvas.drawBitmap(bitmapRemaining, 0, 0, new Paint(1));
        Bitmap bitmap1 = circleRemaining.getBitmap();
        Bitmap bitmap2 = circleTop.getBitmap();
        canvas.drawBitmap(bitmap2,
                (int) ((bitmap1.getWidth() - bitmap2.getWidth()) / 2),
                (int) ((bitmap1.getHeight() - bitmap2.getHeight()) / 2),
                new Paint(1));
        if (bitmapBought != null) {
            bitmapBought.recycle();
        }
        if (bitmapGuarantee != null) {
            bitmapGuarantee.recycle();
        }
        if (bitmapRemaining != null) {
            bitmapRemaining.recycle();
        }
        return new BitmapDrawable(bitmap);
    }

    private static void creatCircle(Canvas canvas, Bitmap bitmapCircle,
                                    int progressNumStart, int progressNum, int width, int height) {
        int start;
        int progress;
        if (progressNumStart + progressNum > 100) {
            start = 0;
            progress = progressNumStart;
        } else {
            start = progressNumStart + progressNum;
            progress = 100 - progressNum;
        }
        RectF rect = new RectF(0, 0, width, height);
        Path path = new Path();
        path.moveTo(width / 2, height / 2);
        path.addArc(rect, -90 + (float) (3.6 * start), (float) (3.6 * progress));
        path.lineTo(width / 2, height / 2);
        path.close();
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        canvas.drawBitmap(bitmapCircle, 0, 0, new Paint(1));
    }

    public static boolean isEmpty(String str) {
        if ("" != str && !"null".equals(str) && !TextUtils.isEmpty(str)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern
//                .compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
//        Pattern p = Pattern
//                .compile("^(1)\\d{10}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
        if (StringUtils.isEmpty(mobiles)) {
            return false;
        }
        return mobiles.length() == 11;
    }

    /**
     * 格式化电话号码
     *
     * @param phoneNum
     * @return
     */
    public static String formatPhoneNum(String phoneNum) {
        StringBuilder phone = new StringBuilder(phoneNum);
        phone.replace(3, 7, "****");
        return phone.toString();
    }

    /**
     * 获取密码强度
     *
     * @param password
     * @return
     */
    public static Integer getPwdStrength(String password) {
        if (Pattern.matches("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", password)) {
            return Consts.PwdStrength.STRONG;
        } else if (Pattern.matches("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", password)) {
            return Consts.PwdStrength.GENERAL;
        }
        // 弱 ^(?:\d+|[a-zA-Z]+|[!@#$%^&*]+)$
        return Consts.PwdStrength.WEAK;
    }

    /**
     * @param context
     * @param resId
     * @return
     */
    public static Drawable getDrawable(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 非法浮点数+0
     */
    public static boolean isMacAddress(String macAddress) {
        String reg = "^\\d+(\\.\\d+)?$";
        return Pattern.compile(reg).matcher(macAddress).find();
    }

    /**
     * 获取软件版本号
     *
     * @return
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = App.getApp().getApplicationContext().getPackageManager().getPackageInfo("com.miaomiaobank", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return versionCode;
    }

    /**
     * 防止按钮连续点击
     */
    private static long lastClickTime;

    public synchronized static boolean isFastClick_1s() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 查找最接近目标值的数，并返回
     *
     * @param array
     * @param targetNum
     * @return
     */
    public static Integer searchNearNum(ArrayList<Integer> array, int targetNum) {
        Arrays.sort(array.toArray());
        int left = 0, right = 0;
        for (right = array.size() - 1; left != right; ) {
            int midIndex = (right + left) / 2;
            int mid = (right - left);
            int midValue = array.get(midIndex);
            if (targetNum == midValue) {
                return targetNum;
            }

            if (targetNum > midValue) {
                left = midIndex;
            } else {
                right = midIndex;
            }

            if (mid <= 1) {
                break;
            }
        }
        int rightnum = array.get(right);
        int leftnum = array.get(left);
        int ret = Math.abs((rightnum - leftnum) / 2) > Math.abs(rightnum - targetNum) ? rightnum : leftnum;
        Logger.e("要查找的数：" + targetNum + "  最接近的数：" + ret);
        return ret;
    }


    public static int dip2px(Context ctx, float dpValue) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
    public static int px2dip(Context ctx, float pxValue) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }


    /**
     * 获取APP版本号
     * @param ctx
     * @return
     */
    public static int getVersionCode(Context ctx) {
        // 获取packagemanager的实例
        int version = 0;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            //getPackageName()是你当前程序的包名
            PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取文件保存路径 sdcard根目录/download/文件名称
     * @param fileUrl
     * @return
     */
    public static String getSaveFilePath(String fileUrl){
        String fileName=fileUrl.substring(fileUrl.lastIndexOf("/")+1,fileUrl.length());//获取文件名称
        String newFilePath= Environment.getExternalStorageDirectory() + "/Download/"+fileName;
        return newFilePath;
    }
}
