/*
 *@author Dawin,2015-3-5
 *
 *
 *
 */
package com.connection.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.connection.R;
import com.connection.constant.Constant;
import com.connection.utils.LogUtil;
import com.connection.wifi.WifiAdmin;
import com.connection.wifi.WifiApAdmin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*测试需要两部安卓手机A,B。A手机创建WIFI热点作为服务器，B手机连接A手机WIFI热点，作为客户端。*/
//A手机服务器    接收数据步骤:1点击创建Wifi热点2点击"turn_on_receiver"接收数据
//B手机客户端    发送数据步骤：1点击连接Wifi2点击"turn_on_send"发送数据
public class QuickTransferActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private TextView tvSend;
    private TextView tvReceiver;
    private Button btCreate;
    private WifiAdmin mWifiAdmin;
    private WifiApAdmin wifiAp;
    private final static String TAG = "WifiConnection";
    private ListView listView;
    private WifiInfoAdapter wifiInfoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tvSend = (EditText) this.findViewById(R.id.send_content);
        tvReceiver = (TextView) this.findViewById(R.id.receiver_content);
        tvReceiver.append("很好");
        btCreate = (Button) findViewById(R.id.create_hot);
        // 创建WIFI热点
        btCreate.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.wifi_list);
        mWifiAdmin = new WifiAdmin(QuickTransferActivity.this) {

            @Override
            public void myUnregisterReceiver(BroadcastReceiver receiver) {
                unregisterReceiver(receiver);
                Log.i(TAG, "WifiAdmin-myUnregisterReceiver");
            }

            @Override
            public Intent myRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter) {
                registerReceiver(receiver, filter);
                Log.i(TAG, "WifiAdmin-myRegisterReceiver");
                return null;
            }

            @Override
            public void onNotifyWifiConnected() {
            }

            @Override
            public void onNotifyWifiConnectFailed() {
            }
        };

        mWifiAdmin.startScan();
        wifiInfoAdapter = new WifiInfoAdapter(this, R.layout.item_wifi, mWifiAdmin.getWifiList());
        listView.setAdapter(wifiInfoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult result = (ScanResult) (parent.getAdapter()).getItem(position);
                showDialog(result);
            }
        });
    }

    private Dialog dialog;
    private EditText etPwd;

    private void showDialog(final ScanResult result) {
        if (null == dialog) {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_connect_wifi);
            etPwd = (EditText) dialog.findViewById(R.id.pwd);
        }
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //连的WIFI热点是用WPA方式保护
                mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo(result.SSID, etPwd.getText().toString()
                        , WifiAdmin.TYPE_WPA));
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                new Sender().start();
                break;
            case R.id.receiver:
                receiver();
                break;
            case R.id.create_hot:
                wifiAp = new WifiApAdmin(QuickTransferActivity.this);
                wifiAp.startWifiAp(Constant.HOST_SPOT_SSID, Constant.HOST_SPOT_PASS_WORD);
                listView.setAdapter(new DeciceAdaper(this,R.layout.item_wifi,WifiApAdmin.getConDevice()));
                break;
            default:
                break;
        }
    }


    private void receiver() {
        if (service != null) {
            service.close();
        }
        service = new Receiver();
        service.start();
    }

    private Receiver service;

    protected void onDestroy() {
        super.onDestroy();
        if (service != null) {
            service.close();
        }
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //接收数据
                case 0:
                    tvReceiver.setText(service.receiverContent);
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * 将获取的int转为真正的ip地址,参考的网上的，修改了下
     */
    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }

    /* 服务器 接收数据 */
    class Receiver extends Thread {
        public StringBuffer receiverContent;
        public boolean flag = true;
        public ServerSocket serverSocket = null;

        public void run() {
            try {
                // 创建ServerSocket
                serverSocket = new ServerSocket(3358);
                while (flag) {
                    // 接受客户端请求
                    Socket client = serverSocket.accept();
                    System.out.println("accept");
                    try {
                        // 接收客户端消息
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        receiverContent = new StringBuffer();
                        String temp;
                        while ((temp = in.readLine()) != null) {
                            receiverContent.append(temp);
                        }
                        Log.i(TAG, "read:" + receiverContent.toString());
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = receiverContent.toString();
                        handler.sendMessage(msg);
                        // 向服务器回复消息
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                client.getOutputStream())), true);
                        out.println("server message");
                        // 关闭流
                        out.close();
                        in.close();
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    } finally {
                        //关闭
                        client.close();
                    }
                }
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        }

        public void close() {
            flag = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* 客户端发送数据 */
    private class Sender extends Thread {
        private String serverIp;
        private String message;

        private Sender() {
            super();
            WifiManager wifiManage = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            DhcpInfo info = wifiManage.getDhcpInfo();
            serverIp = intToIp(info.serverAddress);
            message = tvSend.getText().toString();
        }

        public void run() {
            Socket sock = null;
            PrintWriter out;
            try {
                // 声明sock，其中参数为服务端的IP地址与自定义端口
                sock = new Socket(serverIp, 3358);
                Log.i("WifiConnection", "I am try to writer" + sock);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                // 向服务器端发送消息
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
                out.println(message);

                // 接收来自服务器端的消息
                BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String msg = br.readLine();
                out.write(msg);
                //关闭流
                out.close();
                out.flush();
                br.close();
                // 关闭Socket
                sock.close();
            } catch (Exception e) {
                LogUtil.e("发送错误:[Sender]run()" + e.getMessage());
            }

        }
    }

}
