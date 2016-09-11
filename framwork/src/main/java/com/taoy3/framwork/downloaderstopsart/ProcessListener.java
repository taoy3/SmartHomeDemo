package com.taoy3.framwork.downloaderstopsart;

/**
 * Created by taoy3 on 16/9/11.
 */
public interface ProcessListener {
    void onProcess(int hasLength,int contentLength,String url);
}
