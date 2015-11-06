package com.example.saikrishna.raven.Async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.saikrishna.raven.DataBase.Tables.DataSource.ContactsDataSource;
import com.example.saikrishna.raven.Fragment.Fragment_contacts;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 24-07-2015.
 */
public class Installation_Async extends AsyncTask<String,Void,List<HashMap<String,String >>> {
    //private final WeakReference<MyRecyclerViewAdapter> adapterReference;

    List<String> contacts_nonex;
    Context context;
    public Installation_Async(List<String> contacts,Context context){this.contacts_nonex=contacts;this.context=context;
        //adapterReference=new WeakReference<MyRecyclerViewAdapter>(adapter);
    }
    @Override
    protected  List<HashMap<String,String >>  doInBackground(String... ifNeeded){
        final List<HashMap<String,String >> contacts=new ArrayList<>();
        List<ParseObject> myEventsList = new ArrayList<ParseObject>();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
            query.selectKeys(Arrays.asList("username","status","profile_pic"));
            myEventsList = query.find();
        } catch(ParseException e) {
            Log.v("Exception", e.getMessage());
        }
        int index=0;String number,status;
        Log.v("Size of Parse object:", String.valueOf(myEventsList.size()));
        while(myEventsList.size()!=index){
            ParseObject contact=myEventsList.get(index);
            number=contact.get("username").toString();
            status=contact.get("status").toString();
            ParseFile profile_pic=(ParseFile)contact.get("profile_pic");
            if(profile_pic==null)Log.v("Profile pic for:", number+"is null");
            String number_e= number.replace("+", "");
            download(profile_pic,number_e);
            Log.v("Number:", number);
            HashMap<String ,String > retrive_contact=new HashMap<>();
            retrive_contact.put("number", number);
            retrive_contact.put("status", status);
            String path= context.getFilesDir().getAbsolutePath()+File.separator+"RevanProfilePics/"+number_e;
            retrive_contact.put("profile_pic", path);
            contacts.add(retrive_contact);
            index++;
        }
        Log.v("Size of list object:", String.valueOf(contacts.size()));
        return contacts;
    }
    @Override
    protected void onPostExecute(List<HashMap<String,String >> contacts){
        int index=0;
        ContactsDataSource database=new ContactsDataSource(context);
        /*validateList(contacts);*/
        while (contacts_nonex.size()!=index){
            String nonex_contact=contacts_nonex.get(index);
            int index2=0;
            while (contacts.size()!=index2){
                HashMap<String,String> retrive_contact;
                retrive_contact=contacts.get(index2);
                if(retrive_contact.get("number").equals(nonex_contact))
                {
                    retrive_contact.put("number",nonex_contact);
                    database.open();
                    database.insertContacts(retrive_contact);
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
    public void download(ParseFile profile_pic,String name_pic)
    {

        final String number_f=name_pic;
        if(profile_pic!=null){
        profile_pic.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap profile_pic = byteArrayToBitmap(data);
                    saveImage(number_f, profile_pic);

                } else {
                      Toast.makeText(context,
                            "Error downloading the image: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }else
        {Log.v("Profile Picture null:", number_f);}

    }
    public static Bitmap byteArrayToBitmap(byte[] image) {

        InputStream inputStream = new ByteArrayInputStream(image);
        BitmapFactory.Options o = new BitmapFactory.Options();
        return BitmapFactory.decodeStream(inputStream, null, o);
        /*return BitmapFactory.decodeByteArray(image, 0, image.length);*/
    }
    public String saveImage(String imagename,Bitmap image)
    {
        File file = new File(new File(context.getFilesDir().getAbsolutePath(),File.separator+"RevanProfilePics/"), imagename);
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
        return context.getFilesDir().getAbsolutePath()+File.separator+"RevanProfilePics/"+imagename;
    }
    public void validateList(List<HashMap<String,String>> list)
    {
        int index=0;
        while(list.size()!=index)
        {
            HashMap<String,String> contact=list.get(index);
            String path=contact.get("pp_path");
            setImage(path);
        }
    }
    public void setImage(String path)
    {

        File file = new File(path);
        if (file.exists()) {


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            Bitmap bitmap2 = BitmapFactory.decodeFile(path, options);
        }
    }
}
