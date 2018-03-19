package net;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daikin.rqm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import base.App;
import base.BaseActivity;
import utils.DialogUtil;
import utils.JsonUtil;
import utils.Logger;
import utils.StringUtils;
import utils.Utils;

/**
 * 接口请求，服务端返回数据结构 >> {"success":code,"msg":{"key1":"value1","key2":"value2"...}}
 * 1)success：服务端返回的状态码
 * 2)msg：内容
 */
@SuppressWarnings("ALL")
public class BaseDataController {
    /**
     * The default socket timeout in milliseconds
     */
    public static final int DEFAULT_TIMEOUT_MS = 15 * 1000;

    /**
     * The default number of retries
     */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /**
     * The default backoff multiplier
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /**
     * 本地responseCode约定
     */
    // -4：服务器返回内容没有按约定json格式
    public static final int response_notFormat = -4;

    // -3：服务器不响应，返回内容为空
    public static final int response_nothing = -3;

    // -2：连接超时
    public static final int timeout = -2;

    // -1：没有网络
    public static final int no_net = -1;

    // 0：失败；如申请更新数据，被服务器拒绝
    public static final int failure = 0;

    // 1：成功
    public static final int success = 1;

    // 2：token无效
    public static final int token_inValid = 2;

    // 3：参数无效
    public static final int param_inValid = 3;

    // 4：请求参数中没有签名sign
    public static final int sign_notNull = 4;

    // 5：签名错误
    public static final int sign_error = 5;

    // 6：没有登录
    public static final int not_login = 6;

    // 7：404；客户端错误
    public static final int err404 = 7;

    // 8：500；服务端错误
    public static final int err500 = 8;
    /**
     * 上下文环境
     */
    public Context mContext;
    /**
     * http请求参数
     */
    public HashMap<String, String> params = new HashMap<String, String>();
    /**
     * https请求返回后的处理函数
     */
    public OnDataGetListener listener;

    private boolean isHttp = false; // 用于控制访问活动页面的域名

    private String method = "";
    private CustomRequest stringRequest;
    private volatile static RequestQueue mRequestQueue;


    /**
     * 构造器
     *
     * @param context
     * @param listener
     */
    public BaseDataController(Context context, OnDataGetListener listener) {
        this.mContext = context;
        this.listener = listener;
    }


