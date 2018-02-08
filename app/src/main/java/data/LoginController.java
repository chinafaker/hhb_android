package data;

import android.content.Context;

import net.BaseDataController;
import net.OnDataGetListener;


public class LoginController extends BaseDataController {

    public LoginController(Context context, OnDataGetListener listener) {
        super(context, listener);
    }

    public void getData(String DIL, String test) {
        String method = "/rest/user/login";
        params.put("loginName", DIL);
        params.put("password", test);
        getDataRQM(params, method);
    }
}
