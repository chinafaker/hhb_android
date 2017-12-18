package data;

import android.content.Context;

import net.BaseDataController;
import net.OnDataGetListener;


public class GetMaintenanceInfoController extends BaseDataController {

    public GetMaintenanceInfoController(Context context, OnDataGetListener listener) {
        super(context, listener);
    }

    public void getData(String timeZone) {
        String method = "/rest/news/getMaintenanceInfo";
        params.put("timeZone", timeZone);
        getDataRQM(params, method);
    }
}
