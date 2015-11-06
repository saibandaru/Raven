package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.saikrishna.raven.Fragment.Fragment_ChatScreen;
import com.example.saikrishna.raven.Fragment.Fragment_contacts;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_ChatScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 23-07-2015.
 */
public class Parse_Push extends BroadcastReceiver {
    SharedPreferences mynumber;
    public static final String MOBILE_NUMBER = "my_number";
    public static String mynumber_value;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String jsonData = extras.getString("com.parse.Data");
                JSONObject json;
                json = new JSONObject(jsonData);
                String message = json.getString("alert");
                String number = json.getString("title");
                String system_number=getMatchedContact(number);
                //insertMessage_recived(msg,number)
                HashMap<String,String> new_msg=new HashMap<>();new_msg.put("message",message);new_msg.put("sender_id",system_number);
                mynumber=context.getSharedPreferences(MOBILE_NUMBER, 0);
                mynumber_value=mynumber.getString("mynumber", null);
                Fragment_ChatScreen.database.open();
                Fragment_ChatScreen.database.insertMessage(message, system_number,mynumber_value);
                Fragment_ChatScreen.database.close();
                updateChatScreen(new_msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String getMatchedContact(String sender)
    {
        int index=0;
        String number,unedited_number;
        List<HashMap<String,String>> contacts= Fragment_contacts.contacts;
        while(contacts.size()!=index){
            HashMap<String,String> contact=contacts.get(index);
            unedited_number=number=contact.get("number");
            number.replace(" ","").replace("-","");
            if(number==sender){
                return unedited_number;
            }
        }
        return sender;
    }
    public void updateChatScreen(HashMap<String,String> msg_block)
    {
        MyRecyclerViewAdapter_ChatScreen.messages.add(msg_block);
        Fragment_ChatScreen.myRecyclerViewAdapter.notifyDataSetChanged();
        Fragment_ChatScreen.recyclerView.scrollToPosition(MyRecyclerViewAdapter_ChatScreen.messages.size() - 1);
    }
}
