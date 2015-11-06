package com.example.saikrishna.raven.DataBase.Tables.Tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sai Krishna on 17-07-2015.
 */


public class Messages_Table extends SQLiteOpenHelper {

    public static final String TABLE_MESSAGE = "messages";
    public static final String COLUMN_ID = "_id";
    public static final String NUMBER_ID = "number";
    public static final String SENDER_ID = "sender";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_STATUS = "flag";
    public static final String COLUMN_SEEN="seen";

    public static final String DATABASE_NAME = "Information.db";
    public static int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MESSAGE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " +NUMBER_ID+" text not null, "+SENDER_ID+" text not null, "+ COLUMN_MESSAGE
            + " text not null, "+COLUMN_DATE+" text not null, "+COLUMN_TIME+" text not null, "
            +COLUMN_STATUS+" text not null, "+COLUMN_SEEN+" text not null"+");";

    public Messages_Table(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Messages_Table.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        onCreate(db);
    }

}
