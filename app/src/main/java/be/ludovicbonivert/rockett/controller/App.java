package be.ludovicbonivert.rockett.controller;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by LudovicBonivert on 13/01/15.
 * We extend the application class to do aditional work when app is launching (and not like initially in an activity)
 * My app crashed previously with the Parse init functions when we switched over activities
 *
 * More info about Parse runtime crash : http://stackoverflow.com/questions/27538261/parse-com-runtime-crash-android
 *
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "XDQEM27EyIS853aZds3vowbjZbDf2dpRg7hs58NC", "Mu4TNkNOd95CB7vtC5g7ArKqZn8h6a7gGoQz4QSy");
        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

    private void createTestObjectForParse(){
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

}
