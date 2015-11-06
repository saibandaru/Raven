package layout.chat;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai Krishna on 05-07-2015.
 */
public class Fetch_Contacts extends Fragment {

    public List<String> contacts() {
        final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
        Cursor cur  = getActivity().getContentResolver().query(uriContact, null, null, null, null);
        List<String> contactNameList = new ArrayList<String>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactNameList.add(name);
            }
        }
        return contactNameList;
    }
}
