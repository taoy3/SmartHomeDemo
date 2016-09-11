package com.taoy3.framwork.downloaderstopsart;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy3 on 16/9/10.
 */
public class Downloader {
    private String urlstr;// 下载的地址
    private String localfile;// 保存路径
    private int threadcount;// 线程数
    private Handler mHandler;// 消息处理器
    private Dao dao;// 工具类
    private int fileSize;// 所要下载的文件的大小
    private List<DownloadInfo> infos;// 存放下载信息类的集合
    private static final int INIT = 1;//定义三种下载的状态：初始化状态，正在下载状态，暂停状态
    private static final int DOWNLOADING = 2;
    private int state = INIT;
    public static final int PAUSE = 3;
    public Downloader(String urlstr, String localfile, int threadcount,
                      Context context, Handler mHandler) {
        this.urlstr = urlstr;
        this.localfile = localfile;
        this.threadcount = threadcount;
        this.mHandler = mHandler;
        dao = Dao.getInastance(context);
    }
    /**
     *判断是否正在下载
     */
    public boolean isdownloading() {
        return state == DOWNLOADING;
    }
    /**
     * 得到downloader里的信息
     * 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
     * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
     */
    public LoadInfo getDownloaderInfors() {
        if (isFirst(urlstr)) {
            Log.v("TAG", "isFirst");
            init();
            int range = fileSize / threadcount;
            infos = new ArrayList();
            for (int i = 0; i < threadcount - 1; i++) {
                DownloadInfo info = new DownloadInfo(i, i * range, (i + 1)* range - 1, 0, urlstr);
                infos.add(info);
            }
            DownloadInfo info = new DownloadInfo(threadcount - 1,(threadcount - 1) * range, fileSize - 1, 0, urlstr);
            infos.add(info);
            //保存infos中的数据到数据库
            dao.saveInfos(infos);
            //创建一个LoadInfo对象记载下载器的具体信息
            LoadInfo loadInfo = new LoadInfo(fileSize, 0, urlstr);
            return loadInfo;
        } else {
            //得到数据库中已有的urlstr的下载器的具体信息
            infos = dao.getInfos(urlstr);
            Log.v("TAG", "not isFirst size=" + infos.size());
            int size = 0;
            int compeleteSize = 0;
            for (DownloadInfo info : infos) {
                compeleteSize += info.getCompeleteSize();
                size += info.getEndPos() - info.getStartPos() + 1;
            }
            return new LoadInfo(size, compeleteSize, urlstr);
        }
    }

    /**
     * 初始化
     */
    private void init() {
        try {
            URL url = new URL(urlstr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            fileSize = connection.getContentLength();

            File file = new File(localfile);
            if (file.exists()) {
                file.delete();
            }else if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            // 本地访问文件
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(fileSize);
            accessFile.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是第一次 下载
     */
    private boolean isFirst(String urlstr) {
        return dao.isHasInfors(urlstr);
    }

    /**
     * 利用线程开始下载数据
     */
    public void download() {
        if (infos != null) {
            if (state == DOWNLOADING)
                return;
            state = DOWNLOADING;
            for (DownloadInfo info : infos) {
                new MyThread(info.getThreadId(), info.getStartPos(),
                        info.getEndPos(), info.getCompeleteSize(),
                        info.getUrl()).start();
            }
        }
    }

    public class MyThread extends Thread {
        private int threadId;
        private int startPos;
        private int endPos;
        private int compeleteSize;
        private String urlstr;

        public MyThread(int threadId, int startPos, int endPos,
                        int compeleteSize, String urlstr) {
            this.threadId = threadId;
            this.startPos = startPos;
            this.endPos = endPos;
            this.compeleteSize = compeleteSize;
            this.urlstr = urlstr;
        }
        @Override
        public void run() {
           HttpUtils.downFile(threadId,startPos,endPos,compeleteSize,urlstr, dao, localfile,  mHandler, state);

        }
    }
    //删除数据库中urlstr对应的下载器信息
    public void delete(String urlstr) {
        dao.delete(urlstr);
    }
    //设置暂停
    public void pause() {
        state = PAUSE;
    }
    //重置下载状态
    public void reset() {
        state = INIT;
    }
}
