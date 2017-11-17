package data;

import android.content.Context;

import net.BaseDataController;
import net.OnDataGetListener;


public class CheckVersionController extends BaseDataController {

    public CheckVersionController(Context context, OnDataGetListener listener) {
        super(context, listener);
    }

    public void getData(String channelN, String current_version) {

        String method = "/app/checkVersion";
        params.put("name", channelN);
        params.put("versionNo", current_version);
        params.put("version4upgrade", "1.1");
        getData(params, method, true);
    }
}
