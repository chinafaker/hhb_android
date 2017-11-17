package utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import base.App;

public class UIUtils {

    // public static final int ACTIONBAR_HEIGHT = android.R.attr.actionBarSize;

    private static Context context = null;
    private static DisplayMetrics dm = null;
    private static boolean followSystemBackground = true;

    public static DisplayMetrics getDM() {
        return dm;
    }

    public static float getDensity() {
        return dm.density;
    }

    public static void initDisplayMetrics(Context ctx, WindowManager wm, boolean isFollowSystemBackground) {
        if (context == null) {
            context = ctx;
        }

        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        followSystemBackground = isFollowSystemBackground;

    }

    public static boolean touchInDialog(Activity activity, MotionEvent e) {
        int leftW, rightW, topH, bottomH;
        leftW = 8;
        rightW = dm.widthPixels - leftW;
        topH = 0;
        bottomH = 450;
        return ((e.getX() > leftW) && (e.getX() < rightW) && (e.getY() > topH) && (e.getY() < bottomH));
    }

    public static boolean isScreenCenter(MotionEvent e) {
        boolean ret = true;
        if (e.getX() < (dm.widthPixels / 2 - 25)) {
            ret = false;
        }
        if (e.getX() > (dm.widthPixels / 2 + 25)) {
            ret = false;
        }
        if (e.getY() < (dm.heightPixels / 2 - 25)) {
            ret = false;
        }
        if (e.getY() > (dm.heightPixels / 2 + 25)) {
            ret = false;
        }
        return ret;
    }

    public static PointF getLeftBottomPoint() {
        return new PointF((dm.widthPixels / 4) + 0.09f, (dm.heightPixels / 4 * 3) + 0.09f);
    }

    public static PointF getRightBottomPoint() {
        return new PointF((dm.widthPixels / 4 * 3) + 0.09f, (dm.heightPixels / 4 * 3) + 0.09f);
    }

    public static PointF getLeftPoint() {
        return new PointF(20, dm.heightPixels / 2);
    }

    public static PointF getRightPoint() {
        return new PointF(dm.widthPixels - 20, dm.heightPixels / 2);
    }

    public static boolean isTouchLeft(MotionEvent e) {
        return (e.getX() < (dm.widthPixels / 2));
    }

    public static int getStatusbarHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (statusHeight == 0) {
            try {
                Class<?> localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {

            }
        }
        return statusHeight;
    }

    public static void setActivitySizePos(Activity activity, int x, int y, int width, int height) {
        WindowManager.LayoutParams p = activity.getWindow().getAttributes();
        p.x = x;
        p.y = y;
        p.width = width;
        p.height = height;
        activity.getWindow().setAttributes(p);
    }

    public static int dipToPx(int dip) {
        if (dm == null) {
            return -1;
        }
        return (int) (dip * dm.density + 0.5f);
    }

    public static int dipToPx(float f) {
        if (dm == null) {
            return -1;
        }
        return (int) (f * dm.density + 0.5f);
    }

    public static float pxToScaledPx(int px) {
        if (dm == null) {
            return -1;
        }
        return px / dm.density;
    }

    public static int scaledPxToPx(float scaledPx) {
        if (dm == null) {
            return -1;
        }
        return (int) (scaledPx * dm.density);
    }

    public static int countViewAdvWidth(int count, int innerMargin, int outerMargin) {
        int width = dm.widthPixels - (outerMargin * 2);
        width = width - (innerMargin * (count - 1));
        width = width / count;
        return width;
    }

    public static int countViewAdvWidthByFrame(int count, int innerMargin, int outerMargin, int frameWidth) {
        int width = frameWidth - (outerMargin * 2);
        width = width - (innerMargin * (count - 1));
        width = width / count;
        return width;
    }

    public static int countViewAdvHeight(int count, int innerMargin, int outerMargin) {
        int height = dm.heightPixels - (outerMargin * 2);
        height = height - (innerMargin * (count - 1));
        height = height / count;
        return height;
    }

    public static int countViewAdvHeightByFrame(int count, int innerMargin, int outerMargin, int frameHeight) {
        int height = frameHeight - (outerMargin * 2);
        height = height - (innerMargin * (count - 1));
        height = height / count;
        return height;
    }

    public static int getWidth() {
        if (dm != null) {
            return dm.widthPixels;
        } else {
            return 0;
        }
    }

    public static int getHeight() {
        if (dm != null) {
            if (dm.heightPixels >= 672 && dm.heightPixels <= 696) {
                return dm.heightPixels;
            } else if (dm.heightPixels >= 696 && dm.heightPixels <= 720) {
                dm.heightPixels = dm.heightPixels * 672 / 720;
                return dm.heightPixels;
            } else if (dm.heightPixels >= 1044 && dm.heightPixels <= 1080) {
                return dm.heightPixels * 672 / 720;
            } else if (dm.heightPixels >= 1000 && dm.heightPixels <= 1080) {
                return dm.heightPixels;
            }
            return dm.heightPixels;
        } else {
            return 0;
        }
    }

    public static void setActivitySizeX(Activity a, int size) {
        WindowManager.LayoutParams lp = a.getWindow().getAttributes();
        lp.width = size;
        a.getWindow().setAttributes(lp);
    }

