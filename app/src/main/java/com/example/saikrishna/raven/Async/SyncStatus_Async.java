package com.example.saikrishna.raven.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.saikrishna.raven.DataBase.Tables.DataSource.ContactsDataSource;
import com.example.saikrishna.raven.Fragment.Fragment_contacts;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 26-07-2015.
 */
public class SyncStatus_Async extends AsyncTask<String,Void,List<HashMap<String,String >>> {
    //private final WeakReference<MyRecyclerViewAdapter> adapterReference;

    Context context;
    public SyncStatus_Async(Context context){this.context=context;
        //adapterReference=new WeakReference<MyRecyclerViewAdapter>(adapter);
    }
    @Override
    protected  List<HashMap<String,String >> doInBackground(final String... number1){
        final List<HashMap<String,String >> parse_list=new ArrayList<>();
        List<ParseObject> userList = new ArrayList<ParseObject>();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
            query.selectKeys(Arrays.asList("username","status"));
            userList = query.find();
        } catch(ParseException e) {
            Log.v("Exception", e.getMessage());
        }
        Log.d("User Parse----------- ", "List Size: " + userList.size());
        int index=0;
        while (userList.size()!=index)
        {
            ParseObject obj=userList.get(index);
            HashMap<String ,String > user=new HashMap<String, String>();

            user.put("status", obj.get("status").toString());
            user.put("number", obj.get("username").toString());
            parse_list.add(user);
            index++;
        }
        Log.d("User Parse----------- ", "Parse Size: " + parse_list.size());
        return parse_list;
    }
    @Override
    protected void onPostExecute(List<HashMap<String,String >> parse_list){
        ContactsDataSource database=new ContactsDataSource(context);
        int index=0;
        database.open();
        List<HashMap<String,String >> database_list= database.retriveContactList();
        database.close();
        while (parse_list.size()!=index){
            HashMap<String ,String> parse_contact=parse_list.get(index);
            int index2=0;
            while (database_list.size()!=index2){
                HashMap<String,String> retrive_contact;
                retrive_contact=database_list.get(index2);
                String retrive_number=retrive_contact.get("number");
                String parse_number=parse_contact.get("number");
                if((retrive_contact.get("number").equals(parse_contact.get("number")))&&!(retrive_contact.get("status").equals(parse_contact.get("status"))))
                {
                    database.open();
                    database.updateStatus(retrive_contact.get("number"),parse_contact.get("status"));
                    database.close();
                    break;}
                index2++;
            }
            index++;
        }
        database.open();
        Fragment_contacts.update_Async(database.retriveContactList());
        database.close();
    }

}