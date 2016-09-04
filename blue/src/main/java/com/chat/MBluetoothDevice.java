package com.chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

public class MBluetoothDevice {
	// BluetoothAdapter actions
	public final String Action_Bluetooth_On = BluetoothAdapter.ACTION_REQUEST_ENABLE;//是否开启
	public final String Action_Bluetooth_Discoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;//是否可被搜索到
	public final String Action_Bluetooth_Discovery_Finish=BluetoothAdapter.ACTION_DISCOVERY_FINISHED;//搜索结束
	// Bluetooth scan mode
	public final int Mode_Connectable_Discoverable = BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE;
	// BluetoothAdapter data
	public final String Data_Dicoverable=BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION;//搜索时长
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;

	public MBluetoothDevice() {
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	/* 判断本地手机是否支持蓝牙服务 */
	public boolean isPhoneDevice_Support() {
		if (mBluetoothAdapter == null)
			return false;
		return true;
	}

	/*判断本地手机是否开启蓝牙服务 */
	public boolean isBluetoothAdapter_Enable() {
		return mBluetoothAdapter.isEnabled();
	}
	/*判断是否正在搜索中 */
	public boolean isBluetooth_discovering() {
		return mBluetoothAdapter.isDiscovering();
	}
	public int getBluetoothScanMode() {
		return mBluetoothAdapter.getScanMode();
	}
	/*得到匹配项*/
	public Set<BluetoothDevice> getPairedDevices() {
		return mBluetoothAdapter.getBondedDevices();
	}
	/*获取蓝牙适配器*/
	public BluetoothAdapter getBluetoothAdapter() {
		return mBluetoothAdapter;
	}
	public boolean discoveryBluetooth_Start() {
		return mBluetoothAdapter.startDiscovery();
	}
	/*取消搜索*/
	public void discoveryBluetooth_Cancel() {
		mBluetoothAdapter.cancelDiscovery();
	}
	/*获取远程蓝牙设备对象*/
	public BluetoothDevice getRomteBluetoothDevice(String address) {
		return mBluetoothAdapter.getRemoteDevice(address);
	}
}
