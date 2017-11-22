package preGuide;

import android.widget.TextView;

import com.huanghaibin.rqm.R;

import net.OnDataGetListener;

import base.BaseActivity;
import base.MainActivity;
import butterknife.BindView;
import data.CheckVersionController;
import data.LoginController;
import utils.GoPageUtil;
import utils.Utils;
import utils.WeakHandler;
import widgets.SystemBarTintManager;


public class Welcome extends BaseActivity {

    protected SystemBarTintManager tintManager;
    CheckVersionController mCheckVersionController;
    @BindView(R.id.message)
    TextView message;

    @Override
    protected int getContentView() {
        return R.layout.layout_welcome;
    }

    @Override
    public void initView() {
        super.initView();
        getLoginController();
    }

    LoginController mLoginController;
    public void getLoginController() {
        if (mLoginController == null)
            mLoginController = new LoginController(this, new OnDataGetListener() {
                @Override
                public void onGetDataSuccess(String result) {
                    message.setText("成功" + result);
                }

                @Override
                public void onGetDataFailed(int responseCode, String result) {
                    message.setText("失败" + result);
                }
            });
        mLoginController.getData("DIL", "test");
    }
}
