package com.taoy3.framwork.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(),"MyService onBind",Toast.LENGTH_SHORT).show();
        return new Binder();
    }
}
