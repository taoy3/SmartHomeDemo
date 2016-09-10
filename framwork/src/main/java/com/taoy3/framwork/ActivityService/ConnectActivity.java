package com.taoy3.framwork.ActivityService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.taoy3.framwork.R;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        //通过getSystemService()方法得到connectionManager这个系统服务类，专门用于管理网络连接
        ConnectivityManager connectionManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //用于返回网络信息对象
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        //判定当前的网络是否可用,即网络是否处于连接状态
        if(networkInfo != null && networkInfo.isAvailable()){
            Toast.makeText(ConnectActivity.this, "network is available",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ConnectActivity.this, "network is unavailable",Toast.LENGTH_SHORT).show();
        }
    }
}
