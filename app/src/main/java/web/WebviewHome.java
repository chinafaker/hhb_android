package web;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.huanghaibin.rqm.R;

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
        iv_rightIcon.setVisibility(View.GONE);
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
                DialogUtil.isSureExitDialog(this,weakhandler);
                break;
            case R.id.iv_rightIcon:
                if (!StringUtils.isEmpty(rightUrl)) {
                    GoPageUtil.jumpTobyUrlLink(this, rightUrl);
                } else {
                    //
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


}
