package preGuide;

import android.view.View;
import android.widget.TextView;

import com.huanghaibin.rqm.R;

import base.BaseActivity2;
import butterknife.BindView;
import utils.GoPageUtil;

public class GuideActivity extends BaseActivity2 {

    @BindView(R.id.click)
    TextView click;
    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }
    @butterknife.OnClick({R.id.click  })
    void OnClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.click:

                GoPageUtil.jumpTobyUrlLink(GuideActivity.this,"www.baidu.com");
                break;

            default:
                break;

        }
    }

}
