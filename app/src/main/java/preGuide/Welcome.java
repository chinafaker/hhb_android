package preGuide;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daikin.rqm.R;

import net.Consts;

import java.util.Random;
import java.util.logging.Handler;

import base.BaseActivity;
import butterknife.BindView;
import data.CheckVersionController;
import utils.DialogUtil;
import utils.GoPageUtil;
import utils.ToastUtils;
import utils.WeakHandler;
import widgets.SystemBarTintManager;


public class Welcome extends BaseActivity {

    protected SystemBarTintManager tintManager;
    CheckVersionController mCheckVersionController;
    @BindView(R.id.iv_loading)
    ImageView iv_loading;

    @BindView(R.id.tv_loading)
    TextView tv_loading;

    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    private int start;
    private Random random;

    @Override
    protected int getContentView() {
        return R.layout.layout_welcome;
    }

    @Override
    public void initView() {
        super.initView();
        setStatusbarLightMode();
        new myThread().start();
    }


    WeakHandler weakhandler = new WeakHandler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 8001:
                    String accountID = msg.getData().getString("accountID");
                    String password = msg.getData().getString("password");
                    GoPageUtil.goPage(Welcome.this, GreatTimeHomeActivity.class);
                    break;
                case 8002:
                    ToastUtils.show(Welcome.this, "close");
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    class myThread extends Thread {
        @Override
        public void run() {
            try {
                random = new Random();
                start = random.nextInt(30);
                while (start < 100) {
                    Thread.sleep(1000);
                    int num = random.nextInt(40) % (11) + 30;//random.nextInt(max)%(max-min+1) + min;
                    start = start + num;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //此时已在主线程中，可以更新UI了
                            if (start > 100) {
                                start = 100;
                            }
                            tv_loading.setText(start + "%");
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_bottom.setVisibility(View.GONE);
                        DialogUtil.BjlLoginDialog(activity, weakhandler, false, "");

                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
