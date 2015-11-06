package com.example.saikrishna.raven.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
public class UpdateStatus_Async extends AsyncTask<String,Void,Boolean> {
    //private final WeakReference<MyRecyclerViewAdapter> adapterReference;

    Context context;
    public  String status;
    public String number;
    public UpdateStatus_Async(Context context,String status,String number){this.context=context;this.status=status;this.number=number;
        //adapterReference=new WeakReference<MyRecyclerViewAdapter>(adapter);
    }
    @Override
    protected  Boolean doInBackground(final String... number1){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("username",number);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    int index=0;
                    while (userList.size()!=index)
                    {
                        ParseObject obj=userList.get(index);
                        obj.put("status",status);
                        obj.saveEventually();
                        index++;
                    }
                } else {
                    Log.d("User Parse ", "Error: " + e.getMessage());
                }
            }
        });
    return true;
    }
    @Override
    protected void onPostExecute(Boolean flag){
        Toast.makeText(context,
                "Successfully updated your status!",
                Toast.LENGTH_LONG).show();

    }

}