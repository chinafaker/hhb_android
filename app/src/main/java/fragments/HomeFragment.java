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

import com.huanghaibin.rqm.R;

import java.util.ArrayList;

import base.MainActivity;
import butterknife.BindView;
import preGuide.GuideActivity;
import preGuide.Welcome;
import utils.DialogUtil;
import utils.GoPageUtil;
import utils.Logger;
import utils.WeakHandler;

public class HomeFragment extends BaseFragment {
    public static final int MSG_FLAG_REFRESH_COMPLETE = 10;
    private static final String[] tabTitles = {"USER ID", "TOUCH ID"};
    private final int FRAGMENT_NUM = 2;
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
        pager.setOffscreenPageLimit(0);
        tabs.setupWithViewPager(pager);
        tabs.setTabsFromPagerAdapter(adapter);
        pager.setCurrentItem(0);
    }

    @Override
    public void doSomething() {
    }


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




    @butterknife.OnClick({R.id.btn_login  })
    void OnClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                DialogUtil.noticeDialog(activity,weakhandler,true);
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

                    break;
                case 8002:

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
}
