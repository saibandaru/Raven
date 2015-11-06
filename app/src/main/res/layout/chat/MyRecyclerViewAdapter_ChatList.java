package layout.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Sai Krishna on 04-07-2015.
 */


public class MyRecyclerViewAdapter_ChatList extends RecyclerView.Adapter<MyRecyclerViewAdapter_ChatList.ViewHolder>{
    private Information info;
    private Context context;
    OnItemClickListener mItemClickListener;

    public MyRecyclerViewAdapter_ChatList(Context context, Information info){
        this.context=context;
        this.info=info;
    }
    @Override
    public long getItemId(int position) {
        return  super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return info.getInformationSize();
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
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_itemrow,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Information.Block_Contact person=info.getItem(position) ;
        holder.bindMovieData(person);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView name;

        public ViewHolder(View v)
        {
            super(v);
            name=(TextView)v.findViewById(R.id.name);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View vi){
                    if(mItemClickListener !=null){
                        mItemClickListener.onItemClick(vi,getPosition());
                    }
                }
            });
        }

        public void bindMovieData(Information.Block_Contact person){
            name.setText((String) person.getContact().getName());
        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }

}
