package com.taoy3.framwork.downloaderstopsart;

/**
 * Created by taoy3 on 16/9/10.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 建立一个数据库帮助类
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper helper;
    // download.db-->数据库名
    private DBHelper(Context context) {
        super(context, "download.db", null, 1);
    }

public static DBHelper getInstance(Context context){
    if(helper==null){
        synchronized (DBHelper.class){
            if(helper==null){
                helper  = new DBHelper(context);
            }
        }
    }
    return helper;
}
    /**
     * 在download.db数据库下创建一个download_info表存储下载信息
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, "
                + "start_pos integer, end_pos integer, compelete_size integer,url char)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
