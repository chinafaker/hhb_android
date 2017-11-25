package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanghaibin.rqm.R;

import base.BaseActivity;
import javaBean.MaintenanceInfoBean;

public class DialogUtil {
    private static Dialog dialog;
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


        //=====================

        return layout;
    }

    public static View initDialog(Context context, int layout_view) {
        return initDialog(context, layout_view, false, false, false, false);
    }

    public static View initDialog(Context context, int layout_view, boolean dismissFlag) {
        return initDialog(context, layout_view, dismissFlag, false, false, false);
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

    public static void normal2Btns(final Context context, String[] btnName, String title, String msg, final Handler handler) {
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
        TextView tvstart = (TextView) layout.findViewById(R.id.tvstart);
        TextView tvend = (TextView) layout.findViewById(R.id.tvend);


        dialogTitle.setText(StringUtils.noNull(maintenanceInfoBean.getMmTitle()));
        dialog_text.setText(StringUtils.noNull(maintenanceInfoBean.getMmDescription()));
        tv_time.setText("--" + StringUtils.noNull(maintenanceInfoBean.getMmSubtitle()) + "--");
        tvstart.setText(StringUtils.noNull(maintenanceInfoBean.getMmStarttime()));
        tvend.setText(StringUtils.noNull(maintenanceInfoBean.getMmEndtime()));

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
        View layout = initDialog(context, R.layout.layout_dialog_register, dismissFlag, false, false, false);
        Button registerBtn = (Button) layout.findViewById(R.id.registerBtn);
        final EditText userEdi = (EditText) layout.findViewById(R.id.userEdi);
        final EditText passwordEdi = (EditText) layout.findViewById(R.id.passwordEdi);
//输入框是密码风格的
        //  userEdi.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        userEdi.setText(usetid);
        userEdi.setSelection(userEdi.getText().toString().trim().length());
        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(userEdi.getText().toString())) {
                    ToastUtils.show(context, "Please enter your account");
                    return;
                }

                if (StringUtils.isEmpty(passwordEdi.getText().toString())) {
                    ToastUtils.show(context, "Please enter your password");
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
                dialog.dismiss();
            }
        });
    }


}
