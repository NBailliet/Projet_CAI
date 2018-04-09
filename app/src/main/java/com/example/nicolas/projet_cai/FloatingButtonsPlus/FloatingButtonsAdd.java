package com.example.nicolas.projet_cai.FloatingButtonsPlus;

import android.util.Log;
import android.view.View;

import com.example.nicolas.projet_cai.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class FloatingButtonsAdd implements View.OnClickListener {


    @Override
    public void onClick(View v) {


        Log.v("Listener","BOUTON LISTENER DETECTED");
        FloatingActionButton myFabStart = (FloatingActionButton)  v.findViewById(R.id.floatingActionButtonStart);
        FloatingActionButton myFabEnd = (FloatingActionButton)  v.findViewById(R.id.floatingActionButtonEnd);
        FloatingActionButton myFabClean = (FloatingActionButton)  v.findViewById(R.id.floatingActionButtonCleantable);


        myFabStart.setVisibility(View.VISIBLE);
        myFabEnd.setVisibility(View.VISIBLE);
        myFabClean.setVisibility(View.VISIBLE);

        FloatingActionsMenu myFabMenu = v.findViewById(R.id.menu_plus);

        v.setOnClickListener(new FloatingButtonsRem());
    }
}
