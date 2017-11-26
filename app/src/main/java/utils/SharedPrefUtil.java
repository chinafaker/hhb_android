package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class SharedPrefUtil {

	private SharedPreferences sp;
	private Context mcontext;
	public static final String ONBACKGROUND = "onbackground";
	public SharedPrefUtil(Context context, String fileName) {
		sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		mcontext = context;
	}

	public String getSharedStr(String key) {
		return sp.getString(key, null);
	}

	public String getSharedStr(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	public int getSharedInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public void setSharedInt(String key, int value) {
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public boolean getSharedBoolean(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public void setSharedBoolean(String key, boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void setSharedStr(String key, String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 针对复杂类型存储<对象>
	 * 
	 * @param key
	 */
	public void setObject(String key, Object object) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {

			out = new ObjectOutputStream(baos);
			out.writeObject(object);
			String objectVal = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			Editor editor = sp.edit();
			editor.putString(key, objectVal);
			editor.commit();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
