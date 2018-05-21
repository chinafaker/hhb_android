package web;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.daikin.rqm.R;

import net.Consts;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import base.LvjianActivity;
import butterknife.BindView;
import utils.DialogUtil;
import utils.GoPageUtil;
import utils.StringUtils;
import utils.ToastUtils;
import utils.WeakHandler;


public class WebviewHome extends BaseWebview {

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
        iv_leftIcon.setVisibility(View.VISIBLE);

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
                onBackPressed();
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
                    Intent intent   = new Intent(WebviewHome.this, LvjianActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
        if (!webView.canGoBack()) {
            DialogUtil.isSureExitDialog(this, weakhandler);
        } else {
            if (sharedPrefUtil.getSharedBoolean(Consts.NOTDISPLAYTODAY, false)&&url.contains(Consts.RQM_CONTAINER_LIST_URL)) {
                DialogUtil.isSureExitDialog(this, weakhandler);
            } else {
                webView.goBack();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String data) {
        url = data;
    }


}
