package com.example.nicolas.projet_cai;

/**
 * Created by Nicolas on 05/04/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "RunYourDataSettingsPref";

    // Memorized Started Manual run
    public static final String KEY_RUNSTART = "IsThereStartedRun";

    // Memorized Started Manual run
    public static final String KEY_START = "IsStartNeeded";

    // Memorized Started Manual run
    public static final String KEY_STOP = "IsStopNeeded";

    // Memorized Number of Runs
    public static final String KEY_RUNNB = "RunNumber";


    // Constructor
    public SettingsManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putBoolean(KEY_RUNSTART,false);
        editor.putBoolean(KEY_START,false);
        editor.putBoolean(KEY_STOP,false);
        editor.putInt(KEY_RUNNB,0);
        editor.commit();
    }

    /**
     * Create settings session
     * */
    public void createSettingsSession(Boolean runPref){

        // Storing name in pref
        editor.putBoolean(KEY_RUNSTART, runPref);
        // commit changes
        editor.commit();
    }



    public Boolean getStartRunPref(){
        Boolean runStartPref = pref.getBoolean(KEY_RUNSTART,false);
        return runStartPref;
    }

    public void setStartRunPref(Boolean runStartPref){
        editor.putBoolean(KEY_RUNSTART,runStartPref);
        editor.commit();
    }

    public Boolean getStartPref(){
        Boolean runStartPref = pref.getBoolean(KEY_START,false);
        return runStartPref;
    }

    public void setStartPref(Boolean runStartPref){
        editor.putBoolean(KEY_START,runStartPref);
        editor.commit();
    }

    public Boolean getStopPref(){
        Boolean runStopPref = pref.getBoolean(KEY_STOP,false);
        return runStopPref;
    }

    public void setStopPref(Boolean runStopPref){
        editor.putBoolean(KEY_STOP,runStopPref);
        editor.commit();
    }


    public int getRunNbPref(){
        int runNbPref = pref.getInt(KEY_RUNNB,0);
        return runNbPref;
    }

    public void setRunNbPref(int runNbPref){
        editor.putInt(KEY_RUNNB,runNbPref);
        editor.commit();
    }

    public void addRunNbPref() {
        editor.putInt(KEY_RUNNB,pref.getInt(KEY_RUNNB,0)+1);
        editor.commit();
    }

}
