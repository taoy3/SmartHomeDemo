package com.taoy3.framwork.ActivityService;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taoy3.framwork.R;

public class SmsActivity extends AppCompatActivity {

    private SmsManager sManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Button send = (Button)findViewById(R.id.sendButton);
        final EditText number =(EditText)findViewById(R.id.number);
        final EditText content =(EditText)findViewById(R.id.content);

        //获取SmsManager
        sManager = SmsManager.getDefault();

        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                PendingIntent pi = PendingIntent.getActivity(SmsActivity.this,
                        0, new Intent(), 0);
                //发送信息
                sManager.sendTextMessage(number.getText().toString(),
                        null, content.getText().toString(), pi, null);
                Toast.makeText(SmsActivity.this, "消息成功送达", Toast.LENGTH_LONG).show();
            }
        });
    }
}
