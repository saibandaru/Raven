package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

/**
 * Created by Sandesh on 7/14/2015.
 */

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Sandesh on 7/8/2015.
 */
@ParseClassName("Profiles")
public class Profiles extends ParseObject {

    public String getUserNumber() {return getString("usernumber");}

    public String getTableId() {return getString("tableid");}

    public void setUserNumber(String usernumber) {
        put("usernumber",usernumber);
    }

    public void setTableId(String tableid) {put("tableid",tableid);}

    public void setProfile(String fullnumber,String table_id){
        put("usernumber",fullnumber);
        put("tableid",table_id);
    }

    public static ParseQuery<Profiles> getQuery() {
        return ParseQuery.getQuery(Profiles.class);
    }
}
