package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import utils.SharedPrefUtil;


/**
 * Home 键退出程序处理 HomeKeyEventBroad
 * 
 */
public class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
	static final String SYSTEM_REASON = "reason";
	static final String SYSTEM_HOME_KEY = "homekey";// home key
	static final String SYSTEM_RECENT_APPS = "recentapps";

	// long home key
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra(SYSTEM_REASON);
			if (reason != null) {
				if (reason.equals(SYSTEM_HOME_KEY)) {
					// home key处理点
					SharedPrefUtil db = new SharedPrefUtil(context, SharedPrefUtil.ONBACKGROUND);
					db.setSharedStr("outTime", System.currentTimeMillis()+"");
					db.setSharedBoolean("OUT", true);
					db.setSharedBoolean("HOMEOUT", true);
				} else if (reason.equals(SYSTEM_RECENT_APPS)) {
					// 最近App key处理点
					SharedPrefUtil db = new SharedPrefUtil(context, SharedPrefUtil.ONBACKGROUND);
					db.setSharedStr("outTime", System.currentTimeMillis()+"");
					db.setSharedBoolean("OUT", true);
					db.setSharedBoolean("HOMEOUT", true);
				}
			}
		}
	}
}
