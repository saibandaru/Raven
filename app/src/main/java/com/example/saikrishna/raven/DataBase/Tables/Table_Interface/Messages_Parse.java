package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

/**
 * Created by Sandesh on 7/14/2015.
 */

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Sandesh on 7/8/2015.
 */
@ParseClassName("Messages")
public class Messages_Parse extends ParseObject {

    public String getNumber() {return getString("number");}

    public String getStatus() {return getString("status");}

    public void setStatus(String status) {
        put("status",status);
    }

    public ParseFile getFile() {return (ParseFile)get("file");}

    public void setFile(ParseFile file) {
        put("file",file);
    }

    public String getSender() {return getString("sender");}

    public void setNumber(String number) {
        put("number",number);
    }

    public void setSender(String sender) {put("sender",sender);}

    public void setId(String id) {put("belongsTo",id);}

    public String getId() {return getString("belongsTo");}

    public String getMessage() {return getString("message");}

    public void setMessage(String message) {put("message",message);}

    public void setWholeMessage_msg(String number,String sender,String message,String id,String status,String seen,long local_id){
        put("number",number);
        put("sender",sender);
        put("message",message);
        put("belongsTo",id);
        put("status",status);
        put("seen",seen);
        put("local_msgId",local_id);
    }
    public void setWholeMessage_img(String number,String sender,String message,String id,String status, ParseFile file,String seen,long local_id){
        put("number",number);
        put("sender",sender);
        put("message",message);
        put("belongsTo",id);
        put("status",status);
        put("file",file);
        put("seen",seen);
        put("local_msgId",local_id);
    }
    public static ParseQuery<Messages_Parse> getQuery() {
        return ParseQuery.getQuery(Messages_Parse.class);
    }
}
