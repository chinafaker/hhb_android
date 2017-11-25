package fragments;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import net.Consts;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import utils.SharedPrefUtil;
import utils.UIUtils;

public abstract class BaseFragment extends Fragment {
    public static final int SHOW_DIALOG = 0;
    public static final int CLOSE_DIALOG = 1;

    protected AppCompatActivity activity;
    protected View v;
    protected int initWidth, initHeight;
    Unbinder unbinder;
    private ProgressDialog dialog;
    private String message = "加载中...";
    public SharedPrefUtil sharedPrefUtil;
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPrefUtil = new SharedPrefUtil(activity, Consts.SHAREDPREFENCERQM);
        UIUtils.initDisplayMetrics(getActivity(), getActivity().getWindowManager(), false);
        initWidth = UIUtils.getWidth();
        initHeight = UIUtils.getHeight();

        v = inflater.inflate(initInflateView(), null);
        unbinder = ButterKnife.bind(this, v);
        getMyViews(v);
        return v;
    }

    protected void getMyViews(View v) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog();
        getMyViews();
        doSomething();
    }

    public abstract int initInflateView();

    public abstract void getMyViews();

    public abstract void doSomething();

    /**
     * @return void 返回类型
     * @Title: initDialog
     * @Description: 初始化进度条对话框
     * @author vincent
     */
    private void initDialog() {
        dialog = new ProgressDialog(activity);
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOwnerActivity(activity);
        // dialog.setMessage(getString(R.string.net_loading));
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                onProdialogCancel();
            }
        });
    }

    public void onProdialogCancel() {
    }

    /**
     * google bug: http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
     */
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // 用于显示和关闭对话框
    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SHOW_DIALOG:
                    if (dialog != null && !dialog.isShowing() && !activity.isFinishing()) {
                        if (message != null && !message.equals("")) {
                            dialog.setMessage(message);
                        }

                        if (!checkActivityExist()) return;

                        if (!dialog.isShowing())
                            dialog.show();
                    }

                    break;

                case CLOSE_DIALOG:
                    if (dialog != null && dialog.isShowing() && checkActivityExist())
                        dialog.dismiss();
                    break;
            }
        }
    };

    private boolean checkActivityExist() {
        final Activity activity = dialog.getOwnerActivity();
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return false;
        }
        return true;
    }

    public void showProDialog() {
        dialog.setCancelable(false);
        dialog.setMessage(message);
        handler.sendEmptyMessage(SHOW_DIALOG);
    }

    public void showProDialogCancel() {
        dialog.setCancelable(true);
        dialog.setMessage(message);
        handler.sendEmptyMessage(SHOW_DIALOG);
    }

    public void disProDialog() {
        handler.sendEmptyMessage(CLOSE_DIALOG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
