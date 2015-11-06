package com.example.saikrishna.raven.Async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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
 * Created by Sai Krishna on 8/4/2015.
 */
public class SyncPP_Async extends AsyncTask<String,Void,List<HashMap<String,String >>> {
    //private final WeakReference<MyRecyclerViewAdapter> adapterReference;

    Context context;
    public SyncPP_Async(Context context){this.context=context;
        //adapterReference=new WeakReference<MyRecyclerViewAdapter>(adapter);
    }
    @Override
    protected  List<HashMap<String,String >> doInBackground(final String... number1){
        final List<HashMap<String,String >> parse_list=new ArrayList<>();
        List<ParseObject> userList = new ArrayList<ParseObject>();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
            query.selectKeys(Arrays.asList("username","status","profile_pic"));
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
            String number=obj.get("username").toString();
            user.put("status", obj.get("status").toString());
            user.put("number", obj.get("username").toString());

            ParseFile profile_pic=(ParseFile)obj.get("profile_pic");
            if(profile_pic==null)Log.v("Profile pic for:", number+"is null");
            String number_e= number.replace("+", "");
            download(profile_pic, number_e);
            user.put("number", obj.get("username").toString());
            String path= context.getFilesDir().getAbsolutePath()+File.separator+"RevanProfilePics/"+number_e;
            user.put("profile_pic", path);
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
                if((retrive_contact.get("number").equals(parse_contact.get("number"))))
                {
                    database.open();
                    database.updatePic(retrive_contact.get("number"),parse_contact.get("profile_pic"));
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
}
