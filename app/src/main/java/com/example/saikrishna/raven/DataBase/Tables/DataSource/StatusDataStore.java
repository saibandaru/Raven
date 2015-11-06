package com.example.saikrishna.raven.DataBase.Tables.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Message;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Messages_Parse;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Status;
import com.example.saikrishna.raven.DataBase.Tables.Tables.Messages_Table;
import com.example.saikrishna.raven.DataBase.Tables.Tables.Status_Table;
import com.example.saikrishna.raven.Fragment.Fragment_home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 26-07-2015.
 */
public class StatusDataStore {

    // Database fields
    private SQLiteDatabase database;
    private Status_Table dbHelper;
    private String[] allColumns = {Status_Table.COLUMN_ID,Status_Table.COLUMN_STATUS};


    public StatusDataStore(Context context) {
        dbHelper = new Status_Table(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<String> retriveStatusList( ) {

        List<String> list=new ArrayList<>();
        Cursor cursor = database.query(Status_Table.TABLE_STATUS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Status newStatus = cursorToStatus(cursor);
            list.add(newStatus.getStatus());
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void setupStatus(List<String > list) {
        int index=0;
        while (list.size()!=index){
            database.execSQL("INSERT INTO "+Status_Table.TABLE_STATUS+ " ("+Status_Table.COLUMN_STATUS+")"
                    +" VALUES('"+list.get(index)+"');");
            index++;
        }


        // return newMessage;
    }

    public void insertStatus(String status) {
    /*  NUMBER_ID||SENDER_ID||COLUMN_MESSAGE*/

        database.execSQL("INSERT INTO "+Status_Table.TABLE_STATUS+ " ("+Status_Table.COLUMN_STATUS+")"
                +" VALUES('"+status+"');");
    }


    private Status cursorToStatus(Cursor cursor) {
        Status status_one = new Status();
        status_one.setId(cursor.getString(0));
        status_one.setStatus(cursor.getString(1));
        return status_one;
    }
}
