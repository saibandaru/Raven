package com.example.saikrishna.raven.DataBase.Tables.DataSource;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.saikrishna.raven.DataBase.Tables.Tables.Contacts_Table;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 24-07-2015.
 */
public class ContactsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private Contacts_Table dbHelper;
    private String[] allColumns = {Contacts_Table.COLUMN_ID,Contacts_Table.COLUMN_STATUS,
            Contacts_Table.COLUMN_CONTACT,Contacts_Table.COLUMN_PROF_PIC};

    public ContactsDataSource(Context context) {
        dbHelper = new Contacts_Table(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<HashMap<String,String>> retriveContactList( ) {

        List<HashMap<String,String>> list=new ArrayList<>();
        Cursor cursor = database.query(Contacts_Table.TABLE_CONTACTS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contacts newMessage = cursorToContacts(cursor);
            HashMap<String,String> contact=new HashMap<>();
            contact.put("number",newMessage.getNumber());
            contact.put("status",newMessage.getStatus());
            contact.put("profile_pic", newMessage.getPic());
            list.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public HashMap<String,String> retriveContact(String number ) {
        number=number.replace(" ","");
        number= number.replace("-","");
        number= number.replace("(","");
        number= number.replace(")","");
        Cursor cursor = database.query(Contacts_Table.TABLE_CONTACTS,
                allColumns, Contacts_Table.COLUMN_CONTACT+" = '"+number+"'", null, null, null, null);
        cursor.moveToFirst();
        HashMap<String,String> contact1=new HashMap<>();
        while (!cursor.isAfterLast()) {
            Contacts newMessage = cursorToContacts(cursor);
            HashMap<String,String> contact=new HashMap<>();
            contact.put("number",newMessage.getNumber());
            contact.put("status", newMessage.getStatus());
            contact.put("profile_pic", newMessage.getPic());
            cursor.moveToNext();
            contact1=contact;
        }
        cursor.close();
        return contact1;
    }

    public void insertContacts(HashMap<String,String> contact) {
        database.execSQL("INSERT INTO " + Contacts_Table.TABLE_CONTACTS + " (" + Contacts_Table.COLUMN_STATUS + "," + Contacts_Table.COLUMN_CONTACT + "," + Contacts_Table.COLUMN_PROF_PIC + ")"
                + " VALUES('" + contact.get("status") + "','" + contact.get("number") + "','" + contact.get("pp_path") + "');");
    }

    public void updateStatus(String number,String status){
        database.execSQL("UPDATE "+Contacts_Table.TABLE_CONTACTS+" SET "+Contacts_Table.COLUMN_STATUS+"='"+status+"' WHERE "+Contacts_Table.COLUMN_CONTACT+"='"+number+"'");
    }
    public void updatePic(String number,String pic){
        database.execSQL("UPDATE "+Contacts_Table.TABLE_CONTACTS+" SET "+Contacts_Table.COLUMN_PROF_PIC+"='"+pic+"' WHERE "+Contacts_Table.COLUMN_CONTACT+"='"+number+"'");
    }
    public String image_path(String number)
    {
        this.open();
        number=number.replace(" ","");
        number= number.replace("-","");
        number= number.replace("(","");
        number= number.replace(")","");
        Cursor cursor = database.query(Contacts_Table.TABLE_CONTACTS,
              new String[]{  Contacts_Table.COLUMN_PROF_PIC}, Contacts_Table.COLUMN_CONTACT+" = '"+number+"'", null, null, null, null);
        cursor.moveToFirst();
        String path=null;
        while (!cursor.isAfterLast()) {
            Contacts newMessage = cursorToimage(cursor);
            HashMap<String,String> contact=new HashMap<>();
            path=newMessage.getPic();
            cursor.moveToNext();
        }
        this.close();
        return path;
    }
    public boolean checkContactIfExists(String contact)
    {
        Cursor cursor = database.query(Contacts_Table.TABLE_CONTACTS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Contacts newMessage = cursorToContacts(cursor);
            String number=newMessage.getNumber();
            if(number.equals(contact))
            {
                cursor.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return false;
    }

    private Contacts cursorToContacts(Cursor cursor) {
        Contacts contact = new Contacts();
        contact.setId(cursor.getInt(0));
        contact.setStatus(cursor.getString(1));
        contact.setNumber(cursor.getString(2));
        contact.setPic(cursor.getString(3));
        return contact;
    }
    private Contacts cursorToimage(Cursor cursor) {
        Contacts contact = new Contacts();
        contact.setPic(cursor.getString(0));
        return contact;
    }
}
