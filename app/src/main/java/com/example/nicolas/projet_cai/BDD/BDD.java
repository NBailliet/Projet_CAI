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

/**
 * Created by Nicolas on 03/04/2018.
 */

public class BDD {

    Gson gson = new Gson();

    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "SmartRideBDD.db";

    //Profil Table
    private static final String TABLE_PROFIL = "TABLE_PROFIL";
    private static final String COL_PROFIL_ID = "PROFIL_ID";
    private static final int NUM_COL_PROFIL_ID = 0;
    private static final String COL_PROFIL_LOGIN = "PROFIL_LOGIN";
    private static final int NUM_COL_PROFIL_LOGIN =1;
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
    private static final String TABLE_RUN = "table_run";
    private static final String COL_RUN_ID = "run_ID";
    private static final int NUM_COL_RUN_ID = 0;
    private static final String COL_RUN_NAME = "Loc_run_name";
    private static final int NUM_COL_RUN_NAME = 1;
    private static final String COL_RUN_DATE = "run_date";
    private static final int NUM_COL_RUN_DATE = 2;
    private static final String COL_RUN_PROFIL = "run_profil";
    private static final int NUM_COL_RUN_PROFIL= 3;

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
        //On créer la BDD et sa table
        maBaseSQLite = new BaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    //Localisation Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertLoc(Localisation localisation){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_LOC_RUN_NAME, localisation.getNameOfRun());
        String t1=gson.toJson(localisation.getLocation());
        values.put(COL_LOC, t1 );
        Log.v("BDD",t1);
        String t2=gson.toJson(localisation.getTime());
        values.put(COL_LOC_TIME, t2);
        values.put(COL_LOC_ALT,localisation.getAltitude());
        //on insère l'objet dans la BDD via le ContentValues
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
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
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
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return locs;
    }

    private Localisation cursorToLoc(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //On créé un livre
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

        //On retourne le livre
        return localisation;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Profil Table
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long insertProfil(User user){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_PROFIL_LOGIN, user.getLogin());
        values.put(COL_PROFIL_PWD, user.getPassword());
        values.put(COL_PROFIL_NAME, user.getName());
        values.put(COL_PROFIL_SURNAME, user.getSurname());
        values.put(COL_PROFIL_AGE, user.getAge());
        String t2=gson.toJson(user.getCreationDate());
        values.put(COL_PROFIL_CREATION, t2);
        //on insère l'objet dans la BDD via le ContentValues
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
        // assurez-vous de la fermeture du curseur
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
