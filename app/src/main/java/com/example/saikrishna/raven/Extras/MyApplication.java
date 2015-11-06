package com.example.saikrishna.raven.Extras;

import android.app.Application;

/*import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;*/
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Messages_Parse;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Profiles;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Repository_Parse;
import com.example.saikrishna.raven.DataBase.Tables.Table_Interface.Users_Parse;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
/*import com.syracuse.android.foodfeast.model.EventDetails;
import com.syracuse.android.foodfeast.model.EventImages;
import com.syracuse.android.foodfeast.model.EventUsers;
import com.syracuse.android.foodfeast.model.UserDetail;*/

/**
 *  This class will save the state across multiple activities and all parts of the application
 *  Created by Sandesh on 7/9/2015.
 */
public class MyApplication extends Application {

    private static ApplicationManager applicationManager;


    public ApplicationManager getApplicationManager() {
        if(applicationManager == null)  {
            applicationManager =  new ApplicationManager();
            return applicationManager;
        }
        else {
            return applicationManager;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Profiles.class);
        ParseObject.registerSubclass(Messages_Parse.class);
        ParseObject.registerSubclass(Repository_Parse.class);
        ParseObject.registerSubclass(Users_Parse.class);
        //Parse.initialize(this);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "F3TC5X06GrHeW0fGOATZFYk5jhjDx9yNpKIDVtra", "prP3LIHyAtPNIP3UBK6FKKU6FUq5SHxbI3c7wc01");
        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        //Enable to receive push
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //Register a channel to test push channels
        ParsePush.subscribeInBackground("");
        /*Context ctx = this.getApplicationContext();
        PushService.subscribe(ctx, "ch1", RespondToPushActivity.class);*/



        //pi.saveEventually();

        // UNIVERSAL IMAGE LOADER SETUP
/*
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
*/

    }


    // Singleton class which contains global attributes
    public class ApplicationManager {

        ApplicationManager() {

        }

/*
        private String userName;
        private String email;
        private String id;
        private String latitude;
        private String longitude;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setUserName(String name) {
            this.userName = name;
        }

        public String getUserName() {
            return userName;
        }

        public String getId() { return id;}

        public void setId(String id) {
            this.id=id;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLongitude() {
            return longitude;
        }
*/


    }
}

