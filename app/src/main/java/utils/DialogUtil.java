package utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daikin.rqm.R;

import java.util.Timer;
import java.util.TimerTask;

import base.BaseActivity;
import javaBean.MaintenanceInfoBean;

public class DialogUtil {
    private static Dialog dialog;
    private static Dialog dialog2;
    private static int screenWidth;
    private static int screenHeight;

    public static Dialog getDialog() {
        return dialog;
    }

    public static void dialogDismiss() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * initDialog
     *
     * @param context
     * @param layout_view
     * @return
     */

    public static View initDialog(Context context, int layout_view, boolean dismissFlag, boolean ifTop, boolean ifFillWidth, boolean ifFillHeight) {

        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(layout_view, null);
        //对话框
        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        //背景透明
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;//完全不透明1.0f;
        if (ifFillWidth) {
            lp.width = screenWidth;
        }
        if (ifFillHeight) {
            lp.height = screenHeight - BaseActivity.STATUS_BAR_HEIGHT;
        }
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (ifTop) {
            dialog.getWindow().setGravity(Gravity.TOP);
        }
        //背景不可点击
        dialog.setCanceledOnTouchOutside(dismissFlag);
        dialog.setCancelable(dismissFlag);

        return layout;
    }

    /**
     * initDialog
     *
     * @param context
     * @param layout_view
     * @return
     */

    public static View initDialog2(Context context, int layout_view, boolean dismissFlag, boolean ifTop, boolean ifFillWidth, boolean ifFillHeight) {

        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(layout_view, null);
        //对话框
        dialog2 = new AlertDialog.Builder(context).create();
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.show();
        dialog2.getWindow().setContentView(layout);
        //背景透明
        WindowManager.LayoutParams lp = dialog2.getWindow().getAttributes();
        lp.dimAmount = 0.4f;//完全不透明1.0f;
        if (ifFillWidth) {
            lp.width = screenWidth;
        }
        if (ifFillHeight) {
            lp.height = screenHeight - BaseActivity.STATUS_BAR_HEIGHT;
        }
        dialog2.getWindow().setAttributes(lp);
        dialog2.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (ifTop) {
            dialog2.getWindow().setGravity(Gravity.TOP);
        }
        //背景不可点击
        dialog2.setCanceledOnTouchOutside(dismissFlag);
        dialog2.setCancelable(dismissFlag);

        return layout;
    }

    public static View initDialog(Context context, int layout_view) {
        return initDialog(context, layout_view, false, false, false, false);
    }

    public static View initDialog(Context context, int layout_view, boolean dismissFlag) {
        return initDialog(context, layout_view, dismissFlag, false, false, false);
    }

    public static View initDialog2(Context context, int layout_view, boolean dismissFlag) {
        return initDialog2(context, layout_view, dismissFlag, false, false, false);
    }


    public static View initDialog2(Context context, int layout_view) {
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(layout_view, null);

        if (dialog != null) {
            dialog.dismiss();
            dialog.cancel();
        }
        //对话框
        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        //背景透明
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;//完全不透明1.0f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //背景不可点击
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return layout;
    }


    /**
     * 两个按钮的提示dialog
     *
     * @param context
     */

