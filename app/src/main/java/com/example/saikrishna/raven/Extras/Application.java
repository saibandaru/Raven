package com.example.saikrishna.raven.Extras;

import com.parse.Parse;

/**
 * Created by Sai Krishna on 22-07-2015.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "F3TC5X06GrHeW0fGOATZFYk5jhjDx9yNpKIDVtra", "prP3LIHyAtPNIP3UBK6FKKU6FUq5SHxbI3c7wc01");
        ;
    }
}
