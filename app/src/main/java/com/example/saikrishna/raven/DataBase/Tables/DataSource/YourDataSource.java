package com.example.saikrishna.raven.DataBase.Tables.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Status;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Your;
import com.example.saikrishna.raven.DataBase.Tables.Tables.Status_Table;
import com.example.saikrishna.raven.DataBase.Tables.Tables.Your_Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai Krishna on 30-07-2015.
 */
public class YourDataSource {

    // Database fields
    private SQLiteDatabase database;
    private Your_Table dbHelper;
    private String[] allColumns = {Your_Table.COLUMN_ID,Your_Table.COLUMN_PIC};


    public YourDataSource(Context context) {
        dbHelper = new Your_Table(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public byte[] retrivePic( ) {
        byte[] pic_ar=new byte[50];
        Cursor cursor = database.query(Your_Table.TABLE_YOUR,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Your newStatus = cursorToStatus(cursor);
            pic_ar=newStatus.getPic();
            cursor.moveToNext();
        }
        cursor.close();
        return pic_ar;
    }
    public String retriveNumber( ) {
       String number=null;
        Cursor cursor = database.query(Your_Table.TABLE_YOUR,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Your newStatus = cursorToStatus(cursor);
            number=newStatus.getId();
            cursor.moveToNext();
        }
        cursor.close();
        return number;
    }

    public void insertYour(String number,byte[] pic) {
            database.execSQL("INSERT INTO " + Your_Table.TABLE_YOUR + " (" + Your_Table.COLUMN_ID + "," + Your_Table.COLUMN_PIC + ")"
                    + " VALUES('" + number + "','" + pic + "');");
        }


    public void updateYour(String number,byte[] pic) {
    /*  NUMBER_ID||SENDER_ID||COLUMN_MESSAGE*/

      /*  database.execSQL("UPDATE " + Your_Table.TABLE_YOUR + " SET " + Your_Table.COLUMN_PIC + "=" + pic + " WHERE " +
                Your_Table.COLUMN_ID + "='" + number + "';");*/

        ContentValues values = new ContentValues();
        values.put(Your_Table.COLUMN_PIC, pic);
        int rowsUpdated = database.update(Your_Table.TABLE_YOUR, values, Your_Table.COLUMN_ID + "=" + number, null);
    }

    public boolean test(String number,byte[] pic) {
    /*  NUMBER_ID||SENDER_ID||COLUMN_MESSAGE*/

      /*  database.execSQL("UPDATE " + Your_Table.TABLE_YOUR + " SET " + Your_Table.COLUMN_PIC + "=" + pic + " WHERE " +
                Your_Table.COLUMN_ID + "='" + number + "';");*/

        ContentValues values = new ContentValues();
        values.put(Your_Table.COLUMN_PIC, pic);
        int rowsUpdated = database.update(Your_Table.TABLE_YOUR, values, Your_Table.COLUMN_ID + "=" + number, null);
        Cursor c= database.query(Your_Table.TABLE_YOUR, new String[]{Your_Table.COLUMN_PIC}, Your_Table.COLUMN_ID + "=?", new String[]{number}, null, null, null);
        byte[] img;
        if(c!=null){
            c.moveToFirst();
            do{
                img=c.getBlob(c.getColumnIndex("image"));
            }while(c.moveToNext());
        }
        return true;
    }


    private Your cursorToStatus(Cursor cursor) {
        Your status_one = new Your();
        status_one.setId(cursor.getString(0));
        status_one.setPic(cursor.getBlob(1));
        return status_one;
    }
}