    public static void normal2Btns(final Context context, String[] btnName, String title, final Handler handler) {
        View layout = initDialog(context, R.layout.layout_dialog_with2btns);
        //立即注册
        Button leftBtn = (Button) layout.findViewById(R.id.leftBtn);
        Button rightBtn = (Button) layout.findViewById(R.id.rightBtn);
        leftBtn.setText(btnName[0]);
        rightBtn.setText(btnName[1]);
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8001);
                }
                dialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8002);
                }
                dialog.dismiss();
            }
        });


        TextView dialogTitle = (TextView) layout.findViewById(R.id.dialogTitle);
        if (StringUtils.isEmpty(title)) {
            dialogTitle.setVisibility(View.GONE);
        } else {
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
    }

    /**
     * 两个按钮的提示dialog
     *
     * @param context
     */

    public static void normal2Btns(final Context context, String[] btnName, String title, String msg, final WeakHandler handler) {
        View layout = initDialog(context, R.layout.layout_dialog_with2btns);
        //立即注册
        Button leftBtn = (Button) layout.findViewById(R.id.leftBtn);
        Button rightBtn = (Button) layout.findViewById(R.id.rightBtn);
        leftBtn.setText(btnName[0]);
        rightBtn.setText(btnName[1]);
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8001);
                }
                dialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8002);
                }
                dialog.dismiss();
            }
        });
        //内容描述
        TextView mTextView = (TextView) layout.findViewById(R.id.dialog_text);
        mTextView.setText(msg);

        TextView dialogTitle = (TextView) layout.findViewById(R.id.dialogTitle);
        if (StringUtils.isEmpty(title)) {
            dialogTitle.setVisibility(View.GONE);
        } else {
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
    }

    /**
     * isSureExitDialog
     */
    public static void isSureExitDialog(final Context context, final WeakHandler handler) {
        View layout = initDialog(context, R.layout.layout_dialog_with2btns);
        //立即注册
        Button leftBtn = (Button) layout.findViewById(R.id.leftBtn);
        Button rightBtn = (Button) layout.findViewById(R.id.rightBtn);

        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(110);
                }
                dialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(120);
                }
                dialog.dismiss();
            }
        });
    }


    /**
     * 单个按钮的提示dialog
     *
     * @param context
     * @param btnName
     * @param msg
     * @param handler
     */

    public static void normalSureBtn(final Context context, String btnName, String msg, final WeakHandler handler) {
        normalSureBtn(context, btnName, "", msg, handler, false);
    }

    public static void normalSureBtn(final Context context, String btnName, String title, String msg, final WeakHandler handler) {
        normalSureBtn(context, btnName, title, msg, handler, false);
    }

    /**
     * 单个按钮的提示dialog
     *
     * @param context
     * @param btnName
     * @param msg
     * @param handler
     * @param dismissFlag 点击其他区域是否自动消失
     */
    public static void normalSureBtn(final Context context, String btnName, String title, String msg, final WeakHandler handler, boolean dismissFlag) {

        if (null != dialog) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        View layout = initDialog(context, R.layout.layout_dialog_surebtn, dismissFlag, false, false, false);
        //立即注册
        Button sureBtn = (Button) layout.findViewById(R.id.sureBtn);
        sureBtn.setText(StringUtils.noNull(btnName));
        sureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8002);
                }
                dialog.dismiss();
            }
        });
        //内容描述
        TextView mTextView = (TextView) layout.findViewById(R.id.dialog_text);
        mTextView.setText(msg);

        TextView dialogTitle = (TextView) layout.findViewById(R.id.dialogTitle);
        if (StringUtils.isEmpty(title)) {
            dialogTitle.setVisibility(View.GONE);
        } else {
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
    }


    /**
     * 两个按钮的提示dialog   和normal2Btns一样  暂时添加
     *
     * @param context
     */

    public static void normal2BtnsTwo(final Context context, String[] btnName, String title, String msg, final WeakHandler handler) {
        View layout = initDialog(context, R.layout.layout_dialog_with2btns);
        //立即注册
        Button leftBtn = (Button) layout.findViewById(R.id.leftBtn);
        Button rightBtn = (Button) layout.findViewById(R.id.rightBtn);
        leftBtn.setText(btnName[0]);
        rightBtn.setText(btnName[1]);
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8001);
                }
                dialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8002);
                }
                dialog.dismiss();
            }
        });
        //内容描述
        TextView mTextView = (TextView) layout.findViewById(R.id.dialog_text);
        mTextView.setText(msg);

        TextView dialogTitle = (TextView) layout.findViewById(R.id.dialogTitle);
        if (StringUtils.isEmpty(title)) {
            dialogTitle.setVisibility(View.GONE);
        } else {
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
    }


    public static void noticeDialog(final Context context, final WeakHandler handler, boolean dismissFlag, MaintenanceInfoBean maintenanceInfoBean) {
        View layout = initDialog(context, R.layout.layout_dialog_notice, dismissFlag, false, false, false);
        TextView dialogTitle = (TextView) layout.findViewById(R.id.dialogTitle);
        TextView dialog_text = (TextView) layout.findViewById(R.id.dialog_text);
        TextView tv_time = (TextView) layout.findViewById(R.id.tv_time);
        TextView tv_shiqu = (TextView) layout.findViewById(R.id.tv_shiqu);
        TextView tvstart = (TextView) layout.findViewById(R.id.tvstart);
        TextView tvend = (TextView) layout.findViewById(R.id.tvend);


        dialogTitle.setText(StringUtils.noNull(maintenanceInfoBean.getMmTitle()));
        dialog_text.setText(StringUtils.noNull(maintenanceInfoBean.getMmDescription()));
        tv_time.setText("--" + StringUtils.noNull(maintenanceInfoBean.getMmSubtitle()) + "--");


        String start = StringUtils.noNull(maintenanceInfoBean.getMmStarttime());
        String start1 = start.substring(0, 18);
        String start2 = start.substring(18, start.length());
        tvstart.setText(start1);

        tv_shiqu.setText(start2);

        String endstr = StringUtils.noNull(maintenanceInfoBean.getMmEndtime());
        String endstr1 = endstr.substring(0, 18);
        String endstr2 = endstr.substring(18, endstr.length());

        tvend.setText(endstr1);

        Button sureBtn = (Button) layout.findViewById(R.id.sureBtn);
        sureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8003);
                }
                dialog.dismiss();
            }
        });
    }

    static TextView touchResult;

    public static void touchIDDialog(final Context context, final WeakHandler handler, boolean dismissFlag) {
        View layout = initDialog(context, R.layout.layout_dialog_notice2, dismissFlag, false, false, false);
        Button cancelButton = (Button) layout.findViewById(R.id.cancelButton);
        touchResult = (TextView) layout.findViewById(R.id.touchResult);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8002);
                }
                dialog.dismiss();
            }
        });
    }

    public static void touchResultSetText(String str) {
        if (null != dialog && dialog.isShowing()) {
            touchResult.setText(str);
        }
    }

    public static void touchResultSetTextClear() {
        if (null != dialog && dialog.isShowing()) {
            touchResult.setText("");
        }
    }


    public static void registerDialog(final Context context, final WeakHandler handler, boolean dismissFlag, String usetid) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        LayoutInflater inflaterDl = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_dialog_register, null);
        //对话框
        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        //背景透明
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;//完全不透明1.0f;
        if (false) {
            lp.width = screenWidth;
        }
        if (false) {
            lp.height = screenHeight - BaseActivity.STATUS_BAR_HEIGHT;
        }
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (false) {
            dialog.getWindow().setGravity(Gravity.TOP);
        }
        //背景不可点击
        dialog.setCanceledOnTouchOutside(dismissFlag);
        dialog.setCancelable(dismissFlag);


        Button registerBtn = (Button) layout.findViewById(R.id.registerBtn);
        final EditText userEdi = (EditText) layout.findViewById(R.id.userEdi);
        final EditText passwordEdi = (EditText) layout.findViewById(R.id.passwordEdi);
        userEdi.setText(usetid);
        userEdi.setSelection(userEdi.getText().toString().trim().length());
        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(userEdi.getText().toString())) {
                    ToastUtils.show(context, "Please enter userID");
                    return;
                }

                if (StringUtils.isEmpty(passwordEdi.getText().toString())) {
                    ToastUtils.show(context, "Please enter password");
                    return;
                }

                if (handler != null) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("accountID", userEdi.getText().toString());
                    bundle.putString("password", passwordEdi.getText().toString());
                    message.setData(bundle);//bundle传值，耗时，效率低
                    handler.sendMessage(message);//发送message信息
                    message.what = 8001;
                }
                imm.hideSoftInputFromWindow(layout.getWindowToken(), 0); //强制隐藏键盘
                dialog.dismiss();
            }
        });

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        userEdi.requestFocus(); //edittext是一个EditText控件
        Timer timer = new Timer(); //设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() { //弹出软键盘的代码
                imm.showSoftInput(layout, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        }, 200);

    }


    /**
     * 版本更新对话框
     *
     * @param context
     * @param
     */
    public static void versionUpdateDialog(final Context context, String update_content, String version, String forceUpdateFlg, final WeakHandler handler) {
        View layout;
        if (forceUpdateFlg.equals("1")) {
            layout = initDialog2(context, R.layout.layout_dialog_checkversion_update_new, false);
        } else {
            layout = initDialog2(context, R.layout.layout_dialog_checkversion_update_new, true);
        }

        //立即更新
        Button dialog_commit = (Button) layout.findViewById(R.id.dialog_commit);
        Button dialog_commit1 = (Button) layout.findViewById(R.id.dialog_commit1);
        LinearLayout ll_cancle = (LinearLayout) layout.findViewById(R.id.ll_cancle);
        //暂不更新
        Button dialog_cancel = (Button) layout.findViewById(R.id.dialog_cancel);

        TextView lay_text_view = (TextView) layout.findViewById(R.id.lay_text_view);
        TextView dialog_text = (TextView) layout.findViewById(R.id.dialog_text);
        TextView updateVersion = (TextView) layout.findViewById(R.id.updateVersion);
        updateVersion.setText("Updated to " + version + " version");
        if (forceUpdateFlg.equals("1")) {//强制更新
            dialog_commit.setVisibility(View.VISIBLE);
            ll_cancle.setVisibility(View.GONE);
        } else {
            dialog_commit.setVisibility(View.GONE);
            ll_cancle.setVisibility(View.VISIBLE);
        }
        lay_text_view.setText(update_content);
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                if (handler != null) {
                    handler.sendEmptyMessage(100);
                }
            }
        });
        dialog_commit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                if (handler != null) {
                    handler.sendEmptyMessage(100);
                }
            }
        });


    }




    public static void BjlLoginDialog(final Context context, final WeakHandler handler, boolean dismissFlag, String usetid) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_dialog_bjllogin, null);
        //对话框
        dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        //背景透明
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;//完全不透明1.0f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //背景不可点击
        dialog.setCanceledOnTouchOutside(dismissFlag);
        dialog.setCancelable(dismissFlag);
        final   EditText userEdi = (EditText) layout.findViewById(R.id.userEdi);
        final  EditText passwordEdi = (EditText) layout.findViewById(R.id.passwordEdi);
        TextView tv_tips = (TextView) layout.findViewById(R.id.tv_tips);
        Button registerBtn = (Button) layout.findViewById(R.id.btn_login);
        ImageView iv_close = (ImageView) layout.findViewById(R.id.iv_close);
        userEdi.setSelection(userEdi.getText().toString().trim().length());
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (handler != null) {
                    handler.sendEmptyMessage(8002);
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(userEdi.getText().toString())) {
                    ToastUtils.show(context, "请输入账号");
                    return;
                }
                if (StringUtils.isEmpty(passwordEdi.getText().toString())) {
                    ToastUtils.show(context, "请输入密码");
                    return;
                }
                if (handler != null) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("accountID", userEdi.getText().toString());
                    bundle.putString("password", passwordEdi.getText().toString());
                    message.setData(bundle);//bundle传值，耗时，效率低
                    handler.sendMessage(message);//发送message信息
                    message.what = 8001;
                }
                imm.hideSoftInputFromWindow(layout.getWindowToken(), 0); //强制隐藏键盘
                dialog.dismiss();
            }
        });
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
