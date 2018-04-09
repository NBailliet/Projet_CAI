package com.example.nicolas.projet_cai.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nicolas.projet_cai.BDD.BaseSQLite;
import com.example.nicolas.projet_cai.BDD.Localisation;
import com.example.nicolas.projet_cai.BDD.Time;
import com.example.nicolas.projet_cai.BDD.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class BDD {

    Gson gson = new Gson();

    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "RunYourDataBDD.db";

    //Profil Table
    private static final String TABLE_PROFIL = "TABLE_PROFIL";
    private static final String COL_PROFIL_ID = "PROFIL_ID";
    private static final int NUM_COL_PROFIL_ID = 0;
    private static final String COL_PROFIL_LOGIN = "PROFIL_LOGIN";
    private static final int NUM_COL_PROFIL_LOGIN = 1;
    private static final String COL_PROFIL_PWD = "PROFIL_PWD";
    private static final int NUM_COL_PROFIL_PWD = 2;
    private static final String COL_PROFIL_NAME = "PROFIL_NAME";
    private static final int NUM_COL_PROFIL_NAME = 3;
    private static final String COL_PROFIL_SURNAME = "PROFIL_SURNAME";
    private static final int NUM_COL_PROFIL_SURNAME = 4;
    private static final String COL_PROFIL_AGE = "PROFIL_AGE";
    private static final int NUM_COL_PROFIL_AGE = 5;
    private static final String COL_PROFIL_CREATION = "PROFIL_CREATION";
    private static final int NUM_COL_PROFIL_CREATION = 6;

    //Run Table
    private static final String TABLE_RUN = "TABLE_RUN";
    private static final String COL_RUN_ID = "RUN_ID";
    private static final int NUM_COL_RUN_ID = 0;
    private static final String COL_RUN_NAME = "RUN_NAME";
    private static final int NUM_COL_RUN_NAME = 1;
    private static final String COL_RUN_LOGIN = "RUN_LOGIN";
    private static final int NUM_COL_RUN_LOGIN = 2;
    private static final String COL_RUN_DIST = "RUN_DIST";
    private static final int NUM_COL_RUN_DIST = 3;
    //private static final String COL_RUN_VIT = "RUN_VIT";
    //private static final int NUM_COL_RUN_VIT = 4;

    //Location Table
    private static final String TABLE_LOC = "TABLE_LOC";
    private static final String COL_LOC_ID = "LOC_ID";
    private static final int NUM_COL_LOC_ID = 0;
    private static final String COL_LOC_RUN_NAME = "LOC_RUN_NAME";
    private static final int NUM_COL_LOC_RUN_NAME = 1;
    private static final String COL_LOC = "LOC";
    private static final int NUM_COL_LOC = 2;
    private static final String COL_LOC_TIME = "LOC_TIME";
    private static final int NUM_COL_LOC_TIME = 3;
    private static final String COL_LOC_ALT = "LOC_ALT";
    private static final int NUM_COL_LOC_ALT = 4;


    private SQLiteDatabase bdd;

    private BaseSQLite maBaseSQLite;

    public BDD(Context context){
        maBaseSQLite = new BaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    //Run Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertRun(Run run){
        ContentValues values = new ContentValues();
        values.put(COL_RUN_NAME, run.getName());
        //String t1=gson.toJson(run.getDate());
        //values.put(COL_RUN_DATE, t1);
        values.put(COL_RUN_LOGIN, run.getLogin());

        values.put(COL_RUN_DIST, run.getDistance());
        //values.put(COL_RUN_VIT, run.getVitesse());
        return bdd.insert(TABLE_RUN, null, values);
    }

    public int updateRun(Run run){
        ContentValues values = new ContentValues();
        //values.put(COL_RUN_NAME, run.getName());
        //String t1=gson.toJson(run.getDate());
        //values.put(COL_RUN_DATE, t1);
        values.put(COL_RUN_LOGIN, run.getLogin());
        values.put(COL_RUN_DIST, run.getDistance());
        //values.put(COL_RUN_VIT, run.getVitesse());
        return bdd.update(TABLE_RUN, values, COL_RUN_NAME + " = " + run.getName(), null);
    }

    public List<Run> getRunsWithLogin(String login){
        List<Run> runs = new ArrayList<>();
        Cursor c = bdd.query(TABLE_RUN, new String[] {COL_RUN_ID, COL_RUN_NAME, COL_RUN_LOGIN, COL_RUN_DIST}, COL_RUN_LOGIN + " LIKE \"" + login +"\"", null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Run run = cursorToRun(c);
            runs.add(run);
            c.moveToNext();
        }
        return runs;
    }

    private Run cursorToRun(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //On créé un run
        Run run = new Run(null, null, 0);

        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        run.setName(c.getString(NUM_COL_RUN_NAME));

        //Type type = new TypeToken<User>() {}.getType();
        run.setLogin(c.getString(NUM_COL_RUN_LOGIN));

        run.setDistance(c.getDouble(NUM_COL_RUN_DIST));


        //run.setVitesse(c.getDouble(NUM_COL_RUN_VIT));

        return run;
    }

    public List<Run> getAllRun() {
        List<Run> runs = new ArrayList<>();
        Cursor cursor = bdd.query(TABLE_RUN,
                new String[] {COL_RUN_ID, COL_RUN_NAME, COL_RUN_LOGIN, COL_RUN_DIST}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            System.out.println("CURSOR POSITION = " + Integer.toString(cursor.getPosition()));
            if (cursorToRun(cursor)==null) {
                return null;
            }
            else {
                Run run = cursorToRun(cursor);
                runs.add(run);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return runs;
    }

    public void clearRunsForLogin(String nameoftable, String login){
        bdd=maBaseSQLite.getWritableDatabase();
        bdd.delete(nameoftable,COL_RUN_LOGIN+ " = " + login,null);
        bdd.close();
    }

    //Localisation Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertLoc(Localisation localisation){
        ContentValues values = new ContentValues();
        values.put(COL_LOC_RUN_NAME, localisation.getNameOfRun());
        String t1=gson.toJson(localisation.getLocation());
        values.put(COL_LOC, t1 );
        String t2=gson.toJson(localisation.getTime());
        values.put(COL_LOC_TIME, t2);
        values.put(COL_LOC_ALT,localisation.getAltitude());
        return bdd.insert(TABLE_LOC, null, values);
    }


    public int updateLoc(Localisation localisation){
        ContentValues values = new ContentValues();
        values.put(COL_LOC_RUN_NAME, localisation.getNameOfRun());
        String t1=gson.toJson(localisation.getLocation());
        values.put(COL_LOC, t1 );
        String t2=gson.toJson(localisation.getTime());
        values.put(COL_LOC_TIME, t2);
        values.put(COL_LOC_ALT,localisation.getAltitude());
        return bdd.update(TABLE_LOC, values, COL_LOC_RUN_NAME + " = " +localisation.getNameOfRun(), null);
    }

    public Localisation getLocalisationWithRunName(String name){
        Cursor c = bdd.query(TABLE_LOC, new String[] {COL_LOC_ID, COL_LOC_RUN_NAME, COL_LOC, COL_LOC_TIME, COL_LOC_ALT}, COL_LOC_RUN_NAME + " LIKE \"" + name +"\"", null, null, null, null);
        return cursorToLoc(c);
    }

    public List<Localisation> getAllLoc() {
        List<Localisation> locs = new ArrayList<Localisation>();
        Cursor cursor = bdd.query(TABLE_LOC,
                new String[] {COL_LOC_ID, COL_LOC_RUN_NAME, COL_LOC, COL_LOC_TIME , COL_LOC_ALT}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Localisation localisation = cursorToLoc(cursor);
            locs.add(localisation);
            cursor.moveToNext();
        }
        cursor.close();
        return locs;
    }

    private Localisation cursorToLoc(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        Localisation localisation = new Localisation(null,null,null,0);
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        localisation.setNameOfRun(c.getString(NUM_COL_LOC_RUN_NAME));

        Type type2 = new TypeToken<LatLng>() {}.getType();
        LatLng location = gson.fromJson(c.getString(NUM_COL_LOC), type2);
        localisation.setLocation(location);

        Type type = new TypeToken<Time>() {}.getType();
        Time time = gson.fromJson(c.getString(NUM_COL_LOC_TIME), type);
        localisation.setTime(time);

        localisation.setAltitude(c.getDouble(NUM_COL_LOC_ALT));

        return localisation;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Profil Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertProfil(User user){
        ContentValues values = new ContentValues();
        values.put(COL_PROFIL_LOGIN, user.getLogin());
        values.put(COL_PROFIL_PWD, user.getPassword());
        values.put(COL_PROFIL_NAME, user.getName());
        values.put(COL_PROFIL_SURNAME, user.getSurname());
        values.put(COL_PROFIL_AGE, user.getAge());
        String t2=gson.toJson(user.getCreationDate());
        values.put(COL_PROFIL_CREATION, t2);
        return bdd.insert(TABLE_PROFIL, null, values);
    }

    public int updateProfil(User user){
        ContentValues values = new ContentValues();
        values.put(COL_PROFIL_LOGIN, user.getLogin());
        values.put(COL_PROFIL_PWD, user.getPassword());
        values.put(COL_PROFIL_NAME, user.getName());
        values.put(COL_PROFIL_SURNAME, user.getSurname());
        values.put(COL_PROFIL_AGE, user.getAge());
        String t2=gson.toJson(user.getCreationDate());
        values.put(COL_PROFIL_CREATION, t2);
        return bdd.update(TABLE_PROFIL, values, COL_PROFIL_LOGIN + " LIKE \"" + user.getLogin() +"\"", null);
    }

    public User getUserWithLogin(String login){
        Cursor c = bdd.query(TABLE_PROFIL, new String[] {COL_PROFIL_ID, COL_PROFIL_LOGIN, COL_PROFIL_PWD, COL_PROFIL_NAME,COL_PROFIL_SURNAME,COL_PROFIL_AGE, COL_PROFIL_CREATION}, COL_PROFIL_LOGIN + " LIKE \"" + login +"\"", null, null, null, null);
        c.moveToFirst();
        return cursorToProfil(c);
    }

    public List<User> getAllProfil() {
        List<User> users = new ArrayList<User>();
        Cursor cursor = bdd.query(TABLE_PROFIL,
                new String[] {COL_PROFIL_ID, COL_PROFIL_LOGIN, COL_PROFIL_PWD, COL_PROFIL_NAME,COL_PROFIL_SURNAME, COL_PROFIL_AGE, COL_PROFIL_CREATION}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToProfil(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    private User cursorToProfil(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        Log.v("cursor", Integer.toString(c.getCount()));
        if (c.getCount() >= 1) {
            Log.v("cursor", "in the if");
            User user = new User(null, null, null, null, 0, null);
            Log.v("cursor", "after new user");
            //Log.v("cursor", "Namelogincolonne"+c.getString(1));
            user.setLogin(c.getString(NUM_COL_PROFIL_LOGIN));
            Log.v("cursor", "after set login"+user.getLogin());
            user.setPassword(c.getString(NUM_COL_PROFIL_PWD));
            Log.v("cursor", "after set pwd");
            user.setName(c.getString(NUM_COL_PROFIL_NAME));
            Log.v("cursor", "after set name");
            user.setSurname(c.getString(NUM_COL_PROFIL_SURNAME));
            Log.v("cursor", "after set Surname");
            user.setAge(c.getInt(NUM_COL_PROFIL_AGE));
            Type type = new TypeToken<Time>() {
            }.getType();
            Time time = gson.fromJson(c.getString(NUM_COL_PROFIL_CREATION), type);
            user.setCreationDate(time);
            Log.v("cursor", "after time");
            return user;
        } else  return null;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void clearTable(String nameoftable){
        bdd=maBaseSQLite.getWritableDatabase();
        bdd.delete(nameoftable,null,null);
        bdd.close();
    }
}
