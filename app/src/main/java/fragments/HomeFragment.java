package fragments;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;

import com.huanghaibin.rqm.R;

import utils.SystemUtils;
import utils.WeakHandler;

public class HomeFragment extends BaseFragment {
    // 首页banner
    private ViewPager viewaPager;
    public static final int MSG_FLAG_REFRESH_COMPLETE = 10;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public int initInflateView() {
        return R.layout.fragment_home_refreshable;
    }


    WeakHandler mHandler = new WeakHandler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FLAG_REFRESH_COMPLETE:// 刷新控件完成刷新
                    mSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 200);

                    break;

            }
            return false;
        }
    });

    @Override
    public void getMyViews() {

        // 包裹标题栏的LinearLayout
        RelativeLayout titleWrapperLin = (RelativeLayout) getView().findViewById(R.id.titleWrapperLin);
        // 初始化标题栏
        if (SystemUtils.isPlatformVersionOver44()) {
            int statusHeight = SystemUtils.getStatusBarHeight(activity);
            int pl = titleWrapperLin.getPaddingLeft();
            int pt = titleWrapperLin.getPaddingTop();
            int pr = titleWrapperLin.getPaddingRight();
            int pb = titleWrapperLin.getPaddingBottom();
            titleWrapperLin.setPadding(pl, pt + statusHeight, pr, pb);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_229EFF);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }



    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }







    @Override
    public void doSomething() {}

}
