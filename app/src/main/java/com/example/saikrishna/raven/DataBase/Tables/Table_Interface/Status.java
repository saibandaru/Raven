package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

/**
 * Created by Sai Krishna on 26-07-2015.
 */
public class Status {
    private String id;
    private String status_col;

    public String getId() {return id;  }

    public void setId(String id) { this.id = id; }

    public String getStatus() {
        return status_col;
    }

    public void setStatus(String status_col) { this.status_col = status_col; }
    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return status_col;
    }
}