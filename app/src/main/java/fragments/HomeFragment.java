package fragments;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.huanghaibin.rqm.R;

import net.Consts;
import net.OnDataGetListener;

import java.util.ArrayList;

import butterknife.BindView;
import customViews.NoScrollViewPager;
import data.LoginController;
import fingerprint.FingerprintCore;
import fingerprint.FingerprintUtil;
import fingerprint.KeyguardLockScreenManager;
import utils.DialogUtil;
import utils.GoPageUtil;
import utils.Logger;
import utils.StringUtils;
import utils.ToastUtils;
import utils.WeakHandler;

public class HomeFragment extends BaseFragment {
    public static final int MSG_FLAG_REFRESH_COMPLETE = 10;
    private static final String[] tabTitles = {"USER ID", "TOUCH ID"};
    private final int FRAGMENT_NUM = 2;
    private int currentItem = 0;
    private boolean isShowToast = true;
    private EditText userEdi;
    private EditText passwordEdi;
    private CheckBox checkSave;
    private MyPagerAdapter adapter;
    private FingerprintCore mFingerprintCore;
    private KeyguardLockScreenManager mKeyguardLockScreenManager;
    private ArrayList<Fragment> arrayList = new ArrayList<>();
    private boolean isFirseGetView = true;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.pager)
    NoScrollViewPager pager;
    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.ll_touchid_setting)
    LinearLayout ll_touchid_setting;

    @Override
    public int initInflateView() {
        return R.layout.fragment_home_refreshable;
    }


    @Override
    public void getMyViews() {
        initFingerprintCore();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_229EFF);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
        adapter = new MyPagerAdapter(activity.getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(1);
        tabs.setupWithViewPager(pager);
        tabs.setTabsFromPagerAdapter(adapter);
        pager.setCurrentItem(0);

        if (mFingerprintCore.isSupport()) {//设备支持指纹
            pager.setNoScroll(false);
        } else {
            pager.setNoScroll(true);
            LinearLayout tabStrip = (LinearLayout) tabs.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                if (tabView != null) {
                    tabView.setClickable(false);
                }
            }
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                if (position == 0) {
                    ll_touchid_setting.setVisibility(View.INVISIBLE);
                } else if (position == 1) {
                    ll_touchid_setting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void doSomething() {
    }

    @butterknife.OnClick({R.id.btn_login, R.id.ll_touchid_setting})
    void OnClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                if (currentItem == 0) {
                    if (isFirseGetView) {
                        userEdi = adapter.getCurrentFragment().getView().findViewById(R.id.userEdi);
                        passwordEdi = adapter.getCurrentFragment().getView().findViewById(R.id.passwordEdi);
                        checkSave = adapter.getCurrentFragment().getView().findViewById(R.id.checkSave);
                        isFirseGetView = false;
                    }
                    getLoginController(userEdi, passwordEdi, checkSave);

                } else if (currentItem == 1) {
                    isShowToast = true;
                    if (sharedPrefUtil.getSharedBoolean(Consts.ISREGISTERUSERIDTIOUC, false)) {
                        startFingerprintRecognition();
                    } else {
                        DialogUtil.registerDialog(activity, weakhandler, true, sharedPrefUtil.getSharedStr(Consts.USETID, ""));
                    }
                }
                break;

            case R.id.ll_touchid_setting:
                DialogUtil.registerDialog(activity, weakhandler, true, sharedPrefUtil.getSharedStr(Consts.USETID, ""));

                break;
            default:
                break;
        }
    }

    LoginController mLoginController;
    LoginController mLoginController2;

    public void getLoginController(final EditText userEdi, EditText passwordEdi, final CheckBox checkSave) {
        if (StringUtils.isEmpty(userEdi.getText().toString())) {
            ToastUtils.show(activity, "Please enter your account");
            return;
        }
        if (StringUtils.isEmpty(passwordEdi.getText().toString())) {
            ToastUtils.show(activity, "Please enter your password");
            return;
        }
        showProDialogCancel();
        if (mLoginController == null) {
            mLoginController = new LoginController(activity, new OnDataGetListener() {
                @Override
                public void onGetDataSuccess(String result) {

                    //是否新用户登录
                    if (userEdi.getText().toString().equals(sharedPrefUtil.getSharedStr(Consts.USETID, ""))) {
                        sharedPrefUtil.setSharedBoolean(Consts.ISREGISTERUSERIDTIOUC, sharedPrefUtil.getSharedBoolean(Consts.ISREGISTERUSERIDTIOUC, false));
                    } else {
                        sharedPrefUtil.setSharedBoolean(Consts.NOTDISPLAYTODAY, false);
                        sharedPrefUtil.setSharedBoolean(Consts.ISREGISTERUSERIDTIOUC, false);
                    }
                    if (checkSave.isChecked()) {

                        sharedPrefUtil.setSharedStr(Consts.USETID, userEdi.getText().toString());
                    } else {
                        sharedPrefUtil.setSharedStr(Consts.USETID, "");
                    }
                    disProDialog();
                    if (sharedPrefUtil.getSharedBoolean(Consts.NOTDISPLAYTODAY, false)) {
                        GoPageUtil.jumpTobyUrlLink(activity, Consts.RQM_CONTAINER_LIST_URL);
                        activity.finish();
                    } else {
                        GoPageUtil.jumpTobyUrlLink(activity, Consts.RQM_TOP_NOTICE_URL);
                    }

                }

                @Override
                public void onGetDataFailed(int responseCode, String result) {
                    disProDialog();
                    ToastUtils.show(activity, result);
                }
            });
        }
        mLoginController.getData(userEdi.getText().toString(), passwordEdi.getText().toString());

    }

    public void registerTouchID(String accountID, String password) {

        showProDialogCancel();
        if (mLoginController2 == null) {
            mLoginController2 = new LoginController(activity, new OnDataGetListener() {
                @Override
                public void onGetDataSuccess(String result) {
                    disProDialog();
                    sharedPrefUtil.setSharedBoolean(Consts.ISREGISTERUSERIDTIOUC, true);
                    ToastUtils.show(activity, "register sucess");
                }

                @Override
                public void onGetDataFailed(int responseCode, String result) {
                    disProDialog();
                    ToastUtils.show(activity, result);
                }
            });
        }
        mLoginController2.getData(accountID, password);
    }

    WeakHandler weakhandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 8001://注册touchid
                    String accountID = msg.getData().getString("accountID");
                    String password = msg.getData().getString("password");
                    registerTouchID(accountID, password);
                    break;
                case 8002:
                    isShowToast = false;
                    cancelFingerprintRecognition();
                    break;
                case MSG_FLAG_REFRESH_COMPLETE:// 刷新控件完成刷新
                    mSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 200);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    class MyPagerAdapter extends FragmentPagerAdapter {
        private FragmentUserID f0;
        private FragmentTouchID f1;
        FragmentUserID mCurrentFragment;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (f0 == null) {
                    f0 = new FragmentUserID();
                }
                return f0;
            } else if (position == 1) {
                if (f1 == null) {
                    f1 = new FragmentTouchID();
                }
                return f1;
            } else {
                Logger.e("zhaoys", "fragment null");
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return FRAGMENT_NUM;
        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (position == 0) {
                mCurrentFragment = (FragmentUserID) object;
            }
            super.setPrimaryItem(container, position, object);
        }

        public FragmentUserID getCurrentFragment() {
            return mCurrentFragment;
        }
    }

    /**
     * 指纹验证回调
     */
    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {
            DialogUtil.touchResultSetText("sucess!");
            new WeakHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogUtil.touchResultSetTextClear();
                    DialogUtil.dialogDismiss();


                    if (sharedPrefUtil.getSharedBoolean(Consts.NOTDISPLAYTODAY, false)) {
                        GoPageUtil.jumpTobyUrlLink(activity, Consts.RQM_CONTAINER_LIST_URL);
                        activity.finish();
                    } else {
                        GoPageUtil.jumpTobyUrlLink(activity, Consts.RQM_TOP_NOTICE_URL);

                    }
                }
            }, 1000);
        }

        @Override
        public void onAuthenticateFailed(int helpId) {//指纹识别失败，请重试
            if (isShowToast) {
                DialogUtil.touchResultSetText("Fail please try again");
                new WeakHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.touchResultSetTextClear();
                    }
                }, 1500);
            } else {
                isShowToast = true;
            }
        }

        @Override
        public void onAuthenticateError(int errMsgId) {//指纹识别错误，等待几秒之后再重试
            if (isShowToast) {
                DialogUtil.touchResultSetText("Fail please try again");
                new WeakHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.touchResultSetTextClear();
                    }
                }, 1500);
            } else {
                isShowToast = true;
            }
        }

        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {

        }
    };

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    };

    /**
     * 开始指纹识别
     */
    private void startFingerprintRecognition() {
        if (mFingerprintCore.isSupport()) {
            if (!mFingerprintCore.isHasEnrolledFingerprints()) {
                ToastUtils.show(activity, R.string.fingerprint_recognition_not_enrolled);
                FingerprintUtil.openFingerPrintSettingPage(activity);
                return;
            }
            if (mFingerprintCore.isAuthenticating()) {
                //  ToastUtils.show(activity, R.string.fingerprint_recognition_authenticating);
            } else {
                mFingerprintCore.startAuthenticate();
            }
            DialogUtil.touchIDDialog(activity, weakhandler, false);
        } else {
            ToastUtils.show(activity, R.string.fingerprint_recognition_not_support);
        }
    }

    /**
     * 取消验证
     */
    private void cancelFingerprintRecognition() {
        if (mFingerprintCore.isAuthenticating()) {
            mFingerprintCore.cancelAuthenticate();
        }
    }

    /**
     * 测试密码解锁
     */
    private void startFingerprintRecognitionUnlockScreen() {
        if (mKeyguardLockScreenManager == null) {
            return;
        }
        if (!mKeyguardLockScreenManager.isOpenLockScreenPwd()) {
            ToastUtils.show(activity, R.string.fingerprint_not_set_unlock_screen_pws);
            FingerprintUtil.openFingerPrintSettingPage(activity);
            return;
        }
        mKeyguardLockScreenManager.showAuthenticationScreen(activity);
    }

    /**
     * 进入系统指纹设置页面
     */
    private void enterSysFingerprintSettingPage() {
        FingerprintUtil.openFingerPrintSettingPage(activity);
    }

    private void initFingerprintCore() {
        mFingerprintCore = new FingerprintCore(activity);
        mFingerprintCore.setFingerprintManager(mResultListener);
        mKeyguardLockScreenManager = new KeyguardLockScreenManager(activity);
    }
}
