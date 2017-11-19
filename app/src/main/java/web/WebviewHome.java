package web;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.huanghaibin.rqm.R;

import butterknife.BindView;
import utils.DialogUtil;
import utils.GoPageUtil;
import utils.StringUtils;
import utils.ToastUtils;


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
                    DialogUtil.noticeDialog(activity, null, true);
                    ToastUtils.show(this, "setting");
                }
                break;

            default:
                break;

        }
    }


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
