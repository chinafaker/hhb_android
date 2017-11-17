package net;

public interface OnDataGetListener {
	public void onGetDataSuccess(String result);

	public void onGetDataFailed(int responseCode, String result);

}
