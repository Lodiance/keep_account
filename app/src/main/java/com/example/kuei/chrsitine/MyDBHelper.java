package com.example.kuei.chrsitine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private final static String DB = "DB2018.db";//資料庫
    private final static String TB = "TB2018" ;//資料表
    private final static int VS = 4;//版本

    public MyDBHelper(Context context) {
        super(context, DB,  null, VS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //創建資料表的格式，VARCHAR(空間大小)
        String SQL = "CREATE TABLE IF NOT EXISTS " + TB + "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,_date INTEGER,_io VARCHAR(20), _money VARCHAR(20),_kind VARCHAR(20),_note VARCHAR(50))";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL = "DROP TABLE " + TB;
        db.execSQL(SQL);
    }
}
