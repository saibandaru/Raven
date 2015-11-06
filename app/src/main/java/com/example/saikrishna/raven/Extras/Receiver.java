package com.example.saikrishna.raven.Extras;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.saikrishna.raven.Activity.Activity_ChatScreen;
import com.example.saikrishna.raven.Activity.MainActivity;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.ChatDataSource;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.MessageDataSource;
import com.example.saikrishna.raven.Fragment.Fragment_ChatScreen;
import com.example.saikrishna.raven.R;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_ChatScreen;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 23-07-2015.
 */
public class Receiver extends ParsePushBroadcastReceiver {
    public static final String ARG_CONTACTS_INDEX = "argument_option";
    public static final String ARG_CONTACTS_INDEX_NAME = "argument_option_name";
    public static final String SENT_IMAGE_NUMBER = "sentimagenumber";
    SharedPreferences recvnumber;

    public void notifyUser(Context context,Intent intent,String message,String system_number,String name) {

                //insertMessage_recived(msg,number)


                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Activity.NOTIFICATION_SERVICE);
                Intent notificationIntent = new Intent(
                        context.getApplicationContext(), Activity_ChatScreen.class);
                notificationIntent.putExtra(ARG_CONTACTS_INDEX, system_number);
                notificationIntent.putExtra(ARG_CONTACTS_INDEX_NAME, name);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_color64x64);

        //stackBuilder.addParentStack(Activity_ChatScreen.class);
                Intent main_activity = new Intent(context, MainActivity.class);
                stackBuilder.addNextIntent(main_activity);
                stackBuilder.addNextIntent(notificationIntent);
                PendingIntent pIntent = stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_ONE_SHOT );
                Notification notification = new Notification.Builder(context)
                        .setContentTitle(name)
                        .setContentText(message)
                        .setContentIntent(pIntent)
                        .setLargeIcon(bm)
                        .setDefaults(
                                Notification.DEFAULT_SOUND
                                        | Notification.DEFAULT_VIBRATE)
                        .setContentIntent(pIntent).setAutoCancel(true)
                        .setSmallIcon(R.drawable.icon_color64x64 ).build();
                notificationManager.notify(2, notification);

    }


    @Override
    public void onPushReceive(Context context, Intent intent) {
        ParseAnalytics.trackAppOpenedInBackground(intent);
        String system_number,name;
        try {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String jsonData = extras.getString("com.parse.Data");
                    JSONObject json;
                    json = new JSONObject(jsonData);

                    String type = json.getString("title");
                    String resp=json.getString("responce");
                    if(resp.equalsIgnoreCase("false")){
                    String number = "ttt";
                    String message = "yyyy";

                    if (type.equalsIgnoreCase("0")) {
                        String id = json.getString("alert");
                        recvMsg(id, context, intent);
                    }
                    else if (type.equalsIgnoreCase("10")) {
                        recvOfflineMsgs(json,context,intent);
                    }
                    else {
                        String id = json.getString("alert");
                        getFileandNumber(id, context, intent);

                    }

            }
            else
            {
                String id = json.getString("alert");
                String recp_number = json.getString("number");
                MessageDataSource dataSource=new MessageDataSource(context);
                dataSource.open();
                dataSource.updateSeen(Long.parseLong(id, 10), type);
                dataSource.close();
                if(Fragment_ChatScreen.myRecyclerViewAdapter!=null)
                {
                    if(Utility.compareNumbers(Fragment_ChatScreen.number,recp_number))
                    dataSource.open();
                    Fragment_ChatScreen.messages= MyRecyclerViewAdapter_ChatScreen.messages=dataSource.retriveMessageList_contact(Fragment_ChatScreen.number);
                    Fragment_ChatScreen.recyclerView.scrollToPosition(MyRecyclerViewAdapter_ChatScreen.messages.size() - 1);
                    Fragment_ChatScreen.myRecyclerViewAdapter.notifyDataSetChanged();
                    dataSource.close();
                }


            }}
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void recvOfflineMsgs(JSONObject jason,Context context,Intent intent)
    {
        try {
            List<String> listdata = new ArrayList<String>();
            JSONArray jArray = (JSONArray) jason.get("alert");
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    listdata.add(jArray.get(i).toString());
                    recvMsgImg(jArray.get(i).toString(),context,intent);
                }
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void recvMsg(String id,Context context,Intent intent)
    {
        String number,system_number,name;
        String message;
        HashMap<String, String> new_msg;
        HashMap<String, String> msg_nd_number = getMesssageandNumber(id);
        number = msg_nd_number.get("number");
        message = msg_nd_number.get("message");
        updateSeen(id);
        system_number = getMatchedContact(number, context);
        new_msg = new HashMap<>();
        new_msg.put("message", message);
        new_msg.put("sender_id", system_number);
        new_msg.put("status", "0");
        new_msg.put("seen", "NA");
        MessageDataSource database = new MessageDataSource(context.getApplicationContext());
        database.open();
        database.insertMessage_recived(message, system_number);
        database.close();
        system_number = getMatchedContact(number, context);
        name = getMatchedContact_Name(number, context);
        insertChatItemIfNotExists(system_number, context);
        handle_Cases(context, intent, message, system_number, name, new_msg);
    }
    public void recvMsgImg(String id,Context context,Intent intent)
    {
        HashMap<String, String> msg_nd_number = getMesssageandNumberImg(id) ;
        if(msg_nd_number.get("type").equalsIgnoreCase("0"))
        {recvMsg(id,context,intent);}
        else
        { getFileandNumber(id, context, intent);}
    }
    public void updateSeen(String number)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.whereEqualTo("objectId",number);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    if (userList.size()==1)
                    {
                        ParseObject obj=userList.get(0);
                        obj.put("seen", "2");
                        obj.saveEventually();
                    }
                } else {
                    Log.d("User Parse ", "Error: " + e.getMessage());
                }
            }
        });
    }
    public void handle_Cases(Context context,Intent intent,String message,String system_number,String name,HashMap<String,String> new_msg)
    {
        if(Activity_ChatScreen.flag==null){
                    /*Intent i = new Intent(context, Activity_ChatScreen.class);
                    i.putExtra(Fragment_contacts.ARG_CONTACTS_INDEX, system_number);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);*/notifyUser(context,intent,message,system_number,name);
            updateChatScreen(new_msg);
        }
        else
        {
            if(Activity_ChatScreen.flag==false)
            {
                        /*Intent i = new Intent(context, Activity_ChatScreen.class);
                        i.putExtra(Fragment_contacts.ARG_CONTACTS_INDEX, system_number);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);*/
                notifyUser(context,intent,message,system_number,name);
                updateChatScreen(new_msg);
            }
            else
            {
                if(Activity_ChatScreen.number.equals(system_number))
                {
                    updateChatScreen(new_msg);
                }
                else
                {
                    notifyUser(context,intent,message,system_number,name);
                }
            }
        }
    }
    public String getMatchedContact(String sender, Context context)
    {
        int index=0;
        String number,unedited_number=sender;
        List<HashMap<String,String>> contact=contacts(context);
        while(contact.size()!=index){
            HashMap<String,String> contact_one=contact.get(index);
            unedited_number=number=contact_one.get("number");
            number=number.replace(" ","");
            number= number.replace("-","");
            number= number.replace("(","");
            number= number.replace(")","");
            if(number.equals(sender) ){
                return unedited_number;
            }
            index++;
        }if(contact.size()==index){

        unedited_number=sender;
        }
        return unedited_number;
    }

    public String getMatchedContact_Name(String sender, Context context)
    {
        int index=0;
        String number,unedited_name,unedited_number=sender;
        unedited_name=sender;
        List<HashMap<String,String>> contact=contacts(context);
        while(contact.size()!=index){
            HashMap<String,String> contact_one=contact.get(index);
            unedited_number=number=contact_one.get("number");
            unedited_name=contact_one.get("name");
            number=number.replace(" ","");
            number= number.replace("-","");
            number= number.replace("(","");
            number= number.replace(")","");
            if(number.equals(sender) ){
                return unedited_name;
            }
            index++;
        }if(contact.size()==index){

        unedited_name=sender;
        }
        return unedited_name;
    }

    public void updateChatScreen(HashMap<String,String> msg_block)
    {
        if(MyRecyclerViewAdapter_ChatScreen.messages!=null) {
            msg_block.put("date", date());
            msg_block.put("time",time());
            MyRecyclerViewAdapter_ChatScreen.messages.add(msg_block);
            Fragment_ChatScreen.myRecyclerViewAdapter.notifyDataSetChanged();
            Fragment_ChatScreen.recyclerView.scrollToPosition(MyRecyclerViewAdapter_ChatScreen.messages.size() - 1);
        }
    }
    public List<HashMap<String,String>> contacts(Context context) {
        //final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
        final Uri uriContact = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(uriContact, null, null, null, null);

        List<HashMap<String,String>> contacts=new ArrayList<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                String has_phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
                HashMap<String,String> contact=new HashMap();
                if(!has_phone.endsWith("0"))
                {
                    contact.put("name", name);
                    String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contact.put("number",phoneNumber);
                    contacts.add(contact);
                }
            }
        }
        return contacts;
    }
    public void insertChatItemIfNotExists(String number,Context context){
        ChatDataSource chat_database=new ChatDataSource(context.getApplicationContext());
        chat_database.open();
        if(!chat_database.checkContactIfExists(number)) {
            chat_database.insertMessage(number);
        }
        chat_database.close();
    }
    private String time()
    {
        Calendar c = Calendar.getInstance();
        String time_v = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) +":"+String.valueOf( c.get(Calendar.MINUTE));
        return time_v;
    }
    private String date()
    {
        Calendar c = Calendar.getInstance();
        String date_v =String.valueOf(c.get(Calendar.YEAR) )+"-"+ String.valueOf(c.get(Calendar.MONTH)) +"-"+ String.valueOf(c.get(Calendar.DATE));
        return date_v;
    }
    private HashMap<String,String> getMesssageandNumber(String id)
    {
        List<ParseObject> userList = new ArrayList<ParseObject>();
        HashMap<String ,String > message=new HashMap<String, String>();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
            query.selectKeys(Arrays.asList("sender", "message"));
            query.whereEqualTo("objectId",id);
            userList = query.find();
        } catch(ParseException e) {
            Log.v("Exception", e.getMessage());
        }
        if (userList.size()==1) {
            ParseObject obj = userList.get(0);
            message.put("message", obj.get("message").toString());
            message.put("number", obj.get("sender").toString());
        }
        return message;
    }
    private HashMap<String,String> getMesssageandNumberImg(String id)
    {
        List<ParseObject> userList = new ArrayList<ParseObject>();
        HashMap<String ,String > message=new HashMap<String, String>();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
            query.selectKeys(Arrays.asList("sender", "message","status"));
            query.whereEqualTo("objectId",id);
            userList = query.find();
        } catch(ParseException e) {
            Log.v("Exception", e.getMessage());
        }
        if (userList.size()==1) {
            ParseObject obj = userList.get(0);
            message.put("message", obj.get("message").toString());
            message.put("number", obj.get("sender").toString());
            message.put("type", obj.get("status").toString());
        }
        return message;
    }
    public String getName(Context context)
    {
        recvnumber = context.getSharedPreferences(SENT_IMAGE_NUMBER, 0);
        int image_number=recvnumber.getInt("recvImage", 0);
        String name="recv_image_"+String.valueOf(image_number);
        SharedPreferences.Editor editor = recvnumber.edit();
        image_number+=1;
        editor.putInt("recvImage", image_number);
        editor.commit();
        return  name;
    }
    private void getFileandNumber(String id,Context context,Intent intent)
    {
        List<ParseObject> userList = new ArrayList<ParseObject>();
        HashMap<String ,String > message=new HashMap<String, String>();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
            query.selectKeys(Arrays.asList("sender", "file"));
            query.whereEqualTo("objectId",id);
            userList = query.find();
        } catch(ParseException e) {
            Log.v("Exception", e.getMessage());
        }
        if (userList.size()==1) {
            ParseObject obj = userList.get(0);
            String sender_number= obj.get("sender").toString();
            ParseFile image=obj.getParseFile("file");
            download(image, getName(context), context, "RevanRecvPics",sender_number,intent);
        }
    }
    public void download(ParseFile profile_pic,String name_pic,final Context context, final String directory, final String sender_number,final Intent intent)
    {

        final String number_f=name_pic;
        makeDir(context, directory);
        if(profile_pic!=null){
            profile_pic.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap profile_pic = byteArrayToBitmap(data);
                        String path=saveImage(number_f, profile_pic, directory, context);
                        String number=sender_number;
                        String message1=path;
                        String message="1 new image";
                        String system_number=getMatchedContact(number, context);
                        HashMap<String,String> new_msg=new HashMap<>();new_msg.put("message", message1);new_msg.put("sender_id", system_number);new_msg.put("status", "1");
                        MessageDataSource database=new MessageDataSource(context.getApplicationContext());
                        database.open();
                        database.insertimg_recived(message1, system_number);
                        database.close();
                        system_number=getMatchedContact(number, context);
                        String name= getMatchedContact_Name(number, context);
                        insertChatItemIfNotExists(system_number, context);
                        handle_Cases(context,intent,message,system_number,name,new_msg);

                    } else {
                        Toast.makeText(context,
                                "Error downloading the image: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }else
        {
            Log.v("Profile Picture null:", number_f);}
    }

    public void makeDir(Context context,String directory)
    {
        String path=context.getFilesDir().getAbsolutePath()+File.separator+directory+"/";
        File file = new File ( path );

        if ( file.exists() )
        {
            Log.d("Download Parse image", "Message: " + directory + " EXISTS");
        }
        else
        {
            File f = new File(context.getFilesDir().getAbsolutePath(),File.separator+directory+"/");
            f.mkdirs();
        }
    }
    public String saveImage(String imagename,Bitmap image,String directory,Context context)
    {
        File file = new File(new File(context.getFilesDir().getAbsolutePath(),File.separator+directory+"/"), imagename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getFilesDir().getAbsolutePath()+File.separator+directory+"/"+imagename;
    }
    public Bitmap byteArrayToBitmap(byte[] image) {

        InputStream inputStream = new ByteArrayInputStream(image);
        BitmapFactory.Options o = new BitmapFactory.Options();
        return BitmapFactory.decodeStream(inputStream, null, o);
        /*return BitmapFactory.decodeByteArray(image, 0, image.length);*/
    }
}