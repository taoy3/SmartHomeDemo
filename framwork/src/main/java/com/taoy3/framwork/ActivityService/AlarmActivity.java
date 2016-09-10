package com.taoy3.framwork.ActivityService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.taoy3.framwork.R;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    private Button setTime;
    private AlarmManager aManager;
    private MediaPlayer alarmMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        // 获取程序界面的按钮
        setTime = (Button) findViewById(R.id.setTime);
        // 获取AlarmManager对象
        aManager = (AlarmManager) getSystemService(
                Service.ALARM_SERVICE);
        // 为“设置闹铃”按钮绑定监听器。
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Calendar currentTime = Calendar.getInstance();
                // 创建一个TimePickerDialog实例，并把它显示出来。
                new TimePickerDialog(AlarmActivity.this, 0, // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp,
                                                  int hourOfDay, int minute) {
                                // 指定启动AlarmActivity组件
                                Intent intent = new Intent(AlarmActivity.this,
                                        AlarmActivity.class);
                                // 创建PendingIntent对象
                                PendingIntent pi = PendingIntent.getActivity(
                                        AlarmActivity.this, 0, intent, 0);
                                Calendar c = Calendar.getInstance();
                                // 根据用户选择时间来设置Calendar对象
                                c.set(Calendar.HOUR, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                // 设置AlarmManager将在Calendar对应的时间启动指定组件
                                aManager.set(AlarmManager.RTC_WAKEUP,
                                        c.getTimeInMillis(), pi);
                                // 显示闹铃设置成功的提示信息
                                Toast.makeText(AlarmActivity.this, "闹铃设置成功啦"
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime
                        .get(Calendar.MINUTE), false).show();
            }
        });
        setAlarmMusic();
    }

    public void setAlarmMusic() {
        // 加载指定音乐，并为之创建MediaPlayer对象
        alarmMusic = MediaPlayer.create(this, R.raw.alarm);
        alarmMusic.setLooping(true);
        // 播放音乐
        alarmMusic.start();
        // 创建一个对话框
        new AlertDialog.Builder(AlarmActivity.this)
                .setTitle("闹钟")
                .setMessage("闹钟响了,Go！Go！Go！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 停止音乐
                        alarmMusic.stop();
                        // 结束该Activity
//                        AlarmActivity.this.finish();
                    }
                }).show();
    }
}