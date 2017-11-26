package preGuide;

import android.view.View;
import android.widget.TextView;

import com.rqm.rqm.R;

import base.BaseActivity;
import butterknife.BindView;
import utils.GoPageUtil;

public class GuideActivity extends BaseActivity {

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
