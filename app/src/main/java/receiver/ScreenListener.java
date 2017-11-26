package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * 锁屏监听事件
 */
public class ScreenListener {
	private Context context;
	private ScreenBroadcastReceiver mScreenReceiver;
	private ScreenStateListener mScreenStateListener;

	public ScreenListener(Context context) {
		this.context = context;
		mScreenReceiver = new ScreenBroadcastReceiver();
	}

	private class ScreenBroadcastReceiver extends BroadcastReceiver {
		private String action = null;

		@Override
		public void onReceive(Context context, Intent intent) {
			action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				mScreenStateListener.onScreenOn();
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				mScreenStateListener.onScreenOff();
			} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
				mScreenStateListener.onUserPresent();
			}
		}
	}

	public void begin(ScreenStateListener listener) {
		mScreenStateListener = listener;
		registerListener();
		getScreenState();
	}

	private void getScreenState() {
		PowerManager manager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		if (manager.isScreenOn()) {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOn();
			}
		} else {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOff();
			}
		}
	}

	public void unregisterListener() {
		context.unregisterReceiver(mScreenReceiver);
	}

	private void registerListener() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		context.registerReceiver(mScreenReceiver, filter);
	}

	public interface ScreenStateListener {
		public void onScreenOn();

		public void onScreenOff();

		public void onUserPresent();
	}
}
