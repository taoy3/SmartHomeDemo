package com.taoy3.framwork.downloaderstopsart;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {
    private static ProcessListener processListener;
    public static String URLSTR = "urlStr";
    public static String LOCAL_FILE = "localFile";
    private Dao dao;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String url = (String) msg.obj;
                int length = msg.arg1;
                int content = msg.arg2;
                processListener.onProcess(length, content, url);
            }
        }
    };
    private Map<String, DownloadInfo> downloadInfoMap = new HashMap<>();

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = Dao.getInastance(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlstr = intent.getStringExtra(URLSTR);
        DownloadInfo downloadInfo = downloadInfoMap.get(urlstr);
        if (downloadInfo == null) {
            if (dao.isHasInfors(urlstr)) {
                List<DownloadInfo> infos = dao.getInfos(urlstr);
                if (infos.size() > 0 && infos.get(0) != null) {
                    downloadInfo = infos.get(0);
                }
            }
            if (downloadInfo == null) {
                downloadInfo = new DownloadInfo();
                downloadInfo.setUrl(urlstr);
            }
        }
        int state = downloadInfo.getState();
        if (state == DownloadInfo.DOWNLOADING) {
            return;
        }
        long hasLength = downloadInfo.getCompeleteSize();
        String localfile = intent.getStringExtra(LOCAL_FILE);
        HttpUtils.downFile(urlstr, mHandler, state, hasLength, localfile
                , dao);
        downloadInfo.setThreadId(1);
        downloadInfo.setState(DownloadInfo.DOWNLOADING);
        dao.saveInfo(downloadInfo);
    }

    public static void setUpdateUI(ProcessListener processListener) {
        DownloadService.processListener = processListener;
    }
}
