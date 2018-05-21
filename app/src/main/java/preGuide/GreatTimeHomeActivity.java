package preGuide;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daikin.rqm.R;

import base.BaseActivity;
import butterknife.BindView;
import utils.ToastUtils;


public class GreatTimeHomeActivity extends BaseActivity {
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.rl_jinbi)
    RelativeLayout rl_jinbi;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.iv_jinnbi)
    ImageView iv_jinnbi;
    @BindView(R.id.tv_jinbi)
    TextView tv_jinbi;
    @BindView(R.id.iv_detail)
    ImageView iv_detail;
    @BindView(R.id.iv_laba)
    ImageView iv_laba;
    @BindView(R.id.iv_jingyin)
    ImageView iv_jingyin;
    @BindView(R.id.rl_music)
    RelativeLayout rl_music;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.iv_baijiele)
    ImageView iv_baijiele;
    @BindView(R.id.tv_goRoom)
    TextView tv_goRoom;
    @BindView(R.id.re_baijialei)
    RelativeLayout re_baijialei;
    private boolean isRunning;
    private MediaPlayer mp;
    private boolean isPause = false;
    private int currentPosition=1;

    @Override
    protected int getContentView() {
        return R.layout.layout_gt_home;
    }

    @Override
    public void initView() {
        super.initView();
        setStatusbarLightMode();
        mp = MediaPlayer.create(GreatTimeHomeActivity.this, R.raw.bgmusic);//创建mediaplayer对象
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                // TODO Auto-generated method stub
                mp=null;
                play();//重新开始播放
            }
        });
        play();
    }

    private void play() {
        try {
            if (mp.isPlaying() && !isPause) {
                iv_jingyin.setVisibility(View.VISIBLE);
                currentPosition = mp.getCurrentPosition();
                mp.pause();
                isPause = true;
            } else {
                iv_jingyin.setVisibility(View.GONE);
                isPause = false;
                if(mp==null){
                    mp.reset();
                    mp = MediaPlayer.create(this, R.raw.bgmusic);//重新设置要播放的音频
                }
                mp.seekTo(currentPosition);
                mp.start();//开始播放
            }
        } catch (Exception e) {
            e.printStackTrace();//输出异常信息
        }
    }

    @butterknife.OnClick({R.id.iv_icon, R.id.tv_account, R.id.rl_jinbi, R.id.rl_music, R.id.tv_goRoom})
    void OnClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_icon:
                ToastUtils.show(activity, "icon");
                break;
            case R.id.tv_account:
                ToastUtils.show(activity, "账号");
                break;
            case R.id.rl_jinbi:
                ToastUtils.show(activity, "金币");
                break;
            case R.id.rl_music:
                play();
                break;
            case R.id.tv_goRoom:
                ToastUtils.show(activity, "快速进入房间");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(mp.isPlaying()){
            mp.stop();
        }
        mp.release();//释放资源
        mp=null;
        super.onDestroy();
    }
}
