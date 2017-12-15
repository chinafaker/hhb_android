package javaBean;

/**
 * Created by hhb on 2017/12/1.
 */

public class Appversion {
    /*  {
          "appName": "",
              "appUrl": "",
              "forceUpdateFlg": "2",
              "updateComment": "Fit Bugs",
              "versionCode": "1001",
              "versionName": "1.0.1"
      }*/
    private String appName;
    private String appUrl;
    private String forceUpdateFlg;//1：强制更新；2：非强制（选择）更新
    private String updateComment;
    private String versionCode;
    private String versionName;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getForceUpdateFlg() {
        return forceUpdateFlg;
    }

    public void setForceUpdateFlg(String forceUpdateFlg) {
        this.forceUpdateFlg = forceUpdateFlg;
    }

    public String getUpdateComment() {
        return updateComment;
    }

    public void setUpdateComment(String updateComment) {
        this.updateComment = updateComment;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
