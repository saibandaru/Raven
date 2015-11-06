package com.example.saikrishna.raven.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saikrishna.raven.Async.LocalImageLoad_Async;
import com.example.saikrishna.raven.Extras.ImageFormatting;
import com.example.saikrishna.raven.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 19-07-2015.
 */
public class MyRecyclerViewAdapter_ChatScreen    extends RecyclerView.Adapter<MyRecyclerViewAdapter_ChatScreen.ViewHolder>{
    public static List<HashMap<String,String>> messages;
    private Context context;
    private String number;
    OnItemClickListener mItemClickListener;

    public MyRecyclerViewAdapter_ChatScreen(Context context,List<HashMap<String,String>> messages,String number){
        this.context=context;
        this.messages=messages;
        this.number=number;
    }



    @Override
    public long getItemId(int position) {
        return  super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return messages.size();
    }
    @Override
    public int getItemViewType(int position) {
        int viewType=getFlag(position);
        return viewType;
    }
    @Override
    public MyRecyclerViewAdapter_ChatScreen.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_msg,parent,false);
        if(viewType==0)
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_msg,parent,false);
        else if(viewType==1)
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_me,parent,false);
        else if(viewType==2)
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_image_resv,parent,false);
        else
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_image ,parent,false);
        ViewHolder vh=new ViewHolder(v,viewType);

        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        HashMap<String ,String> msg=messages.get(position);
        holder.bindMovieData(msg);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView msg;
        public TextView time;
        public CardView card;
        public ImageView image;
        public ImageView seen1;
        public ImageView seen2;

        public ViewHolder(View v,int viewType)
        {
            super(v);
            seen1=(ImageView) v.findViewById(R.id.seen1);
            seen2=(ImageView) v.findViewById(R.id.seen2);
            if(viewType==2||viewType==3)
            {
                image=(ImageView) v.findViewById(R.id.msg_image);
                time = (TextView) v.findViewById(R.id.time);
            }else {
                msg = (TextView) v.findViewById(R.id.msg);
                time = (TextView) v.findViewById(R.id.time);
                card = (CardView) v.findViewById(R.id.card);
            }
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View vi){
                    if(mItemClickListener !=null){
                        mItemClickListener.onItemClick(vi,getPosition());
                    }
                }
            });
        }

        public void bindMovieData(HashMap<String ,String > msge){
            if(msge.get("status").toString().equalsIgnoreCase("1"))
            {
                time.setText((String) msge.get("time"));
                LocalImageLoad_Async loadimage=new LocalImageLoad_Async(image,(String) msge.get("message"),100);
                loadimage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"nothimg"});
                seen1.setImageBitmap(ImageFormatting.shrinkBitmap_seen(msge.get("seen"), context));
                /*loadimage.execute(new String[]{"nothimg"});*/
                /*Bitmap image_bit=getImage((String) msge.get("message"));
                if(image_bit!=null)
                    image.setImageBitmap(image_bit);*/
            }
            else {
                seen1.setImageBitmap(ImageFormatting.shrinkBitmap_seen(msge.get("seen"),context));
                msg.setText((String) msge.get("message"));
                time.setText((String) msge.get("time"));
            }
        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }
    public int getFlag(int position)
    {
        String sender_id=(String)messages.get(position).get("sender_id");
        String status=(String)messages.get(position).get("status");
        if(status.equalsIgnoreCase("1")) {
            if (number.equals(sender_id)) {
                return 2;
            } else {
                return 3;
            }
        }
        else {
            if (number.equals(sender_id)) {
                return 0;
            } else {
                return 1;
            }
        }
    }
    public Bitmap getImage(String path)
    {

        File file = new File(path);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            return bitmap;
        }
        return null;
    }
}
