package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

/**
 * Created by Sai Krishna on 30-07-2015.
 */
public class Your {
    private String id;
    private byte[] pic;

    public String getId() {return id;  }

    public void setId(String id) { this.id = id; }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) { this.pic = pic; }
    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String  toString() {
        return "image";
    }
}
