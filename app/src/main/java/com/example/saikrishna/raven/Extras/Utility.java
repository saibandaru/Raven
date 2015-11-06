package com.example.saikrishna.raven.Extras;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sai Krishna on 8/7/2015.
 */
public class Utility {
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public static boolean compareNumbers(String chatlist,String reciv_num)
    {
        chatlist=chatlist.replace(" ","");
        chatlist= chatlist.replace("-","");
        chatlist= chatlist.replace("(","");
        chatlist= chatlist.replace(")","");
        if(chatlist.equalsIgnoreCase(reciv_num))
            return true;
        else
            return false;
    }
}
