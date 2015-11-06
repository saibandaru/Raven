package layout.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 05-07-2015.
 */
public class MyRecyclerViewAdapter_Contacts extends RecyclerView.Adapter<MyRecyclerViewAdapter_Contacts.ViewHolder> {
    List<HashMap<String,String>> contacts;
    private Context context;
    OnItemClickListener mItemClickListener;

    public MyRecyclerViewAdapter_Contacts(Context context,List<HashMap<String,String>> contacts){
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
        public TextView number;
        public ViewHolder(View v)
        {
            super(v);
            name=(TextView)v.findViewById(R.id.name);
            number=(TextView)v.findViewById(R.id.number);
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
            number.setText(person.get("number").toString());

        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener=mItemClickListener;
    }

}
