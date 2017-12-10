package utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.rqm.rqm.R;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;


public class MMKUpdateService extends Service {

    private static final String TAG = "DownloadService";

    /**
     * 定义通知管理者对象
     */
    private NotificationManager manager;

    /**
     * 定义通知对象
     */
    private Notification notification;

    /**
     * 通知的id
     */
    private int notificationId = 1001;

    /**
     * 下载成功
     */
    private final int SUCCESS = 1002;

    /**
     * 下载失败
     */
    private final int FAILURE = 1003;


    /**
     * 定义意图对象
     */
    private PendingIntent pendingIntent;

    /**
     * 通知栏默认显示的View
     */
    private RemoteViews contentView;

    /**
     * 下载资源保存的路径
     */
    private String save_path;

    /**
     * 下载名称
     */
    private String down_name;
    /**
     * 下载路径
     */
    private String down_url;

    /**
     * APP的名字
     */
    private String app_name;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        down_name = intent.getStringExtra("down_name");
        save_path = initfile(down_name);
        down_url = intent.getStringExtra("down_url");
        app_name = intent.getStringExtra("app_name");

        // 创建通知
        createNotification();
        // 线程下载
        createThread();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 创建通知的方法
     */
    private void createNotification() {

        // 实例化通知管理者
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //加载自定义布局
        contentView = new RemoteViews(getPackageName(), R.layout.notification_miaomiaoke_update);
        contentView.setTextViewText(R.id.download_states, "正在下载");
        contentView.setTextViewText(R.id.percent, "0.0%");
        contentView.setProgressBar(R.id.progress_num, 100, 0, false);

        notification = new Notification.Builder(MMKUpdateService.this)
                .setTicker("RQM文件更新！")
                .setAutoCancel(true)
                .setContentTitle("RQM")
                .setContentIntent(pendingIntent)
                .setContentText("下载......")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(contentView)
                .build();
        manager.notify(notificationId, notification);
    }

    /**
     * 创建线程下载资源的方法
     */
    @SuppressLint("HandlerLeak")
    private void createThread() {
        /**
         * 自定义handler更新UI和处理下载下来的资源
         */
        final Handler mHandler = new Handler() {

            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                switch (msg.what) {
                    case SUCCESS: //文件下载成功
                        //下载完成后点击通知栏进行安装
                        File file = new File(save_path);
                        Uri uri = Uri.fromFile(file);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        pendingIntent = PendingIntent.getActivity(MMKUpdateService.this, 0, intent, 0);
                        notification = new Notification.Builder(MMKUpdateService.this)
                                .setTicker("RQM更新文件下载中。。。，请注意查收！")
                                .setAutoCancel(true)
                                .setContentTitle("RQM")
                                .setContentIntent(pendingIntent)
                                .setContentText("下载成功，点击安装")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .build();
                        manager.notify(notificationId, notification);

                        InstallApkMsg installApkMsg = new InstallApkMsg();
                        installApkMsg.setPath(save_path);
                        EventBus.getDefault().post(installApkMsg);
                        //关闭 服务
                        stopSelf();
                        break;
                    case FAILURE: //文件下载失败
                        notification = new Notification.Builder(MMKUpdateService.this)
                                .setTicker("RQM更新文件下载中。。。，请注意查收！")
                                .setAutoCancel(true)
                                .setContentTitle("RQM")
                                .setContentIntent(pendingIntent)
                                .setContentText("下载失败")
                                .setSmallIcon(R.mipmap.icon_launcher)
                                .build();
                        manager.notify(notificationId, notification);
                        //关闭 服务
                        stopSelf();
                        break;
                    default:
                        //关闭 服务
                        stopSelf();
                        break;
                }
            }
        };

        //下载文件
        final Message message = new Message();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    long downloadSize = downloadUpdateFile(down_url, save_path);
                    if (downloadSize > 0) {
                        message.what = SUCCESS;
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    if (Logger.B_LOG_OPEN) {
                        e.printStackTrace();
                    }
                    message.what = FAILURE;
                    mHandler.sendMessage(message);
                }

            }
        }).start();
    }


    /**
     * 初始化被下载的文件保存的路径
     */
    private String initfile(String fileName) {

//		Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//		intent.setData(Uri.parse("package:" + getPackageName()));
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(intent);

//		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//				!= PackageManager.PERMISSION_GRANTED) {
//			//申请WRITE_EXTERNAL_STORAGE权限
//			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//		}

        File file = null;
        File parent = null;
        //判断内存卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 存在SD卡 //sdcard/
//			File sd = Environment.getExternalStorageDirectory();
//			if(sd.canWrite()){
            parent = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test" + File.separator);
//			}
            Log.i(TAG, "----Environment.getExternalStorageDirectory().getAbsolutePath()--->" +
                    Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "text" + File.separator);
        }


//		String tempStr =getFilePath(parent+"",fileName).getPath();
//		//如果这个文件夹不存在就自己生成这个文件夹
        if (!parent.exists()) {
            parent.mkdirs();
        }
        file = new File(parent + fileName);
        String tempStr = file.getPath();
        return tempStr;
    }

    /***
     * 下载文件
     *
     * @return
     * @throws MalformedURLException
     */
    public long downloadUpdateFile(String down_url, String save_path)
            throws Exception {
        // 提示step
        int down_step = 1;
        // 文件总大小
        double totalSize;
        // 已经下载好的大小
        double downloadCount = 0.00;
        // 已经上传的文件大小
        int updateCount = 0;
        // 输入流
        InputStream inputStream;
        // 输出流
        OutputStream outputStream = null;

        // 下载链接
        URL url = new URL(down_url);
        // 网络请求
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setConnectTimeout(2000);
        httpURLConnection.setReadTimeout(5000);
        // 获取下载文件的总size
        totalSize = httpURLConnection.getContentLength();
        contentView.setTextViewText(R.id.total, "/" + doubleFormat(totalSize / (1024 * 1024)) + "M");
        // 网络请求失败
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }

        inputStream = httpURLConnection.getInputStream();

        File bitmapFile = new File(save_path + save_path);//"cjj.apk"
        try {
            outputStream = new FileOutputStream(save_path, false);
        } catch (Exception e) {
            e.toString();
        }

        byte buffer[] = new byte[1024];
        int readsize = 0;
        while ((readsize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小
            /**
             * 每次增张5%
             */
            if (updateCount == 0
                    || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                contentView.setTextViewText(R.id.percent, updateCount + "%");
                contentView.setProgressBar(R.id.progress_num, 100, updateCount,
                        false);
                contentView.setTextViewText(R.id.download, doubleFormat(downloadCount / (1024 * 1024)) + "M");
                // show_view
                manager.notify(notificationId, notification);

            }

        }
        //判断是否下载完成
        if (totalSize > downloadCount) {
            downloadUpdateFile(down_url, save_path);
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();

        return (long) downloadCount;

    }

    /**
     * 将字节流转换成M的转换工具
     *
     * @param d
     * @return
     */
    public String doubleFormat(double d) {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(d);
    }

    public static File getFilePath(String filePath,
                                   String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (Logger.B_LOG_OPEN) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }
}


