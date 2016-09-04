//package com.svasan.bluetoothmmochatroom;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.Intent.ShortcutIconResource;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.net.Uri;
//
//public class BluetoothShortcut {
//	private Context mContext;
//
//	public BluetoothShortcut(Context context) {
//		mContext = context;
//	}
//
//	public boolean hasShortcut() {
//		boolean result = false;
//	    String title = null;
//	    try {
//	        final PackageManager pm = mContext.getPackageManager();
//	        title = pm.getApplicationLabel(
//	                pm.getApplicationInfo(mContext.getPackageName(),
//	                        PackageManager.GET_META_DATA)).toString();
//	    } catch (Exception e) {
//	    }
//
//	    final String uriStr;
//	    if (android.os.Build.VERSION.SDK_INT < 8) {
//	        uriStr = "content://com.android.launcher.settings/favorites?notify=true";
//	    } else {
//	        uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
//	    }
//	    final Uri CONTENT_URI = Uri.parse(uriStr);
//	    final Cursor c = mContext.getContentResolver().query(CONTENT_URI, null,
//	            "title=?", new String[] { title }, null);
//	    if (c != null && c.getCount() > 0) {
//	        result = true;
//	    }
//	    return result;
//	}
//
//	public void addShortcut() {
//		Intent shortcut = new Intent(
//				"com.android.launcher.action.INSTALL_SHORTCUT");
//
//		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
//				mContext.getString(R.string.app_name));
//		shortcut.putExtra("duplicate", false);
//		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
//		shortcutIntent.setClassName(mContext, mContext.getClass().getName());
//		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
//				mContext, R.mipmap.app_icon);
//		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
//		mContext.sendBroadcast(shortcut);
//	}
//    public void delShortcut(){
//        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
//
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.app_name));
//        String appClass = mContext.getPackageName() + "." +mContext.getClass().getSimpleName();
//        ComponentName comp = new ComponentName(mContext.getPackageName(), appClass);
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
//
//        mContext.sendBroadcast(shortcut);
//
//    }
//}
