package layout.chat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sai Krishna on 02-07-2015.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{
    List<Information.Message> messages;
        private Context context;
    OnItemClickListener mItemClickListener;

    public MyRecyclerViewAdapter(Context context,List<Information.Message> messages){
        this.context=context;
        this.messages=messages;
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
        int viewType;
        boolean flag=align(position);
        if(flag)
            viewType=1;
        else
            viewType=0;
        return viewType;
    }
    public boolean align(int position)
    {
        Information.Message msg=messages.get(position);
        return (Boolean)msg.getFlag();
    }
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_msg,parent,false);
        if(viewType==0)
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_msg,parent,false);
        else
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_me,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Information.Message msg=messages.get(position);
        holder.bindMovieData(msg);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
       
        public TextView msg;
        public CardView card;

        public ViewHolder(View v)
        {
            super(v);
            msg=(TextView)v.findViewById(R.id.msg);
            card=(CardView)v.findViewById(R.id.card);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View vi){
                    if(mItemClickListener !=null){
                        mItemClickListener.onItemClick(vi,getPosition());
                    }
                }
            });
        }

        public void bindMovieData(Information.Message msge){
           msg.setText((String) msge.getMessage());

            if((Boolean)msge.getFlag()==false) {
                card.setCardBackgroundColor(Color.parseColor("#fffffdf7"));

            }
            else {
                card.setCardBackgroundColor(Color.parseColor("#5c32f93a"));
            }


        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }

}