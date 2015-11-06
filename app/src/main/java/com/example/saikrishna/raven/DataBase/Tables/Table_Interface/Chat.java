package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

/**
 * Created by Sai Krishna on 19-07-2015.
 */
public class Chat {
    private int id;
    private String contact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactChat() {
        return contact;
    }

    public void setContactChat(String contact) {
        this.contact = contact;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return contact;
    }
}
