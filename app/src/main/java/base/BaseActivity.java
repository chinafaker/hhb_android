package base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daikin.rqm.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import receiver.HomeKeyEventBroadCastReceiver;
import receiver.ScreenListener;
import utils.Logger;
import utils.SharedPrefUtil;
import utils.StringUtils;
import utils.ToastUtils;
import utils.UIUtils;
import utils.WeakHandler;
import widgets.SystemBarTintManager;


/**
 * @author vincent
 * @ClassName: BaseActivity
 * @Description: 基类
 */
public abstract class BaseActivity extends MainbaseActivity {
    public static int STATUS_BAR_HEIGHT = 0;
    protected int initWidth = 0;
    protected int initHeight = 0;
    private ProgressDialog dialog;
    public static final int SHOW_DIALOG = 0;
    public static final int CLOSE_DIALOG = 1;
//    private String message = "Loading...";
    private String message = "加载中...";
    private int num = 0;

    protected SystemBarTintManager tintManager;
    protected boolean ifFinish = false;
    @BindView(R.id.leftIcon)
    TextView leftIcon;
    @BindView(R.id.leftTv)
    TextView leftTv;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.rlleft)
    public RelativeLayout rlleft;

    public
    @BindView(R.id.rightTv)
    TextView rightTv;
    @BindView(R.id.titleDev)
    View titleDev;
    @BindView(R.id.titleWrapperLin)
    LinearLayout titleWrapperLin;
    @BindView(R.id.titleRe)
    RelativeLayout titleRe;


    protected AppCompatActivity activity;

    protected void hideTitle() {
        titleWrapperLin.setVisibility(View.GONE);
        titleRe.setVisibility(View.GONE);
    }

    protected void showTitle() {
        titleWrapperLin.setVisibility(View.VISIBLE);
        titleRe.setVisibility(View.VISIBLE);
    }

    protected void setTitleBg(int color) {
        titleWrapperLin.setBackgroundColor(color);
    }

    protected void setTitleWhiteBg() {
        titleRe.setBackgroundColor(getResources().getColor(R.color.white));
        titleTv.setTextColor(getResources().getColor(R.color.dark_purple));
        leftTv.setTextColor(getResources().getColor(R.color.blue_dark));
    }

    protected void hideTitleDev() {
        titleDev.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        init();
    }



    protected int getContentView() {
        return 0;
    }

    private void init() {
        // 设置systembar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager = new SystemBarTintManager(this);
            tintManager.setTranslucentStatus(true);

            int pl = titleWrapperLin.getPaddingLeft();
            int pt = titleWrapperLin.getPaddingTop();
            int pr = titleWrapperLin.getPaddingRight();
            int pb = titleWrapperLin.getPaddingBottom();

            int statusHeight = tintManager.getConfig().getStatusBarHeight();
            STATUS_BAR_HEIGHT = statusHeight;
            titleWrapperLin.setPadding(pl, pt + statusHeight, pr, pb);
        }

        setStatusbarDarkMode();

        activity = this;

        UIUtils.initDisplayMetrics(this, getWindowManager(), false);
        initWidth = UIUtils.getWidth() / 2;
        initHeight = UIUtils.getHeight();
        ApplicationManager.getInstance().addActivity(this);
        initDialog();
        initView();
    }

    protected void setStatusbarDarkMode() {
        setMiuiStatusBarDarkMode(this, true);
        setMeizuStatusBarDarkIcon(this, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    protected void setStatusbarLightMode() {
        setMiuiStatusBarDarkMode(this, false);
        setMeizuStatusBarDarkIcon(this, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            if (Logger.B_LOG_OPEN) {

            }
        }
        return false;
    }

    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * @return void 返回类型
     * @Title: initDialog
     * @Description: 初始化进度条对话框
     * @author vincent
     */
    private void initDialog() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOwnerActivity(this);
        // dialog.setMessage(getString(R.string.net_loading));
        dialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                onProdialogCancel();
            }
        });
    }

    // 子类用于复写方法来声明相关组件
    public void initView() {

    }

    public void onProdialogCancel() {
    }

    // 用于显示和关闭对话框
    public WeakHandler handler = new WeakHandler(new Handler.Callback() {
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SHOW_DIALOG:
                    if (dialog != null && !dialog.isShowing() && !ifFinish && App.isActive) {
                        if (message != null && !message.equals("")) {
                            dialog.setMessage(message);
                        }

                        if (!checkActivityExist()) return true;

                        dialog.show();
                    }
                    break;

                case CLOSE_DIALOG:
                    if (dialog != null && dialog.isShowing() && checkActivityExist())
                        dialog.dismiss();
                    break;
            }
            return true;
        }
    });

    private boolean checkActivityExist() {
        final Activity activity = dialog.getOwnerActivity();
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return false;
        }
        return true;
    }

    @OnClick(R.id.leftIcon)
    public void leftIcon() {
        onLeftClick();
    }

    @OnClick(R.id.leftTv)
    public void leftTv() {
        onLeftClick();
    }

    @OnClick(R.id.rightTv)
    public void rightTv() {
        onRightClick();
    }

    @OnClick(R.id.titleTv)
    public void titleTv() {
        onTitleClick();
    }

    /**
     * 点击title后执行
     */
    public void onTitleClick() {

    }

    /**
     * @return void 返回类型
     * @Title: onRightClick
     * @Description: 右边点击了搜索后的方法，子类需覆盖此方法实现自己的逻辑
     * @author vincent
     */
    public void onRightClick() {

    }

    /**
     * @return void 返回类型
     * @Title: onBackClick
     * @Description: 右边icon方法，子类注意覆盖此方法
     * @author vincent
     */
    public void onLeftClick() {

    }

    /**
     * @param title
     * @return void 返回类型
     * @Title: setTitle
     * @Description: 设置标题
     * @author vincent
     */
    public void setTitle(String title) {
        titleTv.setText(StringUtils.noNull(title));
    }

    /**
     * @param title
     * @return void 返回类型
     * @Title: setTitle
     * @Description: 设置标题
     * @author vincent
     */
    public void setTitle(CharSequence title) {
        titleTv.setText(title);
    }

    /**
     * @param title
     * @return void 返回类型
     * @Title: setTitle
     * @Description: 设置标题
     * @author vincent
     */
    public void setTitle(int title) {
        titleTv.setText(getString(title));
    }

    /**
     * @return void 返回类型
     * @Title: setBackButtonGone
     * @Description: 让左侧按钮消失
     * @author vincent
     */
    public void setLeftIconGone() {
        leftIcon.setVisibility(View.GONE);
    }

    public void setRightIconGone() {
        rightTv.setVisibility(View.GONE);
    }

    /**
     * @param res
     * @return void 返回类型
     * @Title: setLeftIcon
     * @Description: 设置左边的图片
     * @author vincent
     */
    public void setLeftIcon(int res) {
        leftIcon.setVisibility(View.VISIBLE);
        Drawable drawable = getResources().getDrawable(res);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        leftIcon.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String data) {

    }


    public void setLeftTv(String str) {
        leftTv.setText(str);
    }

    public void setRightIcon(int res) {
        Drawable drawable = getResources().getDrawable(res);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        rightTv.setCompoundDrawables(null, null, drawable, null);
    }


    public void setRightText(String string) {
        rightTv.setText(string);
    }

    public void setRightText(String string, int color) {
        rightTv.setText(string);
        rightTv.setTextColor(getResources().getColor(color));
    }

    public TextView getRightView() {
        return rightTv;
    }

    public void showProDialog() {
        dialog.setCancelable(false);
        dialog.setMessage(message);
        handler.sendEmptyMessage(SHOW_DIALOG);
    }

    public void showProDialogCancel() {
        dialog.setCancelable(true);
        dialog.setMessage(message);
        handler.sendEmptyMessage(SHOW_DIALOG);
    }

    public void disProDialog() {
        handler.sendEmptyMessage(CLOSE_DIALOG);
    }

    public int getTitleHeight() {
        return titleRe.getHeight();
    }

    @Override
    public void finish() {
        ifFinish = true;
        super.finish();
        disProDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ScreenListener screenListener;
    private HomeKeyEventBroadCastReceiver receiver;

    /**
     * 监听锁屏事件，锁屏后解锁自动跳到密码锁页面
     */
    private void onAppBackGroundWatcher() {
        receiver = new HomeKeyEventBroadCastReceiver();
        registerReceiver(receiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        screenListener = new ScreenListener(this);
        screenListener.begin(new ScreenListener.ScreenStateListener() {

            @Override
            public void onUserPresent() {
            }

            // 解锁
            @Override
            public void onScreenOn() {
                // 如果从home键到后台，解锁不做任何事儿
                if (!isHomeOut()) {
                    ToastUtils.show(activity, "isHomeOut false");
                } else {
                    ToastUtils.show(activity, "isHomeOut true");
                }
            }

            // 锁屏
            @Override
            public void onScreenOff() {
                SharedPrefUtil db = new SharedPrefUtil(BaseActivity.this,
                        SharedPrefUtil.ONBACKGROUND);
                db.setSharedBoolean("OUT", true);
                db.setSharedStr("outTime", System.currentTimeMillis() + "");
            }
        });
    }

    private boolean isHomeOut() {
        SharedPrefUtil db = new SharedPrefUtil(BaseActivity.this,
                SharedPrefUtil.ONBACKGROUND);
        return db.getSharedBoolean("HOMEOUT", false);
    }
}
