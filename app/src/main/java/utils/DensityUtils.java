package utils;

import android.content.Context;
import android.util.TypedValue;

import base.App;


/**
 *
 * sp:与缩放无关的抽象像素（Scale-independent Pixel）
 * sp和dp很类似但唯一的区别是，Android系统允许用户自定义文字尺寸大小（小、正常、大、超大等等），
 * 当文字尺寸是“正常”时1sp=1dp=0.00625英寸，而当文字尺寸是“大”或“超大”时，1sp>1dp=0.00625英寸。类似我们在windows里调整字体尺寸以后的效果——窗口大小不变，只有文字大小改变。
 */
public class DensityUtils {
    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * dp转px
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, App.getContext().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @return
     */
    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param px
     * @return
     */
    public static float px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (px / scale);
    }

    /**
     * px转sp
     *
     * @param px
     * @return
     */
    public static float px2sp(Context context, float px) {
        return (px / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
