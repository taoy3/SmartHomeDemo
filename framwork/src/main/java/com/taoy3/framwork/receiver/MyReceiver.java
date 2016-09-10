package com.taoy3.framwork.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        if(intent.getAction().equals("Abc"))
        Toast.makeText(context,"接受到了",Toast.LENGTH_SHORT).show();
    }
}
