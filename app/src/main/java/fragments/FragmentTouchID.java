package fragments;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanghaibin.rqm.R;

import net.Consts;

import butterknife.BindView;
import utils.DialogUtil;

/**
 * Created by hhb on 2017/11/19.
 */

public class FragmentTouchID extends BaseFragment {

    @BindView(R.id.rluserid)
    RelativeLayout rluserid;

    @BindView(R.id.textUserName)
    TextView textUserName;

    @Override
    public int initInflateView() {
        return R.layout.layout_fragment_touchid;
    }


    @Override
    public void getMyViews() {

        if (sharedPrefUtil.getSharedBoolean(Consts.ISREGISTERUSERIDTIOUC, false)) {
            if (sharedPrefUtil.getSharedStr(Consts.USETID, "") != null && sharedPrefUtil.getSharedStr(Consts.USETID, "").length() > 0) {
                rluserid.setVisibility(View.VISIBLE);
            } else {
                rluserid.setVisibility(View.GONE);
            }
        } else {
            rluserid.setVisibility(View.GONE);
        }

        textUserName.setText(sharedPrefUtil.getSharedStr(Consts.USETID, ""));
    }

    @Override
    public void onResume() {
        getMyViews();
        super.onResume();
    }

    @Override
    public void doSomething() {

    }

}
