package web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daikin.rqm.R;

import net.Consts;
import net.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import base.BaseActivity;
import butterknife.BindView;
import utils.DialogUtil;
import utils.Logger;
import utils.SharedPrefUtil;
import utils.StringUtils;
import utils.TimeUtils;
import utils.Utils;

public class BaseWebview extends BaseActivity {
    @BindView(R.id.webView)
    protected WebView webView;
    //  @BindView(R.id.swipeLayout)
    //   protected SwipeRefreshLayout swipeLayout;

    public String url = "", title = "";
    protected String articleId = "";
    protected int isOpenMode = 0;
    protected boolean ifCloseWhenClickBack = false;
    protected String rightUrl = "";

    protected boolean needClearHistory = false;
    protected boolean justFinishWhen2Home = false;

    /**
     * 501：债权 502：现金红包 503:加息券 504:积分 505:体验金
     */
    private int dealLogin = 501;
    private boolean ifShowProgressbar = true;
    public SharedPrefUtil sharedPrefUtil;
    public SharedPrefUtil db;

    @Override
    public void initView() {
        super.initView();
        setLeftIcon(R.mipmap.icon_back);
        setLeftTv(getResources().getString(R.string.go_back));
        sharedPrefUtil = new SharedPrefUtil(activity, Consts.SHAREDPREFENCERQM);
        db = new SharedPrefUtil(activity, SharedPrefUtil.ONBACKGROUND);
        EventBus.getDefault().register(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            if (!StringUtils.isEmpty(url)) {
                if (!url.startsWith("http")) {
                    url = "http://" + url;
                }
            }
            Logger.e("传递过来的地址：" + url);
            title = bundle.getString("title");
            articleId = bundle.getInt("articleId") + "";
            if (StringUtils.isEmpty(articleId) || articleId.equals("0")) {
                if (!StringUtils.isEmpty(url) && url.contains("articleId")) {
                    articleId = url.substring(url.lastIndexOf("=") + 1);
                }
            }
            isOpenMode = bundle.getInt("isOpenMode");
            setTitle(StringUtils.noNull(title));

            int iconSrc = bundle.getInt("right_icon");
            if (iconSrc != 0) {
                setRightIcon(iconSrc);
            }

            rightUrl = bundle.getString("right_url");

            justFinishWhen2Home = bundle.getBoolean("justFinishWhen2Home");

        }

        initWebView();
    }


    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            ifShowProgressbar = false;
            loadUrl();
        }
    };

    private void initWebView() {
        webView.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!StringUtils.isEmpty(title)) {
                    if (StringUtils.isEmpty(BaseWebview.this.title)) {
                        setTitle(title);
                    }
                }
            }

        };
        webView.setWebChromeClient(wvcc);// 使h5内的js可用

        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //解决5.0以上高版本不显示图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //这两句基本上就可以做到屏幕适配了
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBlockNetworkLoads(false);
        settings.setBlockNetworkImage(false);
        settings.setGeolocationEnabled(true);
        //添加对js功能的支持
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);
        //当要对网页内的源码进行解析功能
        settings.setDomStorageEnabled(true);
        //主要用于平板，针对特定屏幕代码调整分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        webView.addJavascriptInterface(this, "RQM");//window.RQM
        webView.addJavascriptInterface(this, "window.RQM");//window.RQM
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view,

                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            // 这个方法在用户试图点开页面上的某个链接时被调用
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                EventBus.getDefault().post(url);
                if (url.contains(Consts.RQM_CONTAINER_LIST_URL)) {      //Consts.RQM_CONTAINER_LIST_URL.equals(url)
                    rlleft.setVisibility(View.VISIBLE);
                } else {
                    rlleft.setVisibility(View.VISIBLE);
                }


                if (!Utils.isEmpty(url)) {
                    if (url.endsWith(".apk")) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        webView.loadUrl(url);
                    }
                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                EventBus.getDefault().post(url);
                if (ifShowProgressbar) {
                    showProDialogCancel();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                disProDialog();

                if (ifFinish) {
                    return;
                }

                if (webView != null && !webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                if (needClearHistory) {
                    needClearHistory = false;
                    if (webView != null) {
                        webView.clearHistory();//清除历史记录
                    }
                }
            }
        });
    }

    protected void loadUrl() {
        needClearHistory = true;
        if (StringUtils.isEmpty(url)) {
            return;
        }
        String postData = getPostData();
        EventBus.getDefault().post(url);
        if (!NetUtils.isConnected(this)) {
            DialogUtil.normalSureBtn(this, "OK", "Kindly  Reminder", "The Internet connection failed. Please check the network setting.", null, true);
            return;
        }
        webView.loadUrl(url + postData);
    }

    /**
     * 加载web页面+相关参数；
     *
     * @return
     */
    protected String getPostData() {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        HashMap<String, String> params = new HashMap<String, String>();
        if (!url.contains("channel")) {
            params.put("channel", Consts.CHANNEL);
        }
        if (!url.contains("milestone")) {
            params.put("milestone", "1");
        }
        if (!url.contains("articleId")) {
            params.put("articleId", articleId + "");
        }
        if (!url.contains("version")) {
            params.put("version", Consts.NET_VERSION);
        }

        if (!url.contains("app_version_num")) {
            params.put("app_version_num", Utils.getVersionCode() + "");
        }


        try {
            String sb = "";
            if (!url.contains("channel")) {
                sb += "channel="
                        + Consts.CHANNEL;
            }
            if (!url.contains("milestone")) {
                sb += "&milestone=1";
            }
            if (!url.contains("articleId") && !StringUtils.isEmpty(articleId)) {
                sb += "&articleId=" + articleId;
            }
            if (!url.contains("version")) {
                sb += "&version="
                        + Consts.NET_VERSION;
            }


            if (!url.contains("app_version_num") && !StringUtils.isEmpty(sb)) {//客户端版本号，如325 方便以后扩展（如h5可根据版本号控制某些内容是否显示）
                sb += "&app_version_num="
                        + Utils.getVersionCode();
            }

            if (StringUtils.isEmpty(sb)) {
                return "";
            }
            if (url.contains("?")) {
                if (sb.startsWith("&")) {
                    return sb;
                } else {
                    return "&" + sb;
                }
            } else {
                if (sb.startsWith("&")) {
                    return "?" + sb.substring(1);
                } else {
                    return "?" + sb;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onLeftClick() {
    }

    @Override
    public void onRightClick() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        }
    }


    //处理WebView内存泄漏
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @JavascriptInterface
    public void dismiss() {
        this.finish();
    }


    @JavascriptInterface
    public void isShowToday(boolean istrue) {
        if (istrue) {
            sharedPrefUtil.setSharedBoolean(Consts.NOTDISPLAYTODAY, true);
            sharedPrefUtil.setSharedStr(Consts.LASTTIMESTR, TimeUtils.getCurrentDateString());
        } else {
            sharedPrefUtil.setSharedBoolean(Consts.NOTDISPLAYTODAY, false);
            sharedPrefUtil.setSharedStr(Consts.LASTTIMESTR, "");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String data) {

    }

}
