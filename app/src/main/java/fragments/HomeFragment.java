package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huanghaibin.rqm.R;

import java.util.ArrayList;

import base.MainActivity;
import butterknife.BindView;
import fingerprint.FingerprintCore;
import fingerprint.FingerprintMainActivity;
import fingerprint.FingerprintUtil;
import fingerprint.KeyguardLockScreenManager;
import preGuide.GuideActivity;
import preGuide.Welcome;
import utils.DialogUtil;
import utils.GoPageUtil;
import utils.Logger;
import utils.ToastUtils;
import utils.WeakHandler;

public class HomeFragment extends BaseFragment {
    public static final int MSG_FLAG_REFRESH_COMPLETE = 10;
    private static final String[] tabTitles = {"USER ID", "TOUCH ID"};
    private final int FRAGMENT_NUM = 2;
    private int currentItem = 0;
    private boolean isShowToast = true;
    private FingerprintCore mFingerprintCore;
    private KeyguardLockScreenManager mKeyguardLockScreenManager;
    private ArrayList<Fragment> arrayList = new ArrayList<>();
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @Override
    public int initInflateView() {
        return R.layout.fragment_home_refreshable;
    }


    @Override
    public void getMyViews() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_229EFF);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
        MyPagerAdapter adapter = new MyPagerAdapter(activity.getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(1);
        tabs.setupWithViewPager(pager);
        tabs.setTabsFromPagerAdapter(adapter);
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        initFingerprintCore();
    }

    @Override
    public void doSomething() {
    }


    @butterknife.OnClick({R.id.btn_login})
    void OnClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                if (currentItem == 0) {

                    DialogUtil.registerDialog(activity, weakhandler, true);

                } else if (currentItem == 1) {
                    isShowToast=true;
                    startFingerprintRecognition();
                }

                break;

            default:
                break;

        }
    }


    WeakHandler weakhandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 8001:
                    GoPageUtil.jumpTobyUrlLink(activity, "www.baidu.com");
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


    private void initFingerprintCore() {
        mFingerprintCore = new FingerprintCore(activity);
        mFingerprintCore.setFingerprintManager(mResultListener);
        mKeyguardLockScreenManager = new KeyguardLockScreenManager(activity);
    }

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
            DialogUtil.touchIDDialog(activity, weakhandler, true);


            if (mFingerprintCore.isAuthenticating()) {
                ToastUtils.show(activity, R.string.fingerprint_recognition_authenticating);
            } else {
                mFingerprintCore.startAuthenticate();
            }
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


    class MyPagerAdapter extends FragmentPagerAdapter {
        private FragmentUserID f0;
        private FragmentTouchID f1;


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
    }


    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {

            ToastUtils.show(activity, R.string.fingerprint_recognition_success);

        }

        @Override
        public void onAuthenticateFailed(int helpId) {//指纹识别失败，请重试
            if(isShowToast){
                ToastUtils.show(activity, R.string.fingerprint_recognition_failed);
            }else{
                isShowToast=true;
            }


        }

        @Override
        public void onAuthenticateError(int errMsgId) {//指纹识别错误，等待几秒之后再重试
            if(isShowToast){
                ToastUtils.show(activity, R.string.fingerprint_recognition_error);
            }else{
                isShowToast=true;
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

}
