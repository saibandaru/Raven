package layout.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 04-07-2015.
 */
public class SqLite extends Fragment {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String ARG_OPTION = "argument_option";
    MessageDataSource database;
    public SqLite() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.sqlite, container, false);
        TextView text=(TextView)rootView.findViewById(R.id.text);
        TextView msg=(TextView)rootView.findViewById(R.id.msg);
        TextView id=(TextView)rootView.findViewById(R.id.id);
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String text2=settings.getString("text",null);
        if(text2==null) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("text", "one");
            editor.putInt("id", 2);
            database=new MessageDataSource(getActivity().getApplicationContext());
            database.open();
            database.insertMessage("1","1");
            List<HashMap<String,String>> list=database.retriveMessageList();
            if(list.size()>0){
            HashMap<String,String> one_msg=list.get(list.size()-1);
            String write_message= (String) one_msg.get("message");
            msg.setText(write_message);
            id.setText(one_msg.get("flag"));}

            // Commit the edits!
            editor.commit();
            text.setText("First time");
            database.close();
        }
        else
        {
            if(text2.equals("one") )
            {
                int number=settings.getInt("id",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("id",number+1);
                editor.commit();
                text.setText("Not First time");
                database=new MessageDataSource(getActivity().getApplicationContext());
                database.open();
                database.insertMessage(String.valueOf(number),String.valueOf(number));
                List<HashMap<String,String>> list=database.retriveMessageList();
                if(list.size()>0){
                    HashMap<String,String> one_msg=list.get(list.size()-1);
                    msg.setText(one_msg.get("message"));
                    id.setText(one_msg.get("flag"));}
                database.close();
            }
            else
            {
                text.setText("Something went wrong");
            }
        }
        int option = this.getArguments().getInt(ARG_OPTION);
        return rootView;
    }

    public static SqLite newInstance(int option) {
        SqLite fragment = new SqLite();
        Bundle args = new Bundle();
        args.putInt(ARG_OPTION, option);
        fragment.setArguments(args);
        return fragment;

    }
}

