package com.example.saikrishna.raven.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saikrishna.raven.Async.LocalCImageLoad_Async;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.ContactsDataSource;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.MessageDataSource;
import com.example.saikrishna.raven.Extras.ImageFormatting;
import com.example.saikrishna.raven.R;
import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 05-07-2015.
 */
public class MyRecyclerViewAdapter_ChatList extends RecyclerView.Adapter<MyRecyclerViewAdapter_ChatList.ViewHolder> {
    public static List<HashMap<String,String>> contacts;
    private Context context;
    OnItemClickListener mItemClickListener;

    public MyRecyclerViewAdapter_ChatList(Context context, List<HashMap<String, String>> contacts){
        this.context=context;
        this.contacts=contacts;
    }
    @Override
    public long getItemId(int position) {
        return  super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return contacts.size();
    }
    /*    @Override
        public int getItemViewType(int position) {
            int viewType;
            boolean flag=align(position);
            if(flag)
                viewType=1;
            else
                viewType=0;
            return viewType;
        }*/
    @Override
    public MyRecyclerViewAdapter_ChatList.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_itemrow,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        HashMap person=contacts.get(position) ;
        holder.bindMovieData(person);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView name;
        public TextView number;
        public TextView time;
        public CircularImageView profile_pic;
        public ViewHolder(View v)
        {
            super(v);
            name=(TextView)v.findViewById(R.id.name);
            number=(TextView)v.findViewById(R.id.number);
            time=(TextView)v.findViewById(R.id.time);
            profile_pic=(CircularImageView )v.findViewById(R.id.imageView);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View vi){
                    if(mItemClickListener !=null){
                        mItemClickListener.onItemClick(vi,getPosition());
                    }
                }
            });
        }

        public void bindMovieData(HashMap person){
            name.setText(person.get("name").toString());
            /*number.setText(person.get("number").toString());*/
            set_msg(number, time,person.get("number").toString());
            ContactsDataSource dataSource=new ContactsDataSource(context);
            dataSource.open();
            HashMap<String,String> contact_pic=  dataSource.retriveContact(person.get("number").toString());
            dataSource.close();
            String path=contact_pic.get("profile_pic").toString();
            LocalCImageLoad_Async loadimage=new LocalCImageLoad_Async(profile_pic,path,50);
            /*loadimage.execute(new String[]{"nothimg"});*/
            loadimage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"nothimg"});
            /*setImage(person.get("number").toString(), profile_pic);*/

        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }
    public void setImage(String number,ImageView profilepic) {
        ContactsDataSource dataSource=new ContactsDataSource(context);
        dataSource.open();
        HashMap<String,String> contact_pic=  dataSource.retriveContact(number);
        dataSource.close();


        String path=contact_pic.get("profile_pic").toString();
        File file = new File(path);
        if (file.exists()) {

            Bitmap bitmap1= ImageFormatting.ShrinkBitmap(path, 50, 50);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            profilepic.setImageBitmap(bitmap1);
        }
    }
    public void set_msg(TextView msg,TextView time,String number)
    {
        MessageDataSource dataSource=new MessageDataSource(context);
        HashMap<String,String> last_msg=dataSource.retriveLastMessage_contact(number);
        if(!last_msg.get("status").toString().equalsIgnoreCase("1"))
         msg.setText(last_msg.get("message"));
        else
            msg.setText("image");
        time.setText(last_msg.get("time"));
    }
}
