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

/*import android.support.v4.widget.CircleImageView;*/
import com.example.saikrishna.raven.Async.LocalCImageLoad_Async;
import com.example.saikrishna.raven.Extras.ImageFormatting;
import com.example.saikrishna.raven.R;
import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 05-07-2015.
 */
public class MyRecyclerViewAdapter_Contacts extends RecyclerView.Adapter<MyRecyclerViewAdapter_Contacts.ViewHolder> {
    public static List<HashMap<String,String>> contacts;
    private Context context;
    OnItemClickListener mItemClickListener;

    public MyRecyclerViewAdapter_Contacts(Context context, List<HashMap<String, String>> contacts){
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
    public MyRecyclerViewAdapter_Contacts.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_itemrow,parent,false);
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
        public TextView status;
        public CircularImageView profilepic;
        public ViewHolder(View v)
        {
            super(v);
            name=(TextView)v.findViewById(R.id.name);
            status=(TextView)v.findViewById(R.id.number);
            profilepic=(CircularImageView)v.findViewById(R.id.imageView);
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
            status.setText(person.get("status").toString());
            /*setImage(person.get("profile_pic").toString(), profilepic);*/
            LocalCImageLoad_Async loadimage=new LocalCImageLoad_Async(profilepic,person.get("profile_pic").toString(),50);
            loadimage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"nothimg"});
            /*loadimage.execute(new String[]{"nothimg"});*/

        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }
    public void setImage(String path,ImageView profilepic) {

        File file = new File(path);
        if (file.exists()) {

            Bitmap bitmap1= ImageFormatting.ShrinkBitmap(path, 50, 50);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            profilepic.setImageBitmap(bitmap1);
        }
    }

}
