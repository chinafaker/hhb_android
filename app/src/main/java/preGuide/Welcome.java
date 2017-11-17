package preGuide;

import android.widget.TextView;

import com.huanghaibin.rqm.R;

import net.OnDataGetListener;

import base.BaseActivity2;
import butterknife.BindView;
import data.CheckVersionController;
import utils.GoPageUtil;
import utils.Utils;
import utils.WeakHandler;
import widgets.SystemBarTintManager;


public class Welcome extends BaseActivity2 {

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
        getVersionInfo();
    }


    public void getVersionInfo() {
        int   current_version = Utils.getVersionCode();//版本名
        if (mCheckVersionController == null)
            mCheckVersionController = new CheckVersionController(this, new OnDataGetListener() {
                @Override
                public void onGetDataSuccess(String result) {
                    message.setText("成功"+result);

                    new WeakHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GoPageUtil.goPage(Welcome.this, GuideActivity.class);
                        }
                    },1000);
                }

                @Override
                public void onGetDataFailed(int responseCode, String result) {
                    message.setText("失败"+result);}
            });
        mCheckVersionController.getData("", current_version + "");
    }
}
