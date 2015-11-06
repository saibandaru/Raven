package com.example.saikrishna.raven.DataBase.Tables.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Buffer_Msg_I;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Message;
import com.example.saikrishna.raven.DataBase.Tables.Tables.Buffer_Msg;
import com.example.saikrishna.raven.DataBase.Tables.Tables.Messages_Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 8/14/2015.
 */
public class BufferMsgDataSource {
    // Database fields
    private SQLiteDatabase database;
    private Buffer_Msg dbHelper;
    Context context;
    private String[] allColumns = {Buffer_Msg.COLUMN_ID,Buffer_Msg.NUMBER_ID,Buffer_Msg.SENDER_ID,
            Buffer_Msg.COLUMN_MESSAGE,Buffer_Msg.COLUMN_TYPE,Buffer_Msg.COLUMN_MSG_PK};

    public BufferMsgDataSource(Context context) {
        this.context=context;
        dbHelper = new Buffer_Msg(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    public List<HashMap<String,String>> retriveBufMessageList() {

        List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        Cursor cursor = database.query(Buffer_Msg.TABLE_MESSAGE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Buffer_Msg_I newMessage = cursorToBufMessage(cursor);
            HashMap newMsg=new HashMap<String,String>();
            newMsg.put("tableid",String.valueOf(newMessage.getTableId()));
            newMsg.put("message",newMessage.getMessage());
            newMsg.put("recv_id",newMessage.getId());
            newMsg.put("sender_id",newMessage.getSenderId());
            newMsg.put("type",newMessage.getType());
            newMsg.put("pk",newMessage.getPk());
            list.add(newMsg);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void insertBufMessage(String message,final String id,String sender,Long num) {

        ContentValues values = new ContentValues();

        values.put(Buffer_Msg.NUMBER_ID, id);
        values.put(Buffer_Msg.SENDER_ID, sender);
        values.put(Buffer_Msg.COLUMN_MESSAGE, message);
        values.put(Buffer_Msg.COLUMN_TYPE, "0");
        values.put(Buffer_Msg.COLUMN_MSG_PK, String.valueOf(num));
        final long pk = database.insert(Buffer_Msg.TABLE_MESSAGE, null, values);
    }
    public void insertBufImage(String path,final String id,String sender,Long num) {

        ContentValues values = new ContentValues();

        values.put(Buffer_Msg.NUMBER_ID, id);
        values.put(Buffer_Msg.SENDER_ID, sender);
        values.put(Buffer_Msg.COLUMN_MESSAGE, path);
        values.put(Buffer_Msg.COLUMN_TYPE, "1");
        values.put(Buffer_Msg.COLUMN_MSG_PK, String.valueOf(num));
        final long pk = database.insert(Buffer_Msg.TABLE_MESSAGE, null, values);
    }
    public void delete(int tableid)
    {
        database.delete(Buffer_Msg.TABLE_MESSAGE, Buffer_Msg.COLUMN_ID + "=" + String.valueOf(tableid), null);
    }
    private Buffer_Msg_I cursorToBufMessage(Cursor cursor) {
        Buffer_Msg_I message = new Buffer_Msg_I();
        message.setTableId(cursor.getInt(0));
        message.setId(cursor.getString(1));
        message.setSenderId(cursor.getString(2));
        message.setMessage(cursor.getString(3));
        message.setType(cursor.getString(4));
        message.setPk(cursor.getString(5));
        return message;
    }
}
