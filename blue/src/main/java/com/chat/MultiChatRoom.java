package com.chat;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MultiChatRoom extends Activity implements OnClickListener, OnItemClickListener {
    // Debugging test
    private static final String TAG = MultiChatRoom.class.getSimpleName();
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_GET_IMG = 4;
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    // Member object for the chat services
    private BluetoothChatService mChatService = null;
    private MBluetoothDevice mBluetoothDevice;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    private ChatMsgEntity newMessage;
    private ImageButton faceButton;
    private EditText messageEditext;
    private Button messageButton;
    private ListView talkRecodeView;
    private ArrayList<ChatMsgEntity> msgList;
    // PopupWindow of showing varoius faces
    private PopupWindow imgPopupWindow;
    private Button searchButton;
    private Button bagsetButton;
    private View roomView;
    private Drawable backgroundDrawable;
    private FaceInfo mFaceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_chat_room);

        roomView = findViewById(R.id.mainlayout);
        faceButton = (ImageButton) findViewById(R.id.imgButton);
        messageEditext = (EditText) findViewById(R.id.MessageText);
        messageButton = (Button) findViewById(R.id.MessageButton);
        talkRecodeView = (ListView) findViewById(R.id.list);
        searchButton = (Button) findViewById(R.id.search);
        bagsetButton = (Button) findViewById(R.id.bag_set);
        mFaceInfo = new FaceInfo(this);
        GridView gvPopupWindow = (GridView) getLayoutInflater().inflate(
                R.layout.face_list, null);
        gvPopupWindow.setAdapter(new GalleryAdapter(this, mFaceInfo.mfacesId));
        gvPopupWindow.setOnItemClickListener(this);
        // popuwindow init
        imgPopupWindow = new PopupWindow(gvPopupWindow,
                LayoutParams.WRAP_CONTENT, 160);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluetoothDevice = new MBluetoothDevice();
        //判断是否支持蓝牙设备
        if (!mBluetoothDevice.isPhoneDevice_Support()) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        msgList = new ArrayList();
        //判断蓝牙是否开启
        if (!mBluetoothDevice.isBluetoothAdapter_Enable()) {
            Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_LONG).show();
        } else {
            if (mChatService == null)
                setupChat();
        }
    }
    public void setupChat() {
        Log.i(TAG, ">>setup chat<<");
        messageButton.setOnClickListener(this);
        faceButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        bagsetButton.setOnClickListener(this);
        // 打开蓝牙监听服务
        mChatService = new BluetoothChatService(this, mHandler);
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
        imgPopupWindow.setFocusable(true);
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        Log.e(TAG, "+ ON RESUME +");
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // 若服务有关闭,则打开
                mChatService.start();
            }
        }
        if (messageEditext.getText().toString().length() > 0) {
            //将字符重新放到编辑处
            messageEditext.setText(mOutStringBuffer);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //安全状态下
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            //不安全状态下
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // 蓝牙开启
                    setupChat();
                } else {
                    // 蓝牙未开启
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case REQUEST_GET_IMG:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    ContentResolver cresolver = getContentResolver();
                    try {
                        //获取图片
                        InputStream is = cresolver.openInputStream(uri);
                        backgroundDrawable = Drawable.createFromStream(is, null);
                        roomView.setBackgroundDrawable(backgroundDrawable);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.multi_chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent;
        switch (item.getItemId()) {
            case R.id.startService:
                // 开启蓝牙
                Intent enableIntent = new Intent(
                        mBluetoothDevice.Action_Bluetooth_On);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                return true;
            case R.id.secure_connect_scan:
                // 启动设备列表界面 安全模式
                serverIntent = new Intent(this, DevicesListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            case R.id.insecure_connect_scan:
                // 启动设备列表界面 不安全模式
                serverIntent = new Intent(this, DevicesListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            case R.id.discoverable:
                // 确保蓝牙可被其它设备搜索到
                ensureDiscoverable();
                return true;
        }
        return false;
    }
    public void ensureDiscoverable() {
        Log.i(TAG, ">>ensure discoverable<<");
        if (mBluetoothDevice.getBluetoothScanMode() != mBluetoothDevice.Mode_Connectable_Discoverable) {
            Intent discoverableIntent = new Intent(mBluetoothDevice.Action_Bluetooth_Discoverable);
            discoverableIntent.putExtra(mBluetoothDevice.Data_Dicoverable, 300);
            startActivity(discoverableIntent);
        }
    }
    @Override
    public void onClick(View view) {
        Log.i(TAG, "Onclick");
        if (view == messageButton) {
            if (mBluetoothDevice.isBluetoothAdapter_Enable()) {
                String msg = messageEditext.getText().toString();
                sendMessage(msg);
            } else {
                Log.i(TAG, "Uneable");
                messageEditext.setText(mOutStringBuffer);
                Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
            }
            return;
        }
        if (view == faceButton) {
            if (imgPopupWindow != null) {
                if (imgPopupWindow.isShowing()) {
                    imgPopupWindow.dismiss();
                } else {
                    ColorDrawable cd = new ColorDrawable(-0000);
                    imgPopupWindow.setBackgroundDrawable(cd);
                    imgPopupWindow.showAtLocation(faceButton, Gravity.BOTTOM, 0, 0);
                }
            }
            return;
        }
        if (view == searchButton) {
            Intent serverIntent;
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DevicesListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return;
        }
        if (view == bagsetButton) {
            Intent imgIntent = new Intent();
            // Launch the DeviceListActivity to see devices and do scan
            imgIntent.setType("image/*");
            imgIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(imgIntent, REQUEST_GET_IMG);

        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(
                DevicesListActivity.EXTRA_DEVICE_ADDRESS);
        Log.e(TAG, address);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothDevice.getRomteBluetoothDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    private final void setStatus(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
    }


    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            messageEditext.setText(mOutStringBuffer);
        }
    }

    /* The Handler that gets information back from the BluetoothChatService */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to,
                                    mConnectedDeviceName));
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    newMessage = new ChatMsgEntity("我", "1013-05-15", writeMessage,
                            R.layout.list_say_me_item);
                    msgList.add(newMessage);
                    talkRecodeView.setAdapter(new ChatMsgViewAdapter(
                            MultiChatRoom.this, msgList));
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    newMessage = new ChatMsgEntity(mConnectedDeviceName,
                            "1013-05-15", readMessage, R.layout.list_say_other_item);
                    msgList.add(newMessage);
                    talkRecodeView.setAdapter(new ChatMsgViewAdapter(
                            MultiChatRoom.this, msgList));
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        imgPopupWindow.dismiss();
        String oldstr = messageEditext.getText().toString();
        String newstr = oldstr + "#{" + mFaceInfo.faceList.get(position)+ "}";
        SpannableStringBuilder ss = mFaceInfo.imagistSpan(newstr);
        messageEditext.setText(ss);
        messageEditext.setSelection(ss.length());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 停止服务
        if (mChatService != null)
            mChatService.stop();
        Log.e(TAG, "--- ON DESTROY ---");
    }
}
