package com.example.saikrishna.raven.DataBase.Tables.Tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sai Krishna on 24-07-2015.
 */
public class Contacts_Table extends SQLiteOpenHelper {

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_PROF_PIC = "pic";

    public static final String DATABASE_NAME = "Information_contact.db";
    public static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CONTACTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "+ COLUMN_STATUS
    + " text not null," + COLUMN_CONTACT
            + " text not null,"+COLUMN_PROF_PIC+" text not null);";

    public Contacts_Table(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

}

