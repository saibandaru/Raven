package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sai Krishna on 8/18/2015.
 */
@ParseClassName("Users")
public class Users_Parse extends ParseObject {
    public void setWholeMessage_msg(){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        put("connected",timeStamp);
    }
    public static ParseQuery<Users_Parse> getQuery() {
        return ParseQuery.getQuery(Users_Parse.class);
    }
}
