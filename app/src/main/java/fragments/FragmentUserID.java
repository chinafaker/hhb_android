package fragments;

import android.widget.CheckBox;
import android.widget.EditText;

import com.huanghaibin.rqm.R;

import butterknife.BindView;


public class FragmentUserID extends BaseFragment {
    @BindView(R.id.userEdi)
    EditText userEdi;
    @BindView(R.id.passwordEdi)
    EditText passwordEdi;
    @BindView(R.id.checkSave)
    CheckBox checkSave;

    @Override
    public int initInflateView() {
        return R.layout.layout_fragment_userid;
    }


    @Override
    public void getMyViews() {

    }

    @Override
    public void doSomething() {

    }

}
