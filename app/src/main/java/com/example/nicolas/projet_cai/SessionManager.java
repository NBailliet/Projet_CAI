package com.example.nicolas.projet_cai;

/**
 * Created by Nicolas on 03/04/2018.
 */


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "RunYourDataPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Login
    public static final String KEY_LOGIN = "login";



    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String login, Boolean test){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, test);

        // Storing name in pref
        editor.putString(KEY_LOGIN, login);

        // commit changes
        editor.commit();
    }


    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.remove(KEY_LOGIN);
        editor.commit();

        // After logout redirect user to Loging Activity
        Intent i = new Intent(_context, RunYourData.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setIsLoggedIn(Boolean test){
        editor.putBoolean(IS_LOGIN, test);
        editor.commit();
    }

    public String getLoginPref(){
        String loginPref = pref.getString(KEY_LOGIN,null);
        return loginPref;
    }

}
