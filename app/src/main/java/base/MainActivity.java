package base;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.rqm.rqm.R;

import net.OnDataGetListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import data.GetAppVersionController;
import data.GetMaintenanceInfoController;
import fragments.HomeFragment;
import javaBean.MaintenanceInfo;
import javaBean.MaintenanceInfoBean;
import utils.DialogUtil;
import utils.EncryptionUtil;
import utils.GloableData;
import utils.InstallApkMsg;
import utils.JsonUtil;
import utils.Logger;
import utils.StringUtils;
import utils.ToastUtils;

public class MainActivity extends BaseActivity {
    private static final int investment_fragment_num = 1;//首页
    private HomeFragment homeFragment;
    private MainPagerAdapter mainPagerAdapter;
    // 系统时间
    public long startTime, endTime;
    /**
     * 下载名称
     */
    private String down_name = "QRM";
    /**
     * 下载路径
     */
    private String down_url = "http://121.40.150.64:8080/rqm/app/rqmClient.apk";
    /**
     * 更新内容
     */
    private String update_content = "更新内容。。。。。";
    /**
     * 版本名称
     */
    private String version_name = "V2.2.0";


    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected int getContentView() {
        return R.layout.layout_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initView() {

        //   String s = EncryptionUtil.getEncryptionStr("黄海宾");
        //   Log.e("-----加密",s);
     /*   String result = EncryptionUtil.getDecryptStr(s);
        Log.e("----解密", result);
        */
        super.initView();
        EventBus.getDefault().register(this);
        setStatusbarLightMode();
        hideTitle();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(mainPagerAdapter);

        getAppVersionController();
        getLoginController();
    }

    class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return investment_fragment_num;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return super.isViewFromObject(view, object);
        }

        @Override
        public Fragment getItem(int pos) {
            if (pos == 0) {
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                return homeFragment;
            }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    GetMaintenanceInfoController getMaintenanceInfoController;

    public void getLoginController() {
        if (getMaintenanceInfoController == null) {
            getMaintenanceInfoController = new GetMaintenanceInfoController(activity, new OnDataGetListener() {
                @Override
                public void onGetDataSuccess(String result) {
                    MaintenanceInfo baseInfo = JsonUtil.objectFromJson(result, MaintenanceInfo.class);
                    MaintenanceInfoBean maintenanceInfoBean = baseInfo.getMaintenanceInfo().get(0);
                    Log.e("getMaintenanceInfo   结果", maintenanceInfoBean.getMmEndtime());
                    DialogUtil.noticeDialog(activity, null, true, maintenanceInfoBean);
                }

                @Override
                public void onGetDataFailed(int responseCode, String result) {
                    //   disProDialog();
                    //    ToastUtils.show(activity, result);
                }
            });
        }
        getMaintenanceInfoController.getData();
    }

    GetAppVersionController mGetAppVersionController;

    public void getAppVersionController() {
        if (mGetAppVersionController == null) {
            mGetAppVersionController = new GetAppVersionController(activity, new OnDataGetListener() {
                @Override
                public void onGetDataSuccess(String result) {
                    Logger.e("getAppVersion   sucess", result);
                }

                @Override
                public void onGetDataFailed(int responseCode, String result) {
                    Logger.e("getAppVersion  failed", result);
                    //         showNoticeDialog(0);
                }
            });
        }
        mGetAppVersionController.getData();
    }

    /**
     * 显示软件更新对话框
     *
     * @param isForse
     */
    private void showNoticeDialog(int isForse) {
        DialogUtil.versionUpdateDialog(this, update_content, version_name, down_name, down_url, isForse);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 时间锁连续两次时间间隔小于2秒
            endTime = System.currentTimeMillis();
            if (endTime - startTime < 2000) {
                // 安全退出
                ApplicationManager.getInstance().finishAllActivity();
                // 杀死自己的进程
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                ToastUtils.show(this, "再按一次退出程序");
                startTime = endTime;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String data) {
        if (!StringUtils.isEmpty(data)) {
            if (data.equals(GloableData.IN_DOWNLOAD)) {
                GloableData.IN_DOWNLOAD_APP = true;
                ToastUtils.show(this, "开始下载...");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(InstallApkMsg installApkMsg) {
        GloableData.IN_DOWNLOAD_APP = false;
        if (installApkMsg != null) {
            if (!StringUtils.isEmpty(installApkMsg.getPath())) {
                startApkInstall(installApkMsg.getPath());
            }
        }
    }

    public void startApkInstall(final String filePath) {

        final File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                startActivity(i);
            }
        }, 500);
    }

}