package web;

import android.content.Intent;
import android.os.Handler;

import com.huanghaibin.rqm.R;

import utils.GoPageUtil;
import utils.StringUtils;


public class WebviewHome extends BaseWebview {

    @Override
    protected int getContentView() {
        return R.layout.layout_webview_normal;
    }

    @Override
    public void initView() {
        super.initView();
        setLeftIcon(R.mipmap.icon_back);
        setLeftTv(getResources().getString(R.string.go_back));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadUrl();
            }
        }, 50);

    }

    @Override
    public void onLeftClick() {
        onBackPressed();
    }

    @Override
    public void onRightClick() {
        if (!StringUtils.isEmpty(rightUrl)) {
            GoPageUtil.jumpTobyUrlLink(this, rightUrl);
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
