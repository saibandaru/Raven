package com.example.saikrishna.raven.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saikrishna.raven.Activity.Activity_ChatScreen;
import com.example.saikrishna.raven.DataBase.Tables.DataSource.ChatDataSource;
import com.example.saikrishna.raven.RecyclerView.MyRecyclerViewAdapter_ChatList;
import com.example.saikrishna.raven.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 20-07-2015.
 */
public class Fragment_chats extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    public static final String ARG_CONTACTS_INDEX = "argument_option";
    public static List<HashMap<String,String>> contacts;
    public static List<HashMap<String,String>> contacts_onRun;
    public RecyclerView recyclerView;
    public static MyRecyclerViewAdapter_ChatList myRecyclerViewAdapter;
    ChatDataSource chat_database;
    public Fragment_chats() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.contacts_o, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_contacts);
        setRetainInstance(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        contacts=contacts();
        Collections.sort(contacts, new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                return one.get("name").compareTo(two.get("name"));
            }
        });
        chat_database=new ChatDataSource(getActivity().getApplicationContext());
        chat_database.open();
        contacts_onRun=chat_database.getChats(contacts);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter_ChatList(getActivity(),contacts_onRun);
        chat_database.close();
        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter_ChatList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String number = getContact(position);
                String name=getContactName(position);
                Intent intent = new Intent(getActivity(), Activity_ChatScreen.class);
                intent.putExtra(Fragment_contacts.ARG_CONTACTS_INDEX, number);
                intent.putExtra(Fragment_contacts.ARG_CONTACTS_INDEX_NAME, name);
                startActivity(intent);
                /*Information.Block_Contact person=info.getItem(position);
                Chat_Fragment fragment=Chat_Fragment.newInstance(person);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                        .addToBackStack(null).commit();*/
            }
        });
        recyclerView.setAdapter(myRecyclerViewAdapter);
        int option = this.getArguments().getInt(ARG_OPTION);
        return rootView;
    }
    @Override
    public void onResume() {
        myRecyclerViewAdapter.notifyDataSetChanged();
        super.onResume();
    }

    public static Fragment_chats newInstance(int option) {
        Fragment_chats fragment = new Fragment_chats();
        Bundle args = new Bundle();
        args.putInt(ARG_OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }


    public List<HashMap<String,String>> contacts() {
        //final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
        final Uri uriContact = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(uriContact, null, null, null, null);

        List<HashMap<String,String>> contacts=new ArrayList<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                String has_phone = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
                HashMap<String,String> contact=new HashMap();
                if(!has_phone.endsWith("0"))
                {
                    contact.put("name", name);
                    String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    contact.put("number",phoneNumber);
                    contacts.add(contact);
                }
            }
        }
        return contacts;
    }
    public String GetPhoneNumber(String id)
    {
        String number = "";
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID + " = " + id+1, null, null);

        if(phones.getCount() > 0)
        {
            while(phones.moveToNext())
            {
                number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            // System.out.println(number);
        }

        phones.close();

        return number;
    }
    public String getContact(int position)
    {
        HashMap<String ,String > person= contacts_onRun.get(position);
        return person.get("number");
    }
    public String getContactName(int position)
    {
        HashMap<String ,String > person= contacts_onRun.get(position);
        return person.get("name");
    }
    /*public List<HashMap<String,String>> getChats()
    {
        chat_database=new ChatDataSource(getActivity().getApplicationContext());
        chat_database.open();
        // if(!chat_database.checkContactIfExists(number)) {
        List<HashMap<String,String>> chat_contacts_num= chat_database.retriveChatList();
        List<HashMap<String,String>> chat_contacts=new ArrayList<HashMap<String, String>>();
        int index=0;
        while(index!=chat_contacts_num.size()){

            HashMap<String ,String > contact=new HashMap<>();
            contact.put("number",chat_contacts_num.get(index).get("number").toString());
            contact.put("name",getContactName(chat_contacts_num.get(index).get("number")));
            chat_contacts.add(contact);
            index++;
        }
        chat_database.close();
        return chat_contacts;
    }
    public String getContactName(String number){//},List<HashMap<String,String >> contacts){
        int index=0;
        String name="not found";
        while(index!=contacts.size()){
            HashMap<String,String> contact=contacts.get(index);
            String number_inside=contact.get("number");
            if(number_inside.equals(number)) {
                name = contact.get("name");
                return name;
            }
            index++;
        }
        return name;
    }*/
}
