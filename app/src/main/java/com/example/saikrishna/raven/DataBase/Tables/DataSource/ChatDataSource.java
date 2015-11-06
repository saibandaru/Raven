package com.example.saikrishna.raven.DataBase.Tables.DataSource;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.saikrishna.raven.DataBase.Tables.Tables.Chat_Table;
import com.example.saikrishna.raven.DataBase.Tables.Tables.Messages_Table;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Chat;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 19-07-2015.
 */
public class ChatDataSource {

    // Database fields
    private SQLiteDatabase database;
    private Chat_Table dbHelper;
    private String[] allColumns = {Chat_Table.COLUMN_ID,
            Chat_Table.COLUMN_MESSAGE};

    public ChatDataSource(Context context) {
        dbHelper = new Chat_Table(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<HashMap<String,String>> retriveChatList( ) {

        List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        Cursor cursor = database.query(Chat_Table.TABLE_MESSAGE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Chat newMessage = cursorToMessage(cursor);
            String msg=newMessage.getContactChat();
            HashMap newMsg=new HashMap<String,String>();
            newMsg.put("number",msg);
            list.add(newMsg);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void insertMessage(String contact) {
    /*ContentValues values = new ContentValues();
    values.put(Messages_Table.COLUMN_MESSAGE, message);
    long insertId = database.insert(Messages_Table.TABLE_MESSAGE, null,
            values);*/
       database.execSQL("INSERT INTO " + Chat_Table.TABLE_MESSAGE + " (" + Chat_Table.COLUMN_MESSAGE + ")" + " VALUES('" + contact + "');");
        // return newMessage;
    }
    public boolean checkContactIfExists(String contact)
    {
        Cursor cursor = database.query(Chat_Table.TABLE_MESSAGE,
                allColumns, Chat_Table.COLUMN_MESSAGE + " = '" + contact+"'", null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
        return false;}

    }

    public void deleteMessage(Message message) {
        String id = message.getId();
        System.out.println("Message deleted with id: " + id);
        database.delete(Messages_Table.TABLE_MESSAGE, Messages_Table.COLUMN_ID
                + " = " + id, null);
    }

    private Chat cursorToMessage(Cursor cursor) {
        Chat chat = new Chat();
        chat.setId(cursor.getInt(0));
        chat.setContactChat(cursor.getString(1));
        return chat;
    }
    public List<HashMap<String,String>> getChats(List<HashMap<String,String>> contacts)
    {
        List<HashMap<String,String>> chat_contacts_num= retriveChatList();
        List<HashMap<String,String>> chat_contacts=new ArrayList<HashMap<String, String>>();
        int index=0;
        while(index!=chat_contacts_num.size()){

            HashMap<String ,String > contact=new HashMap<>();
            contact.put("number",chat_contacts_num.get(index).get("number").toString());
            contact.put("name",getContactName(chat_contacts_num.get(index).get("number"),contacts));
            chat_contacts.add(contact);
            index++;
        }
        return chat_contacts;
    }
    public String getContactName(String number,List<HashMap<String,String>> contacts){//},List<HashMap<String,String >> contacts){
        int index=0;
        String name="not found";
        while(index!=contacts.size()){
            HashMap<String,String> contact=contacts.get(index);
            String number_inside=contact.get("number");
            if(number_inside.equals(number)) {
                name = contact.get("name");
                return name;
            }
            index++;
        }
        return name;
    }
}
