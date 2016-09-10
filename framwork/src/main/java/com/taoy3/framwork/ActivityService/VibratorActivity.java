package com.taoy3.framwork.ActivityService;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.taoy3.framwork.R;

public class VibratorActivity extends AppCompatActivity {

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrator);
        vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);

    }

    //重写onTouchEvent方法
    @Override
    public boolean onTouchEvent(MotionEvent event){
        Toast.makeText(this, "震动来了", Toast.LENGTH_LONG).show();
        vibrator.vibrate(2000);
        return super.onTouchEvent(event) ;
    }
}
