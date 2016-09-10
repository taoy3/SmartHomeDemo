package com.taoy3.framwork.ActivityService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.taoy3.framwork.R;

public class NotificationActivity extends AppCompatActivity {

    private NotificationManager nm;
    //用这个变量来唯一的标定一个Notification对象
    final static int NOTIFY = 0x123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        nm = (NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE) ;
    }


    public void send(View view){
        Intent intent = new Intent(NotificationActivity.this , SystemServiceActivity.class);
        PendingIntent pi = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);

        Notification notify = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            notify = new Notification.Builder(this)
                    //设置打开通知后， 该标题栏通知自动消失
                    .setAutoCancel(true)
                    //设置显示在状态栏中的通知提示信息
                    .setTicker("有新消息")
                    //设置通知的图标，注意图片不能够太大，否则不能够正常的显示
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //设置通知内容的标题
                    .setContentTitle("一条新消息")
                    //设置通知内容
                    .setContentText("阿奇从远方发来贺电")
                    //设置使用系统默认的声音、默认LED灯
                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
                    //设置通知的自定义声音
                    .setSound(Uri.parse("android.resource://com.taoy3.framwork/"+R.raw.alarm))
                    //设置消息中显示的发送时间
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)   //设置是否setWhe指定的显示时间
                    //设置点击通知将要启动程序的Intent
                    .setContentIntent(pi)
                    //返回Notification对象
                    .build();
        }

        //发送通知
        nm.notify(NOTIFY , notify);

    }

    public void del(View view){
        nm.cancel(NOTIFY);
    }
}
