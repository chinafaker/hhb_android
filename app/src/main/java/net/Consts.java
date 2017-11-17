package net;

public class Consts {
    public static final String CHANNEL = "android";
    public static final String NET_VERSION = "1.0";
    public static String SIGH_KEY="DhoxwCWcw8XnnvR0W5YDIpMT8cCb2nbaut9jlBbCj1PZl8Zel5BXefjefcZWDFZhSeBMkNHinOVwPzRvt7Tips2O2Vno4jolYEkqBx3qf25szJYoEtu2wDGWLpm4YKU2";
    public static String BASE = "guangqun.cn";
    private static final String BASE_T = "https://t.";
    private static final String BASE_API = "https://api.";
    private static final String BASE_WX = "https://wx.";

    public static String BASE_ADDRESS = "https://api.guangqun.cn";
    public static String BASE_ADDRESS_HTTP = "https://api.guangqun.cn";

    public static String COMMUNITY_DETAIL = "https://t.guangqun.cn/appCommunity/articleDetail.htm";


    // 新手攻略
    public static String WEB_NEWER_GUIDE = "https://wx.guangqun.cn/prom/register.htm";
    // 发红包
    public static String WEB_RECOMMEND = "https://wx.guangqun.cn/prom/recommend.htm";
    // 大转盘
    public static String WEB_LOTTERYDAILY = "https://wx.guangqun.cn/prom/lotteryDaily.htm";
    // 分享红包
    public static String WEB_REDPACKET = "https://wx.guangqun.cn/com/toSendBonus.htm";
    // 安全保障
    public static String WEB_HOME_SECURITY = "https://t.guangqun.cn/safeGuarantee/index.html";
    // 喵喵宝详情
    public static String WEB_MMB_DETAIL = "https://wx.guangqun.cn/com/toMmbNewDetail.htm";
    //注册就送1999元体验金+3%加息券
    public static String WEB_ZCM_BANNER_ONE = "https://wx.guangqun.cn/prom/register.htm";
    // 积分规则
    public static String WEB_CREDIT_RULE = "https://t.guangqun.cn/credit/rule.htm";
    // 赚积分
    public static String WEB_CREDIT_ERAN = "https://t.guangqun.cn/credit/spreadDetail.htm";

    public static String WEB_PAY_EXPLAIN = "https://t.guangqun.cn/pay/mmb/gainPayExplain.htm";


    //摇奖活动规则
    public static String MONEY_TREE_INSTRUCTION = "https://t.guangqun.cn/shakeMoneyTree/shakeMoneyTreeRule.htm";
    //摇奖惊喜攻略
    public static String MONEY_TREE_STRATEGY = "https://t.guangqun.cn/shakeMoneyTree/shakeMoneyTreeRewardShow.htm";
    //摇奖分享url
    public static String MONEY_TREE_SHARED_URL = "https://t.guangqun.cn/shakeMoneyTree/shakeMoneyTreeShare.htm";

    // 风险揭示书
    public static String WEB_RISK_DISCLOSURE = "https://t.guangqun.cn/riskevaluation/disclosure.htm";
    // 风险测评
    public static String WEB_RISK_EVALUATION = "http://t.guangqun.cn/riskevaluation/index.htm";
    // 一元购
    public static String WEB_YIYUANGOU = "http://t.guangqun.cn/cloudpurchase/userRecord.htm";

    // 云购结果页
    public static String WEB_ORDER_RESULT = "https://t.guangqun.cn/cloudpurchase/orderResult.htm";
    //智能投资规则
    public static String WEB_INTELLIGENT_INVEST_RULE = "https://t.guangqun.cn/inteinv/rule.htm";
    //智能投资协议
    public static String WEB_INTELLIGENT_INVEST_DEAL = "https://t.guangqun.cn/inteinv/protocol.htm";
    //券中心规则
    public static String WEB_INTELLIGENT_COUPON_RULE = "https://t.guangqun.cn/payCoupon/rule.htm";
    //签到H5
    public static String WEB_SIGN = "https://t.guangqun.cn/sign/show.htm";
    //签到规则
    public static String WEB_SIGN_RULE = "https://t.guangqun.cn/sign/rule.htm";
    //协议范本
    public static String WEB_CLAIM_RULE = "http://t.guangqun.cn/claimsProtocol/claimsProtocol.htm";
    //协议范本
    public static String WEB_MSG_DETAIL = "http://t.guangqun.cn/cmscontent/";
    //注册协议
    public static String WEB_REGIST = "http://t.guangqun.cn/agreement/registerAgreement.htm";
    //出借人协议
    public static String LEND_AGREEMENT = "http://t.guangqun.cn/static/template/lendAgreement.html";
    //开户帮助说明
    public static String BINDBANK_HELP = "http://t.guangqun.cn/static/template/helpText.html";

