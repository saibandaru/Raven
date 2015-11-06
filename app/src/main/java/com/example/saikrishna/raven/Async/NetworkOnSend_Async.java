package com.example.saikrishna.raven.Async;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.saikrishna.raven.DataBase.Tables.DataSource.BufferMsgDataSource;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.MessageDataSource;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Messages_Parse;
import com.example.saikrishna.raven.Extras.ImageFormatting;
import com.example.saikrishna.raven.Extras.Utility;
import com.example.saikrishna.raven.Fragment.Fragment_ChatScreen;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_ChatScreen;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 8/14/2015.
 */
public class NetworkOnSend_Async extends AsyncTask<String,Void,Boolean> {
    //private final WeakReference<MyRecyclerViewAdapter> adapterReference;

    Context context;
    SharedPreferences sentnumber;
    public static final String SENT_IMAGE_NUMBER = "sentimagenumber";
    public NetworkOnSend_Async(Context context){this.context=context;
        //adapterReference=new WeakReference<MyRecyclerViewAdapter>(adapter);
    }
    @Override
    protected  Boolean doInBackground(final String... number1){
        int index=0;HashMap<String,String> msg;
        BufferMsgDataSource dataSource=new BufferMsgDataSource(context);
        dataSource.open();
        List<HashMap<String,String>> list_buf=dataSource.retriveBufMessageList();
        dataSource.close();
        while (list_buf.size()!=index){
            msg=list_buf.get(index);
            Log.d("Sending Order", msg.get("message").toString());
            int tableId=Integer.valueOf( msg.get("tableid"));
            dataSource.open();
            dataSource.delete(tableId);
            dataSource.close();
            if(!sendMsgndI(msg)) break;
            index++;
        }Log.d("For Sending Order", "end of Async");
        return true;
    }
    @Override
    protected void onPostExecute(Boolean flag){

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

                        }
                    }

                });
            }else
            {

            }
        return returnValue;
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



}