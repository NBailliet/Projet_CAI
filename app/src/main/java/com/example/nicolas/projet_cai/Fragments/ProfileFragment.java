package com.example.nicolas.projet_cai.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicolas.projet_cai.BDD.BDD;
import com.example.nicolas.projet_cai.BDD.User;
import com.example.nicolas.projet_cai.R;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class ProfileFragment extends Fragment {

    private BDD bdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.activity_profil, container, false);

        bdd = new BDD(getContext());
        String login = null;
        if (savedInstanceState == null) {
            Bundle extras = this.getActivity().getIntent().getExtras();
            if (extras == null) {
                login = null;
            } else {
                login = extras.getString("STRING_I_NEED");
            }
        }

        User user = bdd.getUserWithLogin(login);

        if (user != null) {
            TextView loginP = (TextView) profileView.findViewById(R.id.textlogin);
            System.out.println(user.getLogin());
            loginP.setText(user.getLogin());
            TextView pswdP = (TextView) profileView.findViewById(R.id.textpswd);
            System.out.println(user.getPassword());
            pswdP.setText("**********");
            TextView nameP = (TextView) profileView.findViewById(R.id.textnomprofil);
            System.out.println(user.getName());
            nameP.setText(user.getName());
            TextView surnameP = (TextView) profileView.findViewById(R.id.textprenomprofil);
            System.out.println(user.getSurname());
            surnameP.setText(user.getSurname());
            TextView ageP = (TextView) profileView.findViewById(R.id.textageprofil);
            System.out.println(user.getAge());
            ageP.setText(user.getAge());

        }

        return profileView;
    }
}