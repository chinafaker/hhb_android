package web;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.rqm.rqm.R;

import net.Consts;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import base.ApplicationManager;
import butterknife.BindView;
import utils.DialogUtil;
import utils.GoPageUtil;
import utils.StringUtils;
import utils.ToastUtils;
import utils.WeakHandler;


public class WebviewHome extends BaseWebview {
    // 系统时间
    public long startTime, endTime;
    @BindView(R.id.iv_leftIcon)
    ImageView iv_leftIcon;

    @BindView(R.id.iv_rightIcon)
    ImageView iv_rightIcon;

    @Override
    protected int getContentView() {
        return R.layout.layout_webview_normal;
    }

    @Override
    public void initView() {
        super.initView();
        setStatusbarLightMode();
        setLeftIconGone();
        setRightIconGone();

        iv_rightIcon.setVisibility(View.VISIBLE);
        if (url.contains(Consts.RQM_CONTAINER_LIST_URL)) {   //Consts.RQM_CONTAINER_LIST_URL.equals(url)
            iv_leftIcon.setVisibility(View.GONE);
        } else {
            iv_leftIcon.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadUrl();
            }
        }, 50);

    }


    @butterknife.OnClick({R.id.iv_leftIcon, R.id.iv_rightIcon})
    void OnClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_leftIcon:
                if (url.contains(Consts.RQM_TOP_NOTICE_URL)) {   //Consts.RQM_TOP_NOTICE_URL.equals(url)
                    DialogUtil.isSureExitDialog(this, weakhandler);
                } else {
                    onBackPressed();
                }
                break;
            case R.id.iv_rightIcon:
                if (!StringUtils.isEmpty(rightUrl)) {
                    GoPageUtil.jumpTobyUrlLink(this, rightUrl);
                } else {
                    ToastUtils.show(this, "setting");
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
                case 110:
                    break;
                case 120:
                    onBackPressed();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    public void onRightClick() {
        if (!StringUtils.isEmpty(rightUrl)) {
            GoPageUtil.jumpTobyUrlLink(this, rightUrl);
        } else {
            ToastUtils.show(this, "setting");
        }
    }

    @Override
    public void onBackPressed() {
        if (ifCloseWhenClickBack) {
            WebviewHome.this.finish();
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                WebviewHome.this.finish();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (url.contains(Consts.RQM_CONTAINER_LIST_URL)) { //Consts.RQM_CONTAINER_LIST_URL.equals(url)
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
        } else if (url.contains(Consts.RQM_TOP_NOTICE_URL)) {   //Consts.RQM_TOP_NOTICE_URL.equals(url)
            DialogUtil.isSureExitDialog(this, weakhandler);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String data) {
        url = data;
    }


}
