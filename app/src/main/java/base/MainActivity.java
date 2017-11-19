package base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.huanghaibin.rqm.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import fragments.HomeFragment;
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