    public static void resetAllUrls(String base_url) {
        BASE = base_url;

        BASE_ADDRESS = BASE_API + BASE;
        // 活动页面域名
        BASE_ADDRESS_HTTP = BASE_API + BASE;

        COMMUNITY_DETAIL = BASE_T + BASE + "/appCommunity/articleDetail.htm";

        // 新手攻略
        WEB_NEWER_GUIDE = BASE_WX + BASE + "/prom/register.htm";
        // 发红包
        WEB_RECOMMEND = BASE_WX + BASE + "/prom/recommend.htm";
        // 大转盘
        WEB_LOTTERYDAILY = BASE_WX + BASE + "/prom/lotteryDaily.htm";
        // 分享红包
        WEB_REDPACKET = BASE_WX + BASE + "/com/toSendBonus.htm";
        // 安全保障
        WEB_HOME_SECURITY = BASE_T + BASE + "/safeGuarantee/index.html";
        // 喵喵宝详情
        WEB_MMB_DETAIL = BASE_WX + BASE + "/com/toMmbNewDetail.htm";
        //注册就送1999元体验金+3%加息券
        WEB_ZCM_BANNER_ONE = BASE_WX + BASE + "/prom/register.htm";
        // 积分规则
        WEB_CREDIT_RULE = BASE_T + BASE + "/credit/rule.htm";
        // 赚积分
        WEB_CREDIT_ERAN = BASE_T + BASE + "/credit/spreadDetail.htm";

        WEB_PAY_EXPLAIN = BASE_T + BASE + "/pay/mmb/gainPayExplain.htm";

        //摇奖活动规则
        MONEY_TREE_INSTRUCTION = BASE_T + BASE + "/shakeMoneyTree/shakeMoneyTreeRule.htm";
        //摇奖惊喜攻略
        MONEY_TREE_STRATEGY = BASE_T + BASE + "/shakeMoneyTree/shakeMoneyTreeRewardShow.htm";
        //摇奖分享url
        MONEY_TREE_SHARED_URL = BASE_T + BASE + "/shakeMoneyTree/shakeMoneyTreeShare.htm";

        // 风险揭示书
        WEB_RISK_DISCLOSURE = BASE_T + BASE + "/riskevaluation/disclosure.htm";
        // 风险测评
        WEB_RISK_EVALUATION = BASE_T + BASE + "/riskevaluation/index.htm";
        // 一元购
        WEB_YIYUANGOU = BASE_T + BASE + "/cloudpurchase/userRecord.htm";

        // 云购结果页
        WEB_ORDER_RESULT = BASE_T + BASE + "/cloudpurchase/orderResult.htm";
        //智能投资规则
        WEB_INTELLIGENT_INVEST_RULE = BASE_T + BASE + "/inteinv/rule.htm";
        //智能投资协议
        WEB_INTELLIGENT_INVEST_DEAL = BASE_T + BASE + "/inteinv/protocol.htm";
        //券中心规则
        WEB_INTELLIGENT_COUPON_RULE = BASE_T + BASE + "/payCoupon/rule.htm";
        //签到H5
        WEB_SIGN = BASE_T + BASE + "/sign/show.htm";
        //签到规则
        WEB_SIGN_RULE = BASE_T + BASE + "/sign/rule.htm";
        //协议范本
        WEB_CLAIM_RULE = BASE_T + BASE + "/claimsProtocol/claimsProtocol.htm";
        //消息详情
        WEB_MSG_DETAIL = BASE_T + BASE + "/cmscontent/";
        //注册协议
        WEB_REGIST = BASE_T + BASE + "/agreement/registerAgreement.htm";
        //出借人协议
        LEND_AGREEMENT = BASE_T + BASE + "/static/template/lendAgreement.html";
        //开户帮助说明
        LEND_AGREEMENT = BASE_T + BASE + "/static/template/helpText.html";
    }

    public static final String CHANNEL_CREDIT = "CREDIT";


    /**
     * 密码强度
     */
    public static class PwdStrength {
        public static final int WEAK = 1;
        public static final int GENERAL = 2;
        public static final int STRONG = 3;
    }

    public static final String JS_PROXY_NAME = "androidmmk";

    public static final String CLAIM_FOR_NEW = "newbie";
    public static final String CLAIM_FOR_TYJ = "tyb";

    public static final String METHOD_MOBILECODE_REGISTER = "/mobileCode/android/register";
    public static final String METHOD_PICCODE_REGISTER = "/picCode/android/register";

    public static final String METHOD_MOBILECODE_FOGOT = "/mobileCode/android/forgot";
    public static final String METHOD_PICCODE_FOGOT = "/picCode/android/forgot";

    public static final String METHOD_MOBILECODE_APPLOGIN = "/mobileCode/android/appLogin";
    public static final String METHOD_PICCODE_APPLOGIN = "/picCode/android/appLogin";

    public static final String METHOD_PICCODE_CHANGETPWD = "/picCode/android/changeTPwd";

    public static final String METHOD_PICCODE_CHANGEMOBILE = "/picCode/android/changeMobile";

    public static final String INVEST_IMMEDIATELY = "立即加入";

    public static final boolean USE_NEW_BANK = false;
    public static boolean HAS_INFO_BINDBANK = false;
}
