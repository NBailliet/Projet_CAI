package com.example.nicolas.projet_cai;

/**
 * Created by Nicolas on 03/04/2018.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.nicolas.projet_cai.BDD.BDD;
import com.example.nicolas.projet_cai.BDD.Run;
import com.example.nicolas.projet_cai.BDD.User;

import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    private BDD bdd;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bdd = new BDD(this);
        setContentView(R.layout.activity_profil);
        setTitle("Profile");

        session = RunYourData.getSessionManager();

        String login = null;
        if (savedInstanceState == null) {
            Bundle extras = this.getIntent().getExtras();
            if (extras == null) {
                login = null;
            } else {
                login = extras.getString("Utilisateur pour BDD");
            }
        }

        bdd.open();
        User user = bdd.getUserWithLogin(login);
        bdd.close();


        int nbRun = session.getNbRun();
        double lastDistance = (double)session.getLastDistance();

        //List<Run> run = bdd.getRunsWithLogin(login);
        //int nbRun = run.size();
        //System.out.println("NB RUN" + nbRun);
        //double lastDistance = run.get(nbRun-1).getDistance();

        if  (user!=null) {
            TextView loginP = (TextView) findViewById(R.id.textlogin);
            System.out.println("GETLOGIN CHECK : " + user.getLogin());
            loginP.setText(user.getLogin());
            TextView pswdP = (TextView) findViewById(R.id.textpswd);
            pswdP.setText("********");
            TextView nameP = (TextView) findViewById(R.id.textnomprofil);
            nameP.setText(user.getName());
            TextView surnameP = (TextView) findViewById(R.id.textprenomprofil);
            surnameP.setText(user.getSurname());
            TextView ageP = (TextView) findViewById(R.id.textageprofil);
            System.out.println("GETAGE CHECK : " + Integer.toString(user.getAge()));
            ageP.setText(Integer.toString(user.getAge()));
            TextView nbrunP = (TextView) findViewById(R.id.textrunprofil);
            System.out.println("NBRUN CHECK : " + Integer.toString(nbRun));
            nbrunP.setText(Integer.toString(nbRun));
            TextView distP = (TextView) findViewById(R.id.textdistprofil);
            String distString = String.format("%.11f", lastDistance);
            System.out.println("DISTANCE CHECK : " + distString);
            distP.setText(distString);
        }
        else
        {
            System.out.println("ERREUR NO USER");
        }

    }

}