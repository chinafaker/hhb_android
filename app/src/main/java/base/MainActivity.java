package base;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ansen.http.net.HTTPCaller;
import com.rqm.rqm.R;

import net.OnDataGetListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import aes.AES;
import butterknife.BindView;
import data.GetAppVersionController;
import data.GetMaintenanceInfoController;
import fragments.HomeFragment;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import javaBean.Appversion;
import javaBean.AppversionBean;
import javaBean.MaintenanceInfo;
import javaBean.MaintenanceInfoBean;
import javaBean.UserInfo;
import javaBean.UserInfoBean;
import utils.DialogUtil;
import utils.GloableData;
import utils.InstallApkMsg;
import utils.JsonUtil;
import utils.Logger;
import utils.StringUtils;
import utils.ToastUtils;
import utils.Utils;
import utils.WeakHandler;

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
//    private String down_url = "http://121.40.150.64:8080/rqm/app/rqmClient.apk";
    private String appUrl = "http://shouji.360tpcdn.com/171201/0234844f23dfba04c274454daeb387b7/com.happyelements.AndroidAnimal_52.apk";
    /**
     * 更新内容
     */
    private String update_content = "";
    /**
     * 版本名称
     */
    private String version_name = "V2.2.0";

    private static final String keyBytes = "0000sl_010122940";
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected int getContentView() {
        return R.layout.layout_main;
    }

    public void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        hideTitle();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(mainPagerAdapter);
        getLoginController();
        getAppVersionController();
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
        TimeZone tz = TimeZone.getDefault();
        String timeZone = tz.getID();
        if (getMaintenanceInfoController == null) {
            getMaintenanceInfoController = new GetMaintenanceInfoController(activity, new OnDataGetListener() {
                @Override
                public void onGetDataSuccess(String result) {
                    MaintenanceInfo baseInfo = JsonUtil.objectFromJson(result, MaintenanceInfo.class);
                    MaintenanceInfoBean maintenanceInfoBean = baseInfo.getMaintenanceInfo().get(0);
                    DialogUtil.noticeDialog(activity, null, true, maintenanceInfoBean);
                }

                @Override
                public void onGetDataFailed(int responseCode, String result) {
                }
            });
        }
        getMaintenanceInfoController.getData(timeZone);
    }

    GetAppVersionController mGetAppVersionController;

    public void getAppVersionController() {
        if (mGetAppVersionController == null) {
            mGetAppVersionController = new GetAppVersionController(activity, new OnDataGetListener() {
                @Override
                public void onGetDataSuccess(String result) {
                    Log.e("getAppVersion:        ", result);

                    AppversionBean userInfo = JsonUtil.objectFromJson(result, AppversionBean.class);
                    Appversion appversion = userInfo.getAppVerInfo().get(0);
                    String versionCode = appversion.getVersionCode();
                    String versionName = appversion.getVersionName();
                    String appName = appversion.getAppName();
                    appUrl = appversion.getAppUrl();
                    appUrl = "https://raw.githubusercontent.com/yjfnypeu/UpdatePlugin/master/screenshots/app-debug.apk";
                    update_content = appversion.getUpdateComment();
                    String forceUpdateFlg = appversion.getForceUpdateFlg();
                    DialogUtil.versionUpdateDialog(activity, update_content, down_name, appUrl, forceUpdateFlg, weakhandler);
                }

                @Override
                public void onGetDataFailed(int responseCode, String result) {
                }
            });
        }
        mGetAppVersionController.getData();
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
                ToastUtils.show(this, "Press again to exit the program");
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
                ToastUtils.show(this, "Downloading...");
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


    private ProgressDialog progressDialog;
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PERMS_REQUEST_CODE = 200;

    /**
     * 开始下载
     *
     * @param downloadUrl 下载url
     */
    private void startUpload(String downloadUrl) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载新版本");
        progressDialog.setCancelable(false);//不能手动取消下载进度对话框

        final String fileSavePath = Utils.getSaveFilePath(downloadUrl);
        HTTPCaller.getInstance().downloadFile(downloadUrl, fileSavePath, null, new ProgressUIListener() {

            @Override
            public void onUIProgressStart(long totalBytes) {//下载开始
                progressDialog.setMax((int) totalBytes);
                progressDialog.show();
            }

            //更新进度
            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                progressDialog.setProgress((int) numBytes);
            }

            @Override
            public void onUIProgressFinish() {//下载完成
                Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                openAPK(fileSavePath);
            }
        });
    }

    /**
     * 下载完成安装apk
     *
     * @param fileSavePath
     */
    private void openAPK(String fileSavePath) {
        File file = new File(fileSavePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
            // "com.ansen.checkupdate.fileprovider"即是在清单文件中配置的authorities
            // 通过FileProvider创建一个content类型的Uri
            data = FileProvider.getUriForFile(this, "com.ansen.checkupdate.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);
    }


    WeakHandler weakhandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100://更新
                    startUpload(appUrl);
                    break;

                default:
                    break;
            }
            return false;
        }
    });
}