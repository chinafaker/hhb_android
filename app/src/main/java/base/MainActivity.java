package base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.huanghaibin.rqm.R;

import net.Consts;
import net.OnDataGetListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import data.GetMaintenanceInfoController;
import data.LoginController;
import fragments.HomeFragment;
import javaBean.MaintenanceInfo;
import javaBean.MaintenanceInfoBean;
import utils.DialogUtil;
import utils.GoPageUtil;
import utils.JsonUtil;
import utils.StringUtils;
import utils.ToastUtils;

public class MainActivity extends BaseActivity {
    private static final int investment_fragment_num = 1;//首页
    private HomeFragment homeFragment;
    private MainPagerAdapter mainPagerAdapter;
    // 系统时间
    public long startTime, endTime;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected int getContentView() {
        return R.layout.layout_main;
    }

    public void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        setStatusbarLightMode();
        hideTitle();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(mainPagerAdapter);
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
                    DialogUtil.noticeDialog(activity, null, true,maintenanceInfoBean);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String data) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}