    /**
     * 获取 RequestQueue 单例，双重锁模式
     *
     * @return
     */
    public RequestQueue getRequestQueueInstance(Context context) {
        if (mRequestQueue == null) {
            synchronized (BaseDataController.this) {
                if (mRequestQueue == null) {
                    KeyStore ksTrust = null;
                    SSLContext sslContext = null;
                    try {
                        final TrustManager[] trustAllCerts = new TrustManager[]{
                                new X509TrustManager() {
                                    @Override
                                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                    }

                                    @Override
                                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                    }

                                    @Override
                                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                        return null;
                                    }
                                }
                        };

                        sslContext = SSLContext.getInstance("SSL");
                        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    SSLSocketFactory sf = sslContext != null ? sslContext.getSocketFactory() : null;
                    //HurlStack两个参数默认都是null,如果传入SSLSocketFactory，那么会以Https的方式来请求网络
                    HurlStack stack = new HurlStack(null, sf);
                    mRequestQueue = Volley.newRequestQueue(context.getApplicationContext(), stack);
                }
            }
        }
        return mRequestQueue;
    }

    private void dealResp(int responseCode, String msg) {
        switch (responseCode) {
            case failure:
                listener.onGetDataFailed(responseCode, msg);
                break;
            case success:
                listener.onGetDataSuccess(msg);
                break;
            case token_inValid:

                listener.onGetDataFailed(responseCode, mContext.getResources().getString(R.string.tip_token_inValid));
                break;
            case param_inValid:
                listener.onGetDataFailed(responseCode, mContext.getResources().getString(R.string.tip_param_inValid));
                break;
            case sign_notNull:
                listener.onGetDataFailed(responseCode, mContext.getResources().getString(R.string.tip_sign_notNull));
                break;
            case sign_error:
                listener.onGetDataFailed(responseCode, mContext.getResources().getString(R.string.tip_sign_error));
                break;
            case not_login:

                listener.onGetDataFailed(responseCode, mContext.getResources().getString(R.string.tip_not_login));
                break;
            case err404:
                listener.onGetDataFailed(responseCode, mContext.getResources().getString(R.string.tip_err404));
                break;
            case err500:
                listener.onGetDataFailed(responseCode, mContext.getResources().getString(R.string.tip_err500));
                break;
            default:
                listener.onGetDataFailed(responseCode, msg);
                break;
        }
    }


    /**
     * @param params
     * @return void 返回类型
     * @Title: getData
     * @Description: 请求数据的方法
     * @author vincent
     */
    public void getData(final HashMap<String, String> params, final String method, Boolean isPost) {
        this.method = method;
        isHttp = method.contains("getSiteBanners");
        params.put("milestone", "1");
        params.put("channel", Consts.CHANNEL);
        params.put("version", Consts.NET_VERSION);
        if (params.containsKey("version4upgrade")) {
            params.put("version", params.get("version4upgrade"));
            params.remove("version4upgrade");
        }
        params.put("token", Utils.isEmpty(App.token()) ? App.normalToken() : App.token());
        try {
            params.remove("sign");
            params.put("sign", "0135c7277354cfca3bd633732bf6132d");
        } catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        if (!NetUtils.isConnected(mContext)) {
            listener.onGetDataFailed(no_net, mContext.getResources().getString(R.string.nonet));
            return;
        }
        if (stringRequest == null) {
            stringRequest = new CustomRequest(Request.Method.POST, getAbsoluteUrl(isHttp, method), new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    if (StringUtils.isEmpty(result)) {
                        if (mContext instanceof BaseActivity) {
                            ((BaseActivity) mContext).disProDialog();
                        }
                        listener.onGetDataFailed(response_nothing, mContext.getResources().getString(R.string.tip_no_response));
                        return;
                    }

                    Logger.i("===== " + method + " 返回数据：" + result);
                    if (Utils.isEmpty(result)) {
                        listener.onGetDataFailed(response_nothing, mContext.getResources().getString(R.string.net_exception));
                    } else {
                        try {
                            JSONObject jo = new JSONObject(result);
                            int responseCode = jo.getInt("success");
                            String msg = jo.getString("msg");
                            dealResp(responseCode, msg);
                        } catch (JSONException e) {
                            if (Logger.B_LOG_OPEN) {
                                e.printStackTrace();
                            }
                            listener.onGetDataFailed(response_notFormat, mContext.getResources().getString(R.string.net_exception));
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        if (mContext instanceof BaseActivity) {
                            ((BaseActivity) mContext).disProDialog();
                        }
                        listener.onGetDataFailed(timeout, mContext.getResources().getString(R.string.tip_timeout));
                        return;
                    }
                    Logger.e("失败原因：" + error.getMessage());
                    String result = new String(error.networkResponse.data);
                    Logger.i("onFailure(" + method + ")----->" + result);

                    if (Utils.isEmpty(result)) {
                        listener.onGetDataFailed(timeout, mContext.getResources().getString(R.string.net_exception));
                    } else {
                        try {
                            JSONObject jo = new JSONObject(result);
                            int responseCode = jo.getInt("success");
                            String msg = jo.getString("msg");
                            dealResp(responseCode, msg);
                        } catch (JSONException e) {
                            if (Logger.B_LOG_OPEN) {
                                e.printStackTrace();
                            }
                            listener.onGetDataFailed(response_notFormat, mContext.getResources().getString(R.string.net_exception));
                        }
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };
        }
        stringRequest.setTag(mContext.getClass().getSimpleName());
        stringRequest.setShouldCache(false);
        Logger.i("===== 请求地址：" + stringRequest.getUrl());
        Logger.i("===== " + method + " 请求参数：" + JsonUtil.jsonFromObject(params));
        addRequest(App.getApp(), stringRequest);


    }

    public void cancelRequest(Context context) {
        if (stringRequest != null) {
            stringRequest.cancel();
        }
    }

    class CustomRequest extends StringRequest {

        public CustomRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
            setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES,
                    DEFAULT_BACKOFF_MULT));
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("X-DJT-TOKEN", "");
            if (TextUtils.isEmpty(App.userAgent)) {
                headerMap.put("User-Agent", App.getUserAgent());
            } else {
                headerMap.put("User-Agent", App.userAgent);
            }
            return headerMap;
        }
    }

    private static String getAbsoluteUrl(boolean isHttp, String relativeUrl) {
        return (isHttp ? Consts.BASE_ADDRESS_HTTP : Consts.BASE_ADDRESS) + relativeUrl + ".htm";

    }


    private static String getAbsoluteUrlRQM(String relativeUrl, HashMap<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(Consts.BASE_ADDRESS_RQM);
        urlBuilder.append(relativeUrl);

        try {
            if (null != params) {
                urlBuilder.append("?");
                Iterator<Map.Entry<String, String>> iterator = params.entrySet()
                        .iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> param = iterator.next();
                    urlBuilder
                            .append(URLEncoder.encode(param.getKey(), "UTF-8"))
                            .append('=')
                            .append(URLEncoder.encode(param.getValue(), "UTF-8"));

                    if (iterator.hasNext()) {
                        urlBuilder.append('&');
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlBuilder.toString();
     /*   if ("/rest/news/getMaintenanceInfo".equals(relativeUrl)) {
            String url = urlBuilder.toString();
            if (url.contains("%2F")) {
                url.replaceAll("%2F", "/");
            }
            return url;
        } else {
            return urlBuilder.toString();
        }*/


    }


    /**
     * @param params
     * @return void 返回类型
     * @Title: getData
     * @Description: 请求数据的方法
     * @author vincent
     */
    public void getDataRQM(final HashMap<String, String> params, final String method) {
        this.method = method;
        if (!NetUtils.isConnected(mContext)) {
            listener.onGetDataFailed(no_net, "");
            DialogUtil.normalSureBtn(mContext, "OK", "Kindly  Reminder", "The Internet connection failed. Please check the network setting.", null, true);
            return;
        }

        stringRequest = new CustomRequest(Request.Method.POST, getAbsoluteUrlRQM(method, params), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Logger.i("===== " + method + " 返回数据：" + result);
                if (Utils.isEmpty(result)) {
                    listener.onGetDataFailed(no_net, "");
                    DialogUtil.normalSureBtn(mContext, "OK", "Kindly  Reminder", "Exception occurred when getting data.", null, true);
                } else {
                    try {
                        JSONObject jo = new JSONObject(result);
                        boolean responseCode = jo.getBoolean("code");
                        if (responseCode) {
                            String data = jo.getString("data");
                            listener.onGetDataSuccess(data);
                        } else {
                            String fc = jo.getString("errorDes");//errorDes：【失败错误码如下：
                            listener.onGetDataFailed(0, fc);
                        }

                    } catch (JSONException e) {
                        if (Logger.B_LOG_OPEN) {
                            e.printStackTrace();
                        }
                        listener.onGetDataFailed(no_net, "");
                        DialogUtil.normalSureBtn(mContext, "OK", "Kindly  Reminder", "Exception occurred when getting data.", null, true);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    if (mContext instanceof BaseActivity) {
                        ((BaseActivity) mContext).disProDialog();
                    }
                    listener.onGetDataFailed(no_net, "");
                    DialogUtil.normalSureBtn(mContext, "OK", "Kindly  Reminder", "Exception occurred when getting data.", null, true);
                    return;
                }
                Logger.e("失败原因：" + error.getMessage());
                String result = new String(error.networkResponse.data);
                Logger.i("onFailure(" + method + ")----->" + result);

                if (Utils.isEmpty(result)) {
                    listener.onGetDataFailed(no_net, "");
                    DialogUtil.normalSureBtn(mContext, "OK", "Kindly  Reminder", "Exception occurred when getting data.", null, true);
                } else {
                    try {
                        JSONObject jo = new JSONObject(result);
                        boolean responseCode = jo.getBoolean("code");
                        String data = jo.getString("data");
                        String fc = jo.getString("fc");//fc：【失败错误码如下：

                    } catch (JSONException e) {
                        if (Logger.B_LOG_OPEN) {
                            e.printStackTrace();
                        }
                        listener.onGetDataFailed(no_net, "");
                        DialogUtil.normalSureBtn(mContext, "OK", "Kindly  Reminder", "Exception occurred when getting data.", null, true);
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };


        Logger.i("===== 请求地址：" + stringRequest.getUrl());
        Logger.i("===== " + method + " 请求参数：" + JsonUtil.jsonFromObject(params));
        stringRequest.setTag(mContext.getClass().getSimpleName());
        stringRequest.setShouldCache(false);
        addRequest(App.getApp(), stringRequest);
    }

    /**
     * 添加网络请求
     *
     * @param request 网络请求
     */
    public void addRequest(Context context, Request request) {
        RequestQueue requestQueue = getRequestQueueInstance(context.getApplicationContext());
        requestQueue.add(request);
    }

}
