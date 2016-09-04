//package com.chat;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//
//public class NotifiManager {
//    private static final String Notification_Service = Context.NOTIFICATION_SERVICE;
//    private static final int Notification_ID = 0x0012;
//    private static final int Notification_Flag = Notification.FLAG_ONGOING_EVENT;
//    private Context mContext;
//    private NotificationManager mNotificationManager;
//    private Notification.Builder builder;
//    private Notification notification;
//
//    public NotifiManager(Context context) {
//        mContext = context;
//        mNotificationManager = (NotificationManager) context.getSystemService(Notification_Service);
//        builder = new Notification.Builder(context);
//			notification = builder.setContentTitle(mContext.getString
//                    (R.string.notification_tilte)).setContentText(mContext.getString(R.string.notification_text)).build();
//    }
//
//    public boolean isNotificationService_ON() {
//        return mNotificationManager != null;
//    }
//
//    public void createDefaultAtrr() {
//        int icon = R.mipmap.app_icon;
//        CharSequence tickerText = mContext.getString(R.string.app_name);
//        long when = System.currentTimeMillis();
//        notification.flags = Notification_Flag;
//        notification.icon = icon;
//        notification.tickerText = tickerText;
//        notification.when = when;
//    }
//
//    public void createPointIntent() {
//        Intent notificationIntent = new Intent(mContext, MultiChatRoom.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
//        notification.contentIntent = contentIntent;
//        builder.setContentIntent(contentIntent);
//    }
//
//    public void notificationFillIn() {
//        mNotificationManager.notify(Notification_ID, notification);
//    }
//
//    public void notificationDelete() {
//        mNotificationManager.cancel(Notification_ID);
//    }
//
//
//}
