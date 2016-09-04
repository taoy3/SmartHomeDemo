/*
*@author taoy3,2015-3-5
*
*
*
*/
package com.connection.wifi;


import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.connection.bean.Device;
import com.connection.chat.MyTimerCheck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建热点
 */
public class WifiApAdmin {
    private static final String TAG = "WifiApAdmin";

    public static void closeWifiAp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        closeWifiAp(wifiManager);
    }

    private WifiManager mWifiManager = null;

    private Context mContext = null;

    public WifiApAdmin(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        closeWifiAp(mWifiManager);
    }

    private String mSSID = "";
    private String mPasswd = "";

    public void startWifiAp(String ssid, String passwd) {
        mSSID = ssid;
        mPasswd = passwd;

        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }

        stratWifiAp();

        MyTimerCheck timerCheck = new MyTimerCheck() {

            @Override
            public void doTimerCheckWork() {
                // TODO Auto-generated method stub

                if (isWifiApEnabled(mWifiManager)) {
                    Log.v(TAG, "Wifi enabled success!");
                    this.exit();
                } else {
                    Log.v(TAG, "Wifi enabled failed!");
                }
            }

            @Override
            public void doTimeOutWork() {
                // TODO Auto-generated method stub
                this.exit();
            }
        };
        timerCheck.start(15, 1000);

    }

    public void stratWifiAp() {
        Method method1;
        try {
            method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;

            netConfig.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            //用WPA方式保护
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.TKIP);

            method1.invoke(mWifiManager, netConfig, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void closeWifiAp(WifiManager wifiManager) {
        if (isWifiApEnabled(wifiManager)) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);

                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isWifiApEnabled(WifiManager wifiManager) {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<Device> getConDevice() {
        List<Device> devices = new ArrayList<Device>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/net/arp"));
            String line = reader.readLine();
            //读取第一行信息，就是IP address HW type Flags HW address Mask Device
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("[ ]+");
                if (tokens.length < 6) {
                    continue;
                }
                devices.add(new Device(tokens[0], Integer.parseInt(tokens[1]),
                        Integer.parseInt(tokens[2]), tokens[3], tokens[4], tokens[5]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        return devices;
    }
}