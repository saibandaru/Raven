package com.example.saikrishna.raven.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saikrishna.raven.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 26-07-2015.
 */
public class MyRecycelrView_Status extends RecyclerView.Adapter<MyRecycelrView_Status.ViewHolder> {
    public List<String > statuses;
    private Context context;
    OnItemClickListener mItemClickListener;

    public MyRecycelrView_Status(Context context, List <String> statuses){
        this.context=context;
        this.statuses=statuses;
    }
    @Override
    public long getItemId(int position) {
        return  super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return statuses.size();
    }

    @Override
    public MyRecycelrView_Status.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.status_itemrow,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        String status_one=statuses.get(position) ;
        holder.bindMovieData(status_one);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView status_one;
        public ViewHolder(View v)
        {
            super(v);
            status_one=(TextView)v.findViewById(R.id.status_row);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View vi){
                    if(mItemClickListener !=null){
                        mItemClickListener.onItemClick(vi,getPosition());
                    }
                }
            });
        }

        public void bindMovieData(String person){
            status_one.setText(person);
        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }

}
