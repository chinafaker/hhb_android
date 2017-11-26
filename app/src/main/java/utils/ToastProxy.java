package utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rqm.rqm.R;


/**
 * 新版提示
 *
 * @author zhenglongchao
 *         created at 2017/1/5 下午2:48
 */
public class ToastProxy {
    private Toast toast;
    private TextView textView;

    public ToastProxy(Activity activity) {
        this(activity, Toast.LENGTH_SHORT);
    }

    public ToastProxy(Activity activity, int time) {
        View layout = LayoutInflater.from(activity)
                .inflate(R.layout.layout_toast, (ViewGroup) activity.findViewById(R.id.layout));
        textView = (TextView) layout.findViewById(R.id.textView);
        toast = new Toast(activity);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, UIUtils.dipToPx(88));
        toast.setDuration(time);
        toast.setView(layout);
    }

    /**
     * 显示Toast
     *
     * @param string 显示的字符串
     */
    public void show(String string) {
        if (!StringUtils.isEmpty(string)) {
            textView.setText(string);
            toast.show();
        }
    }

    public void show(int string) {
        textView.setText(string);
        toast.show();
    }
}
