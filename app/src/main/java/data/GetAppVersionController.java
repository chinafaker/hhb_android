package data;

import android.content.Context;

import net.BaseDataController;
import net.OnDataGetListener;


public class GetAppVersionController extends BaseDataController {

    public GetAppVersionController(Context context, OnDataGetListener listener) {
        super(context, listener);
    }

    public void getData() {
        String method = "/rest/version/getAppVersion";
        params.put("mobileType", "2");
        getDataRQM(params, method);
    }
}
