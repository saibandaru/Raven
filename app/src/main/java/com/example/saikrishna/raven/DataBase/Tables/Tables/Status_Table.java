package com.example.saikrishna.raven.DataBase.Tables.Tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sai Krishna on 26-07-2015.
 */
public class Status_Table extends SQLiteOpenHelper {

    public static final String TABLE_STATUS = "status";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STATUS = "status_col";

    public static final String DATABASE_NAME = "Information_status.db";
    public static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STATUS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_STATUS
            + " text not null);";

    public Status_Table(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Status_Table.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        onCreate(db);
    }

}
