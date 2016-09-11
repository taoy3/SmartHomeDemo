package com.taoy3.framwork.downloaderstopsart;

/**
 * Created by taoy3 on 16/9/10.
 */
public class DownloadInfo {
    private int threadId;// 下载器id
    private int startPos;// 开始点
    private int endPos;// 结束点
    private int compeleteSize;// 完成度
    private String url;// 下载器网络标识
    private int state = INIT;
    public static final int INIT = 1;//定义三种下载的状态：初始化状态，正在下载状态，暂停状态
    public static final int DOWNLOADING = 2;
    public static final int PAUSE = 3;
    public DownloadInfo(int threadId, int startPos, int endPos,
                        int compeleteSize, String url) {
        this.threadId = threadId;
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.url = url;
    }

    public void setState(int state){
        this.state = state;
    }
    public int getState(){
        return state;
    }
    public DownloadInfo() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public int getCompeleteSize() {
        return compeleteSize;
    }

    public void setCompeleteSize(int compeleteSize) {
        this.compeleteSize = compeleteSize;
    }

    @Override
    public String toString() {
        return "DownloadInfo [threadId=" + threadId + ", startPos=" + startPos
                + ", endPos=" + endPos + ", compeleteSize=" + compeleteSize
                + "]";
    }
}
