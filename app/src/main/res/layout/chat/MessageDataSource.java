package layout.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageDataSource {

  // Database fields
  private SQLiteDatabase database;
  private Messages_Table dbHelper;
  private String[] allColumns = {Messages_Table.COLUMN_ID,
          Messages_Table.COLUMN_MESSAGE};

  public MessageDataSource(Context context) {
    dbHelper = new Messages_Table(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Message createMessage(String message) {
    ContentValues values = new ContentValues();
    values.put(Messages_Table.COLUMN_MESSAGE, message);
    long insertId = database.insert(Messages_Table.TABLE_MESSAGE, null,
            values);
    Cursor cursor = database.query(Messages_Table.TABLE_MESSAGE,
            allColumns, Messages_Table.COLUMN_ID + " = " + insertId, null,
            null, null, null);
    cursor.moveToFirst();
    Message newMessage = cursorToMessage(cursor);
    cursor.close();
    return newMessage;
  }

  public List<HashMap<String,String>> retriveMessageList( ) {

    List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
      Cursor cursor = database.query(Messages_Table.TABLE_MESSAGE,
              allColumns, null, null, null, null, null);
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Message newMessage = cursorToMessage(cursor);
      String msg=newMessage.getMessage();
      String id=newMessage.getId();
      HashMap newMsg=new HashMap<String,String>();
      newMsg.put("message",msg);
      newMsg.put("flag",id);
      list.add(newMsg);
        cursor.moveToNext();
    }
    cursor.close();
    return list;
  }

  public void insertMessage(String message, String id) {
    /*ContentValues values = new ContentValues();
    values.put(Messages_Table.COLUMN_MESSAGE, message);
    long insertId = database.insert(Messages_Table.TABLE_MESSAGE, null,
            values);*/

      database.execSQL("INSERT INTO "+Messages_Table.TABLE_MESSAGE + " VALUES("+message+","+id+");");

    // return newMessage;
  }

  public void deleteMessage(Message message) {
    String id = message.getId();
    System.out.println("Message deleted with id: " + id);
    database.delete(Messages_Table.TABLE_MESSAGE, Messages_Table.COLUMN_ID
            + " = " + id, null);
  }

  public List<Message> getAllMessages() {
    List<Message> messages = new ArrayList<Message>();

    Cursor cursor = database.query(Messages_Table.TABLE_MESSAGE,
            allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Message message = cursorToMessage(cursor);
      messages.add(message);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return messages;
  }

  private Message cursorToMessage(Cursor cursor) {
      Message message = new Message();
    message.setId(cursor.getString(0));
    message.setMessage(cursor.getString(1));
    return message;
  }
}
