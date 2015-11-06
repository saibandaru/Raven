package com.example.saikrishna.raven.Extras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.example.saikrishna.raven.DataBase.Tables.DataSource.BufferMsgDataSource;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.MessageDataSource;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Messages_Parse;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Users_Parse;
import com.example.saikrishna.raven.Fragment.Fragment_ChatScreen;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_ChatScreen;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 8/14/2015.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    SharedPreferences sentnumber;
    public static final String SENT_IMAGE_NUMBER = "sentimagenumber";
    Handler mHandler = new Handler();
    public Context context;
    public static int index;
    public static List<HashMap<String,String >> list;
    public static boolean flag=true;
    SharedPreferences mynumber;
    public static final String MOBILE_NUMBER = "my_number";
    public static String mynumber_value;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        this.context=context;
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        flag=!flag;
        if ((wifi.isAvailable() || mobile.isAvailable())&&flag) {
            // Do something
            sendMsgs();
        }
    }
    public void sendMsgs()
    {
       index=0;HashMap<String,String> msg;
        BufferMsgDataSource dataSource=new BufferMsgDataSource(context);
        dataSource.open();
        list=dataSource.retriveBufMessageList();
        dataSource.close();
        recur_send();
        ping_for_holdMsgs();
        index=0;
       /* while (list_buf.size()!=index){
            msg=list_buf.get(index);
            Log.d("Sending Order", msg.get("message").toString());
            int tableId=Integer.valueOf( msg.get("tableid"));
            dataSource.open();
            dataSource.delete(tableId);
            dataSource.close();
            if(!sendMsgndI(msg)) break;
            delay();
            index++;
        }Log.d("For Sending Order", "end of Async");*/
    }
    public void recur_send()
    {
        HashMap<String,String> msg;
        BufferMsgDataSource dataSource=new BufferMsgDataSource(context);
        if(index!=list.size())
        {
            msg=list.get(index);
            /*Log.d("Sending Order", msg.get("message").toString());*/
            int tableId=Integer.valueOf( msg.get("tableid"));
            dataSource.open();
            dataSource.delete(tableId);
            dataSource.close();
            if(!sendMsgndI(msg)) {
                return;}

        }
    }
    public boolean sendMsgndI(HashMap<String ,String > msg){
        if(msg.get("type").toString().equalsIgnoreCase("0"))
        {
            return sendMsg(msg);
        }
        else if(msg.get("type").toString().equalsIgnoreCase("1"))
        {
            return sendImg(msg);
        }
        else return false;
    }

    public boolean sendMsg(HashMap<String ,String > msg){
        boolean returnValue=false;
        final String id=msg.get("recv_id").toString();
        String sender=msg.get("sender_id").toString();
        String message=msg.get("message").toString();
        final String pk=msg.get("pk").toString();
        if(Utility.haveNetworkConnection(context)) {
            returnValue=true;
            Messages_Parse message_p = new Messages_Parse();
            String parse_id = id.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
            message_p.setWholeMessage_msg(parse_id, sender, message, sender, "0", "1",Long.valueOf(pk));
            message_p.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(context,
                                "Error saving: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        MessageDataSource dataSource = new MessageDataSource(context);
                        dataSource.open();
                        dataSource.updateSeen(Long.parseLong(pk, 10), "1");
                        dataSource.close();
                        if (Fragment_ChatScreen.myRecyclerViewAdapter != null) {
                            if (Utility.compareNumbers(Fragment_ChatScreen.number, id)) {
                                dataSource.open();
                                MyRecyclerViewAdapter_ChatScreen.messages = dataSource.retriveMessageList_contact(Fragment_ChatScreen.number);
                                Fragment_ChatScreen.recyclerView.scrollToPosition(MyRecyclerViewAdapter_ChatScreen.messages.size() - 1);
                                Fragment_ChatScreen.myRecyclerViewAdapter.notifyDataSetChanged();
                                dataSource.close();

                            }
                        }
                        index++;
                        sleep();
                        recur_send();
                    }
                }

            });
        }else
        {

        }
        return returnValue;
    }
    public void sleep()
    {
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean sendImg(HashMap<String,String>msg){
        final String id=msg.get("recv_id").toString();
        final String sender=msg.get("sender_id").toString();
        String message=msg.get("message").toString();
        final String pk=msg.get("pk").toString();
        if(Utility.haveNetworkConnection(context)) {
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(message);
            byte[] image_byte= ImageFormatting.bitmapToByteArray(yourSelectedImage);
            final ParseFile photoFile = new ParseFile(sender.replace("+", "") + generateSINmae() + ".jpg", image_byte);
            photoFile.saveInBackground(new SaveCallback() {

                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(context,
                                "Error saving: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Messages_Parse message_p = new Messages_Parse();
                        String parse_id = id.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
                        message_p.setWholeMessage_img(parse_id, sender, "##image##", sender, "1", photoFile, "1",Long.valueOf(pk));
                        message_p.saveInBackground();
                        MessageDataSource dataSource = new MessageDataSource(context);
                        dataSource.open();
                        dataSource.updateSeen(Long.parseLong(pk, 10), "1");
                        dataSource.close();
                    }
                    recur_send();
                }

            });
            return true;
        }
        else {
            return false;
        }
    }
    public String generateSINmae()
    {
        sentnumber = context.getSharedPreferences(SENT_IMAGE_NUMBER, 0);
        int image_number=sentnumber.getInt("sentImage", 0);
        String name="sent_image_"+String.valueOf(image_number);
        SharedPreferences.Editor editor = sentnumber.edit();
        image_number+=1;
        editor.putInt("sentImage", image_number);
        editor.commit();
        return  name;
    }
    public void delay(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(10000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                // Write your code here to update the UI.
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
    }
    public void ping_for_holdMsgs()
    {
        Log.d("User Parse ", "Ping Called");
        mynumber=context.getSharedPreferences(MOBILE_NUMBER, 0);
        mynumber_value=mynumber.getString("mynumber", null);

        mynumber_value=mynumber_value.replace(" ","");
        mynumber_value= mynumber_value.replace("-","");
        mynumber_value= mynumber_value.replace("(","");
        mynumber_value= mynumber_value.replace(")","");
        final String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("username", mynumber_value);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    if (userList.size() == 1) {
                        ParseObject obj = userList.get(0);
                        obj.put("connected", timeStamp);
                        obj.saveInBackground();
                    }
                } else {
                    Log.d("User Parse ", "Error: " + e.getMessage());
                }
            }
        });
    }
}
