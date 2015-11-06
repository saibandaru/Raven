package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

/**
 * Created by Sai Krishna on 24-07-2015.
 */
public class Contacts {
    private int id;
    private String number;
    private String status;
    private String pic;

    public int getId() {return id;  }

    public void setId(int id) { this.id = id; }

    public String getNumber() {
        return number;
    }
    public String getPic() {
        return pic;
    }

    public void setNumber(String number) { this.number = number; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }
    public void setPic(String pic) { this.pic = pic; }
    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return number;
    }
}