    public static void setActivitySizeY(Activity a, int size) {
        WindowManager.LayoutParams lp = a.getWindow().getAttributes();
        lp.height = size;
        a.getWindow().setAttributes(lp);
    }

    public static void setActivityPercentX(Activity a, float percent) {
        WindowManager.LayoutParams lp = a.getWindow().getAttributes();
        lp.width = (int) (getWidth() * percent / 100);
        a.getWindow().setAttributes(lp);
    }

    public static void setActivityPercentY(Activity a, float percent) {
        WindowManager.LayoutParams lp = a.getWindow().getAttributes();
        lp.height = (int) (getHeight() * percent / 100);
        a.getWindow().setAttributes(lp);
    }

    public static void setViewSizeX(View v, int size) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.width = size;
        v.setLayoutParams(lp);
    }

    public static void setViewSizeY(View v, int size) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = size;
        v.setLayoutParams(lp);
    }

    public static void setViewPercentX(View v, float percent) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.width = (int) (getWidth() * percent / 100);
        v.setLayoutParams(lp);
    }

    public static void setViewPercentY(View v, float percent) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = (int) (getHeight() * percent / 100);
        v.setLayoutParams(lp);
    }

    public static void setViewPercentXByFrame(View v, float percent, float frameXPercent) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.width = (int) ((getWidth() * frameXPercent / 100) * percent / 100);
        v.setLayoutParams(lp);
    }

    public static void setViewPercentYByFrame(View v, float percent, float frameYPercent) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = (int) ((getHeight() * frameYPercent / 100) * percent / 100);
        v.setLayoutParams(lp);
    }

    public static void setViewMarginPercent(View v, float marginLeft, float marginTop, float marginRight, float marginBottom) throws Exception {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
            mlp.leftMargin = (int) (getWidth() * marginLeft / 100);
            mlp.topMargin = (int) (getHeight() * marginTop / 100);
            mlp.rightMargin = (int) (getWidth() * marginRight / 100);
            mlp.bottomMargin = (int) (getHeight() * marginBottom / 100);
            v.setLayoutParams(mlp);
        } else {
            throw new Exception("LayoutParams is not a MarginLayoutParams");
        }

    }

    public static void setViewMarginSize(View v, int marginLeft, int marginTop, int marginRight, int marginBottom) throws Exception {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
            mlp.leftMargin = marginLeft;
            mlp.topMargin = marginTop;
            mlp.rightMargin = marginRight;
            mlp.bottomMargin = marginBottom;
            v.setLayoutParams(mlp);
        } else {
            throw new Exception("LayoutParams is not a MarginLayoutParams");
        }
    }

    public static void setViewPaddingPercent(View v, float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        int pLeft = (int) (getWidth() * paddingLeft / 100);
        int pTop = (int) (getHeight() * paddingTop / 100);
        int pRight = (int) (getWidth() * paddingRight / 100);
        int pBottom = (int) (getHeight() * paddingBottom / 100);
        v.setPadding(pLeft, pTop, pRight, pBottom);
    }

    public static void makeListViewFullSize(ListView lv, int itemHeight) {
        int itemCount = lv.getAdapter().getCount();
        int divider = lv.getDividerHeight();
        int height = (itemHeight + divider) * itemCount;
        ViewGroup.LayoutParams lp = lv.getLayoutParams();
        lp.height = height;
        lv.setLayoutParams(lp);
    }

    public static void makeGridViewFullSize(GridView gv, int itemHeight,
                                            int rowNum) {
        int itemCount = gv.getAdapter().getCount();
        int lines = (int) (itemCount / rowNum);
        if (itemCount % rowNum != 0) {
            lines++;
        }
        ViewGroup.LayoutParams lp = gv.getLayoutParams();
        lp.height = lines * itemHeight;
        gv.setLayoutParams(lp);

    }

    // public static int getActionBarHeight() {
    // TypedArray a = context
    // .obtainStyledAttributes(new int[] { ACTIONBAR_HEIGHT });
    // int ret = a.getDimensionPixelSize(0, -1);
    // a.recycle();
    // return ret;
    // }

    public static boolean isFollowSystemBackground() {
        return followSystemBackground;
    }

    public static void setFollowSystemBackground(boolean isFollowSystemBackground) {
        followSystemBackground = isFollowSystemBackground;
    }

    public static Bitmap getCompressedImage(String srcPath, float newWidth, float newHeight) {//1280  720
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = newHeight;// 这里设置高度为800f
        float ww = newWidth;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, bitmapOptions);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 读取图片旋转角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    /**
     * 绘制小圆点
     *
     * @param color
     * @return
     */
    public static BitmapDrawable createDotIcon(int color, int radiusDp) {
        // 圆点半径
        int radius = DensityUtils.dp2px(App.getContext(), radiusDp);

        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(radius, radius, radius, paint);

        return new BitmapDrawable(App.getContext().getResources(), bitmap);
    }

    /**
     * 绘制小方点
     *
     * @param color
     * @return
     */
    public static BitmapDrawable createSquareIcon(int color, int radiusDp) {
        // 圆点半径
        int radius = DensityUtils.dp2px(App.getContext(), radiusDp);

        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, radius * 2, radius * 2, paint);

        return new BitmapDrawable(App.getContext().getResources(), bitmap);
    }
}
