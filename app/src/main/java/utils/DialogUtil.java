package utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanghaibin.rqm.R;

import base.BaseActivity;

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
     * 充值提现   确认退出
     */
    public static void isSureExitDialog(final Context context, String[] btnName, String title, String msg, final WeakHandler handler) {
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
                    handler.sendEmptyMessage(8002);
                }
                dialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8003);
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


    public static void noticeDialog(final Context context,  final WeakHandler handler, boolean dismissFlag) {
        View layout = initDialog(context, R.layout.layout_dialog_notice, dismissFlag, false, false, false);
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


    public static void touchIDDialog(final Context context,  final WeakHandler handler, boolean dismissFlag) {
        View layout = initDialog(context, R.layout.layout_dialog_notice2, dismissFlag, false, false, false);
        Button cancelButton = (Button) layout.findViewById(R.id.cancelButton);
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


    public static void registerDialog(final Context context,  final WeakHandler handler, boolean dismissFlag) {
        View layout = initDialog(context, R.layout.layout_dialog_register, dismissFlag, false, false, false);
        Button registerBtn = (Button) layout.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.sendEmptyMessage(8001);
                }
                dialog.dismiss();
            }
        });
    }


}
