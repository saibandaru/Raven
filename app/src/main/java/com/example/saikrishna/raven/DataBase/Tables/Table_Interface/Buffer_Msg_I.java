package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

/**
 * Created by Sai Krishna on 8/14/2015.
 */


public class Buffer_Msg_I {
    private int table_id;
    private String id;
    private String message;
    private String sender_id;
    private String type;
    private String primaryKey;

    public int getTableId() {return table_id;  }

    public void setTableId(int table_id) { this.table_id = table_id; }
    public String getId() {return id;  }

    public void setId(String id) { this.id = id; }

    public String getMessage() {
        return message;
    }
    public String getSenderId() {
        return sender_id;
    }

    public void setMessage(String message) { this.message = message; }
    public void setSenderId(String sender_id) {this.sender_id = sender_id; }
    public void setType(String type) { this.type = type; }

    public String getType() {
        return type;
    }
    public void setPk(String primaryKey) { this.primaryKey = primaryKey; }

    public String getPk() {
        return primaryKey;
    }
    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return message;
    }
}
