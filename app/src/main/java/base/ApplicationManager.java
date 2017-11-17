package base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import java.util.ArrayList;
import java.util.Stack;

import utils.Logger;

public class ApplicationManager {

	private static Stack<Activity> activityStack;

	private static ApplicationManager instance;

	private ApplicationManager() {

		activityStack = new Stack<Activity>();
	}

	/**
	 *
	 * @Description描述:
	 * @return
	 * @return AppManager
	 */
	public static ApplicationManager getInstance() {

		if (instance == null) {
			instance = new ApplicationManager();
		}

		return instance;
	}

	/**
	 *
	 * @Description描述:
	 * @return
	 * @return boolean
	 */

	public boolean isEmpty() {
		return activityStack.isEmpty();
	}

	/**
	 *
	 * @Description描述:
	 * @param activity
	 * @return void
	 */
	public void addActivity(Activity activity) {
		activityStack.add(activity);
	}

	/**
	 *
	 * @Description描述:
	 * @return
	 * @return Activity
	 */
	public Activity currentActivity() {
		if (activityStack.isEmpty()) {
			return null;
		}
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 *
	 * @Description描述:
	 * @return
	 * @return Activity
	 */
	public Activity lastActivity() {

		if (activityStack.size() < 1) {
			return null;
		}
		return activityStack.get(activityStack.size() - 1);
	}

	/**
	 *
	 * @Description描述: *结束当前Activity（堆栈中最后一个压入的）
	 * @return void
	 */

	public void finishActivity() {
		if (activityStack.empty()) {
			return;
		}
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 从栈中移除指定的Activity
	 *
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
		}
	}

	/**
	 * 从栈中移除指定的Activity
	 */
	public void removeActivity(Class<?> cls) {
		try {
			for (Activity activity : activityStack) {
				if (activity.getClass().equals(cls)) {
					finishActivity(activity);
				}
			}
		} catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
		}

	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			if (activityStack.remove(activity)) {
				activity.finish();
			}
		}

	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		try {
			for (Activity activity : activityStack) {
				if (activity.getClass().equals(cls)) {
					finishActivity(activity);
				}
			}
		} catch (Exception e) {
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
		}

	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		ArrayList<Activity> activityList = new ArrayList<Activity>(
				activityStack);
		for (int i = 0, size = activityList.size(); i < size; i++) {
			if (activityList.get(i) != null) {
				activityList.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 结束所有Activity保留主界面
	 */
	public void finishAllActivityExcludeMain() {
		int stackSize = activityStack.size();
		if (stackSize >= 1) {
			ArrayList<Activity> activityList = new ArrayList<Activity>(
					activityStack.subList(1, stackSize));
			for (int i = 0, size = activityList.size(); i < size; i++) {
				Activity activity = activityList.get(i);
				if (activity != null) {
					activity.finish();
					activityStack.remove(activity);
				}
			}
		}
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit() {
		try {
			finishAllActivity();

			int id = android.os.Process.myPid();
			if (id != 0) {
				android.os.Process.killProcess(id);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 退出应用程序并停止服务
	 */
	public void AppExit(Context context, Intent intent) {

		try {

			context.stopService(intent);
			finishAllActivity();

		} catch (Exception e) {

		}

	}
}