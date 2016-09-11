package com.taoy3.framwork.downloaderstopsart;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by taoy3 on 16/9/10.
 */
public class HttpUtils {
private static Map<String,HttpURLConnection> connectionMap = new HashMap<>();
    private static String TAG = "HttpUtils";

    public static void downFile(int threadId, int startPos, int endPos, int compeleteSize,
                                String urlstr, Dao dao, String localfile, Handler mHandler, int state) {
        HttpURLConnection connection = null;
        RandomAccessFile randomAccessFile = null;
        InputStream is = null;
        try {

            URL url = new URL(urlstr);
            connection = (HttpURLConnection) url.openConnection();
            connectionMap.put(urlstr,connection);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            // 设置范围，格式为Range：bytes x-y;
            connection.setRequestProperty("Range", "bytes=" + (startPos + compeleteSize) + "-" + endPos);

            randomAccessFile = new RandomAccessFile(localfile, "rwd");
            randomAccessFile.seek(startPos + compeleteSize);
            Log.i("RG", "connection--->>>" + connection);
            // 将要下载的文件写到保存在保存路径下的文件中
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "respinseCode=" + connection.getResponseCode());
                return;
            }
            is = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, length);
                compeleteSize += length;
                // 更新数据库中的下载信息
                dao.updataInfos(threadId, compeleteSize, urlstr);
                // 用消息将下载信息传给进度条，对进度条进行更新
                Message message = Message.obtain();
                message.what = 1;
                message.obj = urlstr;
                message.arg1 = length;
                mHandler.sendMessage(message);
                if (state == Downloader.PAUSE) {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                randomAccessFile.close();
                connection.disconnect();
                dao.closeDb();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void downFile(String urlstr, Handler mHandler, int state, long hasLength, String localfile
            , Dao dao) {
        HttpURLConnection connection = null;
        RandomAccessFile randomAccessFile = null;
        InputStream is = null;
        try {
            URL url = new URL(urlstr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            long fileSize = connection.getContentLength();
            // 设置范围，格式为Range：bytes x-y;
//            connection.setRequestProperty("Range", "bytes="+(startPos + compeleteSize) + "-" + endPos);
            File file = new File(localfile);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            randomAccessFile = new RandomAccessFile(localfile, "rwd");
            randomAccessFile.seek(hasLength);
            Log.i("RG", "connection--->>>" + connection);
            // 将要下载的文件写到保存在保存路径下的文件中
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "respinseCode=" + connection.getResponseCode());
                return;
            }
            is = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, length);
                hasLength += length;
                // 更新数据库中的下载信息
                dao.updataInfos(1, (int) hasLength, urlstr);
                // 用消息将下载信息传给进度条，对进度条进行更新
                Message message = Message.obtain();
                message.what = 1;
                message.obj = urlstr;
                message.arg1 = (int) hasLength;
                message.arg2 = (int) fileSize;
                mHandler.sendMessage(message);
                if (state == Downloader.PAUSE) {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                randomAccessFile.close();
                connection.disconnect();
                dao.closeDb();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 取消当前HTTP连接处理
     */
    public static void cancelHttpRequest(String url) {
        HttpURLConnection conn = connectionMap.get(url);
        if (conn != null) {
            conn.disconnect();
            conn = null;
            System.gc();
        }
    }
}
