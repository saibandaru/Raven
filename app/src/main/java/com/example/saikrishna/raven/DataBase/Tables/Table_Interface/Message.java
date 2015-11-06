package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

public class Message {
  private String id;
  private String message;
  private String sender_id;
  private String date;
  private String time;
  private String status;
  private String seen;

  public String getId() {return id;  }

  public void setId(String id) { this.id = id; }

  public String getMessage() {
    return message;
  }
  public String getSenderId() {
    return sender_id;
  }

  public void setMessage(String message) { this.message = message; }
  public void setSenderId(String sender_id) {this.sender_id = sender_id; }
  public void setDate(String date) { this.date = date; }

  public String getDate() {
    return date;
  }
  public void setTime(String time) { this.time = time; }

  public String getTime() {
    return time;
  }
  public void setStatus(String status) { this.status = status; }

  public void setSeen(String seen){this.seen=seen;}
  public String getSeen(){return this.seen;}

  public String getStatus() {
    return status;
  }
  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
    return message;
  }
} 