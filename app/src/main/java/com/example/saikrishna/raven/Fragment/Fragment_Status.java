package com.example.saikrishna.raven.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saikrishna.raven.Activity.Activity_Menu;
import com.example.saikrishna.raven.Async.UpdateStatus_Async;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.StatusDataStore;
import com.example.saikrishna.raven.R;
import com.example.saikrishna.raven.RecyclerView.MyRecycelrView_Status;
import com.example.saikrishna.raven.Extras.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai Krishna on 26-07-2015.
 */
public class Fragment_Status extends Fragment {

    public SharedPreferences status;
    public static final String STATUS = "Status";
    public static final String MOBILE_NUMBER = "my_number";

    private static final String ARG_OPTION = "argument_option";
    Toolbar mToolbar;
    MyRecycelrView_Status myRecycelrViewAdapter_status;
    public Fragment_Status() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.layout_status, container, false);
        mToolbar=(Toolbar)((Activity_Menu)getActivity()).getToolBar();
        mToolbar.setTitle("Your Status");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText status_tv=(EditText)rootView.findViewById(R.id.status_edit);
        status= getActivity().getSharedPreferences(STATUS, 0);
        ImageView save=(ImageView)rootView.findViewById(R.id.save_status);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.haveNetworkConnection(getActivity().getApplicationContext())) {
                    String new_status=status_tv.getText().toString();
                    SharedPreferences.Editor editor = status.edit();
                    editor.putString(STATUS, new_status);
                    editor.commit();
                    updateParse(new_status);
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Internet connection not available please try later.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        setRecycler(rootView,status_tv);
        String stus=status.getString(STATUS, null);
        status_tv.setText(stus);
        return rootView;
    }

    public static Fragment_Status newInstance() {
        Fragment_Status fragment = new Fragment_Status();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void setRecycler(View rootView, final TextView status_tv)
    {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_status);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        List<String> list;
        StatusDataStore dataStore=new StatusDataStore(getActivity().getApplicationContext());
        dataStore.open();
         list= dataStore.retriveStatusList();
        dataStore.close();
        if(list.size()==0)
            list=setupStatusTable();
        myRecycelrViewAdapter_status = new MyRecycelrView_Status(getActivity(),list);
        myRecycelrViewAdapter_status.setOnItemClickListener(new MyRecycelrView_Status.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                TextView selected_stat=(TextView)v.findViewById(R.id.status_row);
                SharedPreferences.Editor editor = status.edit();
                editor.putString(STATUS, selected_stat.getText().toString());
                editor.commit();
                String new_status=selected_stat.getText().toString();
                status_tv.setText(new_status);
                updateParse(new_status);
            }
        });
        recyclerView.setAdapter(myRecycelrViewAdapter_status);
    }
    public List<String> setupStatusTable()
    {
        List<String > list=new ArrayList<>();
        list.add("Hii, Revan is cool! lets try together.");
        list.add("Available, ");
        list.add("Busy..ttyl");
        list.add("At Work");
        list.add("At Movies");
        list.add("Cant take calls, lets chat");
        list.add("Not your hours");
        list.add("Call, if urgent");
        list.add("Doing my workout");
        list.add("I have a really bad battery");
        StatusDataStore dataStore=new StatusDataStore(getActivity().getApplicationContext());
        dataStore.open();
        dataStore.setupStatus(list);
        dataStore.close();
        SharedPreferences.Editor editor = status.edit();
        editor.putString(STATUS, list.get(0));
        editor.commit();
        return list;
    }
    public void updateParse(String new_status)
    {
        SharedPreferences mynumber=getActivity().getSharedPreferences(MOBILE_NUMBER, 0);
        String number=mynumber.getString("mynumber", "+10000000000");
        UpdateStatus_Async getAppContacts=new UpdateStatus_Async(getActivity().getApplicationContext(),new_status,number);
        getAppContacts.execute(new String[]{number});
    }
}

