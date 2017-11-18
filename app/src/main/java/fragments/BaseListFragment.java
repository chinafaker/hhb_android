package fragments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huanghaibin.rqm.R;

import net.Consts;

import base.BaseActivity;


/**
 * 列表Fragment
 */
public abstract class BaseListFragment extends BaseFragment {
    protected boolean ifClearArray = false;
    protected static final int PAGE_NUM_START = 0;
    protected int pageNum = PAGE_NUM_START;
    protected int totalPage = 0;
    protected int currentPage = 0;
    protected int pageSize = Consts.PAGE_SIZE;

    protected boolean mLoadMoreEndGone = true;

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageView network_product_tip;

    private LinearLayoutManager layoutManager;

    @Override
    public int initInflateView() {
        return R.layout.recyclerview_normal;
    }

    @Override
    public void getMyViews() {
    }

    @Override
    public void getMyViews(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(getPullId());
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(getRefreshId());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_229EFF);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
        layoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(layoutManager);
      //  network_product_tip = (ImageView) v.findViewById(R.id.network_product_tip);
    }

    public int getScollYDistance() {
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    public abstract int getPullId();

    public abstract int getRefreshId();

    public SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            ifClearArray = true;

            pageNum = PAGE_NUM_START;
            // 获取数据
            getData(true);
        }
    };


    @Override
    public void doSomething() {
        // TODO Auto-generated method stub

    }

    protected void getData(boolean isPull) {

    }

    /**
     * 获取数据成功后的预处理
     *
     * @param adapter
     */
    protected void preProcess(BaseQuickAdapter adapter) {
//        if (network_product_tip != null) {
//            network_product_tip.setVisibility(View.GONE);
//        }
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).disProDialog();
        }
        if (ifClearArray) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
            ifClearArray = false;
        }
        if (pageNum == PAGE_NUM_START) {
            adapter.getData().clear();
        }
    }

    public void resetPageNum() {
        pageNum = PAGE_NUM_START;
    }

    public void setIfClearArry(boolean ifClearArray) {
        this.ifClearArray = ifClearArray;
    }

    /**
     * 创建没有内容时候显示的View
     *
     * @return
     */
    protected View createEmptyView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_no_content, null);
        TextView tipTv = (TextView) view.findViewById(R.id.tipTv);
        tipTv.setText(getNoContentTip());
        ImageView img = (ImageView) view.findViewById(R.id.img);
        if (getNoContentImg() == 0) {

        } else {
            img.setImageResource(getNoContentImg());
        }
        return view;
    }

    protected String getNoContentTip() {
        return "没有内容";
    }

    protected int getNoContentImg() {
        return 0;
    }
}
