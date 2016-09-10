package com.taoy3.framwork.ActivityService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.taoy3.framwork.MainActivity;
import com.taoy3.framwork.R;

public class SystemServiceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_service);
    }

    public void onClick(View view) {
        Class<? extends AppCompatActivity> clazz = MainActivity.class;
        switch (view.getId()) {
            case R.id.alarm:
                clazz = AlarmActivity.class;
                break;
            case R.id.audio:
                clazz = AudioActivity.class;
                break;
            case R.id.connectivity:
                clazz = ConnectActivity.class;
                break;
            case R.id.notification:
                clazz = NotificationActivity.class;
                break;
            case R.id.sms:
                clazz = SmsActivity.class;
                break;
            case R.id.telephony:
                clazz = TelActivity.class;
                break;
            case R.id.vibrator:
                clazz = VibratorActivity.class;
                break;
            default:
                break;
        }
        startActivity(new Intent(this, clazz));
    }
}
