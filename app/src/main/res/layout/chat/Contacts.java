package layout.chat;

import android.content.ContentResolver;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sai Krishna on 05-07-2015.
 */
public class Contacts extends Fragment {

    private static final String ARG_OPTION = "argument_option";
    public Contacts() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.contacts, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_contacts);
        setRetainInstance(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        List<HashMap<String,String>> contacts=contacts();
        Collections.sort(contacts, new Comparator<HashMap<String, String>>(){
            public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                return one.get("name").compareTo(two.get("name"));
            }
        });

        MyRecyclerViewAdapter_Contacts myRecyclerViewAdapter = new MyRecyclerViewAdapter_Contacts(getActivity(),contacts);
        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter_Contacts.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
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

    public static Contacts newInstance(int option) {
        Contacts fragment = new Contacts();
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
}
