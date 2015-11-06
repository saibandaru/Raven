package com.example.saikrishna.raven.DataBase.Tables.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.saikrishna.raven.DataBase.Tables.Tables.Contacts_Table;
import com.example.saikrishna.raven.DataBase.Tables.Tables.Messages_Table;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Message;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Messages_Parse;
import com.example.saikrishna.raven.Extras.Utility;
import com.example.saikrishna.raven.Fragment.Fragment_ChatScreen;
import com.example.saikrishna.raven.Fragment.Fragment_home;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_ChatScreen;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MessageDataSource {

  // Database fields
  private SQLiteDatabase database;
  private Messages_Table dbHelper;
  Context context;
  private String[] allColumns = {Messages_Table.COLUMN_ID,Messages_Table.NUMBER_ID,Messages_Table.SENDER_ID,
          Messages_Table.COLUMN_MESSAGE,Messages_Table.COLUMN_DATE,Messages_Table.COLUMN_TIME,
          Messages_Table.COLUMN_STATUS,Messages_Table.COLUMN_SEEN};

  public MessageDataSource(Context context) {
        this.context=context;
      dbHelper = new Messages_Table(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public List<HashMap<String,String>> retriveMessageList_contact(String id_where ) {

    List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
    Cursor cursor = database.query(Messages_Table.TABLE_MESSAGE,
            allColumns, Messages_Table.NUMBER_ID + " = '" + id_where+"'", null, null, null, null);
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Message newMessage = cursorToMessage(cursor);
      String msg=newMessage.getMessage();
      String id=newMessage.getSenderId();
      HashMap newMsg=new HashMap<String,String>();
      newMsg.put("message",msg);
      newMsg.put("sender_id",id);
      newMsg.put("date",newMessage.getDate());
      newMsg.put("time",newMessage.getTime());
      newMsg.put("status",newMessage.getStatus());
        newMsg.put("seen",newMessage.getSeen());
      list.add(newMsg);
      cursor.moveToNext();
    }
    cursor.close();
    return list;
  }


    public HashMap<String,String> retriveLastMessage_contact(String id_where ) {
        this.open();
        List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        Cursor cursor = database.query(Messages_Table.TABLE_MESSAGE,
                allColumns, Messages_Table.NUMBER_ID + " = '" + id_where + "'", null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Message newMessage = cursorToMessage(cursor);
            String msg=newMessage.getMessage();
            String id=newMessage.getSenderId();
            HashMap newMsg=new HashMap<String,String>();
            newMsg.put("message",msg);
            newMsg.put("sender_id",id);
            newMsg.put("date",newMessage.getDate());
            newMsg.put("time",newMessage.getTime());
            newMsg.put("status",newMessage.getStatus());
            newMsg.put("seen",newMessage.getSeen());
            list.add(newMsg);
            cursor.moveToNext();
        }
        cursor.close();
        this.close();
        return list.get(list.size()-1);
    }

  public void insertMessage(String message,final String id,String sender) {
      ContentValues values = new ContentValues();

      values.put(Messages_Table.NUMBER_ID, id);
      values.put(Messages_Table.SENDER_ID, sender);
      values.put(Messages_Table.COLUMN_MESSAGE, message);
      values.put(Messages_Table.COLUMN_DATE, date());
      values.put(Messages_Table.COLUMN_TIME, time());
      values.put(Messages_Table.COLUMN_STATUS, "0");
      values.put(Messages_Table.COLUMN_SEEN, "0");
      final long pk= database.insert(Messages_Table.TABLE_MESSAGE, null, values);
      if(Utility.haveNetworkConnection(context)) {
          Messages_Parse message_p = new Messages_Parse();
          String parse_id = id.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
          message_p.setWholeMessage_msg(parse_id, sender, message, sender, "0", "1", pk);
          message_p.saveInBackground(new SaveCallback() {
              public void done(ParseException e) {
                  if (e != null) {
                      Toast.makeText(context,
                              "Error saving: " + e.getMessage(),
                              Toast.LENGTH_LONG).show();
                  } else {
                      MessageDataSource dataSource = new MessageDataSource(context);
                      dataSource.open();
                      dataSource.updateSeen(Long.parseLong(Long.toString(pk), 10), "1");
                      dataSource.close();
                      Log.d("Update Delovered", "updated database");
                      if (Fragment_ChatScreen.myRecyclerViewAdapter != null) {
                          if (Utility.compareNumbers(Fragment_ChatScreen.number, id)) {
                              dataSource.open();
                              Log.d("Update Delovered", "coming inside");
                              Fragment_ChatScreen.messages= MyRecyclerViewAdapter_ChatScreen.messages = dataSource.retriveMessageList_contact(Fragment_ChatScreen.number);
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
          BufferMsgDataSource dataSource=new BufferMsgDataSource(context);
          dataSource.open();
          dataSource.insertBufMessage(message, id, sender, pk);
          dataSource.close();
      }
  }

  public void insertImage(final String message,final String id,final String sender,byte[] image,String imagename) {
      ContentValues values = new ContentValues();

      values.put(Messages_Table.NUMBER_ID, id);
      values.put(Messages_Table.SENDER_ID, sender);
      values.put(Messages_Table.COLUMN_MESSAGE, message);
      values.put(Messages_Table.COLUMN_DATE, date());
      values.put(Messages_Table.COLUMN_TIME, time());
      values.put(Messages_Table.COLUMN_STATUS, "1");
      values.put(Messages_Table.COLUMN_SEEN, "0");
      final long pk= database.insert(Messages_Table.TABLE_MESSAGE, null, values);

      if(Utility.haveNetworkConnection(context)) {
          final ParseFile photoFile = new ParseFile(sender.replace("+", "") + imagename + ".jpg", image);
          photoFile.saveInBackground(new SaveCallback() {

              public void done(ParseException e) {
                  if (e != null) {
                      Toast.makeText(context,
                              "Error saving: " + e.getMessage(),
                              Toast.LENGTH_LONG).show();
                  } else {
                      Messages_Parse message_p = new Messages_Parse();
                      String parse_id = id.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
                      message_p.setWholeMessage_img(parse_id, sender, "##image##", sender, "1", photoFile, "1", pk);
                      message_p.saveInBackground();
                      MessageDataSource dataSource = new MessageDataSource(context);
                      dataSource.open();
                      dataSource.updateSeen(Long.parseLong(Long.toString(pk), 10), "1");
                      dataSource.close();
                  }
              }

          });
      }
      else {
          BufferMsgDataSource dataSource=new BufferMsgDataSource(context);
          dataSource.open();
          dataSource.insertBufMessage(message, id, sender, pk);
          dataSource.close();
      }
  }

  public void insertMessage_recived(String message, String sender_number) {
    /*  NUMBER_ID||SENDER_ID||COLUMN_MESSAGE*/

    database.execSQL("INSERT INTO " + Messages_Table.TABLE_MESSAGE + " (" + Messages_Table.NUMBER_ID + "," + Messages_Table.SENDER_ID + "," + Messages_Table.COLUMN_MESSAGE + "," + Messages_Table.COLUMN_DATE + "," +
            Messages_Table.COLUMN_TIME + "," + Messages_Table.COLUMN_STATUS + "," +
            Messages_Table.COLUMN_SEEN + ") " + " VALUES('" + sender_number + "','" + sender_number + "','" + message + "','" +
            date() + "','" + time() + "','" + "0" + "','" + "NA" + "');");

  }

  public void insertimg_recived(String imageloc, String sender_number) {
    /*  NUMBER_ID||SENDER_ID||COLUMN_MESSAGE*/

        database.execSQL("INSERT INTO " + Messages_Table.TABLE_MESSAGE + " (" + Messages_Table.NUMBER_ID + "," + Messages_Table.SENDER_ID + "," + Messages_Table.COLUMN_MESSAGE +","+Messages_Table.COLUMN_DATE+","+
                Messages_Table.COLUMN_TIME+","+Messages_Table.COLUMN_STATUS+","+
                Messages_Table.COLUMN_SEEN+") "+" VALUES('" + sender_number + "','" + sender_number + "','" + imageloc+"','"+
                date()+"','"+time()+"','"+"1"+"','"+"NA"+"');");

  }

  public void deleteMessage(Message message) {
    String id = message.getId();
    System.out.println("Message deleted with id: " + id);
    database.delete(Messages_Table.TABLE_MESSAGE, Messages_Table.COLUMN_ID
            + " = " + id, null);
  }

    public void updateSeen(Long id,String seen){
        database.execSQL("UPDATE " + Messages_Table.TABLE_MESSAGE + " SET " + Messages_Table.COLUMN_SEEN+ "='" + seen + "' WHERE " + Messages_Table.COLUMN_ID + "=" + id + ";");
    }

  private Message cursorToMessage(Cursor cursor) {
      Message message = new Message();
    message.setId(cursor.getString(1));
    message.setSenderId(cursor.getString(2));
    message.setMessage(cursor.getString(3));
    message.setDate(cursor.getString(4));
    message.setTime(cursor.getString(5));
    message.setStatus(cursor.getString(6));
    message.setSeen(cursor.getString(7));
    return message;
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
}
