package data;

import android.content.Context;

import net.BaseDataController;
import net.OnDataGetListener;


public class GetMaintenanceInfoController extends BaseDataController {

    public GetMaintenanceInfoController(Context context, OnDataGetListener listener) {
        super(context, listener);
    }

    public void getData() {
        String method = "/rest/news/getMaintenanceInfo";
        getDataRQM(params, method);
    }
}
