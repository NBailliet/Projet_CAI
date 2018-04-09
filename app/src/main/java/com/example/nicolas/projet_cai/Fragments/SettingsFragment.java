package com.example.nicolas.projet_cai.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.projet_cai.BDD.BDD;
import com.example.nicolas.projet_cai.BDD.Run;
import com.example.nicolas.projet_cai.BDD.User;
import com.example.nicolas.projet_cai.R;
import com.example.nicolas.projet_cai.SessionManager;
import com.example.nicolas.projet_cai.SettingsManager;
import com.example.nicolas.projet_cai.RunYourData;

import java.util.List;

/**
 * Created by Nicolas on 01/02/2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {

    String TABLE_RUN = "TABLE_RUN";
    SettingsManager settings;
    SessionManager session;
    TextView textViewRun;
    BDD bdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.settings, container, false);
        settings = RunYourData.getSettingsManager();
        session = RunYourData.getSessionManager();
        textViewRun = (TextView) settingsView.findViewById(R.id.buttonRun);
        bdd = new BDD(getActivity());
        return settingsView;
    }

    @Override
    public void onClick(View v) {

        //View parent = (View)v.getParent();
        Activity activity = getActivity();
        if (v.hasOnClickListeners()) {


            switch (v.getId()) {

                case R.id.buttonRun :

                bdd.open();
                System.out.println("LOGIN FOR USER TEST IN SETTINGS" + session.getLoginPref());
                bdd.clearRunsForLogin(TABLE_RUN, session.getLoginPref());
                bdd.close();
                break;
            }

        }
    }


}
