package utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
    private static Toast topToast;
    private static ToastProxy toast;
    private static ToastProxy toastLong;


    public static void show(Context context, int res) {
        if (res < 0) {
            return;
        }
        if (toast == null) {
            toast = new ToastProxy((Activity) context);
        }
        toast.show(context.getResources().getString(res));
    }

    public static void show(Context context, String res) {
        if (StringUtils.isEmpty(res)) {
            return;
        }
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = new ToastProxy((Activity) context);
        }
        toast.show(res);
    }

    public static void showLong(Context context, String res) {
        if (StringUtils.isEmpty(res)) {
            return;
        }
        if (toastLong == null) {
            toastLong = new ToastProxy((Activity) context, Toast.LENGTH_LONG);
        }
        toastLong.show(res);
    }

    /**
     * @param context
     * @param res
     * @param top     距离顶端的距离
     */
    public static void showAtTop(Context context, String res, int top) {
        if (topToast == null) {
            topToast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
        } else {
            topToast.setText(res);
        }
        topToast.setGravity(Gravity.TOP, 0, DensityUtils.dp2px(context, top));
        topToast.show();
    }

    public static void showAtTop(Context context, int res, int top) {
        if (topToast == null) {
            topToast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
        } else {
            topToast.setText(res);
        }
        topToast.setGravity(Gravity.TOP, 0, DensityUtils.dp2px(context, top));
        topToast.show();
    }


}
