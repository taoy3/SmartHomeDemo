package com.taoy3.framwork;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.taoy3.framwork.receiver.MyReceiver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter("Abc");
        registerReceiver(new MyReceiver(),filter);
        Intent intent = new Intent("Abc");

        sendBroadcast(intent);
    }
}
