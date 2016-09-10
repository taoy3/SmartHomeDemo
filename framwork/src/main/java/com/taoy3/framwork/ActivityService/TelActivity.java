package com.taoy3.framwork.ActivityService;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.taoy3.framwork.R;

public class TelActivity extends AppCompatActivity {

    private TelephonyManager tManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel);
        final TextView et = (TextView) findViewById(R.id.et);

        // 取得TelephonyManager对象
        tManager = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        // 创建一个通话状态监听器
        PhoneStateListener listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String number) {
                switch (state) {
                    // 无任何状态
                    case TelephonyManager.CALL_STATE_IDLE:
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    // 来电铃响时
                    case TelephonyManager.CALL_STATE_RINGING:
//                        OutputStream os = null;
//                        try {
//                            os = openFileOutput("phoneList", MODE_APPEND);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        PrintStream ps = new PrintStream(os);
//                        // 将来电号码记录到文件中
//                        ps.println(new Date() + " 来电：" + number);
//                        ps.close();
                        Toast.makeText(TelActivity.this,number,Toast.LENGTH_LONG).show();
                        et.append(number);
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, number);
            }
        };
        // 监听电话通话状态的改变
        tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }
}
