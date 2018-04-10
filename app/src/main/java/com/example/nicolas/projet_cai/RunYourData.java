package com.example.nicolas.projet_cai;


/**
 * Created by Nicolas on 03/04/2018.
 */


import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.projet_cai.BDD.BDD;
import com.example.nicolas.projet_cai.BDD.Time;
import com.example.nicolas.projet_cai.BDD.User;
import com.example.nicolas.projet_cai.Fragments.CameraFragment;
import com.example.nicolas.projet_cai.Fragments.HomeFragment;
import com.example.nicolas.projet_cai.Fragments.MapViewFragment;
import com.example.nicolas.projet_cai.Fragments.SendFragment;
import com.example.nicolas.projet_cai.Fragments.SettingsFragment;
import com.example.nicolas.projet_cai.Fragments.ShareFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RunYourData extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private BDD bdd;
    static SessionManager session;
    static SettingsManager settings;

    public User user;
    public User utilisateurCo;
    public Boolean connectionFlag = false;

    private ListView listViewProfiles;
    private ArrayList<String> mProfilesList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //System.out.println("DELETING DATABASE\n");
        //this.deleteDatabase("RunYourDataBDD.db");

        bdd = new BDD(this);

        session = new SessionManager(getApplicationContext());

        if (!isMyServiceRunning(com.example.nicolas.projet_cai.Services.LocalService.class)) {
            settings = new SettingsManager(getApplicationContext());
        }


        ///////demande permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else {
                //Sinon demander la permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.home));


        System.out.println(connectionFlag);
        System.out.println(session.isLoggedIn());
        if ((session.isLoggedIn())==false){
            showBeginDialog();
        }
        else {
            System.out.println(session.getLoginPref());
            utilisateurCo = new User(session.getLoginPref());
            System.out.println(utilisateurCo.getLogin());
            connectionFlag=true;
            Toast.makeText(RunYourData.this, "Heureux de vous revoir " + utilisateurCo.getLogin() + " !", Toast.LENGTH_SHORT).show();
        }
    }

    public void showBeginDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RunYourData.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RunYourData.this, android.R.layout.simple_list_item_1);
        bdd.open();
        List<User> list = bdd.getAllProfil();
        bdd.close();
        int i =0;

        while(i<list.size()){
            mProfilesList.add(list.get(i).getLogin());
            i++;
        }

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.profiles_list, null);

        listViewProfiles = (ListView) convertView.findViewById(R.id.listViewProfiles);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RunYourData.this, android.R.layout.simple_list_item_1, mProfilesList);
        View footerView = getLayoutInflater().inflate(R.layout.new_account_item_custom, null);
        listViewProfiles.addFooterView(footerView);
        listViewProfiles.setAdapter(adapter);
        builder.setView(convertView);
        builder.setIcon(R.drawable.runyourdatatgrand2);
        builder.setTitle("Bienvenue sur RunYourApp !");
        builder.setMessage("Merci de créer un compte ou vous connecter.");
        builder.setCancelable(false);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        listViewProfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) listViewProfiles.getItemAtPosition(position);

                if(temp!=null){
                    showConnectDialog(temp);
                    mProfilesList.clear();
                    dialog.cancel();
                    //Toast.makeText(SmartRide.this, temp, Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(SmartRide.this, "footview", Toast.LENGTH_SHORT).show();
                    showNewAccDialog();
                    mProfilesList.clear();
                    dialog.cancel();
                }

            }
        });


    }

    public void showNewAccDialog() {
        final LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogcreate, null);
        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Créer un compte");
        adb.setIcon(R.drawable.runyourdatapetit2);
        adb.setCancelable(false);

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                EditText et1 = (EditText) alertDialogView.findViewById(R.id.identifiant1);
                EditText et2 = (EditText) alertDialogView.findViewById(R.id.motdepasse1);
                String login = et1.getText().toString();
                String pswd = et2.getText().toString();
                //System.out.println(pswd);

                if (login.isEmpty() || pswd.isEmpty()) {
                    //showErrorDialog();
                    Toast.makeText(RunYourData.this, "Identifiant ou mot de passe non renseigné(s) !", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    showNewAccDialog();
                } else {
                    bdd.open();
                    // Log.w("NewAcount",bdd.getUserWithLogin(login).getLogin());
                    if (bdd.getUserWithLogin(login) == null) {
                        Log.w("Nouveau compte", "Login not found");
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH) + 1;
                        int day = c.get(Calendar.DATE);
                        int hours = c.get(Calendar.HOUR);
                        int mins = c.get(Calendar.MINUTE);
                        int seconds = c.get(Calendar.SECOND);
                        int milliseconds = c.get(Calendar.MILLISECOND);
                        Time time = new Time(year, month, day, hours, mins, seconds, milliseconds);
                        user = new User(login, pswd, null, null, 0, time);

                        bdd.insertProfil(user);

                        Toast.makeText(RunYourData.this, "Compte créé avec succès !" + user.getLogin() + user.getPassword(), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        showConnectDialog(login);
                    } else {
                        Toast.makeText(RunYourData.this, "Compte déjà existant, merci d'en créer un autre !", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        showNewAccDialog();
                    }
                    bdd.close();

                }

            }


        });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on retourne a la page précedente
                dialog.cancel();
                showBeginDialog();
            }
        });
        adb.show();
    }

    public void showConnectDialog(final String log) {

        Log.v("Page de connexion...", "Début");

        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialogconnect, null);
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
        EditText editText = (EditText) alertDialogView.findViewById(R.id.identifiant);
        editText.setText(log, TextView.BufferType.EDITABLE);
        adb.setView(alertDialogView);


        adb.setTitle("Login");
        adb.setIcon(R.drawable.runyourdatapetit2);
        adb.setCancelable(false);

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {

                EditText et1 = (EditText) alertDialogView.findViewById(R.id.identifiant);
                EditText et2 = (EditText) alertDialogView.findViewById(R.id.motdepasse);

                String login = et1.getText().toString();
                String pswd = et2.getText().toString();

                //User utilisateurCo = new User(login,pswd,null,null,0,null);
                bdd.open();
                utilisateurCo = bdd.getUserWithLogin(login);
                bdd.close();

                if (utilisateurCo != null) {
                    Log.v("Connexion", utilisateurCo.getLogin() + utilisateurCo.getPassword() + login + pswd);
                    if (utilisateurCo.getPassword().equals(pswd)) {
                        Log.v("Connexion", "Mot de passe correct");
                        connectionFlag=true;
                        if (utilisateurCo.getName() == null || utilisateurCo.getSurname() == null || utilisateurCo.getAge() == 0) {
                            dialog.cancel();
                            session.createLoginSession(login,connectionFlag,false);
                            //settings.setRunPref(false);
                            System.out.println(session.isLoggedIn());
                            System.out.println(session.getLoginPref());
                            showInfoDialog(utilisateurCo);
                            Toast.makeText(RunYourData.this, "Connexion réalisée avec succès !", Toast.LENGTH_SHORT).show();

                        } else {
                            session.createLoginSession(login,connectionFlag,false);
                            dialog.cancel();
                            Toast.makeText(RunYourData.this, "Connexion réalisée avec succès !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RunYourData.this, "Identifiant ou mot de passe incorrect, merci d'essayer à nouveau !", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        showConnectDialog(log);
                    }
                } else {
                    Toast.makeText(RunYourData.this, "Aucun utilisateur trouvé, un problème est survenu...", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    showConnectDialog(log);
                }

            }
        });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Lorsque l'on cliquera sur annuler on retourne a la page précedente
                dialog.cancel();
                showBeginDialog();
            }
        });
        adb.show();
    }

    public void showInfoDialog(final User user) {

        final LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.alertdialoginfo, null);

        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);

        adb.setView(alertDialogView);
        adb.setTitle("Information");

        adb.setIcon(R.drawable.runyourdatapetit2);
        adb.setCancelable(false);


        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                EditText et1 = (EditText) alertDialogView.findViewById(R.id.name);
                EditText et2 = (EditText) alertDialogView.findViewById(R.id.surname);
                EditText et3 = (EditText) alertDialogView.findViewById(R.id.age);

                String nameConnect = et1.getText().toString();
                String surnameConnect = et2.getText().toString();
                int ageConnect;
                if (et3.getText().toString().isEmpty()) {
                    ageConnect = 0;
                } else ageConnect = Integer.parseInt(et3.getText().toString());

                if (nameConnect.isEmpty() || surnameConnect.isEmpty() || ageConnect == 0) {

                    Toast.makeText(RunYourData.this, "Merci de remplir tous les champs.", Toast.LENGTH_SHORT).show();
                    showInfoDialog(user);

                } else {
                    user.setName(nameConnect);
                    user.setSurname(surnameConnect);
                    user.setAge(ageConnect);
                    bdd.open();
                    bdd.updateProfil(user);
                    bdd.close();
                    Log.v("Information", user.getLogin() + " " + user.getPassword() + " " + user.getAge() + " " + user.getName() + " " + user.getSurname() + " " + user.getCreationDate());
                    Toast.makeText(RunYourData.this, "Information sauvegardées !", Toast.LENGTH_SHORT).show();
                    dialog.cancel();

                }

            }


        });

        adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //showConnectDialog();
                showBeginDialog();
            }
        });
        adb.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        int labelColor = getColor(R.color.mat_black);
        String сolorString = String.format("%X", labelColor).substring(2); // !!strip alpha value!!

        if (id == R.id.action_profile) {
            if (connectionFlag) {
                Intent intent = new Intent(RunYourData.this, ProfilActivity.class);
                intent.putExtra("Utilisateur pour BDD", utilisateurCo.getLogin());
                startActivity(intent);
                overridePendingTransition(R.anim.profil_animation1, R.anim.profil_animation2);
            }
            return true;
        } else if (id == R.id.action_settings) {
            fm.beginTransaction().replace(R.id.frame, new SettingsFragment()).addToBackStack(null).commit();
            setTitle(Html.fromHtml(String.format("<font color=\"#%s\">Options</font>", сolorString)));
            return true;

        } else if (id == R.id.action_logout) {
                connectionFlag=false;
                session.setIsLoggedIn(connectionFlag);
                session.logoutUser();
                return true;

        } else if (id == R.id.action_quitter) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        int labelColor = getColor(R.color.mat_black);
        String сolorString = String.format("%X", labelColor).substring(2); // !!strip alpha value!!

        if (id == R.id.home) {

            fm.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
            //setTitle(getString(R.string.action_home));
            setTitle(Html.fromHtml(String.format("<font color=\"#%s\">Accueil</font>", сolorString)));


        } else if (id == R.id.map) {

            fm.beginTransaction().replace(R.id.frame, new MapViewFragment()).commit();
            setTitle(Html.fromHtml(String.format("<font color=\"#%s\">Carte</font>", сolorString)));

        } else if (id == R.id.nav_camera) {
            fm.beginTransaction().replace(R.id.frame, new CameraFragment()).commit();
            setTitle(Html.fromHtml(String.format("<font color=\"#%s\">Appareil Photo</font>", сolorString)));

        } else if (id == R.id.nav_share) {

            fm.beginTransaction().replace(R.id.frame, new ShareFragment()).commit();
            setTitle(getString(R.string.action_share));

        } else if (id == R.id.nav_send) {

            fm.beginTransaction().replace(R.id.frame, new SendFragment()).commit();
            setTitle(getString(R.string.action_send));

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Données RunYourData");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
            File root = Environment.getExternalStorageDirectory();
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(root.getAbsolutePath() + "/DCIM/100MEDIA/IMG0398.jpg"))); //ATTENTION CHANGER NOM FICHIER JPG POUR TEST !!!
            intent.putExtra(Intent.EXTRA_TEXT, "Bonjour, vous pouvez trouver ci-joint mes résultats obtenus avec l'application RunYourData !");

            startActivity(Intent.createChooser(intent, "Envoyer Email"));

        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static SettingsManager getSettingsManager() {
        return settings;
    }

    public static SessionManager getSessionManager() {
        return session;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) RunYourData.this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
